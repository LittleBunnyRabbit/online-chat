package commands;

import server.*;
import colors.*;
import file_transfer.*;
import database.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;

public class ExecuteCommand {
    private Server server;
    private Socket clientSocket;
    private ColorList cl;

    public ExecuteCommand(Server server, Socket clientSocket, ColorList cl) {
        this.server = server;
        this.clientSocket = clientSocket;
        this.cl = cl;
    }

    public void getCommand(String data) {
        String[] split = splitInput(data);
        String command = split[0];
        data = split[1];
        runCommand(command.toUpperCase(), data);
    }

    public void runCommand(String command, String data) {
        switch (command) {
            case "DM":
                command_DM(data);
                break;

            case "HELP":
                command_HELP();
                break;

            case "LF":
                command_LISTFILES();
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

    public void command_HELP() {
        String br = "----------------------------------------";
        String intro = "Help list: ";
        String dm = "- DM: /dm {user} {text}";
        String edit = "- EDIT: /edit {color_code} {text}\n" + 
                      "  Example: /edit black:b:br \n" +
                      "  Codes: :b, :u, :bg, :br, :b:br, :bg:br "; 
        String msg = String.format("%s\n%s\n%s\n%s\n%s", br, intro, dm, edit, br);
        server.sendToClient(clientSocket, msg);
    }

    public void command_DM(String data) {
        String[] split = splitInput(data);
        String user = split[0];
        data = split[1];

        String msg = null;

        for(Map.Entry<Socket, Clients> entry : server.clientList.entrySet()) {
            String clientName = entry.getValue().getName();
            if(clientName.equals(user)) {
                msg = "[" + server.clientList.get(clientSocket).getName() + " -> " + user + "]: " + data;
                server.serverMsg(msg);
                server.sendToClient(clientSocket, msg);
                server.sendToClient(server.findClientSocket(user), msg);
                continue;
            }
        }
        if(msg == null) {
            server.sendToClient(clientSocket, "[server] Wrong username!");
        }
    }

    public void command_LISTFILES() {
        String files = server.getAllFiles(", ");
        server.sendToClient(clientSocket, files);
    }
}
