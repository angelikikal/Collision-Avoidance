import com.sun.deploy.util.SessionState;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import javax.security.auth.callback.Callback;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.Semaphore;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.sql.Types.NULL;

public class Main {
    private static String Sig="";
    private static Pthread Pt;
    private static Pthread Out_thread=new Pthread("buffer_out");
    private static Pthread In_thread=new Pthread("buffer_in");
    private static int d_sec2=0;
    private static String topic_pub="ec:d0:9f:39:c0:4c1";
    private static int d_sec1=0;
    private static Semaphore sem = new Semaphore(0);
    private static Semaphore semlog = new Semaphore(0);
    private static double csv_sum=0;
    private static double classified_sum=0;
    private static Str cl=new Str();
    private static double d=25.0;
    private static ResultGenerator RE=new ResultGenerator();
    private static WebSocketsServer Socket = new WebSocketsServer();
    private static GpsDist D=new GpsDist();
    private static Subscriber s;
    private static Publisher p;
    private static String topic_t="";
    private static String Signal=null;
   // private static String topic1 = "2:0:0:44:55:66";
    private static String topic1 = "a8:c:63:b3:c7:bc";
    private static String topic2 = "ec:d0:9f:39:c0:4c";
    private static String topic3 = "a8:c:63:b3:c7:bc1";
   //private static String topic3 = "2:0:0:44:55:661";
    private static String topic4 = "ec:d0:9f:39:c0:4c1";
    private static String content = "Message from MqttPublishSample";
    private static int qos = 0;
    private static String broker = "tcp://192.168.1.5:1883";
    private static String Empty_file = "AF3,F7,F3,FC5,T7,P7,O1,O2,P8,T8,FC6,F4,F8,AF4,CQ_AF3,CQ_F7,CQ_F3,CQ_FC5,CQ_T7,CQ_P7,CQ_O1,CQ_O2,CQ_P8,CQ_T8,CQ_FC6,CQ_F4,CQ_F8,CQ_AF4,Marker,MarkerH\n";

    private static String co = "0";
    private static MemoryPersistence persistence = new MemoryPersistence();
    private static MqttClient sClient;

    private static double x1=0.0;
    private static double y1=0.0;
    private static double x2=0.0;
    private static double y2=0.0;
//    private static knn kat=new knn();
    private static int Closed1=0;
    private static int Closed2=0;

    private static String acc="";
    private static String lat="";
    private static String lot="";
    private static String title="";
    private static String mes="";
    private static int Danger_n1=0;
    private static int Danger_n2=0;
    private static ServerSocket servsock=null;

    private final static int SOCKET_PORT = 13267;
    private  static java.net.Socket sock=null;


    private static  String [][] entr;


    private static MqttMessage newM = new MqttMessage(content.getBytes());


