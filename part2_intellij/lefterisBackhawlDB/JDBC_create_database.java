package LogsHandling;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

import static LogsHandling.FileClient.FILE_TO_RECEIVE;

public class JDBC_create_database {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306";
    static int event_ID = -1;

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "root";


    static void createTable() {

        Connection conn = null;
        Statement stmt = null;
        try {
            //Open a connection
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL + "/backhaul", USER, PASS);
            System.out.println("Connected database successfully...");

            System.out.println("Creating table in given database...");
            stmt = conn.createStatement();

            String sql = "CREATE TABLE Event_Log" +
                    "(Event_ID INTEGER not NULL, " +
                    " Device_ID VARCHAR(255), " +
                    " Date_Time VARCHAR(255), " +
                    " Latitude VARCHAR(255), " +
                    " Longitude VARCHAR(255), " +
                    " Criticality_Level VARCHAR(255), " +
                    " PRIMARY KEY ( event_id ))";

            stmt.executeUpdate(sql);
            System.out.println("Created table in given database...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void insertValues(int event_id, String device_id,
                             String date_t, String latitude, String longitude, String criticality) {
        Connection conn = null;
        Statement stmt = null;
        try {
            //Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //Open a connection
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL + "/backhaul", USER, PASS);
            System.out.println("Connected database successfully...");
            stmt = conn.createStatement();

            stmt.executeUpdate("INSERT INTO `Event_Log`(event_id,device_id,Date_Time,latitude,longitude,Criticality_Level)" +
                    " VALUES ('" + event_id + "','" + device_id + "','" + date_t + "'," + latitude + ",'" + longitude + "','" + criticality + "')");
            System.out.println("Inserted records into the table...");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }

    }

    public static void holdLogsCreated() {
        Connection conn = null;
        Statement stmt = null;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_TO_RECEIVE))) {
            {
                String line;
                while ((line = br.readLine()) != null) {
                    StringTokenizer tokenizer = new StringTokenizer(line, ",");
                    String[] fields = new String[5];
                    event_ID++;

                    for (int i = 0; i < fields.length; i++) {
                        fields[i] = tokenizer.nextToken();
                    }

                    insertValues(event_ID, fields[0], fields[1], fields[2], fields[3], fields[4]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }//end LogsHandling.JDBC_create_database
}