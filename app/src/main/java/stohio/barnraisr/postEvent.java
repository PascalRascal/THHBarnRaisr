package stohio.barnraisr;

import android.content.ContentValues;
import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.android.volley.*;


/**
 * Created by Blaise on 12/18/2015.
 */
public class postEvent extends AsyncTask<String, Long, String> {

    @Override
    protected String doInBackground(String... urls) {
        int response = HttpRequest.post("http://4bf85a29.ngrok.io").send("I have no gf").code();
        String returnstring = "response code " + response;
        return returnstring;

    }
}
