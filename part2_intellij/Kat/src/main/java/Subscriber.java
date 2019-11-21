import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.sql.Timestamp;

public class Subscriber implements  MqttCallback {
    public static void sub(String topic, int qos,  MqttClient client){
        System.out.println("subscribing to topic: "+ topic);
        try {
            client.subscribe(topic,qos);
        } catch (MqttException e) {
            System.out.println("ERROR");
            e.printStackTrace();
        }

    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println(cause.getCause().toString());
        cause.printStackTrace();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("Delivery complete with token number: "+token.toString()
        );
    }
}