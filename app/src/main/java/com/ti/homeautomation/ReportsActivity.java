package com.ti.homeautomation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class ReportsActivity extends Activity {
    ImageView back1;
    Spinner mySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_reports);

        back1=findViewById(R.id.backicon1);
        mySpinner=findViewById(R.id.raportuldorit);

        //Alegerea programului
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(ReportsActivity.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.rapoarte));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Alegeți raportul dorit"))
                {
                    //do nothing
                }
                else
                {
                    String item =parent.getItemAtPosition(position).toString();
                    Toast.makeText(parent.getContext(),"Ați selectat: " + item, Toast.LENGTH_SHORT).show();
                    if(parent.getItemAtPosition(position).equals("Raport pentru acces"))
                    {
                        //Intent intent = new Intent(ReportsActivity.this, ReportAcces.class);
                        //startActivity(intent);
                    }
                    if(parent.getItemAtPosition(position).equals("Raport de temperatură"))
                    {
                        //Intent intent = new Intent(ReportsActivity.this, ReportTemp.class);
                        //startActivity(intent);
                    }
                }
                if(parent.getItemAtPosition(position).equals("Raport de lumini"))
                {
                    //Intent intent = new Intent(ReportsActivity.this, ReportLights.class);
                    //startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ReportsActivity.this,ControlActivity.class);
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
                                TextView tdate1 = (TextView) findViewById(R.id.date1);
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy\nhh:mm a");
                                String dateString = sdf.format(date);
                                tdate1.setText(dateString);
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

