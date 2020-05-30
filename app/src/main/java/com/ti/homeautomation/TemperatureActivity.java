package com.ti.homeautomation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class TemperatureActivity extends AppCompatActivity {
    ImageView back2;
    Spinner mySpinner2;
    TextView tempActuala;
    TextInputLayout tempDorita;
    Button setTemp,anulareTemp,setProgram;



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
        setProgram=findViewById(R.id.btn_setprogram);


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
        anulareTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempDorita.getEditText().setText("");
            }
        });

        try {
            Connection sql;
            ResultSet rs = null;
            sql = DbConnection.connectionclass();
            Statement st = sql.createStatement();
            String query = ("SELECT TOP 1 * FROM dbo.temp_app ORDER BY Id DESC");
            rs = st.executeQuery(query);
            if (rs.next()) {
                String temp = rs.getString("SetedTemp");
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
        Connection sql;
        boolean ok = false;
        ResultSet rs = null;
        sql = DbConnection.connectionclass();
        Statement st = sql.createStatement();
        String query = ("INSERT INTO dbo.temp_app VALUES (?,?,?,?)");
        PreparedStatement pstmt = sql.prepareStatement(query);
        pstmt.setInt(1, (int)(System.currentTimeMillis() % 2000000000));
        pstmt.setString(2, "admin");
        pstmt.setFloat(3,temp);
        pstmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));

        int rows = pstmt.executeUpdate();

        if(rows > 0) {
            ok = true;
        }

        sql.close();

        return ok;
    }
}
