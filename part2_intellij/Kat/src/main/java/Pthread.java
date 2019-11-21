import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class Pthread extends Thread {
        private String threadName;

        Pthread(String name){
            threadName = name;
        }
        public String getdangerlevel(int  danger,String topic,String topic2){
            String dangerlevel = "no danger yet";
            switch(danger){
                case 0:
                    dangerlevel = "Execute Eyes Closed Danger Level 0"+"/"+topic;
                    break;
                case 1:
                    dangerlevel = "Execute Eyes Closed Danger Level 1"+"/"+topic;
                    break;
                case 2:
                    dangerlevel = "Execute Eyes Closed Danger Level 2"+"/"+topic;
                    break;
                case 3:
                    dangerlevel = "Execute Eyes Closed Danger Level 3"+"/"+topic+"/"+topic2;
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
        //Signal = getdangerlevel(danger,topic1,topic2);
        //addtobuffer(L,Signal);
        public void addtobuffer(LinkedList<String> L, String Signal){
            L.addLast(Signal);
        }
        //Signal = removefrombuffer(L);
        public String removefrombuffer(LinkedList<String> L){
            String temp = L.getFirst();
            L.removeFirst();
            return temp;
        }
        public void run(String Logs, WebSocketsServer Socket, Socket sock){
            try {

                Socket.Server(Logs,sock);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //.socket(Logs);
        }
        public void run(LinkedList<String> b_in, Semaphore sem,String Signal) {
            if (threadName == "buffer_in") {
                try {
                        System.out.println("In buffer_in");
                        System.out.println(Signal);
                        sem.acquire();
                        b_in.addLast(Signal);
                        sem.release();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(threadName == "buffer_out"){
                try{
                        sem.acquire();
                        System.out.println("In buffer_out");
                        if(b_in.isEmpty()){
                            System.out.println("Empty buffer");
                           // currentThread().wait(1000);
                        }else{
                            Signal = b_in.getFirst();
                            b_in.removeFirst();
                        }
                        System.out.println(Signal);
                        sem.release();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

