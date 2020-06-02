package com.ti.homeautomation;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.textclassifier.ConversationActions;
import android.view.textclassifier.TextLanguage;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.net.Authenticator;
import java.net.ResponseCache;
import java.sql.SQLException;
import java.text.SimpleDateFormat;


public class Login extends AppCompatActivity {

    Button callRegister,loginBtn;
    ImageView image;
    TextInputLayout username,password;
    //TextView Info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        //callRegister =findViewById(R.id.register_screen);
        image=findViewById(R.id.imageView2);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        loginBtn=findViewById(R.id.btn_login);
        //Info=findViewById(R.id.textInfo);

        //Info.setText("-");

       loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //validate(username.getEditText().getText().toString(),password.getEditText().getText().toString());

                try {
                    if(Profil.getInstance().Login(username.getEditText().getText().toString(),password.getEditText().getText().toString()))
                        startControlActivity();
                    else Toast.makeText(getApplicationContext(), "Date incorecte",Toast.LENGTH_SHORT).show();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        // Apelare "Inregistrare"


        /*callRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent (Login.this,Register.class);
                startActivity(intent);
            }
        });*/

        //Data si timp
        Thread t = new Thread(){
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView tdate = (TextView) findViewById(R.id.date);
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy\nhh:mm a");
                                String dateString = sdf.format(date);
                                tdate.setText(dateString);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();
        onBackPressed();
    }

    public void startControlActivity()
    {
        Intent intent =new Intent (Login.this,ControlActivity.class);
        startActivity(intent);
    }

    public void onBackPressed() {
       //
    }

    //Validare date
   /* private void validate (String userName, String userPassword){
        if((userName.equals("Admin")&&(userPassword.equals("1234")))){
            Intent intent=new Intent (Login.this,ControlActivity.class);
            startActivity(intent);
        }
    }*/


   /*public void onLogin(View view) {
        String username= usernamelog.getEditText().getText().toString();
        String password= passwordlog.getEditText().getText().toString();

        String type="login";

        Background background =new Background(this);
        background.execute(type,username,password);
        }

    }*/
}
