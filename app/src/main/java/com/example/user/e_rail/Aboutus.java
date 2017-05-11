package com.example.user.e_rail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class Aboutus extends AppCompatActivity {
private Button btnFacebook;
    private Button btnLinked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        TextView aboutus=(TextView) findViewById(R.id.aboutus);
        aboutus.setText("A computer science student pursuing graduation from JIIT noida, sector-62,  passionate for developing android application. Always ready to face " +
                "any challenge which comes in the way during the developemnt phase of an app. I want to encorporate my ideas to build " +
                "amazing android applications. Love to know about new technologies in the field of android, which can be used to provide a " +
                "great exeperience to android users.");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
