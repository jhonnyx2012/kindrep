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

Write-Host "==> Running Kindrep verification harness (UI Architecture & Previews + Host Unit Tests + Android Debug Build)..."
& .\gradlew.bat :composeApp:verifyUiArchitecture :composeApp:testAndroidHostTest :androidApp:assembleDebug --no-daemon @args
exit $LASTEXITCODE
