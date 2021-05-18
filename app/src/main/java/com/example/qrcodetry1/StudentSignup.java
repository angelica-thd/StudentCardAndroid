package com.example.qrcodetry1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StudentSignup extends AppCompatActivity {
    private EditText university,department,id_number;
    private String auth_token, email, username, name;
    private StudentAdminRequest studentAdminRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_signup2);

        studentAdminRequest = new StudentAdminRequest(this);
        auth_token = getIntent().getStringExtra("auth_token");
        Log.i("json", auth_token);
        email = getIntent().getStringExtra("email");
        Log.i("json", email);
        username = getIntent().getStringExtra("username");
        Log.i("json", username);
        name = getIntent().getStringExtra("name");
        Log.i("json", name);
        university = findViewById(R.id.uni_txt);
        department = findViewById(R.id.dept_txt);
        id_number = findViewById(R.id.id_num_txt);

    }

    public void send2main(View view){
        if(!university.getText().toString().equals("") || !department.getText().toString().equals("") || !id_number.getText().toString().equals("")) {
            //studentAdminRequest.signup_student(auth_token,name,university.getText().toString(),department.getText().toString(),id_number.getText().toString(), new Student());
            startActivity(new Intent(this, MainStudent.class).
                    putExtra("auth_token",auth_token).
                    putExtra("activity","stu_signup").
                    putExtra("userType","Student user").
                    putExtra("email",email).
                    putExtra("university",university.getText().toString()).
                    putExtra("department",department.getText().toString()).
                    putExtra("id_number",id_number.getText().toString()).
                    putExtra("username",username).
                    putExtra("name",name));
        }else {
            Toast.makeText(this, R.string.blankField, Toast.LENGTH_SHORT).show();
        }

    }

}






