package com.example.rohankankapurkar.facebook;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class RegisterActivity extends AppCompatActivity {

    //String email,firstname,lastname,password,password_confirm;
    EditText register_email,register_firstname,register_lastname,register_password,register_password_confirm ;

    private  UserRegisterTask userRegisterTask = null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        Context registerContext = this;


       // TextView rg_welcome = (TextView)findViewById(R.id.register_heading);
        register_email = (EditText) findViewById(R.id.register_email);
        register_firstname = (EditText) findViewById(R.id.register_firstname);
        register_lastname = (EditText) findViewById(R.id.register_lastname);
        register_password = (EditText) findViewById(R.id.register_password);
         register_password_confirm = (EditText) findViewById(R.id.register_password_confirm);

        Button register_button = (Button) findViewById(R.id.register);

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

    }

    private void attemptRegister() {
        Log.d("hmmm2", "onCreate: pringing emal"+register_email.getText().toString());
        userRegisterTask =  new UserRegisterTask(register_email.getText().toString(), register_password.getText().toString(),register_firstname.getText().toString(),register_lastname.getText().toString(),register_password_confirm.getText().toString());
        userRegisterTask.execute((Void) null);

    }


    //adding asynctask here
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final String mfirstname;
        private final String mlastname;
        private final String mpassword_confirm;



        UserRegisterTask(String email, String password, String firstname, String lastname, String password_confirm) {
            mEmail = email;
            mPassword = password;
            mfirstname=firstname;
            mlastname = lastname;
            mpassword_confirm=password_confirm;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Log.d("Inside_do_in_backgrund", "doInBackground: ");

            Context context = getApplicationContext();
            RequestQueue queue = Volley.newRequestQueue(context);  // this = context
            String url = "http://10.0.2.2:3000/register";
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("Response", response);
                            Log.d("res ", response.toString());


                            Context context = getApplicationContext();
                            CharSequence text = "user added successfully"+ response.toString();
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
//adding code to navigate to confirm mail request.
                            Intent confirmMailIntent = new Intent(RegisterActivity.this,ConfirmMailActivity.class);
                            String send_email = register_email.getText().toString();
                            confirmMailIntent.putExtra("email",send_email);
                            RegisterActivity.this.startActivity(confirmMailIntent);

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

                    String email = register_email.getText().toString();
                    Log.d("printing email sppa", "getParams: "+email);
                    String password = register_password.getText().toString();
                    Log.d("printing_password",password);
                    params.put("email",register_email.getText().toString());
                    params.put("password",register_password.getText().toString());
                    params.put("firstname",register_firstname.getText().toString());
                    params.put("lastname",register_lastname.getText().toString());
                    params.put("password_confirm",register_password_confirm.getText().toString());

                    return params;
                }
            };

            //Log.d("12", "Before posting the request"+register_email.getText().toString());
            queue.add(postRequest);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            Log.d("done with registration", "onPostExecute: ");

        }

        @Override
        protected void onCancelled() {

        }
    }

}
