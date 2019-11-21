package katerinamqttpublish.com.publish;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Csv {
    Context context;
    String fileName;
    List<String> rows = new ArrayList<>();

    public Csv(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public List<String> readCSV() throws IOException {

        InputStream is = context.getAssets().open(fileName);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;

        while ((line = br.readLine()) != null) {
            String row = line;
            rows.add(row);
        }
        return rows;
    }

    public List<String> readCSVTest() throws IOException {

        InputStream is = context.getAssets().open("Test_set/"+fileName);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;

        while ((line = br.readLine()) != null) {
            String row = line;
            rows.add(row);
        }
        return rows;
    }
}