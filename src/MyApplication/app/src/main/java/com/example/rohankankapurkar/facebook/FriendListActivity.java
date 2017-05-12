package com.example.rohankankapurkar.facebook;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class FriendListActivity extends AppCompatActivity {

    private friendListGenerationTask friendListGeneration = null;
    private sendFriendRequestTask sendFriendRequest = null;
    private ArrayList<ListModel> friendList = null;
    private ListView friendLv;
    private CustomAdapter friendAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        getFriendList();
        init();
    }

    public void init() {
        friendLv = (ListView) findViewById(R.id.friend_list_lv);
    }

    private void getFriendList() {
        Toast.makeText(this, "NACHIKET-inside getFriendList function", Toast.LENGTH_SHORT).show();
        friendListGeneration =  new friendListGenerationTask();
        friendListGeneration.execute((Void) null);
    }

    public void sendFriendRequest() {
        sendFriendRequest =  new sendFriendRequestTask();
        sendFriendRequest.execute((Void) null);
    }

    //Following class takes care of creating FriendList and bringing it to the ListView
    public class friendListGenerationTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            Context context = getApplicationContext();
            RequestQueue queue = Volley.newRequestQueue(context);  // this = context
            String url = "http://10.0.2.2:3000/getFriendList";


            StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("Response ala re ala", response);
                            Log.d("res ", response.toString());
                            try {
                                JSONArray arr =  new JSONArray(response.toString());
                                friendList = new ArrayList<ListModel>();
                                for( int i=0 ; i<arr.length(); i++) {
                                    JSONObject jsonObj = arr.getJSONObject(i);
                                    friendList.add(new ListModel(jsonObj.getString("firstname"),jsonObj.getString("lastname"),jsonObj.getString("email")));
                                }
                                if(friendList!=null && friendList.size()>0) {
                                    friendAdapter = new CustomAdapter(friendList,FriendListActivity.this);
                                    friendLv.setAdapter(friendAdapter);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", error.toString());
                        }
                    }
            );
            queue.add(getRequest);
            return true;
        }
    }



    //Following class takes care of sending a friend request

    public class sendFriendRequestTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            Log.d("NACHIKET", "inside sendFriendRequestTask ASYNC task: ");
            Context context = getApplicationContext();
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = "http://10.0.2.2:3000/sendFriendRequest";

            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("NACHIKET", response);
                            Log.d("NACHIEKT", response.toString());

                            Toast.makeText(FriendListActivity.this, "POST CONNECTION ESTABLISHED", Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", error.toString());
                        }
                    }
            );

            queue.add(postRequest);
            return true;
        }
    }
}
