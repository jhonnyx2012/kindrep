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
}

kotlin {
    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

abstract class VerifyDomainImmutabilityTask : DefaultTask() {
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val modelRoot: DirectoryProperty

    @TaskAction
    fun verify() {
        val rootDirectory = modelRoot.get().asFile
        if (!rootDirectory.exists()) return

        val mutablePropertyPattern = Regex("""\bvar\s+\w+""")
        val mutableCollectionPattern = Regex("""\bMutable(List|Set|Map|Collection)\b""")
        val violations = mutableListOf<String>()

        rootDirectory.walkTopDown()
            .filter { it.isFile && it.extension == "kt" }
            .forEach { file ->
                val relativePath = file.relativeTo(rootDirectory).invariantSeparatorsPath
                val content = file.readText()
                if (mutablePropertyPattern.containsMatchIn(content)) {
                    violations += "$relativePath declares a mutable 'var' property; :domain models must be deeply immutable ('val' only) to satisfy compose_stability.conf."
                }
                if (mutableCollectionPattern.containsMatchIn(content)) {
                    violations += "$relativePath exposes a MutableCollection; use read-only List/Set/Map in :domain models."
                }
            }

        if (violations.isNotEmpty()) {
            val details = violations.joinToString("\n") { " - $it" }
            throw GradleException("Domain immutability verification failed:\n$details")
        }
    }
}

val verifyDomainImmutability = tasks.register<VerifyDomainImmutabilityTask>("verifyDomainImmutability") {
    group = "verification"
    description = "Verifies that :domain models remain deeply immutable for Compose cross-module stability."
    modelRoot.set(layout.projectDirectory.dir("src/commonMain/kotlin/com/jhonnyb/kindrep/domain/model"))
}

tasks.named("check") {
    dependsOn(verifyDomainImmutability)
}
