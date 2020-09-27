![alt text](NKUA.jpg)
#Collision Avoidance

The purpose of this project is to detect and avoid car collisions/accidents on a road, by informing the drivers for potential dangers from the cars around them. It consists two android terminals which are considered as moving vehicles, an Edge Server, a Backhaul Server and a Database.

![alt text](network_structure.png)

##Description of the App:
The interface between the application and the user includes six buttons, three TextView and a Settings' Menu.
+ The Menu is consisting the options for the user to select the wanted rate of which the android terminal can publish the measurments (in seconds), as well as give the user the option to exit the app.
+ There is a FLASHLIGHT ON / OFF button, which is obvioysly used to turn either on or off the flash of the device.
+ A SOUND button, which when pressed produces an audible alarm for 10sec.
+ A CONNECT button, which implemets the connection between the android device and the Edge Server. 
+ A DISCONNECT button, to end the connection.
+ A SUBSCRIBE button for the suitable topic.
+ An UNSUBSCRIBE button.
Finally, there is a display of the accelerometer readings, as well as the coordinates of the android device.

##Android Devices:
The devices collect electroencephalographic (EEG) data from the sensors attached to the drivers, in order to detect if the drivers fall asleep during driving. They communicate with the Edge Server using the MQTT protocol.

##Edge Server
The Edge Server collects the EEG data sent by the Android terminals and is responsible for the classification of those data in order to determine their status. After the classification is done, it sends status messages back to the devices using the MQTT protocol, and also to the Backhaul Server using a socket.

##Backhaul Server
The Backhaul Server is responsible for the training of the model, which will be sent to the Edge Server and then be used for the classification. It also stores to the Database the status messages it receives from the Edge Server.

##MySQL Database
The Database is used to keep a status history of the Android devices
