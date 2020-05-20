import java.io.*;
import java.net.*;

public class FileTransferListeren extends Thread {
    private Server server;
    private Socket clientSocket;
    private ExecuteTransfer et;

    public FileTransferListeren(Server server, Socket clientSocket, ExecuteTransfer et) {
        this.server = server;
        this.clientSocket = clientSocket;
        this.et = et;
    }

    public void run() {
        server.serverMsg("TransferClient connected! " + this.clientSocket.getPort());
        DataInputStream input = server.createInputStream(this.clientSocket);
        
        while(true) {
            String dataRecived = null;
            try {
                dataRecived = server.checkForInput(input, clientSocket);
            } catch (Exception e) {
                System.err.println(e);
                server.removeClient(clientSocket);
            }
            

            if(input == null || dataRecived == null) {
                break;
            } else if (dataRecived.length() == 0) {
                continue;
            }

            if(dataRecived.charAt(0) == '$') {
                et.getCommand(dataRecived.substring(1));
                continue;
            }

            System.out.println(dataRecived);

            //server.sendToAllClients(clientSocket, dataRecived);
        }
    }
}
