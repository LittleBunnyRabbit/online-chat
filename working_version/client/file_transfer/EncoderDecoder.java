package file_transfer;
import client.*;

import java.io.*;
import java.util.*;

public class EncoderDecoder {
    public static void encode(String fileName, TransferClient tc) {
        tc.transferMsg("Started encoding: " + fileName);

        File dir = new File("./sending/");
        File file = new File(dir, fileName);
        String fileWDir = file.toPath().toString();

        try(FileInputStream fileInput = new FileInputStream(fileWDir)) {
            Base64.Encoder encoder = Base64.getMimeEncoder();
            OutputStream outputStream = encoder.wrap(new FileOutputStream(fileWDir + ".enc"));
            int _byte;
            while((_byte = fileInput.read()) != -1) {
                outputStream.write(_byte);
            }
            outputStream.close();

        } catch (IOException ioe) {
            tc.transferMsg("Failed to encode!");
            System.err.printf("I/O error: %s%n", ioe.getMessage());
        }
        tc.transferMsg("Finished encoding!");
    }

    public static void decode(String fileName, TransferClient tc) {
        tc.transferMsg("Started decoding: " + fileName);

        File dir = new File("./receiving/");
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
            System.err.printf("I/O error: %s%n", ioe.getMessage());
        }

        tc.transferMsg("Finished decoding!");
        File delF = new File(file + ".enc");
        delF.delete();
    }  
}