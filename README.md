# Shell-In-Java

> A lightweight command shell built from scratch in Java 25, wrapped in the RainShower terminal interface.

Shell-In-Java accepts interactive commands, runs external programs, understands quoting and redirection, and provides terminal autocomplete on POSIX systems.

## ✨ Technologies

- Java 25
- Maven
- JNA
- ProcessBuilder
- POSIX terminal APIs

## 🚀 Features

- Built-in commands: `cd`, `pwd`, `echo`, `type`, `help`, and `exit`
- External command execution through the system `PATH`
- Single and double quotes with escape handling
- Standard output and error redirection
- Append and overwrite file modes
- Tab autocomplete on Linux, macOS, and WSL
- Windows PowerShell support with normal line input
- Standalone executable JAR with dependencies included

## 🧠 The Process

The shell follows a simple command pipeline:

1. `Main.java` reads input from the terminal.
2. `Parser.java` converts the line into arguments and redirection rules.
3. `Shell.java` selects a built-in command or external process.
4. Built-ins execute directly inside the application.
5. External commands run through Java's `ProcessBuilder`.
6. Terminal support restores the previous terminal state after each command.

## 📦 Running the Project

### Windows app

Download `RainShower-Windows.zip` from the Releases page, extract it, and double-click `RainShower.exe`.

Keep the extracted `app` and `runtime` folders beside `RainShower.exe`.

### Linux, macOS, or WSL

```bash
cd path/to/Shell-In-Java
chmod +x run.sh
./run.sh
```

### Run the JAR directly

Download `Shell-In-Java.jar` from the Releases page and run:

```bash
java -jar Shell-In-Java.jar
```

Windows PowerShell:

```powershell
java -jar .\Shell-In-Java.jar
```

### Run the Windows app

Download `RainShower-Windows.zip` from the Releases page and extract it. Open the extracted folder and double-click:

```text
RainShower.exe
```

This opens the black RainShower terminal interface directly, without PowerShell. Keep the extracted `app` and `runtime` folders beside the executable.

For a simple Java-only launch, download `Shell-In-Java.jar` and run `java -jar Shell-In-Java.jar` with Java 25 installed.

## 🧪 Build From Source

```bash
mvn clean package
java -jar target/Shell-In-Java.jar
```

## 🖥️ Preview

```text
$ pwd
/home/user/Shell-In-Java

$ echo Hello from Shell-In-Java
Hello from Shell-In-Java

$ type echo


$ echo First line > output.txt
$ echo Second line >> output.txt
$ cat output.txt
First line
Second line

$ exit
```

## 🧰 Available Commands

| Command | Description |
|---------|-------------|
| `cd <directory>` | Change the current directory |
| `pwd` | Print the current working directory |
| `echo <text>` | Print text to the terminal |
| `type <command>` | Identify built-in and external commands |
| `help` | Show the integrated Shell-In-Java commands |
| `exit` | Close the shell |

External commands available in the system `PATH` can also be executed:

```text
ls
java -version
whoami
cat file.txt
```

## 📁 Project Structure

```text
src/main/java/
├── Main.java
└── shell/
	├── Shell.java
	├── autocomplete/
	├── command/
	├── environment/
	├── io/
	├── parser/
	├── process/
	└── terminal/
```

## 🔀 Redirection Examples

```text
echo Hello > output.txt
echo Another line >> output.txt
unknown-command 2> errors.txt
unknown-command 2>> errors.txt
```

Linux, macOS, and WSL provide raw terminal input and Tab autocomplete. Windows uses normal line-based input.

## 🔗 Repository

https://github.com/moksh033/Shell-In-Java
