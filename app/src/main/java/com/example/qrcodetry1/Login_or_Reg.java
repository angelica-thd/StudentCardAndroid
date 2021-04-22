package com.example.qrcodetry1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login_or_Reg extends AppCompatActivity {
    private EditText pass, email;
    private TextView emailtxt, passtxt;
    private StudentAdminRequest studentAdminRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_or_reg);
        studentAdminRequest = new StudentAdminRequest(this);

        pass = findViewById(R.id.password);
        passtxt = findViewById(R.id.passtxtstudent);
        email = findViewById(R.id.email_logreg);
        emailtxt = findViewById(R.id.emailtxtstudent);

        /*
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_baseline_badge_24);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().hide();*/


    }

    @SuppressLint("ResourceAsColor")
    public void loginClick(View view) {
        Log.i("click","click");
        if(!email.getText().toString().equals(null) || !pass.getText().toString().equals(null)) {
            Log.i("text", email.getText().toString());
            studentAdminRequest.auth_login(email.getText().toString(),pass.getText().toString());
        }else {
            email.setHintTextColor(R.color.red);
            pass.setHintTextColor(R.color.red);
            Toast.makeText(this, R.string.blankField, Toast.LENGTH_SHORT).show();
        }
    }


    public void send2signup(View view){
       startActivity(new Intent(this,RegUser.class));
    }



/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            if(result.getContents()!=null){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                //builder.setMessage(result.getContents()).setTitle("Scanning Result");

                    builder.setPositiveButton("Scan Again", (dialog, which) -> new QR().scan_QR(Login_or_Reg.this)).setNegativeButton("Finish", (dialog, which) -> finish());
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                Toast.makeText(this,"No Results",Toast.LENGTH_LONG).show();
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    } */
}