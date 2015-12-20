package stohio.barnraisr;

        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.support.v4.widget.SwipeRefreshLayout;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.View;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;

        import com.facebook.*;
        import com.facebook.CallbackManager;
        import com.facebook.FacebookCallback;
        import com.facebook.FacebookCallback;
        import com.facebook.FacebookException;
        import com.facebook.FacebookSdk;
        import com.facebook.login.LoginManager;
        import com.facebook.login.LoginResult;
        import com.facebook.AccessToken;
        import com.facebook.GraphRequest;
        import com.facebook.Profile;

        import org.json.JSONArray;
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
    private boolean loggedIn = false;
    private SwipeRefreshLayout swipeContainer;
    ListView lv = null;
    Context context;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private int dataCode = 3;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

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

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(dataCode);

                if(swipeContainer.isRefreshing())
                    swipeContainer.setRefreshing(false);
            }
        });

        mDrawerList = (ListView)findViewById(R.id.navList);


        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //My Events
                        dataCode = 4;
                        break;
                    case 1:
                        dataCode = 5;
                        break;
                    case 2:
                        dataCode = 6;
                        break;
                }
                loadData(dataCode);
            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EventCreate.class);
                startActivity(intent);
            }
        });

    addDrawerItems();

    }




    public void addDrawerItems() {
        String[] osArray = {"My Events", "Participating", "Completed"};
        mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
    }





    public void loadData(int code) {
        Event list;
        final ArrayList<Event> arrayList;
        arrayList = new ArrayList<Event>();

        Log.e("DATA", "DATA LOADED");
        list = new Event(Integer.toString(code));
        list.post();

        while(list.isRefereshing) {

            try {
                Thread.sleep(100);
            } catch (java.lang.InterruptedException e) {
                e.printStackTrace();
            }
        }
           JSONArray dataArray;
        dataArray = list.getDataArray();
            //Log.e("ARRAYJSON", dataArray.toString());

        for(int i = 0; i < dataArray.length(); i++) {
            JSONObject object = null;
            try {
                object = dataArray.getJSONObject(i);
            } catch (org.json.JSONException e) {
                e.printStackTrace();
            }
            System.out.println(object.toString());
             arrayList.add(new Event(object));
        }

        lv = (ListView) findViewById(R.id.eventList);
        EventArrayAdapter listAdapter = new EventArrayAdapter(context,arrayList);
        if(listAdapter != null) {

            Log.e("TEST", "TRYING TO LOAD ADA5tPTER");
            lv.setAdapter(listAdapter);
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EventActivity.class);
                 final ArrayList<Event> arrayData = arrayList;
                    Log.d("HELP DATA", arrayData.get(position).toJSON().toString());
                    intent.putExtra("Data", arrayData.get(position).toJSON().toString());
                    startActivity(intent);

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
                        Event registration = new Event("2", Profile.getCurrentProfile().getId());
                        Log.d("facebookload", Profile.getCurrentProfile().getId());
                        registration.post();
                        loggedIn = true;
                        loadData(3);
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

                        if(loggedIn == false) {
                            finish();
                            System.exit(0);
                        }

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
        }else{
            System.out.println("not connected! ;_;");
        }
    }


}

