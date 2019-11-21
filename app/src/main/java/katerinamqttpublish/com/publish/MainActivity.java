package katerinamqttpublish.com.publish;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.SensorEvent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Inflater;



// exit-button
import android.view.View;
import android.widget.Button;

import static android.provider.Telephony.Mms.Part.FILENAME;
import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity implements LocationListener, AccelerometerListener {

    Context ccc;
    LocationManager Locmag;
    Context loccontext;
    private Button btnPublish;
    private Button btnConnect;
    public MqttAndroidClient client;
    private String mac;
    private Context context = this;
    private Button btnSub;
    private Button btnusub;
    private Button btnD;
    private Button Loc;
    public TextView longitude;
    public TextView latitude;
    public EditText frequency;
    double Longt;
    double Lat;

    public Thread main;
    public static String TAG = "kwstas2";
    public String Longitude;
    private String provider;
    public boolean isC;
    public String csv_me;

    public String MAC;
    public MACaddress Mac;
    private Button sound;




    TextView acceleration;
    String TAG4="nikoletatki";
    String csvtag="CSVMANAGER";
    String[] CSVLIST;
    static CSVManager csvm;

    private Button buttonEnable;
    boolean hasCameraFlash;

    private static final int CAMERA_REQUEST = 50;
    private boolean flashLightStatus = false;
    private Integer CSVindex;
    MediaPlayer Alarm;
    MediaPlayer Alarm2;

//    final CSVManager csvm=new CSVManager(CSVLIST);

//    final String FILENAME = "1.EyesClosedChristos.csv";
//    EditText text;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Alarm = MediaPlayer.create(MainActivity.this, R.raw.alarm );
        Alarm2 = MediaPlayer.create(MainActivity.this, R.raw.alert2 );
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        InternetConnection i = new InternetConnection();
        InternetConnectionCheck con=new InternetConnectionCheck();
        String[] CSVLIST = new String[0];
        try {
            CSVLIST = getAssets().list("Test_set");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Create New object of CSV manager with CSV list
         csvm=new CSVManager(CSVLIST);
        con.Check(this, i);

//test-set
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("k");
//        csv_me= in.toString();
        System.out.println("HIIIIIIIIII "+in);




        buttonEnable = (Button) findViewById(R.id.buttonEnable);

        hasCameraFlash = getPackageManager().
                hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        boolean isEnabled = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;

//        buttonEnable.setEnabled(!isEnabled);

        buttonEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST);
                if (hasCameraFlash) {
                    if (flashLightStatus)
                        flashLightOff();
                    else
                        flashLightOn();
                } else {
                    Toast.makeText(MainActivity.this, "No flash available on your device",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });


//check if there is internet connection
        isC = i.InternetC( context );
        if (isC == true) {
            Toast.makeText( MainActivity.this, "There is internet connection", Toast.LENGTH_SHORT ).show();
        } else {
            Toast.makeText( MainActivity.this, "No internet connection", Toast.LENGTH_SHORT ).show();
        }




//MAC address
        MAC = Mac.getMacAddr();
        String MACP=MAC+"1";

        Toast.makeText( MainActivity.this, MAC, Toast.LENGTH_SHORT ).show();
        Log.d( TAG4, MAC  );


//button declaration
        btnConnect = (Button) findViewById( R.id.btnConnect );
        btnSub = (Button) findViewById( R.id.btnSub );
        btnusub = (Button) findViewById( R.id.btnusub );
        btnD = (Button) findViewById( R.id.btnD );
        Locmag = (LocationManager) getSystemService( LOCATION_SERVICE );
        longitude = findViewById( R.id.longi );
        latitude = findViewById( R.id.lat );
        sound = findViewById( R.id.Sound );

//sound notification
        sound.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer Alarm = MediaPlayer.create(MainActivity.this, R.raw.alarm );

                Alarm.start();
                new Handler().postDelayed( new Runnable() {
                    @Override
                    public void run() {
                        Alarm.stop();
                        Alarm.reset();
                        Alarm.release();
                    }
                }, 10000 );

            }
        });


        if (ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) ==
                PackageManager.PERMISSION_GRANTED) {
            Log.d( TAG, "FINE LOCATION GRANTED" );

        } else {
            ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 1000 );
            Log.d( TAG, "FINE LOCATION not GRANTED" );

        }
        if (ContextCompat.checkSelfPermission( this, Manifest.permission.INTERNET ) == PackageManager.PERMISSION_GRANTED) {
            Log.d( TAG, "internet GRANTED" );
            Locmag.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, this );

        } else {
            ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.INTERNET}, 999 );
        }

        Locmag.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, this );



        btnD.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    try {
                        IMqttToken disconToken = client.disconnect();
                        disconToken.setActionCallback( new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                // we are now successfully disconnected
                                Toast.makeText( MainActivity.this, "Successful Disconnection!", Toast.LENGTH_SHORT ).show();

                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken,
                                                  Throwable exception) {
                                // something went wrong, but probably we are disconnected anyway
                            }
                        } );
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                } catch (NullPointerException e) {
                    Toast.makeText( MainActivity.this, "ERROR:You should be connected!", Toast.LENGTH_SHORT ).show();

                }

            }
        } );

        btnConnect.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (isC == false) {
//                    Toast.makeText( MainActivity.this, "ERROR:you should  be connected to  internet", Toast.LENGTH_SHORT ).show();
//
//                } else {

                    String clientId = MqttClient.generateClientId();
                    client = new MqttAndroidClient( MainActivity.this, "tcp://192.168.1.5:1883", clientId );
                    MqttConnectOptions options = new MqttConnectOptions();
                    options.setUserName( "USERNAME" );
                    options.setPassword( "PASSWORD".toCharArray() );

                        try {
                            IMqttToken token = client.connect();
                            token.setActionCallback( new IMqttActionListener() {
                                @Override
                                public void onSuccess(IMqttToken asyncActionToken) {
                                    Toast.makeText( MainActivity.this, "Successful connection to the MQTT broker!", Toast.LENGTH_SHORT ).show();


                                }

                                @Override
                                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                    Toast.makeText( MainActivity.this, "ERROR:You are not connected to the MQTT broker", Toast.LENGTH_SHORT ).show();
                                    // Something went wrong e.g. connection timeout or firewall problems
                                    //Log.d(TAG, "onFailure");

                                }

                            } );
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }

                }
