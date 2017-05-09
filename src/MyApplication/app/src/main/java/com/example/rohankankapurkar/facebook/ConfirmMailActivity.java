package com.example.rohankankapurkar.facebook;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
public class ConfirmMailActivity extends AppCompatActivity {

    TextView showTextOnScreen;
    Button confirmCode;
    EditText verification_id;
    UserValidationTask userValidationTask;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_mail);

        String s = getIntent().getStringExtra("email");
        email = s;
        Log.d("email in confirm mail", "onCreate: "+s);

        showTextOnScreen = (TextView) findViewById(R.id.confirm_mail_screen_emailid);
        showTextOnScreen.append("  "+s);

        confirmCode = (Button) findViewById(R.id.button_verify_account);
        confirmCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("validation_press", "onClick: pressing the validation button");
                attemptValidation();
            }
        });

    }

    void attemptValidation(){

        verification_id = (EditText)findViewById(R.id.verification_id);
        String id = verification_id.getText().toString();
        if (id == null || id == ""){
            Log.d("Empty id", "attemptValidation: incorrect id entered by user ");
        }
        else{


            userValidationTask =  new UserValidationTask(id);
            userValidationTask.execute((Void) null);
            Log.d("Verify_id_validation", "attemptValidation: created the async task for validation ");

        }


        }









    public class UserValidationTask extends AsyncTask<Void, Void, Boolean> {

        private final String id_confirm;



        UserValidationTask(String id_confirm) {

            this.id_confirm=id_confirm;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Log.d("Inside_do_in_backgrund", "inside validation user do in background: ");

            Context context = getApplicationContext();
            RequestQueue queue = Volley.newRequestQueue(context);  // this = context
            String url = "http://10.0.2.2:3000/verifyUser";
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {

                            JSONObject jObject  = null; // json
                            try {
                                jObject = new JSONObject(response);
                                 int statusCode = jObject.getInt("statusCode"); // get the name from data.
                                Log.d("statusCode", "onResponse: printing the statuscode"+statusCode);
                                Context context = getApplicationContext();

                                if(statusCode == 200) {
                                    CharSequence text = "Correct ID entered. Login to continue";
                                    int duration = Toast.LENGTH_SHORT;

                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();
                                    //adding code to take to the login activity

                                    Intent loginIntent = new Intent(ConfirmMailActivity.this,LoginActivity.class);

                                    ConfirmMailActivity.this.startActivity(loginIntent);




                                }else{
                                    CharSequence text = "Incorrect code entered. Kindly check again";
                                    int duration = Toast.LENGTH_SHORT;

                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();
                                }






                            } catch (JSONException e) {
                                e.printStackTrace();
                            }





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


                    params.put("confirm_id",verification_id.getText().toString());
                    params.put("email",email);
                    Log.d("blah", "getParams: putting the varification parameter in the map for ->"+email);

                    return params;
                }
            };

            queue.add(postRequest);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            Log.d("done with_validation", "onPostExecute: ");

        }

        @Override
        protected void onCancelled() {

        }
    }
    }















