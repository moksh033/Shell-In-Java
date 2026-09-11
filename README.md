# Shell

A lightweight command shell which has been built from scratch using Java 25.

Shell accepts interactive commands, is capable of running external programs, understands quoting and redirection, and has terminal autocomplete facilities on POSIX systems.

## Tech Stack Used

- Java 25
- Maven
- JNA
- ProcessBuilder
- POSIX terminal APIs

## Features

- Built-in commands: `cd`, `pwd`, `echo`, `type`, `help`, and `exit`
- External command execution through the system `PATH`
- Single and double quotes with escape handling
- Standard output and error redirection
- Append and overwrite file modes
- Tab autocomplete on Linux, macOS, and WSL
- Windows PowerShell support with normal line input
- Standalone executable JAR with dependencies included

##  Workflow

The shell follows a simple command pipeline:

The program Main.java obtains its input from the terminal.
The file Parser.java turns the line into arguments and redirection rules.
The built-in command or external process is selected by Shell.java.
4. The built-in features operate directly within the application.
Commands from outside the system are processed via Java's ProcessBuilder.
After each command, terminal support reinstates the earlier terminal state.

## Running the Project

### Windows app

Get the compact Shell-Windows.zip file from the Releases page, extract it and then double-click on Shell.exe. The package comes with a minimal Java runtime and has a size of about 36 MB. The window has the title Shell and initially displays a green symbol artwork with the heading BUILT BY MOKSH.

Leave the extracted `app` and `runtime` folders next to `Shell.exe`.

### Linux, macOS, or WSL

```bash
cd path/to/Shell-In-Java
chmod +x run.sh
./run.sh
```

### Run the JAR directly

Get Shell.jar from the Releases page and run:

```bash
java -jar Shell.jar
```

Windows PowerShell:

```powershell
java -jar .\Shell.jar
```

### Run the Windows app

Get the Shell-Windows.zip file from the Releases page and extract it. Once you have extracted it, open the folder and double-click:

```text
Shell.exe
```

The black Shell terminal interface is opened directly, with no use of PowerShell. Make sure that the extracted `app` and `runtime` folders are kept next to the executable.

If you want to carry out a simple launch using just Java, download `Shell.jar` and then run the command `java -jar Shell.jar` making sure that Java 25 is installed.

## Build From Source

```bash
mvn clean package
java -jar target/Shell-In-Java.jar
```


## Preview

![Shell Preview](Screenshot%202026-09-12%20043057.png)

```text
$ pwd
/home/user/Shell-In-Java

$ echo Hello from Shell-In-Java
Hello from Shell-In-Java

$ type echo


First line > output.txt
Append 'Second line' to output.txt
$ more output.txt
First line
Second line

$ exit
```

## Available Commands

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

## Project Structure

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

ps: Redirection Examples

```text
echo Hello > output.txt
echo "Another line" >> output.txt
unknown-command 2> errors.txt
unknown-command 2>> errors.txt
```

Linux, macOS and WSL offer direct access to terminal input together with Tab completion, while Windows uses standard line-by-line input.

## Repo Link

https://github.com/moksh033/Shell-In-Java
