$ErrorActionPreference = "Stop"

$repositoryRoot = Split-Path -Parent $PSScriptRoot
Set-Location $repositoryRoot

if (-not $env:JAVA_HOME -or -not (Test-Path (Join-Path $env:JAVA_HOME "bin\java.exe"))) {
    $androidStudioJavaRuntime = "C:\Program Files\Android\Android Studio\jbr"
    if (Test-Path (Join-Path $androidStudioJavaRuntime "bin\java.exe")) {
        $env:JAVA_HOME = $androidStudioJavaRuntime
    }
}

if (-not $env:ANDROID_HOME) {
    $defaultAndroidSdk = Join-Path $env:LOCALAPPDATA "Android\Sdk"
    if (Test-Path $defaultAndroidSdk) {
        $env:ANDROID_HOME = $defaultAndroidSdk
    }
}

Write-Host "==> Running Kindrep multi-module verification harness (Domain + Data + Compose Architecture & Previews + Host Tests + Android Debug Build)..."
& .\gradlew.bat `
    :domain:verifyDomainImmutability `
    :domain:jvmTest `
    :data:testAndroidHostTest `
    :composeApp:verifyUiArchitecture `
    :composeApp:testAndroidHostTest `
    :androidApp:assembleDebug `
    --no-daemon @args
exit $LASTEXITCODE
