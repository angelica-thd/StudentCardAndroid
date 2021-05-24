package com.example.qrcodetry1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;


public class Register extends AppCompatActivity {
    FloatingActionButton fab;
    EditText email,username,name,pass,pass_conf;
    private StudentAdminRequest studentAdminRequest;
    private boolean isAdmin;
    private int pressed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);

        pressed = 0;
        studentAdminRequest = new StudentAdminRequest(this);
        email =  findViewById(R.id.email_st);
        username =  findViewById(R.id.username_st);
        name =  findViewById(R.id.fname_st);
        pass_conf =  findViewById(R.id.pass_st);
        pass =  findViewById(R.id.pass_conf_st);


        fab =  findViewById(R.id.admin_fab);
        fab.setOnClickListener(v -> {
            Log.i("click","click");
            if(!email.getText().toString().equals("")
                    || !email.getText().toString().equals(null)
                    || !username.getText().toString().equals("")
                    || !username.getText().toString().equals(null)
                    || !name.getText().toString().equals("")
                    || !name.getText().toString().equals(null)
                    || !pass.getText().toString().equals("")
                    || !pass.getText().toString().equals(null)
                    || !pass.getText().toString().equals("")
                    || !pass_conf.getText().toString().equals(null)) {
                if(pressed > 0){
                    JSONObject postData = new JSONObject();
                    try {
                        postData.put("username", username.getText().toString());
                        postData.put("name", name.getText().toString());
                        postData.put("email", email.getText().toString());
                        postData.put("password", pass.getText().toString());
                        postData.put("password_confirmation", pass_conf.getText().toString());


                    } catch (
                            JSONException e) {
                        e.printStackTrace();
                    }
                    studentAdminRequest.signup_user(postData, isAdmin);
                }else
                    Toast.makeText(this,R.string.usertype,Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this, R.string.blankField, Toast.LENGTH_SHORT).show();
            }

        });


    }

    public void go2login(View view){
        startActivity(new Intent(this, Login_or_Reg.class));
    }
    public void onStudentclicked(View view){
        pressed+=1;
        isAdmin = false;
        ImageView admin = findViewById(R.id.admin);
        view.setBackgroundDrawable(getResources().getDrawable(R.drawable.pressed));
        admin.setBackgroundDrawable(getResources().getDrawable(R.drawable.normal));
    }

    public void onAdminclicked(View view){
        pressed+=1;
        isAdmin= true;
        ImageView student = findViewById(R.id.student);
        view.setBackgroundDrawable(getResources().getDrawable(R.drawable.pressed));
        student.setBackgroundDrawable(getResources().getDrawable(R.drawable.normal));
    }
}

