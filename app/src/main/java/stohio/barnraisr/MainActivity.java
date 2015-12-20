package stohio.barnraisr;

        import android.content.Context;
        import android.content.Intent;
        import android.content.pm.PackageInfo;
        import android.content.pm.PackageManager;
        import android.content.pm.Signature;
        import android.os.Bundle;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.util.Base64;
        import android.util.Log;
        import android.view.View;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.widget.AdapterView;
        import android.widget.ListView;

        import com.facebook.*;
        import com.facebook.CallbackManager;
        import com.facebook.FacebookCallback;
        import com.facebook.FacebookException;
        import com.facebook.FacebookSdk;
        import com.facebook.login.LoginManager;
        import com.facebook.login.LoginResult;
        import com.facebook.AccessToken;
        import com.facebook.GraphRequest;

        import org.json.JSONObject;

        import java.security.MessageDigest;
        import java.security.NoSuchAlgorithmException;
        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.List;


public class MainActivity extends AppCompatActivity {
    List<String> permissionNeeds = Arrays.asList("user_photos", "email", "user_birthday", "user_friends", "user_likes");

    private CallbackManager callbackManager;
    private LoginManager loginManager;
    private AccessToken accessToken;
    private AccessTokenTracker accessTokenTracker;
    private boolean loggedIn = false;
    ListView lv = null;
    ArrayList<Event> arrayList = null;
    Event list = null;
    Context context;
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        setUpFacebook();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                AccessToken.setCurrentAccessToken(newAccessToken);
            }
        };

        System.out.println("We tried logging in!");
        if(Profile.getCurrentProfile() == null){
            System.out.println("The profile we have stored is null");
        }else{
            System.out.println(Profile.getCurrentProfile().getFirstName());
        }

        loginManager.logInWithReadPermissions(this, permissionNeeds);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lv = (ListView)findViewById(R.id.eventList);

        loadData();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "lol", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                printFB();
                list.post();
                Intent myIntent = new Intent(MainActivity.this, EventCreate.class);
                startActivity(myIntent);

            }
        });
    }


    public void loadData() {
        Log.e("DATA", "DATA LOADED");
        if (Profile.getCurrentProfile() != null) {
            list = new Event("My First Event", "This is an Event", "2015-06-15", "6:00 PM", "123123", "44.4563", "38.9283", Profile.getCurrentProfile().getId(), 13);
        }else{
            list = new Event("My First Event", "This is an Event", "2015-06-15", "6:00 PM", "123123", "44.4563", "38.9283", "asfsafasfsa", 13);
        }
        arrayList = new ArrayList<Event>();
        arrayList.add(list);
        EventArrayAdapter listAdapter = new EventArrayAdapter(getApplicationContext(), arrayList);
        if (listAdapter != null) {

            Log.e("TEST", "TRYING TO LOAD ADA5tPTER");
            lv.setAdapter(listAdapter);
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EventActivity.class);
                if (arrayList != null) {
                    intent.putExtra("Data", arrayList.get(position).toJSON().toString());
                    startActivity(intent);
                }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void setUpFacebook()
    {
        // First initialize the Facebook SDK
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        System.out.println("Facebook set up!");

        // create the access token
        accessToken = AccessToken.getCurrentAccessToken();

        // create the login manager
        loginManager = LoginManager.getInstance();



        // create the callback for the login manager
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResults) {
                        System.out.println("Logged in!");
                        loggedIn = true;
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResults.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        // Application code
                                        Log.v("LoginActivity", response.toString());
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {



                        Log.e("dd", "facebook login canceled");

                    }


                    @Override
                    public void onError(FacebookException e) {


                        e.printStackTrace();

                    }

                });
        System.out.println("Facebook is all set up!");
    }


    public boolean isConnectedFacebook()
    {

        if (accessToken.getCurrentAccessToken() != null)
        {

            return true;
        }
        else
        {
            return false;
        }

    }
    public void printFB(){
        if(isConnectedFacebook()) {
            System.out.println("Check it! " + AccessToken.getCurrentAccessToken().getToken());
            System.out.println(Profile.getCurrentProfile().getId());
        }else{
            System.out.println("not connected! ;_;");
        }
    }


}

