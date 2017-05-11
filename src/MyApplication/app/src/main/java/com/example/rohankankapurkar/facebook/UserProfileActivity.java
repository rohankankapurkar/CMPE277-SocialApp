package com.example.rohankankapurkar.facebook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class UserProfileActivity extends AppCompatActivity {

    int PICK_IMAGE_REQUEST = 111;
    Uri filePath;
    ProgressDialog pd;
    Button chooseImg;
    ImageView imgView;
    TextView name, email, profession, address, interest, about;
    EditText newname, newemail, newprofession, newaddress, newinterest, newabout;
    Button updateProfile;
    String imagePath;
    //creating reference to firebase storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://myapplication-574b6.appspot.com");

    private  UpdateProfile updateProfileDetails = null;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        chooseImg = (Button)findViewById(R.id.uploadImg);
        updateProfile =(Button) findViewById(R.id.updateProfile);


        loadTextDetails();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setVisibility(View.GONE);
                newname.setVisibility(View.VISIBLE);

                email.setVisibility(View.GONE);
                newemail.setVisibility(View.VISIBLE);

                address.setVisibility(View.GONE);
                newaddress.setVisibility(View.VISIBLE);

                profession.setVisibility(View.GONE);
                newprofession.setVisibility(View.VISIBLE);

                interest.setVisibility(View.GONE);
                newinterest.setVisibility(View.VISIBLE);

                about.setVisibility(View.GONE);
                newabout.setVisibility(View.VISIBLE);

                updateProfile.setVisibility(View.VISIBLE);

                chooseImg.setVisibility(View.VISIBLE);



            }
        });


        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();


            }
        });


        ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    123);






    }

    private void updateProfile()
    {
        updateProfileDetails =  new UserProfileActivity.UpdateProfile(newname.getText().toString(), newaddress.getText().toString(),newprofession.getText().toString(),newinterest.getText().toString(),newabout.getText().toString());
        updateProfileDetails.execute((Void) null);

    }


    //adding asynctask here
    public class UpdateProfile extends AsyncTask<Void, Void, Boolean> {

        private final String mName;
        private final String mAddress;
        private final String mprofession;
        private final String minterest;
        private final String maboutme;



        UpdateProfile(String name, String address, String profession, String interest, String aboutme) {
            mName = name;
            mAddress = address;
            mprofession=profession;
            minterest = interest;
            maboutme=aboutme;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Log.d("Inside_do_in_backgrund", "doInBackground: ");

            Context context = getApplicationContext();
            RequestQueue queue = Volley.newRequestQueue(context);  // this = context
            String url = "http://10.0.2.2:3000/updateProfile";
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("*****Response", response);
                            Log.d("******res ", response.toString());

                            Toast.makeText(UserProfileActivity.this, "Update Successful", Toast.LENGTH_LONG).show();

                            TextView em=(TextView) findViewById(R.id.userEmailText);
                            Intent intent = new Intent(UserProfileActivity.this, Home.class);
                            intent.putExtra("email", em.getText().toString());
                            startActivity(intent);
                            finish();

                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", error.toString());
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String>  params = new HashMap<String, String>();

                    params.put("email",newemail.getText().toString());
                    params.put("name",newname.getText().toString());
                    params.put("address",newaddress.getText().toString());
                    params.put("profession",newprofession.getText().toString());
                    params.put("interests",newinterest.getText().toString());
                    params.put("about",newabout.getText().toString());
                    params.put("profilePic",imagePath);


                    return params;
                }
            };

            //Log.d("12", "Before posting the request"+register_email.getText().toString());
            queue.add(postRequest);
            return true;
        }
    }

    private void loadTextDetails()
    {
        String nameVal = getIntent().getStringExtra("firstname");
        String emailVal = getIntent().getStringExtra("email");
        String professionVal = getIntent().getStringExtra("profession");
        String interestVal = getIntent().getStringExtra("interests");
        String addressVal = getIntent().getStringExtra("address");
        String aboutVal = getIntent().getStringExtra("aboutme");
        if(!getIntent().getStringExtra("profilePic").isEmpty() && getIntent().getStringExtra("profilePic")!= null)
        {
            String profilePicture =getIntent().getStringExtra("profilePic");
            ImageView profileImg = (ImageView) findViewById(R.id.profileImage);
            Log.d("IMAGE IMAGE",Uri.parse(profilePicture).toString());
        //    profileImg.setImageURI(Uri.parse(profilePicture));
            imagePath = profilePicture;
            Picasso.with(this)
                    .load(profilePicture)
                    .into(profileImg);
        }
         TextView userEmail = (TextView) findViewById(R.id.firstname);
        userEmail.setText(nameVal);

        name = (TextView) findViewById(R.id.userNameText);
        email = (TextView) findViewById(R.id.userEmailText);
        address = (TextView) findViewById(R.id.userAddressText);
        profession = (TextView) findViewById(R.id.userProfessionText);
        interest = (TextView) findViewById(R.id.userInterestsText);
        about = (TextView) findViewById(R.id.userAboutMeText);


        name.setText(nameVal);
        email.setText(emailVal);
        address.setText(addressVal);
        profession.setText(professionVal);
        interest.setText(interestVal);
        about.setText(aboutVal);


        newname = (EditText) findViewById(R.id.userName);
        newemail = (EditText) findViewById(R.id.userEmail);
        newaddress = (EditText) findViewById(R.id.userAddress);
        newprofession = (EditText) findViewById(R.id.userProfession);
        newinterest = (EditText) findViewById(R.id.userInterests);
        newabout = (EditText) findViewById(R.id.userAboutMe);

        newname.setText(nameVal);
        newemail.setText(emailVal);
        newaddress.setText(addressVal);
        newprofession.setText(professionVal);
        newinterest.setText(interestVal);
        newabout.setText(aboutVal);


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
                Random rnd = new Random();
                //Setting image to ImageView
                imgView.setImageBitmap(bitmap);
                if(filePath != null) {
                    pd.show();

                    String useremail = ((TextView) findViewById(R.id.userEmailText)).getText().toString();

                    StorageReference childRef = storageRef.child("images/"+useremail+".jpg");
                    //uploading the image
                    UploadTask uploadTask = childRef.putFile(filePath);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            String useremail = ((TextView) findViewById(R.id.userEmailText)).getText().toString();

                            pd.dismiss();
                            Toast.makeText(UserProfileActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                            storageRef.child("images/"+useremail+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri)
                                {
                                    imagePath = uri.toString();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });

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