//            }
        } );





        btnSub.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topic = MACP;
                int qos = 1;
                try {
                    try {
                        IMqttToken subToken = client.subscribe( topic, qos );
                        subToken.setActionCallback( new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Toast.makeText( MainActivity.this, "Successful subscription"+topic, Toast.LENGTH_SHORT ).show();
                                // The message was published
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken,
                                                  Throwable exception) {
                                Toast.makeText( MainActivity.this, "ERROR", Toast.LENGTH_SHORT ).show();
                                // The subscription could not be performed, maybe the user was not
                                // authorized to subscribe on the specified topic e.g. using wildcards

                            }
                        } );
                        client.setCallback(new MqttCallback() {
                            public void connectionLost(Throwable cause) {
                            }

                            public void messageArrived(String topic, MqttMessage message) throws Exception {
                                System.out.println("Message: " + message.toString());
                                System.out.println("**********************************************\n\n\n");
                                String mes = message.toString();

                                int cr_l = Integer.parseInt(mes);
                                if(cr_l==1 ){
                                    Danger1();
                                }
                                else if(cr_l==2){
                                    Danger2();
                                }
                                else if(cr_l==3){
                                    Danger3();
                                }
                                else if (cr_l==0){
                                    Danger0();
                                }
                                else if(cr_l==4){
                                    Danger4();
                                }

                                //String time = new Timestamp( System.currentTimeMillis() ).toString();
                                return;
                            }

                            @Override
                            public void deliveryComplete(IMqttDeliveryToken token) {

                            }
                        });
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                } catch (NullPointerException e) {
                    Toast.makeText( MainActivity.this, "ERROR:You should be connected", Toast.LENGTH_SHORT ).show();

                }
            }

        } );

        btnusub.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final String topic = MAC;
                    try {
                        IMqttToken unsubToken = client.unsubscribe( topic );
                        unsubToken.setActionCallback( new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Toast.makeText( MainActivity.this, "Successful unsubscribe", Toast.LENGTH_SHORT ).show();
                                // The subscription could successfully be removed from the client
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken,
                                                  Throwable exception) {
                                Toast.makeText( MainActivity.this, "ERROR", Toast.LENGTH_SHORT ).show();

                                // some error occurred, this is very unlikely as even if the client
                                // did not had a subscription to the topic the unsubscribe action
                                // will be successfully
                            }
                        } );
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                } catch (NullPointerException e) {
                    Toast.makeText( MainActivity.this, "ERROR:You should be connected", Toast.LENGTH_SHORT ).show();

                }
            }
        } );




    }



    private void flashLightOn() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);
            flashLightStatus = true;
        } catch (CameraAccessException e) {
        }
    }

    private void flashLightOff() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);
            flashLightStatus = false;
        } catch (CameraAccessException e) {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case CAMERA_REQUEST :
                if (grantResults.length > 0  &&  grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    buttonEnable.setEnabled(true);
//                    buttonEnable.setText("Camera Enabled");

                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied for the Camera", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Locmag.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
    }

    @Override
    public void onLocationChanged(Location location) {
        double latit = location.getLatitude();
        Lat = latit;
        double Longit = location.getLongitude();
        Longt = Longit;
        Longitude = Double.toString( Longit );
        Log.d( TAG, Double.toString( Longit ) );
        longitude.setText( Longitude );
        latitude.setText( Double.toString( latit ) );
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    public void dialogBox() {
        AlertDialog.Builder alert = new AlertDialog.Builder( this );
        alert.setMessage( "Are you sure you want to exit?" );
        alert.setPositiveButton( "Yes",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        try {

                            try {
                                clean_m();
                                IMqttToken disconToken = client.disconnect();
                                disconToken.setActionCallback( new IMqttActionListener() {
                                    @Override
                                    public void onSuccess(IMqttToken asyncActionToken) {
                                        // we are now successfully disconnected
                                       // Toast.makeText( MainActivity.this, "Disconnect-Success", Toast.LENGTH_SHORT ).show();

                                    }

                                    @Override
                                    public void onFailure(IMqttToken asyncActionToken,
                                                          Throwable exception) {
                                        // something went wrong, but probably we are disconnected anyway
                                    }
                                } );
                            } catch (MqttException e) {
                                e.printStackTrace();
                            }
                        } catch (NullPointerException e) {
                            Toast.makeText( MainActivity.this, "ERROR:You should be connected", Toast.LENGTH_SHORT ).show();

                        }
                        if (android.os.Build.VERSION.SDK_INT >= 21) {
//
                            finishAndRemoveTask();
                        } else {
                            finish();

                            moveTaskToBack( false );
                            System.exit( 0 );
                        }
                    }
                } );

        alert.setNegativeButton( "No",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                } );

        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }


