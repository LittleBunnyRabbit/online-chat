package client;

import file_transfer.*;
import face.*;

import java.io.*;
import java.net.*;

public class ChatReceiver extends Thread {	
	private DataInputStream input;
	private DataOutputStream output;
	private Client client;
	private Interface intf = null;


	public ChatReceiver(DataInputStream input, Client client, DataOutputStream output) {
		this.input = input;
		this.client = client;
		this.output = output;
	}

	public void run() {
        while(true) {
            String dataRecived = client.checkForInput(input);

            if(input == null || dataRecived == null) {
                break;
            } else if (dataRecived.length() == 0) {
                continue;
            }

			if(dataRecived.charAt(0) == '$') {
				String[] data = client.splitInput(dataRecived.substring(1));
				switch (data[0]) {
					case "interface":
						if(intf == null) {
							intf = new Interface(client);
							client.storeInterface(intf);
							client.closeBufferReader();
							client.clientMsg("TransferClient created");
							client.createTC();
						}
						break;

					default:
						if(intf != null) { client.interfaceAppendMsg(dataRecived); }
						else { System.out.println(dataRecived); }
						break;
				}
				
			} else {
				if(intf != null) { client.interfaceAppendMsg(dataRecived); }
				else { System.out.println(dataRecived); }
			}
        }
	}
}
