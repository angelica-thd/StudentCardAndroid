package com.example.qrcodetry1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

public class MainStudent<photourl> extends AppCompatActivity {
    private TextView cred, info;
    private QR qr;
    private ImageView QRimg2, photo_img;
    private String userType, auth_token, activity;
    private BottomNavigationView bottomBar;
    private StudentAdminRequest studentAdminRequest;

    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Log.i("mainstudent", "student");
        photo_img = findViewById(R.id.photo_id);
        Bitmap photo = (Bitmap) getIntent().getParcelableExtra("photo_id");
        photo_img.setImageBitmap(photo);

        studentAdminRequest = new StudentAdminRequest(this);
        info = findViewById(R.id.student_info);
        cred = findViewById(R.id.cred);
        QRimg2 = findViewById(R.id.QRimg2);
        qr = new QR();


        activity = getIntent().getStringExtra("activity");
        auth_token = getIntent().getStringExtra("auth_token");
        userType = getIntent().getStringExtra("userType");
        cred.setText(userType);




        studentAdminRequest.me(auth_token);
        Log.i("userType", userType);
        Log.i("student from me() ", preferences.getString("student_info", "student info"));


        if (userType.contains("Student")) {

            StringBuilder infoStr = new StringBuilder();
            try {
                JSONObject student = new JSONObject(preferences.getString("student_info", "student info"));
                String photourl = student.getString("photoURL");
                student.remove("photoURL");
                Log.i("photourl",photourl);
                new DownloadImage().execute(photourl);
                for (Iterator<String> it = student.keys(); it.hasNext(); ) {
                    String key = it.next();

                    if (!key.contains("id") && !key.contains("_at") && student.has(key)) {

                        infoStr.append(key).append(": ").append(student.get(key)).append("\n");
                    }
                }
                qr.generate_QR(this, student.toString(), QRimg2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            info.setText(infoStr.toString());
        }


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


    private class DownloadImage extends AsyncTask {


        @Override
        protected Object doInBackground(Object[] URL) {
            String imageURL = (String) URL[0];

            Bitmap bitmap = null;

            try {
                // Download Image from URL
                URL url = new URL(imageURL);
                URLConnection conn = url.openConnection();
                conn.connect();
                for(int i=0; i<=5; i++){
                    InputStream input = url.openStream();
                    // Decode Bitmap
                    bitmap = BitmapFactory.decodeStream(input);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainStudent.this);
            mProgressDialog.setTitle("Download Image Tutorial");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected void onPostExecute(Object result) {
            // Set the bitmap into ImageView
            photo_img.setImageBitmap((Bitmap) result);
            // Close progressdialog
            mProgressDialog.dismiss();
        }
    }
}