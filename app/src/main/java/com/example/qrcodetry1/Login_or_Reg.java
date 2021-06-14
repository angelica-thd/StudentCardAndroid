package com.example.qrcodetry1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
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
    private StudentAdminRequest studentAdminRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_or_reg);
        studentAdminRequest = new StudentAdminRequest(this);

        pass = findViewById(R.id.password);
        email = findViewById(R.id.email_logreg);
        pass.setText("");
        email.setText("");

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
            Toast toast = Toast.makeText(this, R.string.blankField, Toast.LENGTH_SHORT);
            View v = toast.getView();
            v.setBackgroundResource(R.drawable.error_toast);
            TextView t = (TextView) toast.getView().findViewById(android.R.id.message);
            t.setTextColor(Color.RED);
            toast.show();
        }
    }


    public void send2signup(View view){
       startActivity(new Intent(this,Register.class));
    }




}