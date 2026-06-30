# 🧮 Java Socket Math Server



---

## 📌 1. Project Description
This project implements a Client-Server architecture based on the TCP protocol. The Server accepts requests for mathematical operations from multiple clients simultaneously (Multithreaded) and logs all activity in a dedicated log file.

## 📂 2. File Structure
The project consists of the following core files:

* **`MathServer.java`**: The main Server class. It creates the `ServerSocket` on port `5555` and assigns each new connection to a separate thread.
* **`ClientHandler.java`**: The class that manages communication with each individual client. It implements the custom communication protocol, performs calculations, maintains user history, and handles file logging.
* **`MathClient.java`**: The client application. It connects to the Server, reads commands from the user, and displays the server's responses.
* **`server.log`**: An auto-generated file containing the history of all requests (Timestamp, IP Address, Command). *(Note: This file should not be pushed to version control)*.

## 🛠 3. Compilation Instructions
To compile the code, open a terminal in the project directory and run the following command:

```bash
javac MathServer.java ClientHandler.java MathClient.java
```

## 🚀 4. Execution Instructions
The application requires at least two separate terminal windows: one for the Server and one (or more) for the Clients.

### Step 1: Start the Server
In your first terminal, run:

```bash
java MathServer
```
*(You will see a message confirming: "O Server ξεκίνησε στην θύρα 5555...")*

### Step 2: Start a Client
In a second terminal, run:

```bash
java MathClient
```
*(You will see a connection success message and a prompt to enter commands).*

## ⌨️ 5. Supported Commands
The user can input the following commands:

* `ADD x y` : Addition (e.g., `ADD 10 5` -> Result: 15.0)
* `SUB x y` : Subtraction (e.g., `SUB 10 2`)
* `MUL x y` : Multiplication (e.g., `MUL 5 5`)
* `DIV x y` : Division (e.g., `DIV 20 4`)
* `POW x y` : Power / Exponentiation (e.g., `POW 2 3` -> 2 to the power of 3)
* `LAST` : Displays the result of the last successful calculation.
* `COUNT` : Displays the total number of requests made by the current user.
* `HIST` : Displays a list of the user's last 5 commands (History).
* `HELP` : Shows the list of available commands.
* `EXIT` : Disconnects from the Server and terminates the client program.

## 🧠 6. Implementation Notes
* **Multithreaded Architecture**: The Server spawns a new thread for every client, allowing concurrent request handling without blocking.
* **Thread-Safe Logging**: Writing to the `server.log` file is `synchronized` to prevent race conditions when multiple threads attempt to log data simultaneously.
* **Stateful Connections**: The history and statistical data (`COUNT`, `LAST`, `HIST`) are maintained independently for each connected client in memory.
