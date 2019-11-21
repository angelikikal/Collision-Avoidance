package katerinamqttpublish.com.publish.WebSockets;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class WebSocketsClient {

    public final static int SOCKET_PORT = 13267;
    public final static String SERVER = "192.168.1.146";  // localhost or remote server
    public final static String
            FILE_TO_RECEIVED = "/Users/katmast/Desktop/intellij/WebSocketClient(final)/FileGenerated/file.csv";

    public final static int FILE_SIZE = 690000000; // file size temporary hard coded

    public static void main(String[] args) throws IOException {
        int bytesRead;
        int current = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket sock = null;
        try {
            /*
             Socket client = new Socket(InetAddress.getByName(SERVER), SOCKET_PORT);
             System.out.println("Just connected to " + client.getRemoteSocketAddress());
             OutputStream outToServer = client.getOutputStream();
             DataOutputStream out = new DataOutputStream(outToServer);*/

            sock = new Socket(SERVER, SOCKET_PORT);
            System.out.println("Connecting...");

            // receive file
            byte[] mybytearray = new byte[FILE_SIZE];
            InputStream is = sock.getInputStream();
            fos = new FileOutputStream(FILE_TO_RECEIVED);
            bos = new BufferedOutputStream(fos);
            bytesRead = is.read(mybytearray, 0, mybytearray.length);
            current = bytesRead;

            do {
                bytesRead =
                        is.read(mybytearray, current, (mybytearray.length - current));
                System.out.println("Bytes read " + bytesRead);
                if (bytesRead >= 0) current += bytesRead;
                if (current >= FILE_SIZE) {
                    mybytearray = null;
                }
            } while (bytesRead > -1);

            bos.write(mybytearray, 0, current);
            bos.flush();
            System.out.println("\nAll files received from client");
            System.out.println("Client created one large CSV file at " + FILE_TO_RECEIVED);
            System.out.println("File created has size " + current + " (" + current + " bytes read)");
        } finally {
            if (fos != null) fos.close();
            if (bos != null) bos.close();
            if (sock != null) sock.close();
        }
    }
}
