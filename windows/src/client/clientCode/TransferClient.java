import java.io.*;
import java.net.*;
import java.lang.*;


public class TransferClient extends Thread {
    private final int SERVER_PORT = 6666;
    private final String CLIENT_SENDING_DIR = "../sending/";
    private final String CLIENT_RECEIVING_DIR = "../receiving/";

    private String serverIPV4;

    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    //private BufferedReader reader;
    private TransferClientReceiver receiver;
    private Client client;

    public TransferClient(String serverIPV4, int clientPort, Client client) throws Exception {
        this.serverIPV4 = serverIPV4;
        this.client = client;

        socket = createSocketConnection();
        input = createInputStream();
        output = createOutputStream();
        createChatReceiver();
        sendMessage("$transferClient");
        sendMessage(String.valueOf(clientPort));
        createDirs();
    }

    public void transferMsg(String msg) { System.out.println("\033[0;33m" + "[transfer]: " + "\033[0m" + msg); }

    public void createDirs() {
        File dir = new File(CLIENT_SENDING_DIR);
        if(!dir.exists()) {
            try {
                dir.mkdir();
            } catch (SecurityException e) {
                System.out.println("Could not make '" + CLIENT_SENDING_DIR + "' directory!");
                System.exit(1);
            }
        }

        dir = new File(CLIENT_RECEIVING_DIR);
        if(!dir.exists()) {
            try {
                dir.mkdir();
            } catch (SecurityException e) {
                System.out.println("Could not make '" + CLIENT_RECEIVING_DIR + "' directory!");
                System.exit(1);
            }
        }
    }

    public Socket createSocketConnection() {
        try {
            return new Socket(serverIPV4, SERVER_PORT);
        } catch (Exception e) {
            transferMsg("Could not create socket connection...");
            System.exit(1);
        }
        return null;
    }

    public DataInputStream createInputStream() {
        try {
            return new DataInputStream(socket.getInputStream());
        } catch (Exception e) {
            transferMsg("Could not create input stream...");
            System.exit(1);
        }
        return null;
    }

    public DataOutputStream createOutputStream() {
        try {
            return new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            transferMsg("Could not create output stream...");
            System.exit(1);
        }
        return null;
    }

    public void createChatReceiver() {
        try {
            receiver = new TransferClientReceiver(this, client);
            receiver.start();
        } catch (Exception e) {
            transferMsg("Could not create ChatReceiver");
            System.exit(1);
        }
    }

    public void reqToSendFile(String fileName) {
        File folder = new File(CLIENT_SENDING_DIR);
        File[] listOfFiles = folder.listFiles();
        String files = "";
        Boolean fileExists = false;

        for (File f : listOfFiles) {
            String name = f.getName();
            System.out.println(name);
            name = name.substring(0, name.length());
            if(name.equals(fileName)) {
                fileExists = true;
                break;
            }
        }

        if(fileExists) {
            transferMsg("Requesting to send: " + fileName);
            try {
                output.writeUTF("$send " + fileName);
                output.flush();
    
            } catch (IOException e) {
                transferMsg("Could not send message...");
            }
        } else {
            transferMsg("That file doesnt exist!");
        }

    }

    public void reqToDownloadFile(String fileName) {
        transferMsg("Requesting to download: " + fileName);
		try {
            output.writeUTF("$download " + fileName);
            output.flush();

		} catch (IOException e) {
            transferMsg("Could not send message...");
		}
    }

    public void sendMessage(String message) {
		try {
            output.writeUTF(message);
            output.flush();

		} catch (IOException e) {
            transferMsg("Could not send message...");
		}
    }
    
    public void closeClient() {
        try {
            output.close();
            input.close();
            //reader.close();
            socket.close(); 
        } catch (Exception e) {
            transferMsg("Could not close client");
            System.exit(1);
        }
    }

    public String checkForInput() {
        try {
            return input.readUTF();
        } catch (Exception e) {
            transferMsg("Could not read input");
            System.exit(1);
            return null;
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
        EncoderDecoder.encode(fileName, this);

        transferMsg("Started reading file: " + fileName);
        clientInterfaceMsg("Started reading file: " + fileName);

        try {
            File dir = new File(CLIENT_SENDING_DIR);
            File file = new File(dir, fileName + ".enc");

            BufferedReader br = new BufferedReader(new FileReader(file));
            String str;

            while((str = br.readLine()) != null) { 
                sendMessage(str);
            }
            
            file.delete();

        } catch (IOException e) {
            transferMsg("Failed to read file!");
            clientInterfaceMsg("Failed to read file!");
        }
        sendMessage("$done");
        transferMsg("Finished reading file!");
        clientInterfaceMsg("Finished reading file!");
    }
    
    public void downloadFile(String fileName) {
        transferMsg("Started writing file: " + fileName);
        clientInterfaceMsg("Started writing file: " + fileName);

        File dir = new File(CLIENT_RECEIVING_DIR);
        File file = new File(dir, fileName + ".enc");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            String encodedData = "1";
            while(!encodedData.equals("$done")) {
                encodedData = checkForInput();
                if(!encodedData.equals("$done")) {
                    try {
                        writer.write(encodedData);
                        writer.newLine();
                    } catch (IOException e) {
                        transferMsg("Unable to write line!");
                        clientInterfaceMsg("Unable to write line!");
                    }

                } else {
                    writer.close();
                    break;
                }
            }

        } catch (IOException e) {
            transferMsg("Failed to write file!");
            clientInterfaceMsg("Failed to write file!");
        }
        transferMsg("Finished writing file!");
        clientInterfaceMsg("Finished writing file!");

        EncoderDecoder.decode(fileName, this);
    }

    public void clientInterfaceMsg(String msg) { client.interfaceAppendMsg("[client]: " + msg); }
    
}
