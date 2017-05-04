package com.example.rohankankapurkar.facebook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ConfirmMailActivity extends AppCompatActivity {

    TextView showTextOnScreen;
    Button confirmCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_mail);

        String s = getIntent().getStringExtra("email");
        Log.d("email in confirm mail", "onCreate: "+s);

        showTextOnScreen = (TextView) findViewById(R.id.confirm_mail_screen_emailid);
        showTextOnScreen.append("  "+s);



    }






}
