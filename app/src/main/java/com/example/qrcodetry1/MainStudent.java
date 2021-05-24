package com.example.qrcodetry1;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.CookieManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.io.File;
import java.util.Iterator;

public class MainStudent<photourl> extends AppCompatActivity {
    private TextView cred, info;
    private QR qr;
    private ImageView QRimg2, photo_img;
    private String userType, auth_token, activity,photourl;
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

        if (userType.contains("Student")) {

            StringBuilder infoStr = new StringBuilder();
            try {
                JSONObject student = new JSONObject(preferences.getString("student_info", "student info"));
                photourl = student.getString("photoURL");
                student.remove("photoURL");
                Log.i("photourl",photourl);
                //new DownloadImage().execute(photourl);
                setIDphoto();

                for (Iterator<String> it = student.keys(); it.hasNext(); ) {
                    String key = it.next();

                    if (!key.contains("id") && !key.contains("_at") && !key.contains("srtoken") && student.has(key)) {

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
            return item.getItemId() == R.id.home;
        });
    }


    private void setIDphoto() {
        String pictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        File imgFile = new  File(pictures+"/photo_id.jpg");
        Log.i("path",imgFile.getAbsolutePath());
        if(imgFile.exists()) {
            Bitmap b = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            photo_img.setBackgroundResource(R.drawable.round);
            photo_img.setImageBitmap(b);
        }else{
          downloadPhoto(photourl);
          setIDphoto();
          Toast.makeText(this, "not found photo", Toast.LENGTH_LONG).show();
        }


    }

    private void downloadPhoto(String downloadUrlOfImage){
        try {
            DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(downloadUrlOfImage);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);

            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle("photo_id")
                    .addRequestHeader("Cookie", CookieManager.getInstance().getCookie(downloadUrlOfImage))
                    .addRequestHeader("User-Agent", "{'User-Agent':\"Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.27 Safari/537.17\"}")
                    .setMimeType("image/jpeg") // Your file type. You can use this code to download other file types also.
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, File.separator + "photo_id.jpg");
            dm.enqueue(request);
            setIDphoto();
            // Toast.makeText(this, "Image download started.", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(this, "Image download failed.", Toast.LENGTH_SHORT).show();
        }
    }
}