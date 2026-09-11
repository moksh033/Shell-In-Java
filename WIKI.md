# Shell-In-Java

## RainShower Interface

Shell-In-Java opens as a black desktop terminal named `Shell-In-Java`. Its green symbol-art heading says **Built By Moksh**.

## Download and Run

1. Download `RainShower-Windows.zip` from release `v1.0.0`.
2. Extract the ZIP without separating the `app` and `runtime` folders.
3. Open the extracted folder.
4. Double-click `RainShower.exe`.

The application starts directly as a Windows desktop terminal. No PowerShell launcher is required.

## Commands

```text
help
cd <directory>
pwd
echo <text>
type <command>
exit
```

External commands available through `PATH` are supported as well.

## Redirection

```text
echo Hello > output.txt
echo More >> output.txt
unknown-command 2> errors.txt
unknown-command 2>> errors.txt
```

## Build From Source

```bash
mvn clean package
java -jar target/Shell-In-Java.jar
```

The native Windows app image can be built with Java 25:

```powershell
jpackage --type app-image --name RainShower --input target --main-jar Shell-In-Java.jar --main-class RainShower --dest target
```
