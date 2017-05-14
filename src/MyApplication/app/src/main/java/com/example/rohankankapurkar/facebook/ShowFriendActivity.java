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
import java.util.HashMap;
import java.util.Map;

public class ShowFriendActivity extends AppCompatActivity {

    private ListView friendListView;
    private GetFriendsTask friends;
    private ArrayList<ListModel> friendList = null;
    private GetFriendAdapter friendAdapter;
    String api_url = "http://10.0.2.2:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_friend);
        init();
        getFriends(getIntent().getExtras().getString("email"));
    }

    public void init() {
        friendListView = (ListView) findViewById(R.id.list_friend);
    }

    public void getFriends(String email) {
            Toast.makeText(this, "NACHIKET-inside getFriendList function", Toast.LENGTH_SHORT).show();
        friends = new GetFriendsTask(email);
        friends.execute((Void) null);
    }

    //Following class takes care of creating FriendList and bringing it to the ListView
    public class GetFriendsTask extends AsyncTask<Void, Void, Boolean> {

        String searchParam;
        public GetFriendsTask(String searchParam) {
            super();
            this.searchParam = searchParam;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Context context = getApplicationContext();
            RequestQueue queue = Volley.newRequestQueue(context);
            final String username = getIntent().getExtras().getString("email");
            String url = api_url + "/getFriendList";


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
                                    friendAdapter = new GetFriendAdapter(friendList, ShowFriendActivity.this);
                                    friendListView.setAdapter(friendAdapter);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
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
}
