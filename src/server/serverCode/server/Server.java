package server;

import database.*;
import file_transfer.*;
import commands.*;
import colors.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;


public class Server {
    public final int SERVER_PORT = 6666;
    public final String SERVER_PASSWORD = "1111";
    public final String SERVER_STORAGE_DIR = "./storage/";
    public final String SERVER_LOG_DIR = "./log/";

    private ColorList cl = new ColorList();

    public HashMap<Socket, Clients> clientList = new HashMap<>();
    public HashMap<Socket, Socket> clientsTransferList = new HashMap<>();

    private SimpleDateFormat logFormat = new SimpleDateFormat("dd.MM.yyy_HH:mm:ss");
    private String logName = logFormat.format(new Date()).toString() + ".log";

    public static void main(String[] args) { new Server(); }

    public Server() {
        clearTerminal();
        createDirs();
        //serverLoggIn();
        createServer();
    }
    
    public void clearTerminal() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("clear");
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.print(line);
            }
        } catch (Exception e) {

        } finally {
            process.destroy();
        }
    }

    public void createDirs() {
        try {
            File dir = new File(SERVER_STORAGE_DIR);
            if(!dir.exists()) {
                try {
                    dir.mkdir();
                } catch (SecurityException e) {
                    serverError("Could not make '" + SERVER_STORAGE_DIR + "' directory!");
                    System.exit(1);
                }
            }
        } catch (Exception e) {
            serverError("Failed to make '" + SERVER_STORAGE_DIR + "' directory!");
            System.exit(1);
        }

        try {
            File dir = new File(SERVER_LOG_DIR);
            if(!dir.exists()) {
                try {
                    dir.mkdir();
                } catch (SecurityException e) {
                    serverError("Could not make '" + SERVER_LOG_DIR + "' directory!");
                    System.exit(1);
                }
            }
        } catch (Exception e) {
            serverError("Failed to make '" + SERVER_LOG_DIR + "' directory!");
            System.exit(1);
        }

    }

    public void writeToLog(String msg) {
        try {
            File dir = new File(SERVER_LOG_DIR);
            File file = new File(dir, logName);
            if(file.exists()) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));   
                if(file.length() != 0) { writer.newLine(); }
                writer.append(msg);
                writer.close();  
            } else {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));   
                writer.append(msg);
                writer.close();  
            }
   
    
        } catch (IOException e) {
            serverError("Could not write to log");
            System.exit(1);
        }
    }

    public void serverMsg(String msg) { 
        String fullMsg = ConsoleColors.YELLOW + "[server] " + ConsoleColors.RESET + msg;
        System.out.println(fullMsg); 
        writeToLog("[server] " + msg);
    }

    public void serverError(String msg) { 
        String fullMsg = ConsoleColors.RED + "[error] " + msg + ConsoleColors.RESET;
        System.out.println(fullMsg); 
        writeToLog("[error] " + msg);
    }

    public void serverLoggIn() {
        Scanner sc = new Scanner(System.in);
        String password = "";
        while(!password.equals(SERVER_PASSWORD)) {
            System.out.print("Password:");
            password = sc.next();

            if(!password.equals(SERVER_PASSWORD)) {
                System.out.println("[error] Wrong Password");
            }
        }
        sc.close();
    }

    public void createServer() {
        serverMsg("Creating server");

        ServerSocket serverSocket = null;

        try {
			serverSocket = new ServerSocket(this.SERVER_PORT);
		} catch (Exception e) {
            serverError("Couldnt create server socket on port: " + this.SERVER_PORT + "\nShutting down...");
			closeServer();
        }
        
        listenForClient(serverSocket);

        try {
			serverSocket.close();
		} catch (IOException e) {
            serverError("Couldnt close server socket\nShutting down...");
			closeServer();
		}

    }

    public void listenForClient(ServerSocket serverSocket) {
        serverMsg("Listening for a client...");
		try {
			while (true) {
				Socket newClientSocket = serverSocket.accept(); 
				synchronized(this) {
                    try {
                        setupClient(newClientSocket);
                    } catch (Exception f) {
                        serverError("Couldnt setup client");
                        removeClient(newClientSocket);
                    }
					
				}
			}
		} catch (Exception e) {
            serverError("Couldnt connect to the client");
			closeServer();
		}
    }

    public void setupClient(Socket clientSocket) {
        serverMsg("New client joined: " + clientSocket.getPort());
        DataInputStream input = createInputStream(clientSocket);
        String name = null;
        String password = null;

        while(!verifyUser(name, password) || name == null || password == null) {
            name = requestFromClient(clientSocket, input, "Username");
            if(name.equals("$transferClient")) { break; }
            password = requestFromClient(clientSocket, input, "Password");

        }

        if(!name.equals("$transferClient")) {
            serverMsg("Setting up client: " + clientSocket);
            DateFormat dateFormat = new SimpleDateFormat("dd.mm.yyyy hh:mm:ss");
            Date date = new Date();
            String loggInTime = dateFormat.format(date).toString();
    
            clientList.put(clientSocket, new Clients(name, clientSocket, loggInTime));
            sendToClient(clientSocket, "$interface");
            serverMsg("Added client:" + ConsoleColors.CYAN + 
                      "\n> Username: " + name +
                      "\n> Socket: " + clientSocket + 
                      "\n> Join Time: " + loggInTime + ConsoleColors.RESET);

            
            String users = getOnlineUsersList();
                    
            updateNotificationAllClients(clientSocket,"$users " + users);
            sendToAllClients(clientSocket, " Joined the server...");

            ChatListener connection = new ChatListener(this, clientSocket, new ExecuteCommand(this, clientSocket, cl));
            connection.start();

        } else {
            serverMsg("Setting up transfer client: " + clientSocket);
            setupTransferClient(clientSocket, input);
        }

    }

    public void setupTransferClient(Socket clientSocket, DataInputStream input) {
        String mcp = requestFromClient(clientSocket, input, "Send main client port: ");
        int mainClientPort = Integer.valueOf(mcp);
        for(Map.Entry<Socket, Clients> entry : clientList.entrySet()) {
            Socket mcSocket = entry.getKey();
            if(mainClientPort == mcSocket.getPort()) {
                if(!clientsTransferList.containsKey(mcSocket)) {
                    clientsTransferList.put(mcSocket, clientSocket);
                    serverMsg("Linked Clients:" + ConsoleColors.CYAN + 
                              "\n> " + mcSocket + " [client]" +
                              "\n> " + clientSocket + " [transfer]" + ConsoleColors.RESET);

                } else {
                    removeClient(mcSocket);
                    try {
                        mcSocket.close();
                    } catch (IOException e) {
                        //TODO: handle exception
                    }
                }
                
                break;
            }
        }

        FileTransferListeren connection = new FileTransferListeren(this, clientSocket, new ExecuteTransfer(this, clientSocket));
        connection.start();
        updateNotificationAllClients(clientSocket, "$files " + getAllFiles(" "));
        updateNotificationAllClients(clientSocket, "$users " + getOnlineUsersList());
    }

    public DataInputStream createInputStream(Socket clientSocket) {
        try {
			return new DataInputStream(clientSocket.getInputStream()); // create input stream for listening for incoming messages
		} catch (IOException e) {
            serverError("Could not open input stream!");
            return null;
        }
    }

    public String checkForInput(DataInputStream input, Socket clientSocket) {
        try {
            return input.readUTF();
        } catch (Exception e) {
            serverError("Problem when reading a msg");
            removeClient(clientSocket);
            return null;
        }
    }

    public String requestFromClient(Socket clientSocket, DataInputStream input, String req) {
        sendToClient(clientSocket, "[server] " + req + ":");
        String data = null;
        data = checkForInput(input, clientSocket);
        return data;
    }

    // TODO:
    public boolean verifyUser(String name, String password) {
        return true;
    }

    public void sendToClient(Socket clientSocket, String msg) {
        if(clientSocket != null) {
            try {
                DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream()); // create output stream for sending messages to the client
                output.writeUTF(msg);
            } catch (Exception e) {
                serverError("Couldnt send msg to client... Client: " + clientSocket.getPort());
            }
        }else {

        }
    }

    public void sendToAllClients(Socket clientSocket, String msg) {
        String username = modifyUsername(clientSocket);
        if(username != null) {
            serverMsg(username + msg);
            for(Map.Entry<Socket, Clients> entry : clientList.entrySet()) {
                Socket socket = entry.getKey();
                sendToClient(socket, username + msg);
                sendToClient(clientsTransferList.get(socket), "[TransferClient] " + username + msg);
            }
        }
    }

    public void sendToAllClientsServer(Socket clientSocket, String msg) {
        String username = modifyUsername(clientSocket);
        if(username != null) {
            serverMsg(username + msg);
            for(Map.Entry<Socket, Clients> entry : clientList.entrySet()) {
                Socket socket = entry.getKey();
                sendToClient(socket,"[server]: " + msg);
            }
        }
    }

    public void updateNotificationAllClients(Socket clientSocket, String msg) {
        for(Map.Entry<Socket, Socket> entry : clientsTransferList.entrySet()) {
            Socket socket = entry.getValue();
            sendToClient(socket, msg);
        }
    }

    public String modifyUsername(Socket clientSocket) {
        if(clientList.containsKey(clientSocket)) {
            String name = clientList.get(clientSocket).getName();
            return "[" + name + "]: ";
        } else {
            return null;
        }    
    }

    public String getOnlineUsersList() {
        String users = "";
            
        for(Map.Entry<Socket, Clients> entry : clientList.entrySet()) {
            String name = entry.getValue().getName();
            if(users.equals("")) {
				users = name;
			} else {
				users = users + " " + name;
			}
        }

        return users;
    }
    

    public void removeClient(Socket clientSocket) {
        try {
            clientSocket.close();
        } catch (Exception e) {
            serverError("Could not close client socket");
        }

        sendToAllClients(clientSocket, " Left the server...");
        //serverMsg(clientList.get(clientSocket).getName() + " Left the server...");

        if(clientList.containsKey(clientSocket)) { clientList.remove(clientSocket); }
        if(clientsTransferList.containsKey(clientSocket)) { clientsTransferList.remove(clientSocket); }

        String users = getOnlineUsersList();

        updateNotificationAllClients(clientSocket,"$users " + users);

    }

    public Socket findClientSocket(String username) {
        for(Map.Entry<Socket, Clients> entry : clientList.entrySet()) {
            String clientName = entry.getValue().getName();
            if(clientName.equals(username)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public String getAllFiles(String devider) {
        try {
            File folder = new File("./storage/");
            File[] listOfFiles = folder.listFiles();
            String files = "";
            for (File f : listOfFiles) {
                String fileName = f.getName();
                fileName = fileName.substring(0, fileName.length() - 4);
                if(files.equals("")) {
                    files = fileName;
                } else {
                    files = files + devider + fileName;
                }
            }
            return files;
        } catch (Exception e) {
            //TODO: handle exception
        }
        return "";
    }

    public Socket getMainClient(Socket transferSocket) {
        for(Map.Entry<Socket, Socket> entry : clientsTransferList.entrySet()) {
            Socket socket = entry.getValue();
            if(socket == transferSocket) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void closeServer() { 
        writeToLog("Server Close Time: " + logFormat.format(new Date()).toString()); 
        System.exit(1);
    }
}