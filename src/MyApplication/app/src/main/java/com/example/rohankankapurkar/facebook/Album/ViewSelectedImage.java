package com.example.rohankankapurkar.facebook.Album;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.rohankankapurkar.facebook.R;
import com.squareup.picasso.Picasso;

public class ViewSelectedImage extends AppCompatActivity {
    String username;
    String albumName;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_activity_view_selected_image);
        username = getIntent().getExtras().getString("userName");
        albumName = getIntent().getExtras().getString("albumName");
        url = getIntent().getExtras().getString("url");
        ImageView imageView=(ImageView)findViewById(R.id.selectedImage);
        Picasso.with(this).load(url).into(imageView);
    }
}
