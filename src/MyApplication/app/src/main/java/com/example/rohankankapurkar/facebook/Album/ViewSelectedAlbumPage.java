package com.example.rohankankapurkar.facebook.Album;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rohankankapurkar.facebook.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by keke on 5/22/2017.
 */

public class ViewSelectedAlbumPage extends AppCompatActivity {
    ArrayList<String> imagesFrieBase = new ArrayList<>();
    String albumName;
    GridView gridview;
    TextView displayAlbName;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference refForSelectedAlbum;
    Button btnAddMorePics;
    final int Read_Extrenat_Data = 666;
    final int PICK_ALBUM_IMAGE_REQUEST = 987;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_veiw_selected_album);
        //*********get the usermail to store under their albums
//                    String useremail = ((TextView) findViewById(R.id.userEmailText)).getText().toString();
//        useremail = "keke";
        albumName = getIntent().getExtras().getString("albumName");
        displayAlbName = (TextView) findViewById(R.id.displayAlbumName);
        displayAlbName.setText(albumName);
        refForSelectedAlbum = myRef.child(getUserName() + "/" + albumName + "/");
        refForSelectedAlbum.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String value = ds.getValue(String.class);
//                    Log.i("keke", "onDataChange: "+value);
                    imagesFrieBase.add(value);
//                    Log.i("keke", "InArray: "+imagesFrieBase);
                }
                Log.i("keke", "onDataChange: " + imagesFrieBase);
                setGridView(imagesFrieBase);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("keke", "onCancelled: Failed to read from database", databaseError.toException());
            }
        });

        btnAddMorePics = (Button) findViewById(R.id.btnaddMorePics);
        btnAddMorePics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMorePics();
            }
        });

    }

    public void setGridView(final ArrayList<String> urlArray) {
        gridview = (GridView) findViewById(R.id.gridview);
        Log.i("keke", "imgUrlList: " + urlArray);
        ImageAdapter imgAdapter = new ImageAdapter(getApplicationContext(), urlArray);
        gridview.setAdapter(imgAdapter);
        gridview.setVisibility(View.VISIBLE);
        Log.i("keke", "getCount: " + imgAdapter.getCount());
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Toast.makeText(getApplicationContext(), "test",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(ViewSelectedAlbumPage.this,ViewSelectedImage.class);
                intent.putExtra("albumName", albumName);
                intent.putExtra("userName",getUserName());
                intent.putExtra("url",urlArray.get(position));
//                                    intent.putExtra("imgURL",downloadUrl.toString());
                startActivity(intent);

            }
        });
    }

    public void addMorePics() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, Read_Extrenat_Data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Read_Extrenat_Data: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_ALBUM_IMAGE_REQUEST);

                } else {
                    Toast.makeText(ViewSelectedAlbumPage.this, "Please enable permission to proceed further!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_ALBUM_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Intent intent = new Intent(ViewSelectedAlbumPage.this, PreviewSelectedImage.class);
                if (filePath != null) {
                    String albumName = ((TextView) findViewById(R.id.displayAlbumName)).getText().toString();
                    Log.i("keke", "albumname: " + albumName);
                    intent.putExtra("albumName", albumName);
                    intent.putExtra("filePath", filePath.toString());
                    intent.putExtra("userName",getUserName());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ViewSelectedAlbumPage.this, "Select an image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public String getUserName(){
        String username = getIntent().getExtras().getString("userName");
        username=username.replace('.','!');
        return username;
    }
}