package katerinamqttpublish.com.publish;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


public class InternetConnection extends MainActivity {

    //network connection
    private String TAG="nontas2";
    public boolean InternetC(Context c) {

        //Toast.makeText(InternetConnection.this, "No ", Toast.LENGTH_SHORT).show();
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        Log.d( TAG, "inside internet" );

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
