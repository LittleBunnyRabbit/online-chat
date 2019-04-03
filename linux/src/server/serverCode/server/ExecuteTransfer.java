package server;

import server.*;
import colors.*;
import file_transfer.*;
import database.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class ExecuteTransfer {
    public final String SERVER_STORAGE_DIR = "./storage/";

    private Server server;
    private Socket clientSocket;
    private String clientName;

    public ExecuteTransfer(Server server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
        this.clientName = server.clientList.get(server.getMainClient(clientSocket)).getName();
    }

    public void getCommand(String data) {
        String[] split = splitInput(data);
        String command = split[0];
        String fileName = split[1];
        runCommand(command, fileName);
    }

    public void runCommand(String command, String fileName) {
        switch (command) {
            case "send":
                server.serverMsg("Got a send request from: [" + clientName + "] for '" + fileName + "'");
                sendFile(fileName);
                break;

            case "download":
                server.serverMsg("Got a download request from: [" + clientName + "] for '" + fileName + "'");
                downloadFile(fileName);
                break;

            default:
                server.sendToClient(clientSocket, "[server] that command doesnt exist!");
                break;
        }
    }

    public String[] splitInput(String data) {
        String firstWord = "";
        int dataLength = data.length();
        for (int i = 0; i < dataLength; i++) {
            char c = data.charAt(i);
            if(c == ' ') {
                data = data.substring(i + 1);
                break;
            } else {
                firstWord = firstWord + c;
            }
        }
        return new String[]{firstWord, data};
    }

    public void sendFile(String fileName) {
        if(!fileName.equals("") && !fileName.equals(" ")) {
            sendToMainClient("Started downloading: " + fileName);
            server.serverMsg(ConsoleColors.CYAN + "Downloading file to the server: : '" + fileName + "' to [" + clientName + "]" + ConsoleColors.RESET);
            server.sendToClient(clientSocket, "$send " + fileName);
                
            File dir = new File(SERVER_STORAGE_DIR);

            File file = new File(dir, fileName + ".enc"); 
            String data = "1";
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                while (!data.equals("$done")) {
                    data = server.checkForInput(server.createInputStream(clientSocket), clientSocket);
                    if(!data.equals("$done")) {
                        writer.write(data);
                        writer.newLine();
                    } else {
                        break;
                    }
                }

                writer.close();
    
            } catch (IOException e) {
                server.serverError("Unable to write file");
            }
            sendToMainClient("Finished downloading!");
            server.serverMsg(ConsoleColors.CYAN + "Finished downloading file to the server: '" + fileName + "' to [" + clientName + "]" + ConsoleColors.RESET);
            server.sendToAllClientsServer(server.getMainClient(clientSocket), "New file added: " + fileName);
            updateFiles();

    
        } else {
            sendToMainClient("File name needed!");
        }

    }

    public void downloadFile(String fileName) {
        if(!fileName.equals("") && !fileName.equals(" ")) {
            File folder = new File(SERVER_STORAGE_DIR);
            File[] listOfFiles = folder.listFiles();
            String files = "";
            Boolean fileExists = false;

            for (File f : listOfFiles) {
                String name = f.getName();
                name = name.substring(0, name.length() - 4);
                if(name.equals(fileName)) {
                    fileExists = true;
                    break;
                }
            }

            if(fileExists) {
                sendToMainClient("Started sending: " + fileName);
                server.serverMsg(ConsoleColors.CYAN + "Sending file from the server: '" + fileName + "' to [" + clientName + "]" + ConsoleColors.RESET);

                server.sendToClient(clientSocket, "$download" + " " + fileName);

                try {
                    File dir = new File(SERVER_STORAGE_DIR);
                    File file = new File(dir, fileName + ".enc");
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String str;
                    while((str = br.readLine()) != null) {
                        server.sendToClient(clientSocket, str); 
                    }
                    server.sendToClient(clientSocket, "$done"); 
                } catch (IOException e) {
                    server.serverError("Unable to read file");
                }

                sendToMainClient("Finished sending!");
                server.serverMsg(ConsoleColors.CYAN + "Finished sending file from the server: '" + fileName + "' to [" + clientName + "]" + ConsoleColors.RESET);
            } else {
                sendToMainClient("That file doesnt exist!");
            }
        } else {
            sendToMainClient("Wrong name!");
        }
    } 

    public void updateFiles() {
        server.updateNotificationAllClients(clientSocket, "$files " + server.getAllFiles(" "));
    }

    public void sendToMainClient(String msg) {
        Socket mainClientSocket = server.getMainClient(clientSocket);
        if(mainClientSocket != null) { server.sendToClient(mainClientSocket, "[server]: " + msg); }
    }
}