//    //network connection
//    public boolean InternetConnection(){
//
//        ConnectivityManager cm =(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        boolean isConnected = activeNetwork != null &&
//                activeNetwork.isConnectedOrConnecting();
//        return isConnected;
//
//    }


    @Override
    protected void onResume() {
        super.onResume();
        if (AccelerometerManager.isSupported( this )) {
            AccelerometerManager.startListening( this );
        }
    }

    @Override
    public void onAccelerationChanged(float x, float y, float z) {
        acceleration = (TextView) findViewById( R.id.acceleration );
//        try {
//           // main.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        acceleration.setText( "X: " + x +
                "Y: " + y +
                "Z: " + z );


    }

    @Override
    public void onShake(float force) {
     //   Toast.makeText( this, "Motion detected", Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onStop() {
        super.onStop();

//Check device supported Accelerometer senssor or not
        if (AccelerometerManager.isListening()) {

//Start Accelerometer Listening
            AccelerometerManager.stopListening();

         //   Toast.makeText( this, "onStop Accelerometer Stopped", Toast.LENGTH_SHORT ).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (AccelerometerManager.isListening()) {
            AccelerometerManager.stopListening();

//            Toast.makeText( this, "onDestroy Accelerometer Stopped", Toast.LENGTH_SHORT ).show();


        }
    }
    public void internetalert()
    {

        Intent settingsIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
//
        startActivityForResult(settingsIntent, 9003);
    }

    //    builder.setOnlyOnce(true)
//    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//    builder.setSound(alarmSound);

