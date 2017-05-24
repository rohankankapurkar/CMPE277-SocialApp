package com.example.rohankankapurkar.facebook.Album;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rohankankapurkar.facebook.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by keke on 5/22/2017.
 */

public class AddAlbumDelatils extends AppCompatActivity {
    Button btnCancel;
    Button btnSaveAlbum;
    final int PICK_ALBUM_IMAGE_REQUEST = 987;
    final int Read_External_Data = 666;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_add_album_content);
        //***privacy dropdown
        Spinner spinner = (Spinner) findViewById(R.id.visibility_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.privacy_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        btnCancel = (Button) findViewById(R.id.btnAlbumCancel);
        btnSaveAlbum = (Button) findViewById(R.id.btnAlbumSave);
        btnSaveAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAlbum();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void saveAlbum() {
        String albumName = ((TextView) findViewById(R.id.albumName)).getText().toString();
        if (albumName.isEmpty()) {
            Toast.makeText(AddAlbumDelatils.this, "Please provide the title!", Toast.LENGTH_LONG).show();
        } else {
//***************88888add saving the album details to db

            String pathsavselectedAlbum = "images/" + getUserName() + "/" + albumName;
            StorageReference riversRef = mStorageRef.child(pathsavselectedAlbum);
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, Read_External_Data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Read_External_Data: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_ALBUM_IMAGE_REQUEST);

                } else {
                    Toast.makeText(AddAlbumDelatils.this, "Please enable permission to proceed further!", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(AddAlbumDelatils.this, PreviewSelectedImage.class);
                if (filePath != null) {
                    String albumName = ((TextView) findViewById(R.id.albumName)).getText().toString();
                    intent.putExtra("userName",getUserName());
                    intent.putExtra("albumName", albumName);
                    intent.putExtra("filePath", filePath.toString());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AddAlbumDelatils.this, "Select an image", Toast.LENGTH_SHORT).show();
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
