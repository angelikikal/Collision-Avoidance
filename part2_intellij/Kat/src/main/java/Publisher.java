import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class Publisher {
    public static void Pub(String topic, int qos,  MqttClient client, String content) {



            System.out.println("Publishing message:" + content);
        MqttMessage message = new MqttMessage(content.getBytes());
        System.out.println("inside");
        message.setPayload(content.getBytes());
        message.setQos(qos);
        System.out.println("inside2");
        try {
            System.out.println("inside3");
            client.publish(topic, message);
            System.out.println("inside publish");
        } catch (MqttException e) {
            e.printStackTrace();
            System.out.println("ERROR");

        }


        System.out.println("Message published");
            System.out.println(content);



    }



}