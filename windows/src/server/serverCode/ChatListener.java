import java.io.*;
import java.net.*;
import java.util.*;

public class ChatListener extends Thread {
    private Server server;
    private Socket clientSocket;
    private ExecuteCommand exc;

    public ChatListener(Server server, Socket clientSocket, ExecuteCommand exc) {
        this.server = server;
        this.clientSocket = clientSocket;
        this.exc = exc;
    }

    public void run() {
        server.serverMsg("Client connected! " + this.clientSocket.getPort());
        DataInputStream input = server.createInputStream(this.clientSocket);

        while(true) {
            String dataRecived = null;
            try {
                dataRecived = server.checkForInput(input, clientSocket);
            } catch (Exception e) {
                server.serverError("Failed to read input");
                server.removeClient(clientSocket);
            }
            

            if(input == null || dataRecived == null) {
                break;
            } else if (dataRecived.length() == 0) {
                continue;
            }

            if(dataRecived.charAt(0) == '/') {
                exc.getCommand(dataRecived.substring(1));
                continue;
            }

            if(dataRecived.charAt(0) == '$') {
                server.sendToClient(clientSocket, "[server]: '$' in the begining is reserver for server only!");
                continue;
            }

            server.sendToAllClients(clientSocket, dataRecived);
        }
    }
}

