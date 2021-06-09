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

import org.json.JSONException;
import org.json.JSONObject;

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

        pass.setText("");
        email.setText("");


        /*
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_baseline_badge_24);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().hide();*/



    }



    @SuppressLint("ResourceAsColor")
    public void loginClick(View view) throws JSONException {
        Log.i("click","click");
        if(!email.getText().toString().equals(null) || !pass.getText().toString().equals(null)) {
            if(email.getText().toString().contains(" ")) email.setText(email.getText().toString().split(" ")[0]);
            Log.i("text", email.getText().toString());
            JSONObject log_params = new JSONObject();
            log_params.put("email",email.getText().toString());
            log_params.put("password",pass.getText().toString());
            studentAdminRequest.auth_login(log_params);
        }else {
            email.setHintTextColor(R.color.red);
            pass.setHintTextColor(R.color.red);
            Toast.makeText(this, R.string.blankField, Toast.LENGTH_SHORT).show();
        }
    }


    public void send2signup(View view){
       startActivity(new Intent(this,Register.class));
    } //RegUser.class




}