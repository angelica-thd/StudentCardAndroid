package com.example.qrcodetry1;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.WriterException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class MainStudent<photourl> extends AppCompatActivity {
    private TextView cred, info;
    private QR qr;
    private ImageButton show_hide_QR;
    private ImageView identifier;
    private String userType, auth_token,photourl;
    private BottomNavigationView bottomBar;
    private StudentAPIrequest StudentAPIrequest;
    private int pressed;
    private TextView address, academicAddress, entryDate, department,fullname, studentshipType, studentNumber, institution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Log.i("mainstudent", "student");
        identifier = findViewById(R.id.identifier);
        address = findViewById(R.id.address_txt);
        academicAddress = findViewById(R.id.academic_address_txt);
        entryDate = findViewById(R.id.entryDate_txt);
        department = findViewById(R.id.department_txt);
        fullname = findViewById(R.id.fullname_txt);
        studentshipType = findViewById(R.id.studentshipType_txt);
        studentNumber = findViewById(R.id.studentNumber_txt);
        institution = findViewById(R.id.institution_txt);


        StudentAPIrequest = new StudentAPIrequest(this);
        cred = findViewById(R.id.cred);
        show_hide_QR = findViewById(R.id.showQR);
        pressed = 1;
        qr = new QR();



        auth_token = getIntent().getStringExtra("auth_token");
        userType = getIntent().getStringExtra("userType");

        File imgFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"photo_id.jpg");
        Log.i("path",imgFile.getAbsolutePath());
        if(imgFile.exists()) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                boolean delete = imgFile.delete();
                String result = delete ? "img deleted" : "img  not deleted";

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }

        }


        StudentAPIrequest.me(auth_token);

        if (userType.contains("Student")) {
            cred.setText("Φοιτητής");

            StringBuilder infoStr = new StringBuilder();
            try {
                JSONObject student = new JSONObject(preferences.getString("student_info", "student info"));
                photourl = student.getString("photo");
                byte[] decodedString = Base64.decode(photourl, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                identifier.setImageBitmap(decodedByte);


                infoStr.append(student.getString("greekFname")).append(" ").append(student.getString("greekLname")).append("\n")
                        .append(student.get("latinFname")).append(" ").append(student.get("latinLname"));
                fullname.setText(infoStr.toString());
                address.setText(student.getString("address"));
                academicAddress.setText(student.getString("academicAddress"));
                entryDate.setText(student.getString("entryDate"));
                department.setText(student.getString("department"));
                studentshipType.setText(student.getString("studentshipType"));
                institution.setText(student.getString("institution"));
                studentNumber.setText(student.getString("studentNumber"));


                 show_hide_QR.setOnClickListener(v -> {
                     Log.i("pressed", String.valueOf(pressed));
                     if(pressed == 1){
                         Log.i("pressed", String.valueOf(pressed));
                         show_hide_QR.setImageResource(R.drawable.ic_baseline_face_24);
                         try {
                             qr.generate_QR(this, student.getString("srtoken"), identifier);
                         } catch (WriterException e) {
                             e.printStackTrace();
                         } catch (JSONException e) {
                             e.printStackTrace();
                         }
                         pressed -=1;
                     }
                     else if (pressed == 0){
                         Log.i("pressed", String.valueOf(pressed));
                         show_hide_QR.setImageResource(R.drawable.ic_twotone_qr_code_24);
                         identifier.setImageBitmap(decodedByte);
                         pressed +=1;
                     }

                 });




            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        bottomBar = findViewById(R.id.bottombar);
        bottomBar.getMenu().getItem(0).setChecked(true);
        bottomBar.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.Settings) {
                startActivity(new Intent(getApplicationContext(), Settings.class).putExtra("user","student").putExtra("auth_token",auth_token));
                overridePendingTransition(0, 0);
                return true;
            }
            if (item.getItemId() == R.id.Logout){
                startActivity(new Intent(getApplicationContext(), Login_or_Reg.class));
                StudentAPIrequest.logout(auth_token);
                overridePendingTransition(0, 0);
                return true;
            }
            return item.getItemId() == R.id.home;
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView bottomBar;
        bottomBar = findViewById(R.id.bottombar);
        bottomBar.getMenu().getItem(0).setChecked(true);
    }
}