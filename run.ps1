$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$buildDirectory = Join-Path $projectRoot "target\Shell-In-Java-build"

function Test-Java25($javaPath) {
    if (-not (Test-Path $javaPath)) {
        return $false
    }

    $previousErrorAction = $ErrorActionPreference
    $ErrorActionPreference = "Continue"
    $version = (& $javaPath -version 2>&1 | Out-String)
    $ErrorActionPreference = $previousErrorAction
    return $version -match 'version "25'
}

$java25Candidates = @("C:\Program Files\Java\jdk-25\bin\java.exe")
$java25Candidates += @(Get-ChildItem "C:\Program Files\Eclipse Adoptium" -Directory -Filter "jdk-25*" -ErrorAction SilentlyContinue | ForEach-Object {
    Join-Path $_.FullName "bin\java.exe"
})
$java25Path = $java25Candidates | Where-Object { Test-Java25 $_ } | Select-Object -First 1

if (-not $java25Path) {
    $winget = Get-Command winget -ErrorAction SilentlyContinue
    if (-not $winget) {
        throw "Java 25 is required, and winget is not available to install it automatically."
    }

    Write-Host "Java 25 was not found. Installing Eclipse Temurin Java 25..."
    & $winget.Source install --id EclipseAdoptium.Temurin.25.JDK --exact --source winget --accept-source-agreements --accept-package-agreements
    if ($LASTEXITCODE -ne 0) {
        throw "Java 25 installation failed."
    }

    $java25Candidates = @("C:\Program Files\Java\jdk-25\bin\java.exe")
    $java25Candidates += @(Get-ChildItem "C:\Program Files\Eclipse Adoptium" -Directory -Filter "jdk-25*" -ErrorAction SilentlyContinue | ForEach-Object {
        Join-Path $_.FullName "bin\java.exe"
    })
    $java25Path = $java25Candidates | Where-Object { Test-Java25 $_ } | Select-Object -First 1
}

if (-not $java25Path) {
    throw "Java 25 was installed or detected unsuccessfully. Restart PowerShell and run this launcher again."
}

$env:JAVA_HOME = Split-Path (Split-Path $java25Path -Parent) -Parent
$env:Path = "$(Split-Path $java25Path -Parent);$env:Path"

$maven = Get-Command mvn -ErrorAction SilentlyContinue
if ($null -eq $maven) {
    $localMaven = Join-Path $projectRoot "..\.maven\maven-3.9.16\bin\mvn.cmd"
    if (Test-Path $localMaven) {
        $maven = $localMaven
    } else {
        throw "Maven was not found. Install Maven or add mvn.cmd to PATH."
    }
}

Push-Location $projectRoot
try {
    & $maven clean package "-Ddir=$buildDirectory"
    if ($LASTEXITCODE -ne 0) {
        exit $LASTEXITCODE
    }

    & $java25Path -jar (Join-Path $buildDirectory "Shell-In-Java.jar") @args
    exit $LASTEXITCODE
} finally {
    Pop-Location
}
