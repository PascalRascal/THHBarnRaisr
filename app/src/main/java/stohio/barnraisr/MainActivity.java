package stohio.barnraisr;

        import android.content.Intent;
        import android.os.Bundle;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
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

        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.List;


public class MainActivity extends AppCompatActivity {
    List<String> permissionNeeds = Arrays.asList("user_photos", "email", "user_birthday", "user_friends", "user_likes");
    private CallbackManager callbackManager;
    private LoginManager loginManager;
    private AccessToken accessToken;
    private AccessTokenTracker accessTokenTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpFacebook();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                AccessToken.setCurrentAccessToken(newAccessToken);
            }
        };

        printFB();
        loginManager.logInWithReadPermissions(this, permissionNeeds);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final event list = new event("My First Event", "This is an Event", "2015/06/15", "6:00 PM", "123123", "44.4563", "38.9283", Profile.getCurrentProfile().getId(), 13);
        final ArrayList<event> arrayList = new ArrayList<event>();
        arrayList.add(list);

        ListView lv = (ListView) findViewById(R.id.eventList);
        EventArrayAdapter listAdapter = new EventArrayAdapter(getApplicationContext(),arrayList);
        lv.setAdapter(listAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent  = new Intent(MainActivity.this, EventActivity.class);
                intent.putExtra("Data",arrayList.get(position).toJSON().toString());
                startActivity(intent);
            }
        });





        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "lol", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                printFB();
                list.post();
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

        // create the callback manager
        callbackManager = CallbackManager.Factory.create();

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


                        Log.e("dd", "facebook login failed error");

                    }
                });
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

