package katerinamqttpublish.com.publish.WebSockets;



import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class WebSocketsServer {

    public final static int SOCKET_PORT = 13267;
    public final static String RESOURCES_PATH = "/Users/katmast/Desktop/intellij/WebSocketsServer(final)/Res";


    public static void main(String[] args) throws IOException {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        ServerSocket servsock = null;
        Socket sock = null;

        File folder = new File(RESOURCES_PATH);
        File[] listOfFiles = folder.listFiles();

        try {
            servsock = new ServerSocket(SOCKET_PORT);
            while (true) {
                System.out.println("Waiting...");
                try {
                    sock = servsock.accept();
                    System.out.println("Accepted connection : " + sock);
                    // send files
                    for (int index = 0; index < listOfFiles.length; index++) {
                        File aFile = new File(listOfFiles[index].getAbsolutePath());
                        byte[] mybytearray = new byte[(int) aFile.length()];
                        fis = new FileInputStream(aFile);
                        bis = new BufferedInputStream(fis);
                        bis.read(mybytearray, 0, mybytearray.length);
                        os = sock.getOutputStream();
                        System.out.println("Sending... file name: " + listOfFiles[index].getName() + " with size " + aFile.length());
                        System.out.println(aFile.getName());
                        os.write(mybytearray, 0, mybytearray.length);
                        os.flush();
                    }
                    System.out.println("Done!!");
                } finally {
                    if (bis != null) bis.close();
                    if (os != null) os.close();
                    if (sock != null) sock.close();
                }
            }
        } finally {
            if (servsock != null) servsock.close();
        }
    }
}
