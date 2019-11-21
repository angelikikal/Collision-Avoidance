package katerinamqttpublish.com.publish;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

public class InternetConnectionCheck extends MainActivity {

    public void Check(Context c, InternetConnection con) {
        final int j = 1000;
        String s = "vrasidas";
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        boolean b = con.InternetC( c );
                        if (b == false) {
                            Log.d( s, "inside" );
                            runOnUiThread( new Runnable() {
                                @Override
                                public void run() {
                                 //   Toast.makeText(c ,"lalaal",Toast.LENGTH_LONG ).show();
                                    Alert(c);
                                   // Intent settingsIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                                  //   startActivityForResult(settingsIntent, 9003);

//                                  //  Toast.makeText( this,"lalaal",Toast.LENGTH_LONG ).show();
//                                    AlertDialog alertDialog = new AlertDialog.Builder( c).create();
//                                    alertDialog.setTitle( "Alert" );
//                               //     Toast.makeText( this, "ERROR:You should be connected", Toast.LENGTH_SHORT ).show();
//                                    alertDialog.setMessage( "No internet connection,please connect to continue " );
//                                    alertDialog.setButton( AlertDialog.BUTTON_NEUTRAL, "OK",
//                                            new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    dialog.dismiss();
//                                                }
//                                            } );
//                                    alertDialog.show();
                                }
                            } );
                          //  test();
                        }
                        Thread.sleep( 5000 );

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread thread = new Thread( runnable );
        thread.start();
    }

    public void test() {
        Log.v( TAG, "reporting back from the Random Number Thread" );
        runOnUiThread( new Runnable() {
            @Override
            public void run() {
              //  Toast.makeText( ,"lalaal",Toast.LENGTH_LONG ).show();

            }
        } );

    }

    void Alert(Context c) {
        AlertDialog alertDialog = new AlertDialog.Builder( c).create();
        alertDialog.setTitle( "Alert" );
        alertDialog.setMessage( "No internet connection,please connect to continue " );
        alertDialog.setButton( AlertDialog.BUTTON_NEUTRAL, "OK",

                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                     //   Intent settingsIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    //   startActivityForResult(settingsIntent, 9003);
//                        c.startActivityForResult(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS), 0);

                        dialog.dismiss();
                    }
                } );
        alertDialog.show();
    }
}





