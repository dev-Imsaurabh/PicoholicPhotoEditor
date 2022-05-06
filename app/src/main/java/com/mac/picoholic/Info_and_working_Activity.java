package com.mac.picoholic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class Info_and_working_Activity extends AppCompatActivity {

    private ImageButton developerBtn;
    private TextView privacyPolicyBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_and_working);
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        developerBtn=findViewById(R.id.developerBtn);
        privacyPolicyBtn=findViewById(R.id.privacyPolicyBtn);

        privacyPolicyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Info_and_working_Activity.this,Privacy_policy_Activity.class));
            }
        });

        developerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"dev.imsaurabh@gmail.com"});
                intent.setType("text/plain");
                intent.setPackage("com.google.android.gm");
                startActivity(intent);
            }
        });
    }
}