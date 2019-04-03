import java.io.*; 
import java.net.*; 

public class ReaderWriterServer {
    public static String readTextFile(String fileName, Socket clientSocket, Server server) {
        server.serverMsg("Started reading file: " + fileName);
        try {
            File dir = new File("../storage/");
            File file = new File(dir, fileName + ".txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String str;
            while((str = br.readLine()) != null) {
                return str;
            }
        } catch (IOException e) {
            server.serverError("Unable to read file");
            server.sendToClient(clientSocket, "[server] Unable to read file");
        }
        server.serverMsg("Finished reading file!");
        return "";
    }

    public static void writeTextFile(String fileName, String data, Socket clientSocket, Server server, File file) {
        server.serverMsg("Started writing file: " + fileName);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.append(data);
            writer.close();
        } catch (IOException e) {
            server.serverError("Unable to write file");
            server.sendToClient(clientSocket, "[server] Unable to write file");
        }
        server.serverMsg("Finished writing file");
    }

    public static void deleteFile(String fileName) {
        File file = new File(fileName);
        try {
            file.delete();
        } catch (Exception e) {
            System.out.println("Could not delete file");
        }
    }
}

