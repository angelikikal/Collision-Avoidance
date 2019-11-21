import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

public class WebSocketsServer {

    private final static int SOCKET_PORT = 13267;
    private final static String FILE_PATH = "/Users/katmast/Desktop/Kat/Kat/src/main/Entropy/file_to_send.csv";


    public void  Server(String s, Socket sock) throws IOException {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        ServerSocket servsock = null;
        //Socket sock = null;


        String message =s;

        try {
           // servsock = new ServerSocket(SOCKET_PORT);
            while (true) {
               // System.out.println("Waiting...");
                try {
//                    sock = servsock.accept();
                    System.out.println("Accepted connection : " + sock);
                    // create file
                    try {
                        File file = new File(FILE_PATH);
                        boolean fvar = file.createNewFile();
                        if (fvar) {
                         //   System.out.println("File has been created successfully");
                        } else {
                          //  System.out.println("File already present at the specified location");
                        }
                        PrintWriter writer = new PrintWriter(file, "UTF-8");
                        writer.println(message);
                        writer.close();
                    } catch (IOException e) {
                        System.out.println("Exception Occurred:");
                        e.printStackTrace();
                    }
                    // send file
                    File eLogs = new File(FILE_PATH);
                    byte[] mybytearray = new byte[(int) eLogs.length()];
                    fis = new FileInputStream(eLogs);
                    bis = new BufferedInputStream(fis);
                    bis.read(mybytearray, 0, mybytearray.length);

                    os = sock.getOutputStream();
                    System.out.println("Sending measurements to backhaul server ");
                   // System.out.println("Sending... file name: " + eLogs.getName() + " with size: " + eLogs.length());
                    os.write(mybytearray, 0, mybytearray.length);
                    os.flush();
                    System.out.println("Sending was successful");
                    break;
                } finally {
                 //   if (bis != null) bis.close();
                //    if (os != null) os.close();
               //     if (sock != null) sock.close();
                }
            }
        } finally {
           // if (servsock != null) servsock.close();
            try {
                Files.deleteIfExists(Paths.get(FILE_PATH));
            } catch (NoSuchFileException e) {
                System.out.println("No such file/directory exists");
            } catch (DirectoryNotEmptyException e) {
                System.out.println("Directory is not empty.");
            } catch (IOException e) {
                System.out.println("Invalid permissions.");
            }
           // System.out.println("Deletion successful.");
        }
    }
}