    public static void main(String args[]) {
        try {

            servsock = new ServerSocket(SOCKET_PORT);
             sock = servsock.accept();
            System.out.println("The server is waiting!");
        } catch (IOException e) {
            e.printStackTrace();
        }


        LinkedList<String> Buffer = new LinkedList<String>();
        double di[][] = new double[36][15];
        double [][] ent_csv = new double[36][15];
//calculate the feature vector for the trainning set[entr array]
        try {
          entr=  RE.Trainning_en();
            System.out.println("Entropy for trainning set was calculated!");
        } catch (IOException e) {
            e.printStackTrace();
        }
//convert string array to double array[ent_csv]--TRAINNING SET
        for (int i=0; i<36 ; i++ ) {
            ent_csv[i][0] = Double.parseDouble(entr[i][0]);
            String[] temp = entr[i][1].split(",");
            for (int k = 0; k < temp.length; k++) {
                ent_csv[i][k + 1] = Double.parseDouble(temp[k]);
            }
        }
//ksekinaei to kalooooo
        try {
            String clientid = MqttAsyncClient.generateClientId();
            sClient = new MqttClient(broker, clientid, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker:" + broker);
            sClient.connect(connOpts);
            System.out.println("Connected");
            sClient.setCallback(new MqttCallback() {
                public void connectionLost(Throwable cause) {
                    System.out.println("connection lost!\n");
                    System.out.println("excep " + cause);
                    cause.printStackTrace();
                    //System.exit(0);

                }

                public void messageArrived(String topic, MqttMessage message) {
                    csv_sum++;
                    String m = message.toString();
//                    String time = new Timestamp(System.currentTimeMillis()).toString();
                    String[] parts = m.split("/");
                     acc = parts[0];
                     lat = parts[1];
                     lot = parts[2];
                     topic_t=topic;
                    if (topic.equals(topic1) && lat!=null) {     //lef
                        x1 = Double.parseDouble(lat);
                       y1 = Double.parseDouble(lot);

                    } else if(topic.equals(topic2)){                        //agg
                        x2 = Double.parseDouble(lat);
                        y2 = Double.parseDouble(lot);
                    }

                   // System.out.println("GPS: " + x1 + " " + y1 + " " + x2 + " " + y2);
                     title = parts[3];

                    int eyes=CheckeyesCSV(title);

                    String mes = parts[4];
                    if (mes.equals(Empty_file)) {
                        System.out.println("EMPTY CSV FILE");
                    } else {
                        double f;
                        String[] rows = mes.split("\n");
                        String[] rows1 = new String[rows.length - 1];
                        for (int i = 1; i < rows.length; i++) {
                            rows1[i - 1] = rows[i];
                            //    System.out.println(rows1.length);
                        }
                        String[][] table_csv = new String[rows1.length][];
                        for (int i = 0; i < rows1.length; i++) {
                            table_csv[i] = rows1[i].split(",");
//                            for (int k = 0; k < table_csv[i].length; k++) {
//
//                            }

                        }
                        double[][] arrDouble = new double[rows1.length][table_csv[0].length];
                        for (int i = 0; i < rows1.length; i++) {
                            for (int k = 0; k < table_csv[i].length; k++) {
                                arrDouble[i][k] = Double.valueOf(table_csv[i][k]);
                            }
                        }
                        double[] entropies = new double[14];
                        double[] column_buffer = new double[rows1.length];

//entropy of the random csv
                        for (int column = 0; column < 14; column++) {
                            for (int row = 0; row < rows1.length; row++) {
                                column_buffer[row] = arrDouble[row][column];
                            }
                            entropies[column] = Entropy.calculateEntropy(column_buffer);
                        }
//                        for (int i = 0; i < entropies.length; i++)
//                            System.out.println("Entropies are : " + entropies[i]);

                        double distance_list[] = new double[36];
                        double Labels[] = new double[36];
                        for(int i=0;i<36;i++){
                            distance_list[i] = cl.calculateDistance(entropies,ent_csv[i]);
                            Labels[i] = ent_csv[i][0];
                        }
                        //KNN
                        cl.BubbleSort(distance_list,Labels);
                        int classify = cl.Classification(distance_list,Labels,7);
                        if(classify==eyes) {
                            System.out.println("The CSV file was classified successfully!");
                            classified_sum++;
                        }
                        else if(classify==0){
                            title="EyesClosed";
                            System.out.println("The CSV file was classified in a different class ");
                            System.out.println(("The original class is: EyesOpened and it was classified as: EyesClosed" ));
                        }
                        else if(classify==1){
                            title="EyesOpen";
                            System.out.println("The CSV file was classified in a different class ");
                            System.out.println(("The original class is: EyesClosed and it was classified as: EyesOpened" ));
                        }
                       double pii= cl.Correct_Percentage(csv_sum,classified_sum);
                        System.out.println("The classification hit percentage is: "+pii);
                       int da= measurements( title);

                       if(da!=-1){
                           if(topic_pub.contains(",")) {
                               String[] str = topic_pub.split(",");
                               String Sig = Pt.getdangerlevel(da,str[0],str[1]);
                               In_thread.run(Buffer,sem,Sig);
                               In_thread.start();
                               try {
                                   In_thread.join();
                               } catch (InterruptedException e) {
                                   e.printStackTrace();
                               }

                           }
                           if(topic_pub.equals(" ")){
                               System.out.println("kati");
                           }else{
                               Sig = getdangerlevel(da,topic_pub,"empty");
                             //  String Sig=" affas gsggadghd 1/ec:d0:9f:39:c0:4c";

                               In_thread.run(Buffer,sem,Sig);
                               In_thread.start();
                               try {
                                   In_thread.join();
                               } catch (InterruptedException e) {
                                   e.printStackTrace();
                               }

                           }
                           Out_thread.run(Buffer,sem,Signal);
                           System.out.println("The sigal is: " +Sig);

                           Out_thread.start();
                           try {
                               Out_thread.join();
                           } catch (InterruptedException e) {
                               e.printStackTrace();
                           }
                           if(Sig!=null) {

                               System.out.println(Sig);
                               String[] temp = Sig.split(" ");
                               String to = temp[2];
                               System.out.println(temp[1]+" "+temp[2]);
                               if (temp[1].equals("1")) {
                                   System.out.println("Connected");
                                   p.Pub("ec:d0:9f:39:c0:4c1", 0, sClient, "1");
                               } else if (temp[0].contains("2")) {
                                   p.Pub(to, 2, sClient, "2");
                               } else if (temp[0].contains("0")) {
                                   p.Pub(to, 2, sClient, "0");
                               } else if (temp[0].contains("4")) {
                                   p.Pub(to, 2, sClient, "4");
                               } else {
                                   p.Pub(temp[1], 2, sClient, "3");
                                   p.Pub(temp[2], 2, sClient, "3");
                               }
                           }



                            String s=Message_bac(topic_t,lat,lot,da);
                            Pthread Log_thread = new Pthread("Logs");
                            Log_thread.run(s,Socket,sock);
                            Log_thread.start();
                            try {
                                Log_thread.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            try {

                                Socket.Server(s,sock);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                     }

                    }
                System.out.println("-------------------------------------------------------------------------------");
                }


                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    System.out.println("deliveryComplete: " + iMqttDeliveryToken.getMessageId());

                }
            });


        } catch (MqttException me) {
            me.printStackTrace();
        }
        //p.Pub(topic3,qos,sClient,"1");
        s.sub(topic2, qos, sClient);
       s.sub(topic1, qos, sClient);
       Out_thread.run(Buffer,sem,Signal);
       Out_thread.start();
        TimerTask tasknew5 = new TimerTask() {
            @Override
            public void run() {
                if(x1!=0.0 && y1!=0.0 && x2!=0.0 && y2!=0.0){
                     d=D.Dist(x1,x2,y1,y2);
                   // System.out.println("The distance is: "+d);
                    d=d*100000;
                    System.out.println("The distance is: "+d);
                }

            }
        };
        Timer timer5 = new Timer();

        // scheduling the task at fixed rate
        timer5.scheduleAtFixedRate(tasknew5,new Date(),5000);
        try {
            Out_thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static int CheckeyesCSV(String title) {
        String r;
        //If title of CSV contains eye closed then its an eye closed CSV
       boolean eyesclosed= title.toLowerCase().contains("eyesclosed".toLowerCase());
        boolean eyesopen=title.toLowerCase().contains("eyesopen".toLowerCase());
        if(eyesclosed) {
            r="EyesClosed";
          //  System.out.println("EYES CLOSED!");
            return 0;
        }
        else {
            r = "EyesOpened";
            //System.out.println("EYES OPEN" );
            return 1;
        }
    }
    public static int measurements(String ti){
        int danger=-1;

        if(CheckeyesCSV(ti)==0) {
            if(topic_t.equals(topic1)) {
                Closed1++;
                if (Closed1 == 3 && d > 20) {
                    danger = 1;
                    topic_pub = topic3;
                    sem.release();
                } else if (Closed1 >= 3 && d <= 20 && Closed2 < 3 && d_sec2 == 0) {
                    d_sec2 = 1;
                    danger = 2;
                    topic_pub = topic4;
                    sem.release();

                } else if (Closed1 >= 3 && d <= 20 && Closed2 >= 3) {
                    danger = 3;
                    topic_pub = topic3+","+topic4;
                    sem.release();

                }
            }
            if(topic_t.equals(topic2)) {
                Closed2++;
                if (Closed2 == 3 && d > 20) {
                    danger = 1;
                    topic_pub = topic4;
                    sem.release();
                } else if (Closed2 >= 3 && d <= 20 && Closed1 < 3 && d_sec1 == 0) {
                    d_sec1 = 1;
                    danger = 2;
                    topic_pub = topic3;
                    sem.release();
                } else if (Closed2 >= 3 && d <= 20 && Closed1 >= 3) {
                    danger = 3;
                    topic_pub = topic4+","+topic3;
                    sem.release();
                }
            }
        } else{
            if(topic_t.equals(topic1)){
                if(Closed1>=3 ){
                    danger=0;
                    topic_pub=topic3;
                    sem.release();
                }
                else if(Closed1>=3 && d_sec2==1){
                    danger=4;
                    d_sec2=0;
                    topic_pub=topic4;
                    sem.release();
                }
                Closed1=0;
            }
            else {
                if(Closed2>=3){
                    danger=0;
                    topic_pub=topic4;
                    sem.release();
                }
                else if(Closed2>=3 && d_sec1==1){
                    danger=4;
                    d_sec1=0;
                    topic_pub=topic3;
                    sem.release();
                }
                Closed2=0;
            }
        }
        return danger;
    }

    public static String Message_bac(String topic, String la, String lo, int da){
        String mes="";
        String time = new Timestamp(System.currentTimeMillis()).toString();
        if(da==2) {
            mes = topic1 + " " + topic2 + "," + time + "," + la + "," + lo + "," + da;
        }
        else
           mes= topic + "," + time + "," + la + "," + lo + "," + da;
        return mes;
    }

    public static String  getdangerlevel(int  danger,String topic,String topic2){
        String dangerlevel = "no danger yet";
        switch(danger){
            case 0:
                dangerlevel = "Execute_Eyes_Closed_Danger_Level 0"+" "+topic;
                break;
            case 1:
                dangerlevel = "Execute Eyes Closed Danger Level 1"+" "+topic;
                break;
            case 2:
                dangerlevel = "Execute Eyes Closed Danger Level 2"+" "+topic;
                break;
            case 3:
                dangerlevel = "Execute Eyes Closed Danger Level 3"+" "+topic+" "+topic2;
                break;
            case 4:
                dangerlevel = "Execute Eyes Closed Danger Level 4"+"/"+topic;
                break;
            default:
                dangerlevel = "error";
                break;
        }
        return dangerlevel;
    }

}

