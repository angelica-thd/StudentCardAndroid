package com.example.qrcodetry1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QR_result_Activity extends AppCompatActivity {
    private ImageView identifier;
    private TextView address, academicAddress, entryDate, department,fullname, studentshipType, studentNumber, institution;
    private String auth_token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_result_);

        identifier = findViewById(R.id.identifier2);
        address = findViewById(R.id.address_txt2);
        academicAddress = findViewById(R.id.academic_address_txt2);
        entryDate = findViewById(R.id.entryDate_txt2);
        department = findViewById(R.id.department_txt2);
        fullname = findViewById(R.id.fullname_txt2);
        studentshipType = findViewById(R.id.studentshipType_txt2);
        studentNumber = findViewById(R.id.studentNumber_txt2);
        institution = findViewById(R.id.institution_txt2);

        String photo = getIntent().getStringExtra("photo");
        Log.i("photo",photo);
        byte[] decodedString = Base64.decode(photo, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        identifier.setImageBitmap(decodedByte);

        fullname.setText(getIntent().getStringExtra("name"));
        address.setText(getIntent().getStringExtra("address"));
        academicAddress.setText(getIntent().getStringExtra("academicAddress"));
        entryDate.setText(getIntent().getStringExtra("entryDate"));
        department.setText(getIntent().getStringExtra("department"));
        studentshipType.setText(getIntent().getStringExtra("studentshipType"));
        institution.setText(getIntent().getStringExtra("institution"));
        studentNumber.setText(getIntent().getStringExtra("studentNumber"));
        auth_token = getIntent().getStringExtra("auth_token");
    }

    public void goBack(View view){
        startActivity(new Intent(this, MainAdmin.class).putExtra("auth_token", auth_token));
    }
}