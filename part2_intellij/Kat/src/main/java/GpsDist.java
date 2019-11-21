import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class GpsDist {
    public double Dist(double x1,double x2,double y1,double y2) {
        double x = pow((x1 - x2), 2.0);
        double y = pow((y1 - y2), 2.0);
        return (sqrt(x + y));

    }
}


//                    if(eyes==false) {
//
//                        if (topic.equals(topic1)) {
//                            Closed1++;
//                            System.out.println("Closed1: " + Closed1);
//                            if (Closed1 == 3 && d > 20) {
//                                p.Pub(topic3, qos, sClient, "1");
//                            }
//
//                            if(Closed1>=3 && d<20 && Danger_n2==0){
//                                p.Pub(topic4, qos, sClient,"2");
//                                Danger_n2=1;
//                            }
//                            if(Closed1>=3 && d<20 && Closed2>=3){
//                                p.Pub(topic3, qos, sClient,"3");
//                                p.Pub(topic4, qos, sClient,"3");
//                            }
//                        }
//                        else {
//                            Closed2++;
//                            if(Closed2==3 && d>20 ){
//                                p.Pub(topic4, qos, sClient,"1");
//                            }
//                            if(Closed2>=3 && d<20 && Danger_n1==0){
//                                p.Pub(topic3, qos, sClient,"2");
//                                Danger_n1=1;
//                            }
//                            if(Closed1>=3 && d<20 && Closed2>=3){
//                                p.Pub(topic3, qos, sClient,"3");
//                                p.Pub(topic4, qos, sClient,"3");
//                            }
//                        }
//                    }
//                    else {
//                        if(topic.equals(topic1) ) {
//
//                            if(Closed1>=3) {
//                                p.Pub(topic3, qos, sClient, "0");
//                            }
//                                if (Danger_n2 == 1) {
//                                    Danger_n2 = 0;
//                                    p.Pub(topic4, qos, sClient, "4");
//                                }
//                          Closed1=0;
//                        }
//                        else if(topic.equals(topic2) && Closed2>=3){
//                            Closed2=0;
//                            p.Pub(topic4, qos, sClient,"0");
//                            if(Danger_n1==1) {
//                                Danger_n1 = 0;
//                                p.Pub(topic3, qos, sClient, "4");
//                            }
//                        }
// }