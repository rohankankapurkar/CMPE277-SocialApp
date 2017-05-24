package com.example.rohankankapurkar.facebook.Album;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import com.example.rohankankapurkar.facebook.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewAlbumsPage extends AppCompatActivity {

    //    Button addAlbumButton;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<String> albumNamesList = new ArrayList<>();
    ArrayList<String> albumDPURL = new ArrayList<>();
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //adding add album as first item
        albumNamesList.add(0, "Add Album");
        albumDPURL.add(0, "Add Album Icon");
        setContentView(R.layout.album_activity_view_albums_page);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        /*addAlbumButton = (Button) findViewById(R.id.addalbumbtn);
        addAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ViewAlbumsPage.this, "Add album", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ViewAlbumsPage.this, AddAlbumDelatils.class);
                startActivity(intent);
                finish();
            }
        });*/
        getAlbums();
    }

    public void getAlbums() {
        username = getIntent().getExtras().getString("username");
        username=username.replace('.','!');
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child(username);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //to handle duplicates
                albumNamesList.clear();
                albumDPURL.clear();
                albumNamesList.add(0, "Add Album");
                albumDPURL.add(0, "Add Album Icon");
                /*Log.i("keke", "displayChildren: " + dataSnapshot.getChildren().toString());
                Log.i("keke", "key: " + dataSnapshot.getKey());
                Log.i("keke", "value: " + dataSnapshot.getValue());
                Log.i("keke", "valuewithkey: " + dataSnapshot.getValue());*/
                HashMap<String, HashMap> selects = new HashMap<String, HashMap>();
                selects = ((HashMap<String, HashMap>) dataSnapshot.getValue());
                if (selects != null)//if albums are present
                    for (Map.Entry<String, HashMap> entry : selects.entrySet()) {
                        String key = entry.getKey();
                        HashMap value = entry.getValue();
//                    Log.i("keke", "albumName: "+key);
                        albumNamesList.add(key);
//                    Log.i("keke", "albumValue: "+value);
                        HashMap<String, String> getFirstImgMap = new HashMap<String, String>();
                        getFirstImgMap = value;
//                    Log.i("keke", "dpURl: "+getFirstImgMap.values().toArray()[0].toString());
                        albumDPURL.add(getFirstImgMap.values().toArray()[0].toString());
                    }
                Log.i("keke", "albumNameList: " + albumNamesList);
                recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                adapter = new RecyclerAdapter(getApplicationContext(), albumNamesList, albumDPURL,username);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}
