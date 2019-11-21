package WebSocketConnect;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringTokenizer;

public class FileClient {

    private final static int SOCKET_PORT = 13267;
    private final static String SERVER = "127.0.0.1";  // localhost or remote server
    private final static String
            FILE_TO_RECEIVE = "C:\\Users\\lefte\\Desktop\\JAVA PROJECTS\\FileViaSocketClient\\FilesGenerated\\file_received.csv";

    private final static int FILE_SIZE = 690000000; // file size temporary hard coded

    public static void main(String[] args) throws IOException {
        int bytesRead;
        int current = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket sock = null;

        try {

            sock = new Socket(SERVER, SOCKET_PORT);
            System.out.println("Connecting...");

            // receive file
            byte[] mybytearray = new byte[FILE_SIZE];
            InputStream is = sock.getInputStream();
            fos = new FileOutputStream(FILE_TO_RECEIVE);
            bos = new BufferedOutputStream(fos);
            bytesRead = is.read(mybytearray, 0, mybytearray.length);
            current = bytesRead;

            do {
                bytesRead =
                        is.read(mybytearray, current, (mybytearray.length - current));
                //System.out.println("Bytes read " + bytesRead);
                if (bytesRead >= 0) current += bytesRead;
                if (current >= FILE_SIZE) {
                    mybytearray = null;
                }
            } while (bytesRead > -1);

            bos.write(mybytearray, 0, current);
            bos.flush();
            System.out.println("\nFile received from client");
            System.out.println("Client created one CSV file at " + FILE_TO_RECEIVE);
            System.out.println("File created has size " + current + " (" + current + " bytes read)");

            ///File openFile = new File(FILE_TO_RECEIVE);
            byte[] encoded = Files.readAllBytes(Paths.get(FILE_TO_RECEIVE));
            String data = new String(encoded, StandardCharsets.UTF_8);
            StringTokenizer tokenizer = new StringTokenizer(data, ",");
            String[] fields = new String[6];

            for (int i = 0; i < fields.length; i++) {
                fields[i] = tokenizer.nextToken();
            }
            for (int i = 0; i < fields.length; i++) {
                System.out.println(fields[i]);
            }


        } finally {
            if (fos != null) fos.close();
            if (bos != null) bos.close();
            if (sock != null) sock.close();
        }
    }
}