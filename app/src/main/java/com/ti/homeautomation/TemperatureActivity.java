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
    TextView tempActuala;
    EditText tempDorita,tempWeek;
    Button setTemp,anulareTemp,btnWeek,btnanulareWeek;

    private long backPressedTime;
    private Toast backToast;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_temperature);

        back2=findViewById(R.id.backicon2);
        tempActuala=findViewById(R.id.tempactuala);
        tempDorita=findViewById(R.id.tempdorita);
        setTemp=findViewById(R.id.btn_settemperatura);
        anulareTemp=findViewById(R.id.btn_anulare1);
        tempWeek=findViewById(R.id.tempweekend);
        btnWeek=findViewById(R.id.btn_settemperatura2);
        btnanulareWeek=findViewById(R.id.btn_anulare2);



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


        btnanulareWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    tempDorita.setText("");
            }
        });




        //Satare temperatura

        setTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validatetemp1()){ return;}
                Connection sql = DbConnection.connectionclass();
                Float temp = Float.valueOf(tempDorita.getText().toString());
                try {
                        if (TemperatureActivityInsert(temp)) {
                            Toast.makeText(getApplicationContext(), "Temperatura a fost setată cu succes!", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getApplicationContext(), "Temperatura nu s-a setat!", Toast.LENGTH_SHORT).show();
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }
        });

        btnWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validatetemp2()){ return;}
                Connection sql = DbConnection.connectionclass();
                Float temp2 = Float.valueOf(tempWeek.getText().toString());
                try {
                    if(TemperatureActivityInsert2(temp2)) {
                        Toast.makeText(getApplicationContext(), "Temperatura a fost setată cu succes!", Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(getApplicationContext(), "Temperatura nu s-a setat!",Toast.LENGTH_SHORT).show();
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
                String temp = rs.getString("temperatura2");
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


        String query = ("UPDATE dbo.program set USerId=?,ciclu_zi=? where zi_sapt!='weekend'");
        PreparedStatement pstmt = sql.prepareStatement(query);
        //pstmt.setInt(1, "");//(int)(System.currentTimeMillis() % 2000000000));
        pstmt.setString(1,profil.username);
        pstmt.setFloat(2,temp);
        //pstmt.setTimestamp(3,new Timestamp(time));
        int rows = pstmt.executeUpdate();

        if(rows > 0) {
            ok = true;
        }

        sql.close();

        return ok;
    }

    public boolean TemperatureActivityInsert2 (Float temp) throws SQLException {
        Connection sql = DbConnection.connectionclass();
        boolean ok = false;

        Calendar calendar = Calendar.getInstance();
        java.util.Date currentTime = calendar.getTime();

        long time = currentTime.getTime();


        String query = ("UPDATE dbo.program set USerId=?,ciclu_zi=? where zi_sapt='weekend'");
        PreparedStatement pstmt = sql.prepareStatement(query);
        //pstmt.setInt(1, "");//(int)(System.currentTimeMillis() % 2000000000));
        pstmt.setString(1,profil.username);
        pstmt.setFloat(2,temp);
        //pstmt.setTimestamp(3,new Timestamp(time));
        int rows = pstmt.executeUpdate();

        if(rows > 0) {
            ok = true;
        }

        sql.close();

        return ok;
    }
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
    private Boolean validatetemp1() {
        String val = tempDorita.getText().toString();

        if (val.isEmpty()) {
            tempDorita.setError("Acest câmp trebuie completat!");
            return false;
        } else {
            tempDorita.setError(null);
            return true;
        }
    }
    private Boolean validatetemp2() {
        String val = tempWeek.getText().toString();

        if (val.isEmpty()) {
            tempWeek.setError("Acest câmp trebuie completat!");
            return false;
        } else {
            tempWeek.setError(null);
            return true;
        }
    }
}