//    public void Message_succ(){
//        AlertDialog alertDialog = new AlertDialog.Builder( MainActivity.this).create();
//        alertDialog.setTitle( "Alert" );
//        alertDialog.setMessage( "Publish-Success! " );
//        alertDialog.setButton( AlertDialog.BUTTON_NEUTRAL, "OK",
//
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        //   Intent settingsIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
//                        //   startActivityForResult(settingsIntent, 9003);
////                        c.startActivityForResult(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS), 0);
//
//                        dialog.dismiss();
//                    }
//                } );
//        alertDialog.show();
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menufile,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        String[] CSVLIST = new String[0];
        try {
            CSVLIST = getAssets().list("Test_set");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Create New object of CSV manager with CSV list
        csvm=new CSVManager(CSVLIST);
        switch(item.getItemId())
        {
            case R.id.exitt:
                dialogBox();

            case R.id.subitem1:
                TimerTask tasknew5 = new TimerTask() {


                    @Override
                    public void run() {

                        try {

                            String CSVtitle=CSV();
                            String topic = MAC;
                            String ac=((TextView) findViewById( R.id.acceleration )).getText().toString();
                            String lat=((TextView) findViewById( R.id.lat )).getText().toString();
                            String  lo=((TextView) findViewById( R.id.longi )).getText().toString();

                            String payload = ac+"/"+lat+"/"+lo+"/"+CSVtitle+"/"+csv_me+"/";
                            int n=payload.length();
                            byte[] encodedPayload = new byte[n];
                            try {
                                encodedPayload = payload.getBytes( "UTF-8" );
                                MqttMessage message = new MqttMessage( encodedPayload );
                                client.publish( topic, message );
                            } catch (UnsupportedEncodingException | MqttException e) {
                                e.printStackTrace();
                            }
//                            Message_succ();
                     //       Toast.makeText( MainActivity.this, "publish-success", Toast.LENGTH_SHORT ).show();

                        } catch (NullPointerException e) {
//                            Toast.makeText( MainActivity.this, "You must be connected ", Toast.LENGTH_SHORT ).show();

                        }



                    }
                };
                Timer timer5 = new Timer();

                // scheduling the task at fixed rate
                timer5.scheduleAtFixedRate(tasknew5,new Date(),5000);
                return true;

            case R.id.subitem2:
                TimerTask tasknew10 = new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            String CSVtitle=CSV();
                            String topic = MAC;
                            String ac=((TextView) findViewById( R.id.acceleration )).getText().toString();
                            String lat=((TextView) findViewById( R.id.lat )).getText().toString();
                            String  lo=((TextView) findViewById( R.id.longi )).getText().toString();

                            String payload = ac+"/"+lat+"/"+lo+"/"+CSVtitle+"/"+csv_me+"/";
                            int n=payload.length();
                            byte[] encodedPayload = new byte[n];
                            try {
                                encodedPayload = payload.getBytes( "UTF-8" );
                                MqttMessage message = new MqttMessage( encodedPayload );
                                client.publish( topic, message );
                            } catch (UnsupportedEncodingException | MqttException e) {
                                e.printStackTrace();
                            }
//                            Message_succ();
                            //       Toast.makeText( MainActivity.this, "publish-success", Toast.LENGTH_SHORT ).show();

                        } catch (NullPointerException e) {
//                            Toast.makeText( MainActivity.this, "You must be connected ", Toast.LENGTH_SHORT ).show();

                        }

                    }
                };
                Timer timer10 = new Timer();

                // scheduling the task at fixed rate
                timer10.scheduleAtFixedRate(tasknew10,new Date(),10000);
                return true;




            case R.id.subitem3:
                TimerTask tasknew20 = new TimerTask() {
                    @Override
                    public void run() {

                        try {

                            String CSVtitle=CSV();
                            String topic = MAC;
                            String ac=((TextView) findViewById( R.id.acceleration )).getText().toString();
                            String lat=((TextView) findViewById( R.id.lat )).getText().toString();
                            String  lo=((TextView) findViewById( R.id.longi )).getText().toString();
                            String payload = ac+"/"+lat+"/"+lo+"/"+CSVtitle+"/"+csv_me+"/";
                            int n=payload.length();
                            byte[] encodedPayload = new byte[n];
                            try {
                                encodedPayload = payload.getBytes( "UTF-8" );
                                MqttMessage message = new MqttMessage( encodedPayload );
                                client.publish( topic, message );
                            } catch (UnsupportedEncodingException | MqttException e) {
                                e.printStackTrace();
                            }
//                            Message_succ();
                            //       Toast.makeText( MainActivity.this, "publish-success", Toast.LENGTH_SHORT ).show();

                        } catch (NullPointerException e) {
//                            Toast.makeText( MainActivity.this, "You must be connected ", Toast.LENGTH_SHORT ).show();

                        }



                    }
                };
                Timer timer20 = new Timer();

                // scheduling the task at fixed rate
                timer20.scheduleAtFixedRate(tasknew20,new Date(),20000);




        }
        return super.onOptionsItemSelected( item );
        
        
    }



    public String test(CSVManager csvm) {
            String r;
            //Get a random Integer that indexes the CSV list
            CSVindex=csvm.RandomCSV(CSVLIST);
            //Check if the Random Selected CSV is Eyes Closed or Open
            r = csvm.CheckeyesCSV(CSVLIST,CSVindex);
            //Print the CSV file
            getfile(CSVLIST[CSVindex]);
            return r;



        //  listAssetFiles("");

    }

    public String CSV(){
        CSVindex=csvm.RandomCSV(CSVLIST);
        //Check if the Random Selected CSV is Eyes Closed or Open
        csvm.CheckeyesCSV(CSVLIST,CSVindex);
        //Print the CSV file
        getfile(CSVLIST[CSVindex]);

        System.out.println("********************\n");
        String CSVtitle;
        CSVtitle=CSVLIST[CSVindex];
        System.out.println(CSVtitle+"\n");
        return CSVtitle;
    }
    private void getfile(String CSVfile) {
        List<String> rows = new ArrayList<>();

        Csv csvReader = new Csv( MainActivity.this, CSVfile );
        try {
            rows = csvReader.readCSVTest();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        for (String s : rows) {
            sb.append( s );
            sb.append( "\n" );
        }
        csv_me = sb.toString();
    }

    public void dialB(String title,String text){
        AlertDialog alertDialog = new AlertDialog.Builder( MainActivity.this).create();
        alertDialog.setTitle( title);
        alertDialog.setMessage( text);
        alertDialog.setButton( AlertDialog.BUTTON_NEUTRAL, "OK",

                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void Danger1(){
     //   MediaPlayer Alarm = MediaPlayer.create(MainActivity.this, R.raw.alarm );
        if (Alarm.isPlaying()) {
            Alarm.pause();
        }
        if (Alarm2.isPlaying()) {
            Alarm2.pause();
        }
        Alarm.start();
        String ti="ALERT";
        String te= "OPEN YOUR EYES!!!!!!!!!!!!!!!!! " ;
        dialB(ti,te);
        return;
    }


    public void Danger2(){
        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST);
        if (hasCameraFlash) {
            if (flashLightStatus)
                flashLightOff();
            else
                flashLightOn();
        } else {
            Toast.makeText(MainActivity.this, "No flash available on your device",
                    Toast.LENGTH_SHORT).show();
        }
        String ti="ALERT";
        String te="THERE IS SOMEONE WITH CLOSED EYES NEAR TO YOU!!!!!!!!!!!!!!!!!";
        dialB(ti,te);
    }

    public void Danger3(){
        if (Alarm.isPlaying()) {
            Alarm.pause();
//            Alarm.stop();
//            Alarm.reset();
//            Alarm.release();
        }
        if (Alarm2.isPlaying()) {
            Alarm2.pause();
        }
        Alarm2.start();
        String ti="ALERT";
        String te= "OPEN YOUR EYESSSSSSSSSS!!!!!!!!!!!!!!!!! " ;
        dialB(ti,te);



    }

    public void Danger0(){
        if (Alarm.isPlaying())
            Alarm.pause();
        if (Alarm2.isPlaying())
            Alarm2.pause();
    }

    public void Danger4(){
        if (hasCameraFlash) {
            if (flashLightStatus)
                flashLightOff();
        }
    }
    public void clean_m() {
        Alarm.stop();
        Alarm.reset();
        Alarm.release();
        Alarm2.stop();
        Alarm2.reset();
        Alarm2.release();
        System.out.println("Mediaplayer memory has been released!");
    }


}
