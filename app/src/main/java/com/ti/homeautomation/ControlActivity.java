package com.ti.homeautomation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class ControlActivity extends AppCompatActivity {

    Button btnReports, btnLock, btnTemp, btnLight, btnLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_control2);

        btnReports = findViewById(R.id.btnRapoarte);
        btnLock = findViewById(R.id.btnZavor);
        btnTemp = findViewById(R.id.btnTemperatura);
        btnLight = findViewById(R.id.btnLumini);
        btnLogout = findViewById(R.id.btnIesire);

        btnLogout.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
        Intent intent = new Intent(ControlActivity.this, Login.class);
        startActivity(intent);
    }
    });
        btnLight.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
        Intent intent1 = new Intent(ControlActivity.this, LightActivity.class);
        startActivity(intent1);
    }
    });
        btnTemp.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
        Intent intent2 = new Intent(ControlActivity.this, TemperatureActivity.class);
        startActivity(intent2);
    }
    });
        btnReports.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
        Intent intent3 = new Intent(ControlActivity.this, ReportsActivity.class);
        startActivity(intent3);
    }
    });
        btnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(ControlActivity.this, LockActivity.class);
                startActivity(intent4);
            }
        });
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }
}
