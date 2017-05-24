package com.example.rohankankapurkar.facebook.Album;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.rohankankapurkar.facebook.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PreviewSelectedImage extends AppCompatActivity {
    ImageView imgView;
    Uri filePath;
    Button btnCancel;
    Button btnUploadPic;
    private StorageReference mStorageRef;
    String albumName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_activity_preview_selected_image);
        imgView = (ImageView) findViewById(R.id.selectedImagetoUpload);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        displaySelectedImage();
        btnCancel = (Button) findViewById(R.id.btnCancelUpload);
        btnUploadPic = (Button) findViewById(R.id.btnUploadPic);
        btnUploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImagetoFirebaseStorage();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void displaySelectedImage() {
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("filePath")) {
            try {
                String getUri = extras.getString("filePath");
                filePath = Uri.parse(getUri);
                //getting image from gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting image to ImageView
                imgView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String setGetAlbumPath(String albumName, String filePath) {
        String pathsavselectedAlbum = "";
        if (!(getUserName().isEmpty()) && !(albumName.isEmpty()) && !(filePath.isEmpty())) {
            String[] fileNameString = filePath.split("/");
            String fileName = fileNameString[fileNameString.length - 1];
//            Log.i("keke", "filename: " + fileName);
            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();
            String fileNamewithTimeStamp = fileName + "_" + ts;
//            Log.i("keke", "fileNameTime: " + fileNamewithTimeStamp);
            pathsavselectedAlbum = "images/" + getUserName() + "/" + albumName + "/" + fileNamewithTimeStamp + ".jpg";
//            Log.i("keke", "path: " + pathsavselectedAlbum);
        }
        return pathsavselectedAlbum;
    }

    private void uploadImagetoFirebaseStorage() {
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("filePath")) {
            try {
                String getUri = extras.getString("filePath");
//                Log.i("filepath", getUri);
                albumName = extras.getString("albumName");
                filePath = Uri.parse(getUri);
                if (filePath != null && albumName != null) {
                    //****************get the usermail to store under their albums
//                    String useremail = ((TextView) findViewById(R.id.userEmailText)).getText().toString();
//                    useremail = "keke";
                    StorageReference riversRef = mStorageRef.child(setGetAlbumPath(albumName, getUri));
                    riversRef.putFile(filePath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Get a URL to the uploaded content
                                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                    Log.i("keke", "pic uploaded!");
                                    // Write a message to the database
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference();
                                    DatabaseReference refForSelectedAlbum = myRef.child(getUserName() + "/" + albumName + "/");
                                    refForSelectedAlbum.push().setValue(downloadUrl.toString());
                                    Log.i("keke", "pic url pushed to db!");
                                    Intent intent = new Intent(PreviewSelectedImage.this, ViewSelectedAlbumPage.class);
                                    intent.putExtra("albumName", albumName);
                                    intent.putExtra("userName",getUserName());
//                                    intent.putExtra("imgURL",downloadUrl.toString());
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                    // ...
                                    Log.i("keke", "pic upload failed :( ");
                                    Log.i("keke", "onFailure: " + exception.toString());
                                }
                            });
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
