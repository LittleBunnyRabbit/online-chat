package file_transfer;
import client.*;

import java.io.*;
import java.util.*;

public class EncoderDecoder {
    private static final String CLIENT_RECEIVING_DIR = "./receiving/";

    public static void encode(String fullFileName, String fileName, TransferClient tc) {
        tc.transferMsg("Started encoding: " + fileName);
        tc.clientInterfaceMsg("Started encoding: " + fileName);

        File file = new File(fullFileName);

        try(FileInputStream fileInput = new FileInputStream(file)) {
            Base64.Encoder encoder = Base64.getMimeEncoder();
            OutputStream outputStream = encoder.wrap(new FileOutputStream(file + ".enc"));
            int _byte;
            while((_byte = fileInput.read()) != -1) {
                outputStream.write(_byte);
            }
            outputStream.close();

        } catch (IOException ioe) {
            tc.transferMsg("Failed to encode!");
            tc.clientInterfaceMsg("Failed to encode!");
            tc.sendMessage("$done");
            System.err.printf("I/O error: %s%n", ioe.getMessage());
        }
        tc.transferMsg("Finished encoding!");
        tc.clientInterfaceMsg("Finished encoding!");
    }

    public static void decode(String fileName, TransferClient tc) {
        tc.transferMsg("Started decoding: " + fileName);
        tc.clientInterfaceMsg("Started decoding: " + fileName);

        File dir = new File(CLIENT_RECEIVING_DIR);
        File file = new File(dir, fileName);
        String fileWDir = file.toPath().toString();
        try(FileOutputStream fileOutput = new FileOutputStream(fileWDir)) {
            Base64.Decoder decoder = Base64.getMimeDecoder();
            InputStream inputStream = decoder.wrap(new FileInputStream(fileWDir + ".enc"));
            int _byte;
            while ((_byte = inputStream.read()) != -1) {
                fileOutput.write(_byte);
            }
            inputStream.close();

        } catch (IOException ioe) {
            tc.transferMsg("Failed to decode!");
            tc.clientInterfaceMsg("Failed to decode!");
            System.err.printf("I/O error: %s%n", ioe.getMessage());
        }

        tc.transferMsg("Finished decoding!");
        tc.clientInterfaceMsg("Finished decoding!");
        File delF = new File(file + ".enc");
        delF.delete();
    }  
}