import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.koinCompiler)
}

kotlin {
    android {
        namespace = "com.jhonnyb.kindrep.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        androidResources {
            enable = true
        }

        withHostTestBuilder {}

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            @Suppress("DEPRECATION")
            implementation(compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.ui.tooling.preview)

            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.navigation.compose)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.annotations)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
        }

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.android)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.test)
        }
    }
}

dependencies {
    "androidRuntimeClasspath"(libs.compose.ui.tooling)
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.jhonnyb.kindrep.generated.resources"
}

abstract class VerifyUiArchitectureTask : DefaultTask() {
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val sourceRoot: DirectoryProperty

    @TaskAction
    fun verify() {
        val rootDirectory = sourceRoot.get().asFile
        val kotlinFiles = rootDirectory.walkTopDown()
            .filter { file -> file.isFile && file.extension == "kt" }
            .toList()

        val violations = mutableListOf<String>()
        val composableAnnotationPattern = Regex("""@Composable\b""")
        val previewAnnotationPattern = Regex("""@Preview\b""")

        kotlinFiles.forEach { file ->
            val relativePath = file.relativeTo(rootDirectory).invariantSeparatorsPath
            val lines = file.readLines()
            val content = lines.joinToString("\n")

            if (lines.size > 700) {
                violations += "$relativePath has ${lines.size} lines (maximum 700 lines per file; split responsibilities cleanly)."
            }

            val isVisualUserInterfaceFile =
                relativePath.startsWith("ui/components/") ||
                    relativePath.startsWith("ui/dialogs/") ||
                    relativePath.startsWith("ui/screens/")

            if (isVisualUserInterfaceFile) {
                val composableCount = composableAnnotationPattern.findAll(content).count()
                val previewCount = previewAnnotationPattern.findAll(content).count()

                if (composableCount > 0 && previewCount != 1) {
                    violations += "$relativePath declares $previewCount @Preview function(s); expected exactly 1 @Preview per visual Compose file."
                }
                if (composableCount > 4) {
                    violations += "$relativePath declares $composableCount @Composable functions (maximum 4 per visual file; extract sub-components)."
                }
            }
        }

        if (violations.isNotEmpty()) {
            val formattedViolations = violations.joinToString("\n") { " - $it" }
            throw GradleException(
                "Kindrep UI & Clean Architecture verification failed:\n$formattedViolations",
            )
        }
    }
}

val verifyUiArchitecture = tasks.register<VerifyUiArchitectureTask>("verifyUiArchitecture") {
    group = "verification"
    description = "Verifies Clean Architecture file size bounds, composable cohesion, and the single-@Preview-per-file rule."
    sourceRoot.set(layout.projectDirectory.dir("src/commonMain/kotlin/com/jhonnyb/kindrep"))
}

tasks.named("check") {
    dependsOn(verifyUiArchitecture)
}
