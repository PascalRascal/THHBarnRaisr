package stohio.barnraisr;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.facebook.*;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String hash = "I have no gf";
    private CallbackManager callbackManager;
    List<String> permissionNeeds = Arrays.asList("user_photos", "email", "user_birthday", "user_friends");
    private static final String POST_URL = "http://4bf85a29.ngrok.io";
    private static final String POST_PARAMS = "Dude i have NO FUCKING GF";
    private static final String USER_AGENT = "Android";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        Profile Fprofile;

        LoginManager.getInstance().logInWithReadPermissions(this, permissionNeeds);

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("L@@K BUY NOW");


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        Fprofile = Profile.getCurrentProfile();


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView lv = (ListView) findViewById(R.id.eventList);
        System.out.println("I have no gf");


        if (Fprofile != null) {
            System.out.println(Fprofile.getFirstName());
        } else {
            System.out.println("Profile is FUCKING null");
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, hash, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
              new postEvent().execute("http://4bf85a29.ngrok.io");

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

}



