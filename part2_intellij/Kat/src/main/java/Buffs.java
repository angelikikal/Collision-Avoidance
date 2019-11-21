import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;


import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Buffs {
    public static void main(String[] args) {
        final Semaphore sem = new Semaphore(0);
        final LinkedList<String> list = new LinkedList<String>();
        final LinkedList<Integer> ilist = new LinkedList<Integer>();
        Runnable remove_f = new Runnable() {
            @Override
            public void run() {
                try {
                    sem.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                list.removeFirst();
                ilist.removeFirst();
                System.out.println("Popped first item from buffer");
                int i;
                for(i=0;i<ilist.size();i++){
                    System.out.println("Item # "+ ilist.get(i)+" is:"+list.get(i));
                }
                sem.release();
            }
        };
        Runnable ttsig = new Runnable() {
            @Override
            public void run() {
                System.out.println("acquiring semaphore...");

                int i;
                int pi = 0;
                for(i=0;i<=4;i++) {
                    Scanner s = new Scanner(System.in);
                    String temp = new String(s.next());
                    list.add(temp);
                    ilist.add(i);
                }

                System.out.println("Buffer contents are");
                for(i=0;i<ilist.size();i++){
                    pi = ilist.get(i) + 1;
                    System.out.println("Item #"+pi+" is:"+list.get(i));
                }
                sem.release();
                System.out.println("semaphore released.....");
            }
        };
        Thread tt = new Thread(ttsig);
        Thread t = new Thread(remove_f);
        tt.start(); //pops first item from buffer
        t.start();  //enqueues signals to buffer

    }
}

class sig_buf {
    private int type;
    private String destination;//mac tou android
    sig_buf(int t,String s){
        type=t;
        destination=s;
        if (destination=="MAC_1")
        {
            //send to topic 1
            //if-clause for sig type
        }
        else if(destination=="MAC_2"){
            //send to topic 2
            //if-clause for sig type
        }
        System.out.println(t+""+s);
    }
    public static void send() {
        /*sends the signal via MQTT.*/
        System.out.println("Signaling...");
    }
}
