package stohio.barnraisr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.facebook.Profile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * Created by csinko on 12/19/2015.
 */
public class EventActivity extends FragmentActivity implements OnMapReadyCallback {
    TextView title,date,time,num,tot, desc;
    ImageView profile;
    Button button;
    MapFragment mapFragment;
    Event thisEvent;
    String hostID, eventID;


    JSONObject jo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_event);
        String storedData = getIntent().getStringExtra("Data");
        try{
            jo = new JSONObject(storedData);
            System.out.println("Blaise has no gf " + jo.toString());
        }catch(JSONException e){
            e.printStackTrace();
        }
        Event thisEvent = new Event(jo);
        hostID = Profile.getCurrentProfile().getId();
        eventID = thisEvent.getEventID();
        System.out.println("dota 2 " + thisEvent.toJSON().toString());
        String maxPart = "" + thisEvent.getEventMaxPart();

        title = (TextView)findViewById(R.id.textView);
        date = (TextView)findViewById(R.id.textView2);
        time = (TextView)findViewById(R.id.textView3);
        num = (TextView)findViewById(R.id.currentNum);
        tot = (TextView)findViewById(R.id.maxNum);
        desc = (TextView)findViewById(R.id.Desc);
        profile = (ImageView)findViewById(R.id.profile);
        button = (Button)findViewById(R.id.button);

        title.setText(thisEvent.getEventTitle());
        date.setText(thisEvent.getEventDate());
        time.setText(thisEvent.getEventTime());
        num.setText("5");
        tot.setText(maxPart);
        desc.setText(thisEvent.getEventDesc());

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMap().setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mapFragment.getMapAsync(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("HANDSHAKE", eventID + " ------ " + hostID);
                Event handshake = new Event("1", eventID, hostID);
                handshake.post();
                Toast toast = new Toast(getApplicationContext());
                toast.setText("Checked into event");
                toast.show();
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });



        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                try {
                    URL imageURL = new URL("https://graph.facebook.com/" + hostID + "/picture?type=large");
                    Bitmap bm = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                    return bm;
                }catch(java.net.MalformedURLException e){
                    e.printStackTrace();
                }catch(IOException e){
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);

                profile.setImageBitmap(bitmap);
            }
        }.execute();




    }

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    @Override
    public void onMapReady(GoogleMap map) {
        double longi = -81.9329870;
        double lati = 41.4685770;
        String storedData = getIntent().getStringExtra("Data");
        try{
            jo = new JSONObject(storedData);
        }catch(JSONException e){
            e.printStackTrace();
        }
        thisEvent = new Event(jo);
        Log.d("MAP", thisEvent.getEventLat() + ", " + thisEvent.getEventLong());
        System.out.println("no gf " + thisEvent.getEventLong());
        LatLng coords = new LatLng(lati,longi);
        map.addMarker(new MarkerOptions()
                .position(new LatLng(lati, longi))
                .title(thisEvent.eventTitle));
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(coords, 15)));


    }
}
