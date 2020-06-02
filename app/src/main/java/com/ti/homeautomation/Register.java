package com.ti.homeautomation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;


public class Register extends AppCompatActivity {
    //Variabile
    TextInputLayout regLname,regFname,regEmail,regUsername,regPassword;
    Button regBtn,regCancelBtn;

    private long backPressedTime;
    private Toast backToast;

    //FirebaseDatabase rootNode;
    //DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        regCancelBtn = findViewById(R.id.btn_anulare);
        regLname=findViewById(R.id.lname);
        regFname=findViewById(R.id.fname);
        regEmail=findViewById(R.id.email);
        regUsername=findViewById(R.id.username);
        regPassword=findViewById(R.id.password);
        regBtn=findViewById(R.id.btn_reg);


        //"Anulare"

        regCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Register.this,Login.class);
                startActivity(intent);
            }
        });
    }

    private Boolean validateFname(){
        String val= regFname.getEditText().getText().toString();

        if(val.isEmpty()){
            regFname.setError("Acest câmp trebuie completat!");
            return false;
        }
        else{
            regFname.setError(null);
            regFname.setErrorEnabled(false);
            return true;
        }


    }

    private Boolean validateLname(){
        String val= regLname.getEditText().getText().toString();

        if(val.isEmpty()){
            regLname.setError("Acest câmp trebuie completat!");
            return false;
        }
        else{
            regLname.setError(null);
            regLname.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validateEmail(){
        String val= regEmail.getEditText().getText().toString();
        String emailPattern= "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(val.isEmpty()){
            regEmail.setError("Acest câmp trebuie completat!");
            return false;
        } else if(!val.matches(emailPattern)){
            regEmail.setError("Adresa email invalida!");
            return false;
        }
        else{
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }


    }

    private Boolean validateUsername(){
        String val= regUsername.getEditText().getText().toString();
        String noWhiteSpace= "^(?=.{5,16}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$";

        if(val.isEmpty()){
            regUsername.setError("Acest câmp trebuie completat!");
            regLname.setErrorEnabled(false);
            return false;
        } else if (val.length()>=16){
            regUsername.setError("Codul/Username-ul este prea lung");
            return false;
        } else if(!val.matches(noWhiteSpace)){
            regUsername.setError("Caracterele nu sunt permise./Cel putin o litera si o cifra!");
            return false;
        }
        else{
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);
            return true;
        }


    }

    private Boolean validatePassword(){
        String val= regPassword.getEditText().getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if(val.isEmpty()){
            regPassword.setError("Acest câmp trebuie completat!");
            return false;
        } else if (!val.matches(passwordVal)) {
            regPassword.setError("Parola este prea scurta!Introduceti cel putin 4 caractere!");
            return false;
        }else{
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }


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

    //Salvarea datelor in FireBase apasand butonul "Continuare"

    /*
    public void registerUser(View view){

        if(!validateLname() | !validateFname() | !validatePassword() | !validateEmail() | !validateUsername())
        {
            return;
        }

       //  rootNode=FirebaseDatabase.getInstance();
        //reference=rootNode.getReference("utilizatori");
        //Luam toate valorile

        String lname= regLname.getEditText().getText().toString();
        String fname = regFname.getEditText().getText().toString();
        String email= regEmail.getEditText().getText().toString();
        String username= regUsername.getEditText().getText().toString();
        String password=regPassword.getEditText().getText().toString();

        UserHelperClass helperClass = new UserHelperClass(lname,fname,email,username,password);
        reference.child(username).setValue(helperClass);
    }*/
}
