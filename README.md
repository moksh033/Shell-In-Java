# Helix

A lightweight desktop command shell built from scratch using Java.

Helix accepts interactive commands, is capable of running external programs, understands quoting and redirection, provides command history recall, and has terminal autocomplete facilities on POSIX systems.

## Tech Stack Used

- **Java 21 / 25**
- **Maven**
- **JNA (Java Native Access)**
- **ProcessBuilder**
- **POSIX terminal APIs (Termios)**
- **Java Swing GUI**

## Features

- **Built-in commands**: `clear`, `cd`, `pwd`, `echo`, `type`, `help`, and `exit`
- **Arrow-Up History**: Press `↑` (Up Arrow) to automatically type the last executed command
- **External command execution**: Seamlessly run any program available in your system `PATH`
- **Output & Error Redirection**: Overwrite (`>`, `1>`, `2>`) and append (`>>`, `1>>`, `2>>`) file redirection
- **Quotes & Escapes**: Single and double quotes with full escape handling
- **Tab autocomplete**: Interactive command completion on Linux, macOS, and WSL
- **Cross-Platform**: Runs on Windows, Linux, and macOS
- **Standalone Windows Desktop App**: Portable native executable bundle (`Shell.exe`) with bundled runtime
- **Fat JAR**: Standalone executable JAR with all dependencies packaged

## Workflow

The shell follows a clean execution pipeline:

1. **Input**: `RainShower.java` (GUI) or `Main.java` (CLI) receives user input.
2. **Parsing**: `Parser.java` tokenizes the input, extracts arguments, and separates redirection directives.
3. **Dispatch**: `Shell.java` checks for built-in commands first.
4. **Execution**:
   - Built-ins execute directly within the shell environment.
   - External commands are executed through `SystemProcessExecutor` via `ProcessBuilder`.
5. **Redirection & Cleanup**: Handles file streams, errors, and restores terminal/view state.

## Running the Project

### 1. Windows App (Recommended)

1. Download **`Shell-Windows.zip`** from [Releases](https://github.com/moksh033/Shell-In-Java/releases).
2. Extract the ZIP.
3. Open the folder and double-click:
   ```text
   Shell.exe
   ```

> **Note:** Windows Defender SmartScreen might flag `Shell.exe` as an unrecognized app and warn that it is unsafe to run. This is a common false positive for unsigned indie software. It is completely safe to run; simply click **More info** -> **Run anyway**.

> Keep the `app` and `runtime` folders in the same directory as `Shell.exe`.

### 2. Standalone JAR

Download **`Shell.jar`** from [Releases](https://github.com/moksh033/Shell-In-Java/releases) and run:

```bash
java -jar Shell.jar
```

On Windows PowerShell:
```powershell
java -jar .\Shell.jar
```

### 3. Linux, macOS, or WSL

```bash
git clone https://github.com/moksh033/Shell-In-Java.git
cd Shell-In-Java
chmod +x run.sh
./run.sh
```

## Preview

![Shell Preview](Screenshot%202026-09-13%20235517.png)

```text
$ pwd
/home/user/Shell-In-Java

$ echo Hello from Shell-In-Java
Hello from Shell-In-Java

$ type echo
echo is a shell builtin

$ echo "First line" > output.txt
$ echo "Second line" >> output.txt
$ more output.txt
First line
Second line

$ clear
```

## Available Commands

### Built-in Commands

| Command | Description | Example |
|---|---|---|
| `clear` | Clears the terminal output screen | `clear` |
| `cd <dir>` | Changes the current working directory | `cd ..`, `cd C:\Users` |
| `pwd` | Prints current working directory path | `pwd` |
| `echo <text>` | Prints text or arguments to standard output | `echo Hello World` |
| `type <cmd>` | Checks if a command is a built-in or finds its path | `type cd`, `type git` |
| `help` | Displays help message with command list | `help` |
| `exit` | Closes the Shell application | `exit` |

### External Commands

Any command in your system's `PATH` works directly inside Shell:

| Category | Commands |
|---|---|
| **Files & Dirs** | `dir`, `ls`, `cat`, `more`, `mkdir`, `rmdir`, `rm`, `touch`, `cp`, `mv` |
| **System Info** | `whoami`, `where`, `which`, `hostname`, `ver`, `uname -a`, `java -version` |
| **Networking** | `ping`, `curl`, `ipconfig`, `ifconfig`, `nslookup`, `netstat` |
| **Developer Tools** | `git`, `python`, `node`, `npm`, `javac`, `mvn`, `docker` |
| **Process & Utilities** | `echo`, `findstr`, `grep`, `sort`, `tar`, `zip` |

### Keyboard Shortcuts

- **`↑` (Up Arrow)**: Automatically types the last executed command into the input box
- **`Enter` / `RUN`**: Executes the entered command
- **`Tab`** *(Linux / macOS)*: Autocompletes matching commands and executables

## Stream Redirection

Shell supports standard output and error redirection:

```text
# Overwrite standard output
echo Hello > output.txt

# Append standard output
echo "Another line" >> output.txt

# Overwrite standard error
unknown-command 2> errors.txt

# Append standard error
unknown-command 2>> errors.txt

# Separate standard output and error
build-command 1> build.log 2> error.log
```

## Build From Source

Requirements: Java 21+ and Maven.

```bash
# Build the standalone JAR
mvn clean package

# Run the built JAR
java -jar target/Shell.jar

# Build the native Windows app image (jpackage)
jpackage --type app-image --name Shell --input target --main-jar Shell.jar --main-class RainShower --dest target
```

## Repo Link

https://github.com/moksh033/Shell-In-Java
