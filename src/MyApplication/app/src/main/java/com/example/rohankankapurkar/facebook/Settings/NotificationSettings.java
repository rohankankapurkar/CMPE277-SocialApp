package com.example.rohankankapurkar.facebook.Settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.rohankankapurkar.facebook.R;

public class NotificationSettings extends AppCompatActivity {
    String api_url = "http://10.0.2.2:3000";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);
        TextView title=(TextView)findViewById(R.id.settingsTitle);
        title.setText("Notification Settings");
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_email:
                if (checked) {

                    // Put some meat on the sandwich
                }
            else
                // Remove the meat
                break;
            case R.id.checkbox_mobile:
                if (checked) {
                    // Cheese me
                }
            else
                // I'm lactose intolerant
                break;
            // TODO: Veggie sandwich
        }
    }
}
