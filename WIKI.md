# Shell

## Shell Interface

Shell opens as a sleek black desktop terminal named `Shell`. Its green symbol-art heading displays **HELIX**.

## Download and Run

1. Download the compact `Shell-Windows.zip` from release `v1.1.0`.
2. Extract the ZIP without separating the `app` and `runtime` folders.
3. Open the extracted folder.
4. Double-click `Shell.exe`.

The application starts directly as a native Windows desktop terminal without requiring PowerShell or an external console launcher.

## Controls & Keyboard Shortcuts

- **`↑` Up Arrow**: Automatically recalls and types the last executed command into the prompt.
- **`Enter` / `RUN` button**: Executes the current command.
- **`Tab`** *(Linux / macOS / WSL)*: Auto-completes built-in and external commands.

## Built-In Commands

```text
clear           Clear the terminal screen
help            Show available commands and examples
cd <directory>  Change the current working directory (supports .. and absolute/relative paths)
pwd             Print the current working directory path
echo <text>     Print text or arguments to standard output
type <command>  Identify whether a command is built-in or locate its binary in PATH
exit            Close the Shell
```

## Supported External Commands

Any command available in your system's `PATH` can be run directly inside Shell:

### File & Directory Management
```text
dir / ls        List contents of directories
cat <file>      Display file contents
more <file>     Display text file screen-by-screen
mkdir <dir>     Create a new directory
rmdir / rm      Remove files or directories
```

### System & Environment
```text
whoami          Print current user name
where / which   Find location of an executable in PATH
hostname        Display system hostname
java -version   Display installed Java runtime version
```

### Networking & Web
```text
ping <host>     Send ICMP echo requests to network hosts
curl <url>      Transfer data from or to a server
ipconfig        Display network adapter configuration
```

### Developer Tools
```text
git <args>      Run Git version control commands
python / node   Run script interpreters
javac / mvn     Compile and build source code
```

## Redirection

Shell supports output and error stream redirection:

```text
echo "Hello World" > output.txt      # Overwrite standard output to file
echo "New log entry" >> output.txt   # Append standard output to file
command 2> errors.txt                # Overwrite standard error to file
command 2>> errors.txt               # Append standard error to file
echo text 1> out.txt 2> err.txt      # Redirect stdout and stderr separately
```

## Build From Source

```bash
mvn clean package
java -jar target/Shell.jar
```

The native Windows app image can be packaged with Java:

```powershell
jpackage --type app-image --name Shell --input target --main-jar Shell.jar --main-class RainShower --dest target
```
