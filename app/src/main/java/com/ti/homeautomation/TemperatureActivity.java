package com.ti.homeautomation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.io.DataInput;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TemperatureActivity extends AppCompatActivity {

    private Profil profil = Profil.getInstance();
    ImageView back2;
    Spinner mySpinner2;
    TextView tempActuala;
    EditText tempDorita;
    Button setTemp,anulareTemp;

    //String record="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_temperature);

        back2=findViewById(R.id.backicon2);
        mySpinner2=findViewById(R.id.programuldorit);
        tempActuala=findViewById(R.id.tempactuala);
        tempDorita=findViewById(R.id.tempdorita);
        setTemp=findViewById(R.id.btn_settemperatura);
        anulareTemp=findViewById(R.id.btn_anulare1);


        //Alegerea programului
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(TemperatureActivity.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.program));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner2.setAdapter(myAdapter);

        mySpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Alegeți programul dorit"))
                {
                    //do nothing
                }
                else
                {
                    String item =parent.getItemAtPosition(position).toString();
                    Toast.makeText(parent.getContext(),"Ați selectat programul: " + item, Toast.LENGTH_SHORT).show();
                    if(parent.getItemAtPosition(position).equals("Weekend"))
                    {

                        try {
                            TemperatureActivityInsert((float) 22.5);
                            Toast.makeText(parent.getContext(),"Temperatura a fost setată la 22.5 °C",Toast.LENGTH_LONG).show();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if(parent.getItemAtPosition(position).equals("Concediu"))
                    {
                        try {
                            TemperatureActivityInsert((float) 21);
                            Toast.makeText(parent.getContext(),"Temperatura a fost setată la 21 °C",Toast.LENGTH_LONG).show();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if(parent.getItemAtPosition(position).equals("Zi de lucru"))
                    {
                        try {
                            TemperatureActivityInsert((float) 23.5);
                            Toast.makeText(parent.getContext(),"Temperatura a fost setată la 23.5 °C",Toast.LENGTH_LONG).show();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Intoarcerea in meniul principal de control
        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TemperatureActivity.this,ControlActivity.class);
                startActivity(intent);
            }
        });
        anulareTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempDorita.setText("");
            }
        });

        setTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection sql = DbConnection.connectionclass();
                Float temp = Float.valueOf(tempDorita.getText().toString());
                try {
                    if(TemperatureActivityInsert(temp)) {
                        Toast.makeText(getApplicationContext(), "Temperatura a fost modificată cu succes!", Toast.LENGTH_SHORT).show();
                        //tempActuala.setText(temp + " °C");
                    }
                    else Toast.makeText(getApplicationContext(), "Temperatura nu s-a modificat!",Toast.LENGTH_SHORT).show();
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }
        });

        try {
            Connection sql;
            ResultSet rs = null;
            sql = DbConnection.connectionclass();
            Statement st = sql.createStatement();
            String query = ("SELECT TOP 1 * FROM dbo.stari_arduino ORDER BY id DESC");
            rs = st.executeQuery(query);
            if (rs.next()) {
                String temp = rs.getString("temperatura1");
                tempActuala.setText(temp+" °C");
                sql.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


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
        };
    public boolean TemperatureActivityInsert (Float temp) throws SQLException {
        Connection sql = DbConnection.connectionclass();
        boolean ok = false;

        Calendar calendar = Calendar.getInstance();
        java.util.Date currentTime = calendar.getTime();

        long time = currentTime.getTime();


        String query = ("INSERT INTO dbo.temp_app(UserId, SetedTemp, DateTime) VALUES (?,?,?)");
        PreparedStatement pstmt = sql.prepareStatement(query);
        //pstmt.setInt(1, "");//(int)(System.currentTimeMillis() % 2000000000));
        pstmt.setString(1,profil.username);
        pstmt.setFloat(2,temp);
        pstmt.setTimestamp(3,new Timestamp(time));
        int rows = pstmt.executeUpdate();

        if(rows > 0) {
            ok = true;
        }

        sql.close();

        return ok;
    }
}
