package com.example.rohankankapurkar.facebook;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.Map;

public class FriendListActivity extends AppCompatActivity {

    private friendListGenerationTask friendListGeneration = null;
    private sendFriendRequestTask sendFriendRequest = null;
    private followFriendTask followFriend = null;
    private searchFriendsTask searchFriends = null;

    private ArrayList<ListModel> friendList = null;
    private ListView friendLv;
    private CustomAdapter friendAdapter;
    private Button searchButton;
    private Button addButton;
    private EditText searchEmail;

    String api_url = "http://10.0.2.2:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        getFriendList();
        init();
    }

    public void init() {
        friendLv = (ListView) findViewById(R.id.friend_list_lv);
        searchButton = (Button) findViewById(R.id.search);
        addButton = (Button) findViewById(R.id.add);

        //Search button functionality implementation
        searchEmail = (EditText) findViewById(R.id.edit_text);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("NACHIKET", searchEmail.getText().toString());

                if (searchEmail.getText().toString().length() == 0) {
                    getFriendList();
                } else {
                    search(searchEmail.getText().toString());
                }
            }
        });

        //add friend functionality implementation
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add();
            }
        });
    }

    private void search(String searchParam) {
        searchFriends = new searchFriendsTask(searchParam);
        searchFriends.execute((Void) null);
    }

    private void getFriendList() {
        Toast.makeText(this, "NACHIKET-inside getFriendList function", Toast.LENGTH_SHORT).show();
        friendListGeneration = new friendListGenerationTask();
        friendListGeneration.execute((Void) null);
    }

    public void sendFriendRequest(String friend_req_sent_to) {
        sendFriendRequest = new sendFriendRequestTask(friend_req_sent_to);
        sendFriendRequest.execute((Void) null);
    }

    public void followFriend(String friend_req_sent_to) {
        followFriend = new followFriendTask(friend_req_sent_to);
        followFriend.execute((Void) null);
    }


    //Following class takes care of searching friends from text box
    public class searchFriendsTask extends AsyncTask<Void, Void, Boolean> {

        String searchParam;
        public searchFriendsTask(String searchParam) {
            super();
            this.searchParam = searchParam;
            // do stuff
        }
        @Override
        protected Boolean doInBackground(Void... params) {

            Log.d("NACHIKET", "inside function searchFriendsTask ASYNC task: ");
            Context context = getApplicationContext();
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = api_url + "/getSearchFriendList";

            final String username = getIntent().getExtras().getString("email");
            Log.d("NACHIKET", "SESSION BELONGS TO: " + username);

            StringRequest getRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("NACHIKET", response);
                            Log.d("NACHIEKT", response.toString());

                            try {
                                JSONArray arr = new JSONArray(response.toString());
                                friendList.clear();
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject jsonObj = arr.getJSONObject(i);
                                    friendList.add(new ListModel(jsonObj.getString("firstname"), jsonObj.getString("lastname"), jsonObj.getString("email")));
                                }

                                friendAdapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

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
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("email", username);
                    params.put("searchParam", searchParam);

                    return params;
                }
            };

            queue.add(getRequest);
            return true;
        }
    }

    //Following class takes care of creating FriendList and bringing it to the ListView
    public class friendListGenerationTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            Context context = getApplicationContext();
            RequestQueue queue = Volley.newRequestQueue(context);
            final String username = getIntent().getExtras().getString("email");
            String url = api_url + "/discoverFriends";


            StringRequest getRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("Response ala re ala", response);
                            Log.d("res ", response.toString());
                            try {
                                JSONArray arr = new JSONArray(response.toString());
                                friendList = new ArrayList<ListModel>();
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject jsonObj = arr.getJSONObject(i);
                                    friendList.add(new ListModel(jsonObj.getString("firstname"), jsonObj.getString("lastname"), jsonObj.getString("email")));
                                }
                                if (friendList != null && friendList.size() > 0) {
                                    friendAdapter = new CustomAdapter(friendList, FriendListActivity.this);
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
            ){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", username);
                    return params;
                }
            };
            queue.add(getRequest);
            return true;
        }
    }

    //Following class takes care of sending a friend request
    public class sendFriendRequestTask extends AsyncTask<Void, Void, Boolean> {

        String friend_req_sent_to;
        public sendFriendRequestTask(String friend_req_sent_to) {
            super();
            this.friend_req_sent_to = friend_req_sent_to;
            // do stuff
        }
        @Override
        protected Boolean doInBackground(Void... params) {

            Log.d("NACHIKET", "inside function sendFriendRequestTask ASYNC task: ");
            Context context = getApplicationContext();
            RequestQueue queue = Volley.newRequestQueue(context);

            String url = api_url + "/sendFriendRequest";

            final String username = getIntent().getExtras().getString("email");
            Log.d("NACHIKET", "SESSION BELONGS TO: " + username);

            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("NACHIKET", response);
                            Log.d("NACHIEKT", response.toString());

                            Toast.makeText(FriendListActivity.this, "!!!FRIEND REQUEST SENT!!!", Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", error.toString());
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", username);
                    params.put("friend_req_sent_to", friend_req_sent_to);
                    return params;
                }
            };
            queue.add(postRequest);
            return true;
        }
    }

    //Following class takes care of following a friend without sending a request

    public class followFriendTask extends AsyncTask<Void, Void, Boolean> {
        String friend_req_sent_to;
        public followFriendTask(String friend_req_sent_to) {
            super();
            this.friend_req_sent_to = friend_req_sent_to;
        }
        @Override
        protected Boolean doInBackground(Void... params) {

            Log.d("NACHIKET", "inside function followFriendTask ASYNC task: ");
            Context context = getApplicationContext();
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = api_url + "/followFriend";
            final String username = getIntent().getExtras().getString("email");

            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("NACHIKET", response);
                            Log.d("NACHIEKT", response.toString());

                            Toast.makeText(FriendListActivity.this, "!!!FOLLOW SUCCESSFUL!!!", Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", error.toString());
                        }
                    }
            ){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", username);
                    params.put("friend_req_sent_to", friend_req_sent_to);
                    return params;
                }
            };
            queue.add(postRequest);
            return true;
        }
    }
}
