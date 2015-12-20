package stohio.barnraisr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * Created by csinko on 12/19/2015.
 */
public class EventActivity extends AppCompatActivity {
    TextView title,date,time,num,tot, desc;
    ImageView profile;
    Button button;

    JSONObject jo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        String storedData = getIntent().getStringExtra("Data");
        try{
            jo = new JSONObject(storedData);
        }catch(JSONException e){
            e.printStackTrace();
        }
        final Event thisEvent = new Event(jo);
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

        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                try {
                    URL imageURL = new URL("https://graph.facebook.com/" + thisEvent.getEventHostID() + "/picture?type=large");
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
}
