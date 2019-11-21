package LogsHandling;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static LogsHandling.JDBC_create_database.DB_URL;
import static LogsHandling.JDBC_create_database.JDBC_DRIVER;
import static LogsHandling.JDBC_create_database.PASS;
import static LogsHandling.JDBC_create_database.USER;
import static LogsHandling.JDBC_create_database.createTable;
import static LogsHandling.JDBC_create_database.holdLogsCreated;

public class FileFromEdge {

    private final static int SOCKET_PORT = 13267;
    private final static String SERVER = "192.168.1.5";  // localhost or remote server
    final static String
            FILE_TO_RECEIVE = "C:\\Users\\lefte\\Desktop\\JAVA PROJECTS\\jdbc-official\\src\\main\\FilesGenerated\\file_received.csv";

    private final static int FILE_SIZE = 690000000; // file size temporary hard coded

    public static void main(String[] args) throws IOException {
        int bytesRead;
        int current = 0;
        // int counter = 120000;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket sock = null;

        Connection conn = null;
        Statement stmt = null;

        try {
            //Register JDBC driver
            Class.forName(JDBC_DRIVER);


            //Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //Execute a query
            System.out.println("Creating database...");
            stmt = conn.createStatement();

            String sql = "CREATE DATABASE BACKHAUL";
            stmt.executeUpdate(sql);
            System.out.println("Database created successfully...");

            conn.close();

            createTable();

            conn.close();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }

        try {
            while (true) {
                sock = new Socket(SERVER, SOCKET_PORT);
                sock.setKeepAlive(true);
                System.out.println("Connecting...");


                try {
                    File newfile = new File(FILE_TO_RECEIVE);
                    if (newfile.exists() && !newfile.isDirectory()) {
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

                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        } catch (ConnectException e) {
            holdLogsCreated();
        }
    }
}