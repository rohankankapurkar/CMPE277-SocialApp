package com.example.rohankankapurkar.facebook.Settings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rohankankapurkar.facebook.R;

import static com.example.rohankankapurkar.facebook.R.id.userName;

public class PrivacySettings extends AppCompatActivity {
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_settings);
        TextView title = (TextView) findViewById(R.id.settingsTitle);
        title.setText("How You Connect");
        username = getIntent().getExtras().getString("userName");
        //// TODO: add api call to get the privacy setting
        RadioButton rbFO= (RadioButton)findViewById(R.id.friendsOnlyPrivacy);
        RadioButton rbP=(RadioButton)findViewById(R.id.publicPrivacy);
        rbFO.setChecked(true);

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.friendsOnlyPrivacy:
                if (checked)
                    Toast.makeText(this, "FriendsOnly privacy applied!", Toast.LENGTH_SHORT).show();
                //todo add api call
                // friends only
                break;
            case R.id.publicPrivacy:
                if (checked)
                    Toast.makeText(this, "Public privacy applied!", Toast.LENGTH_SHORT).show();
                // public
                //// TODO add api call
                break;
        }
        Intent intent=new Intent(PrivacySettings.this,Viewsettings.class);
        intent.putExtra("userName",userName);
        PrivacySettings.this.startActivity(intent);
    }
}
