import java.io.*;
import java.net.*;

public class CreateTransferClient extends Thread {	
    private String serverIPV4;
    private Client client;
    private int clientPort;

	public CreateTransferClient(String serverIPV4, Client client, int clientPort) {
        this.serverIPV4 = serverIPV4;
        this.client = client;
        this.clientPort = clientPort;
	}

	public void run() {
        try {
            TransferClient tc = new TransferClient(serverIPV4, clientPort, client);
            client.storeTC(tc);
        } catch (Exception e) {
            //TODO: handle exception
        }
    }
}
