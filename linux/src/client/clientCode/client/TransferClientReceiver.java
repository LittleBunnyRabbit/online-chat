package client;

import file_transfer.*;

import java.lang.*;

public class TransferClientReceiver extends Thread {	
    private TransferClient tc;
    private Client client;


	public TransferClientReceiver(TransferClient tc, Client client) {
        this.tc = tc;
        this.client = client;
	}

	public void run() {
        while(true) {
            String dataRecived = tc.checkForInput();

            if(dataRecived == null) {
                break;
            } else if (dataRecived.length() == 0) {
                continue;
            }

            if(dataRecived.charAt(0) == '$') {
				String[] data = tc.splitInput(dataRecived.substring(1));
				switch (data[0]) {                
                    case "send":
                        tc.sendFile(data[1]);
						break;

                    case "download":
                        tc.downloadFile(data[1]);
                        break;

                    case "users":
                        client.interfaceUpdateOnlineList(data[1]);
						break;
					
                    case "files":
                        client.interfaceUpdateFileList(data[1]);
                        break;
                        
                    default:
                        tc.transferMsg("Wrong command! " + data[0]);
                        break;
                }	
			}
        }
	}
}
