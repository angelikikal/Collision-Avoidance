package katerinamqttpublish.com.publish;

import android.util.Log;

import java.util.Random;

public class CSVManager {
    private static String TAG="CSVGERTAG";
    private boolean eyesclosed;
    private boolean eyesopen;
    private String[] CSVlist; //CSV list of CSV Files in assets
    CSVManager(String[] CSVlist)
    {
        this.CSVlist=CSVlist;
        CSVgetlist(); //Print to log the list
    }
    //Print List of CSV Files
    private void CSVgetlist()
    {

        for(String file : CSVlist)
        {
            Log.d(TAG,"Arxeio CSV : "+file);
        }

    }
    //Random CSV index function
    public Integer RandomCSV(String[] CSVlist)
    {
        Integer length=CSVlist.length;
        //Get a Random Integer Between index 0 ----> CSVlist.length
        int CSVindex=randInt(0,length);
        //Write to Log the Chosen file
        Log.d(TAG,"to tuxaio CSV arxeio einai : "+CSVlist[CSVindex]);
        //Return the index to Main
        return CSVindex;
    }
    //Check Eyes CSV function
    public String CheckeyesCSV(String[] CSVlist, int CSVindex) {
        String r;
        //eyes --> EyesClosed

        //Title of CSV
        String CSVtitle;
        CSVtitle=CSVlist[CSVindex];
        //If title of CSV contains eye closed then its an eye closed CSV
        eyesclosed= CSVtitle.toLowerCase().contains("eyesclosed".toLowerCase());
        eyesopen=CSVtitle.toLowerCase().contains("eyesopen".toLowerCase());
        if(eyesclosed) {
            r="eyesclosed";
            Log.d(TAG,"EYES CLOSED!");

            return r;
        }
        //Check if Eyes open
        else {
            r = "eyesopen";
            Log.d( TAG, "EYES OPEN" );
            return r;
        }
    }

    public static int randInt(int min, int max) {
        Random rand=new Random();
        int randomNum = rand.nextInt((max - min)) + min;
        Log.d(TAG,"o tuxaios arithmos einai : "+randomNum);
        return randomNum;
    }
}

