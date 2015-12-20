
package stohio.barnraisr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader;

import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Blaise on 12/18/2015.
 */
public class Event {

    private static final String POST_URL = "http://005studios.com";



    String eventCode, eventTitle, eventDesc, eventDate, eventTime, eventTimestamp, eventLong, eventLat, eventHostID;
    int eventMaxPart;
    String message;
    JSONArray dataArray = null;
    public Event(){
        //Empty Constructor
    }
    public Event(JSONObject jo){
        try {
            this.eventTitle = jo.getString("eventTitle");
            this.eventDesc = jo.getString("eventDesc");
            this.eventDate = jo.getString("eventDate");
            this.eventTime = jo.getString("eventDate");
            this.eventTimestamp = jo.getString("eventDate");
            this.eventLong = jo.getString("eventLong");
            this.eventLat =  jo.getString("eventLat");
            Log.d("LMAO", jo.getString("eventLong") + ", " + jo.getString("eventLat"));
            this.eventHostID = jo.getString("eventHostID");
            this.eventMaxPart = jo.getInt("eventMaxPart");
        }catch(JSONException e){
            e.printStackTrace();
        }


    }

    public Event(String eventCode,String eventTitle, String eventDesc, String eventDate, String eventTime, String eventTimestamp, String eventLong, String eventLat, String eventHostID, int eventMaxPart) {
        this.eventCode = eventCode;
        this.eventTitle = eventTitle;
        this.eventDesc = eventDesc;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventTimestamp = eventTimestamp;
        this.eventLong = eventLong;
        this.eventLat = eventLat;
        this.eventHostID = eventHostID;
        this.eventMaxPart = eventMaxPart;
    }

    public Event(String eventCode) {
        this.eventCode = eventCode;
    }

    public JSONArray getDataArray() {
        return dataArray;
    }

    public String getMessage() {
        return message;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventHostID() {
        return eventHostID;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public String getEventLat() {
        return eventLat;
    }

    public String getEventLong() {
        return eventLong;
    }

    public String getEventTime() {
        return eventTime;
    }

    public String getEventTimestamp() {
        return eventTimestamp;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public int getEventMaxPart(){return eventMaxPart;}

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public void setEventHostID(String eventHostID) {
        this.eventHostID = eventHostID;
    }

    public void setEventLat(String eventLat) {
        this.eventLat = eventLat;
    }
    public void setEventLong(String eventLong) {
        this.eventLong = eventLong;
    }
    public void setEventTitle(String eventTitle){
        this.eventTitle = eventTitle;
    }
    public void setEventTime(String eventTime){
        this.eventTime = eventTime;
    }
    public void setEventTimestamp(String eventTimeStamp){
        this.eventTimestamp = eventTimeStamp;
    }
    public JSONObject toJSON(){
        JSONObject jo = new JSONObject();

        try {
            jo.put("code", eventCode);
            jo.put("eventTitle", eventTitle);
            jo.put("eventDesc", eventDesc);
            jo.put("eventDate", eventDate);
            jo.put("eventTime", eventTime);
            jo.put("eventTimestamp",eventTimestamp);
            jo.put("eventLong", eventLong);
            jo.put("eventLat", eventLat);
            jo.put("eventHostID", eventHostID);
            jo.put("eventMaxPart", eventMaxPart);
        }catch(JSONException e){
            e.printStackTrace();
        }
        return jo;
    }



    public String post(){
        String jsonString = "";
        if (eventCode.equals("0"))
            jsonString = this.toJSON().toString();
        else if (eventCode.equals("3")) {
            jsonString = "3";
        }
        System.out.println(jsonString);
        class postEvent extends AsyncTask<String, Long, String> {

            @Override
            protected String doInBackground(String... urls) {
                try{
                   sendPOST();
                }catch(IOException e){
                    e.printStackTrace();
                }
                return null;


            }
        }
        new postEvent().execute();
        return message;
    }

    public JSONArray stringToArray(String s) {
        Log.e("STRING TO CONVERT", s);
        JSONObject jObject = null;
        try {
            jObject = new JSONObject(s);
        }
        catch (org.json.JSONException e) {
            e.printStackTrace();
        }
        Log.e("JSON",jObject.toString());
        JSONArray jArray = null;

        try {
            jArray = jObject.getJSONArray("events");
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < jArray.length(); i++) {
            JSONObject object = null;
        }

        dataArray = jArray;
        Log.e("DATA ARRAY TEST", dataArray.toString());
        return jArray;
    }

    public Bitmap getPicture(){
        Bitmap bm = null;

         class getpic extends AsyncTask<Bitmap, Long, Bitmap>{
             Bitmap bm = null;

            @Override
            protected Bitmap doInBackground(Bitmap... params) {

                try {
                    URL imageURL = new URL("https://graph.facebook.com/" + eventHostID + "/picture?type=small");
                    bm = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                    return bm;
                }catch(java.net.MalformedURLException e){
                    e.printStackTrace();
                }catch(IOException e){
                    e.printStackTrace();
                }
                return null;
            }
             public Bitmap getBitmap(){
                 return bm;
             }


        }

        getpic gp = new getpic();
        gp.execute();
        bm = gp.getBitmap();
        return bm;
    }
    private  String sendPOST() throws IOException {
        URL obj = new URL(POST_URL);
        String POST_PARAMS = "&myjson=" + this.toJSON().toString();
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "i have no gf");

        // For POST only - START
        con.setDoOutput(true); // i think this is google sample Code
        OutputStream os = con.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        // For POST only - END

        int responseCode = con.getResponseCode();
        String msg = "";
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            msg = response.toString();
        } else {
            System.out.println("POST request not worked");
        }
        message = msg;
        Log.d("MESSAGE REPLY", getMessage());
        if(eventCode.equals("3"))
            stringToArray(msg);
        return msg ;
    }




}

