package client;

import face.*;

import java.io.*;
import java.net.*;


public class Client extends Thread {
    private final int SERVER_PORT = 6666;
    protected static String serverIPV4;

    private ChatReceiver message_receiver;
    private DataOutputStream out;
    private Boolean chatSender = true;
    private BufferedReader reader;
    private TransferClient tc;
    private Socket socket;
    private Interface intf;

    public static void main(String[] args) {
        serverIPV4 = args[0];
        try {
            new Client();
        } catch (Exception e) {
            System.out.println("\033[0;31m" + "[error]: " + "Could not create new Client");
            System.exit(1);
        }
    }

    public Client() throws Exception {
        clearTerminal();
        socket = createSocketConnection();
        DataInputStream input = createInputStream(socket);
        DataOutputStream output = createOutputStream(socket);
        out = output;
        createChatReceiver(input, output);
        reader = createChatSender(output, input);
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

    public void clientMsg(String msg) { System.out.println("\033[0;33m" + "[client] " + "\033[0m" + msg); }

    public Socket createSocketConnection() {
        try {
            return new Socket(serverIPV4, SERVER_PORT);
        } catch (Exception e) {
            clientMsg("Could not create socket connection...");
            System.exit(1);
        }
        return null;
    }

    public DataInputStream createInputStream(Socket socket) {
        try {
            return new DataInputStream(socket.getInputStream());
        } catch (Exception e) {
            clientMsg("Could not create input stream...");
            System.exit(1);
        }
        return null;
    }

    public DataOutputStream createOutputStream(Socket socket) {
        try {
            return new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            clientMsg("Could not create output stream...");
            System.exit(1);
        }
        return null;
    }

    public void createChatReceiver(DataInputStream input, DataOutputStream output) {
        try {
            message_receiver = new ChatReceiver(input, this, output);
            message_receiver.start();
        } catch (Exception e) {
            clientMsg("Could not create ChatReceiver");
            System.exit(1);
        }
    }
    
    public BufferedReader createChatSender(DataOutputStream output, DataInputStream input) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(System.in));
            String userInput;
            while (((userInput = reader.readLine()) != null) && chatSender) {
                if(!chatSender) { break; }
                sendMessage(userInput);
            }
            reader.close();
        } catch (Exception e) {
            clientMsg("Could not create ChatSender");
            System.exit(1);
        }
       
        return reader;
    }

    public void closeBufferReader() {
        try {
            chatSender = false;  
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            clientMsg("Could not close BufferReader");
        }        
    }
    
    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
            out.flush();
    
        } catch (IOException e) {
            clientMsg("Could not send message...");
        }
    }

    public void sendFile(String fullFileName, String fileName) {
        tc.reqToSendFile(fullFileName, fileName);
    }

    public void downloadFile(String fileName) {
        tc.reqToDownloadFile(fileName);
    }
    
    private void closeClient(Socket socket, DataInputStream input, DataOutputStream output, BufferedReader reader) {
        try {
            output.close();
            input.close();
            reader.close();
            socket.close(); 
        } catch (Exception e) {
            clientMsg("Could not close client");
            System.exit(1);
        }
    }

    public String checkForInput(DataInputStream input) {
        try {
            return input.readUTF();
        } catch (Exception e) {
            clientMsg("Could not read input");
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

    public void createTC() {
        CreateTransferClient startTC = new CreateTransferClient(serverIPV4, this, socket.getLocalPort());
        startTC.start();
    }

    public void storeTC(TransferClient tc) { this.tc = tc; }

    public void storeInterface(Interface intf) { this.intf = intf; }

    public void interfaceAppendMsg(String msg) { intf.appendChat(msg); }

    public void interfaceUpdateOnlineList(String users) { intf.updateOnlineList(users); }

    public void interfaceUpdateFileList(String files) { intf.updateFileList(files); }
}
