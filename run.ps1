$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$buildDirectory = Join-Path $projectRoot "target\Shell-In-Java-build"
$java25 = "C:\Program Files\Java\jdk-25"

if (Test-Path (Join-Path $java25 "bin\java.exe")) {
    $env:JAVA_HOME = $java25
    $env:Path = "$(Join-Path $java25 'bin');$env:Path"
}

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

    & java -jar (Join-Path $buildDirectory "Shell-In-Java.jar") @args
    exit $LASTEXITCODE
} finally {
    Pop-Location
}
