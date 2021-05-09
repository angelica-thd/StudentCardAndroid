package com.example.qrcodetry1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.WriterException;

public class Main extends AppCompatActivity {
    private TextView cred,info;
    private QR qr;
    private ImageView QRimg2,photo_img;
    private String userType, username, name, university, department, id_number,auth_token,activity;
    private StringBuilder student_info;
    private BottomNavigationView bottomBar;
    private StudentAdminRequest studentAdminRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        photo_img = findViewById(R.id.photo_id);
        Bitmap photo = (Bitmap) getIntent().getParcelableExtra("photo_id");
        photo_img.setImageBitmap(photo);

        studentAdminRequest = new StudentAdminRequest(this);
        info = findViewById(R.id.student_info);
        cred = findViewById(R.id.cred);
        QRimg2 = findViewById(R.id.QRimg2);
        qr = new QR();

        userType = preferences.getString("userType","userType");
        activity = getIntent().getStringExtra("activity");
        auth_token = preferences.getString("auth_token","auth_token");

        username = preferences.getString("username","username");
        name = preferences.getString("name","name");

        if(name.contains("+")) name = name.replace("+"," ");
        Log.i("info",name);

        studentAdminRequest.me(auth_token);

        if(userType.contains("Student user")){
            Student student = getIntent().getParcelableExtra("student");
            Log.i("student",student.getGreekFname());
            university = preferences.getString("university","university");
            department = preferences.getString("department","department");
            id_number = preferences.getString("id_number","id_number");
            }

            if(university.contains("+") || department.contains("+")) {
                university = university.replace("+"," ");
                department = department.replace("+"," ");
            }



            student_info = new StringBuilder().
                    append("University: ").append(university).append("\n").
                    append("Department: ").append(department).append("\n").
                    append("ID number: ").append(id_number).append("\n").
                    append("Name: ").append(name);
            info.setText(student_info.toString());
            Log.i("info",info.toString());
            try {
                qr.generate_QR(this,student_info.toString(),QRimg2);
            } catch (WriterException e) {
                e.printStackTrace();
            }


        cred.setText(userType);



        bottomBar = findViewById(R.id.bottombar);
        bottomBar.getMenu().getItem(0).setChecked(true);
        bottomBar.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.Settings) {
                startActivity(new Intent(getApplicationContext(), Settings.class));
                overridePendingTransition(0, 0);
                return true;
            }
            if (item.getItemId() == R.id.Schedule) {
                startActivity(new Intent(getApplicationContext(), Schedule.class));
                overridePendingTransition(0, 0);
                return true;
            }
            if (item.getItemId() == R.id.Grades) {
                startActivity(new Intent(getApplicationContext(), Grades.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return item.getItemId() == R.id.home;
        });
    }
}