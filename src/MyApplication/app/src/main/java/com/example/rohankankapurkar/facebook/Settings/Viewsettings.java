package com.example.rohankankapurkar.facebook.Settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.rohankankapurkar.facebook.Album.RecyclerAdapter;
import com.example.rohankankapurkar.facebook.R;

public class Viewsettings extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_settings);
        TextView title=(TextView)findViewById(R.id.settingsTitle);
        title.setText("Settings");
        username=getIntent().getExtras().getString("userName");
        getSettings();

    }
    public void getSettings(){
        recyclerView = (RecyclerView) findViewById(R.id.settings_recycler_view);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SettingsRecyclerAdapter(username);
        recyclerView.setAdapter(adapter);
    }
}
