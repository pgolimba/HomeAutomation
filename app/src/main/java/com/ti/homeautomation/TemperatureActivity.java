package com.ti.homeautomation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class TemperatureActivity extends AppCompatActivity {
    ImageView back2;
    Spinner mySpinner2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_temperature);

        back2=findViewById(R.id.backicon2);
        mySpinner2=findViewById(R.id.programuldorit);

        //Alegerea programului
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(TemperatureActivity.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.program));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner2.setAdapter(myAdapter);

        //Intoarcerea in meniul principal de control
        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TemperatureActivity.this,ControlActivity.class);
                startActivity(intent);
            }
        });

        //Data si timp
        Thread t = new Thread() {
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView tdate3 = (TextView) findViewById(R.id.date3);
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy\nhh:mm a");
                                String dateString = sdf.format(date);
                                tdate3.setText(dateString);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();
    }
}
