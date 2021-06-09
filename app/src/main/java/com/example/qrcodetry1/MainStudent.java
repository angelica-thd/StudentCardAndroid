package com.example.qrcodetry1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.WriterException;

import org.json.JSONException;
import org.json.JSONObject;

public class MainStudent<photourl> extends AppCompatActivity {
    private TextView cred, info;
    private QR qr;
    private ImageButton show_hide_QR;
    private ImageView identifier;
    private String userType, auth_token,photourl;
    private BottomNavigationView bottomBar;
    private StudentAdminRequest studentAdminRequest;
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


        studentAdminRequest = new StudentAdminRequest(this);
        cred = findViewById(R.id.cred);
        show_hide_QR = findViewById(R.id.showQR);
        pressed = 1;
        qr = new QR();



        auth_token = getIntent().getStringExtra("auth_token");
        userType = getIntent().getStringExtra("userType");




        studentAdminRequest.me(auth_token);

        if (userType.contains("Student")) {
            cred.setText("Φοιτητής");

            StringBuilder infoStr = new StringBuilder();
            try {
                JSONObject student = new JSONObject(preferences.getString("student_info", "student info"));
                photourl = student.getString("photo");
                byte[] decodedString = Base64.decode(photourl, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                identifier.setImageBitmap(decodedByte);
                Log.i("photourl",photourl);

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


               /* for (Iterator<String> it = student.keys(); it.hasNext(); ) {
                    String key = it.next();
                    if (!key.contains("id") && !key.contains("_at") && !key.contains("srtoken") && !key.contains("photo") && student.has(key)) {
                        infoStr.append(key).append(": ").append(student.get(key)).append("\n");
                    }
                } */
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
                startActivity(new Intent(getApplicationContext(), Settings.class));
                overridePendingTransition(0, 0);
                return true;
            }
            if (item.getItemId() == R.id.Logout){
                startActivity(new Intent(getApplicationContext(), Login_or_Reg.class));
                studentAdminRequest.logout(auth_token);
                overridePendingTransition(0, 0);
                return true;
            }
            return item.getItemId() == R.id.home;
        });
    }



    /*private void setIDphoto() {
        String pictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        File imgFile = new  File(pictures+"/photo_id.jpg");
        Log.i("path",imgFile.getAbsolutePath());
        if(imgFile.exists()) {
            Bitmap b = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            photo_img.setBackgroundResource(R.drawable.round);
            photo_img.setImageBitmap(b);
            Toast.makeText(this,"found photo",Toast.LENGTH_LONG).show();
        }else{
          downloadPhoto(photourl);
          setIDphoto();
         //Toast.makeText(this, "not found photo", Toast.LENGTH_LONG).show();
        }


    }

    private void downloadPhoto(String downloadUrlOfImage){
        try {
            String cookie =  CookieManager.getInstance().getCookie(downloadUrlOfImage);
            DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(downloadUrlOfImage);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);

            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle("photo_id")
                    .addRequestHeader("Cookie",cookie)
                    .addRequestHeader("User-Agent", "{'User-Agent':\"Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.27 Safari/537.17\"}")
                    .setMimeType("image/jpeg") // Your file type. You can use this code to download other file types also.
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, File.separator + "photo_id.jpg");
            dm.enqueue(request);
            setIDphoto();
             Toast.makeText(this, "Image download started.", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(this, "Image download failed.", Toast.LENGTH_SHORT).show();
        }
    }*/
}