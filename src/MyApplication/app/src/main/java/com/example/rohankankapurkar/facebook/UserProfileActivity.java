package com.example.rohankankapurkar.facebook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UserProfileActivity extends AppCompatActivity {

    int PICK_IMAGE_REQUEST = 111;
    Uri filePath;
    ProgressDialog pd;
    Button chooseImg;
    ImageView imgView;
    //creating reference to firebase storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://myapplication-574b6.appspot.com");



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        String firstname = getIntent().getStringExtra("firstname");
        TextView userEmail = (TextView) findViewById(R.id.firstname);
        userEmail.setText(firstname);

        ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    123);






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
           int id = item.getItemId();
           if (id == R.id.logout) {
            Intent logout = new Intent(UserProfileActivity.this,LoginActivity.class);
            UserProfileActivity.this.startActivity(logout);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {

        switch (requestCode)
        {
            case 123: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    chooseImg = (Button)findViewById(R.id.uploadImg);
                    imgView = (ImageView)findViewById(R.id.profileImage);

                    pd = new ProgressDialog(this);
                    pd.setMessage("Uploading....");


                    chooseImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_PICK);
                            startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);

                        }
                    });

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }


        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                //getting image from gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                //Setting image to ImageView
                imgView.setImageBitmap(bitmap);
                if(filePath != null) {
                    pd.show();

                    StorageReference childRef = storageRef.child("image3.jpg");
                    //uploading the image
                    UploadTask uploadTask = childRef.putFile(filePath);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pd.dismiss();
                            Toast.makeText(UserProfileActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(UserProfileActivity.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(UserProfileActivity.this, "Select an image", Toast.LENGTH_SHORT).show();
                }




            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




}
