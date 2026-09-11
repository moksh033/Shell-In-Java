# Shell-In-Java

A small interactive shell written in Java. It supports built-in commands, external processes, quoting, redirection, and command completion on POSIX terminals.

## Requirements

- Java 25
- Maven 3.9+
- Linux, macOS, or WSL for raw terminal mode and autocomplete

Native Windows PowerShell is also supported with normal line input through `run.ps1`.

## Run on Windows

From PowerShell:

```powershell
cd path\to\Shell-In-Java
Set-ExecutionPolicy -Scope Process Bypass
.\run.ps1
```

The launcher selects the installed Java 25 runtime, builds the project, and starts the shell.

## Run on Linux, macOS, or WSL

```bash
cd path/to/Shell-In-Java
chmod +x run.sh
./run.sh
```

## Build Manually

```bash
mvn clean package -Ddir=target/Shell-In-Java-build
java -jar target/Shell-In-Java-build/Shell-In-Java.jar
```

## Run From the JAR

To share the shell, send the file `Shell-In-Java.jar` from `target/Shell-In-Java-build/`.
The recipient needs Java 25 or newer, then runs:

```bash
java -version
java -jar Shell-In-Java.jar
```

On Windows PowerShell:

```powershell
java -jar .\Shell-In-Java.jar
```

Linux, macOS, or WSL provides raw terminal input and Tab completion. Windows runs with normal line input.

## Supported Commands

Built-ins:

- `cd`
- `pwd`
- `echo`
- `type`
- `exit`

External commands are resolved through `PATH` and executed with Java's `ProcessBuilder`.

Redirection is supported with `>`, `>>`, `2>`, and `2>>`.

## Structure

- `Main.java`: interactive input loop
- `shell/Shell.java`: command dispatch
- `shell/parser`: quoting, escaping, and redirection parsing
- `shell/command`: built-in and external commands
- `shell/process`: process execution
- `shell/terminal`: POSIX raw terminal support through JNA
- `shell/autocomplete`: command and executable completion
