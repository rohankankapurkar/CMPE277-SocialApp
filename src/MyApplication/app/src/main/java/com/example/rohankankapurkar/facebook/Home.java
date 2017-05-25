package com.example.rohankankapurkar.facebook;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rohankankapurkar.facebook.Album.ViewAlbumsPage;
import com.example.rohankankapurkar.facebook.Settings.Viewsettings;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context mainContext = this;
    private UserProfile mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getUserDetails();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    private void getUserDetails() {
        mAuthTask = new UserProfile();
        mAuthTask.execute((Void) null);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            Intent logout = new Intent(Home.this, LoginActivity.class);
            Home.this.startActivity(logout);

            return true;
        }
        else if(id==R.id.settings){
            Intent settingsIntent=new Intent(Home.this, Viewsettings.class);
            String username = getIntent().getExtras().getString("email");
            settingsIntent.putExtra("userName", username);
            Home.this.startActivity(settingsIntent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            Toast.makeText(this, id + "NACHIKET-FRIENDLIST LOGIC", Toast.LENGTH_SHORT).show();
            Intent friendList = new Intent(Home.this, ShowFriendActivity.class);
            String username = getIntent().getExtras().getString("email");
            friendList.putExtra("email", username);
            Home.this.startActivity(friendList);

        } else if (id == R.id.nav_gallery) {

            Toast.makeText(this, id + "NACHIKET-DISCOVER FRIENDS LOGIC", Toast.LENGTH_SHORT).show();
            //pass username as session ID to the new intent here
            Intent friendList = new Intent(Home.this, FriendListActivity.class);
            String username = getIntent().getExtras().getString("email");
            friendList.putExtra("email", username);

            Home.this.startActivity(friendList);

        }
        else if(id==R.id.nav_album){
            Toast.makeText(this, id + "Album!", Toast.LENGTH_SHORT).show();
            Log.i("keke", "Album clciked: ");
            //pass username as session ID to the new intent here
            Intent viewAlbumIntent = new Intent(Home.this, ViewAlbumsPage.class);
            String username = getIntent().getExtras().getString("email");
            viewAlbumIntent.putExtra("username", username);
            Home.this.startActivity(viewAlbumIntent);
        }
        else if (id == R.id.nav_slideshow) {
            Log.d("NACHIKET", "slideshow button clicked");
        } else if (id == R.id.nav_share) {
            Log.d("NACHIKET", "share button clicked");
        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * Represents an asynchronous get user data information
     * the user.
     */
    public class UserProfile extends AsyncTask<Void, Void, Boolean> {
        public JSONObject userDetails;

        String firstname, lastname, email, profilePic, address, profession, interests, aboutme, isPrivate,myTweets;

        @Override
        protected Boolean doInBackground(Void... params) {

            RequestQueue queue = Volley.newRequestQueue(mainContext);  // this = context
            String username = getIntent().getExtras().getString("email");
            String url = String.format("http://10.0.2.2:3000/getUserData?username=%1$s", username);
            StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("Response", response);
                            Log.d("res ", response.toString());
                            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                            TextView username = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username);
                            TextView userEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userEmail);
                            Button button = (Button) navigationView.getHeaderView(0).findViewById(R.id.editProfile);
                            ImageView profile = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);
                            final Context context = getApplicationContext();
                            CharSequence text = "Successful";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                            try {
                                userDetails = new JSONObject(response);
                                firstname = userDetails.getString("firstname");
                                lastname = userDetails.getString("lastname");
                                email = userDetails.getString("email");
                                if (userDetails.has("profilePic")) {
                                    profilePic = userDetails.getString("profilePic");
                                }
                                if (userDetails.has("address")) {
                                    address = userDetails.getString("address");
                                }
                                if (userDetails.has("profession")) {
                                    profession = userDetails.getString("profession");
                                }
                                if (userDetails.has("about")) {
                                    aboutme = userDetails.getString("about");
                                }
                                if (userDetails.has("interests")) {
                                    interests = userDetails.getString("interests");
                                }
                                if (userDetails.has("myTweets")) {
                                    myTweets = userDetails.getString("myTweets");
                                }

                                isPrivate = userDetails.getString("isPrivate");


                                username.setText(firstname);
                                userEmail.setText(email);
                                if (profilePic != null && !profilePic.isEmpty()) {
                                    Picasso.with(getApplicationContext())
                                            .load(profilePic)
                                            .into(profile);
                                }

                                button.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        Intent userProfile = new Intent(Home.this, UserProfileActivity.class);
                                        userProfile.putExtra("firstname", firstname + " " + lastname);
                                        userProfile.putExtra("email", email);
                                        userProfile.putExtra("isPrivate", isPrivate);

                                        if (profilePic != null && !profilePic.isEmpty()) {
                                            userProfile.putExtra("profilePic", profilePic);
                                        }

                                        if (interests != null && !interests.isEmpty()) {
                                            userProfile.putExtra("interests", interests);
                                        } else {
                                            userProfile.putExtra("interests", "Travelling");
                                        }
                                        if (address != null && !address.isEmpty()) {
                                            userProfile.putExtra("address", address);

                                        } else {
                                            userProfile.putExtra("address", "101 E San Fernando St");

                                        }
                                        if (profession != null && !profession.isEmpty()) {
                                            userProfile.putExtra("profession", profession);
                                        } else {
                                            userProfile.putExtra("profession", "Software Developer");
                                        }
                                        if (aboutme != null && !aboutme.isEmpty()) {
                                            userProfile.putExtra("aboutme", aboutme);

                                        } else {
                                            userProfile.putExtra("aboutme", "Want to travel the whole world");
                                        }
                                        if (myTweets != null && !myTweets.isEmpty()) {
                                            userProfile.putExtra("myTweets", myTweets);

                                        } else {
                                            userProfile.putExtra("myTweets", "");
                                        }


                                        Home.this.startActivity(userProfile);
                                    }
                                });


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
            queue.add(postRequest);
            return true;
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        getUserDetails();

    }
}



