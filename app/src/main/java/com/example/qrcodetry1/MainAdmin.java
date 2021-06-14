package com.example.qrcodetry1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainAdmin extends AppCompatActivity {
    private StudentAdminRequest studentAdminRequest;
    private TextView adminType;
    private ImageButton scan;
    private QR qr;
    private String activity, auth_token;
    private EditText studentNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        Log.i("mainadmin", "admin");
        studentAdminRequest = new StudentAdminRequest(this);
        adminType = findViewById(R.id.user);
        scan = findViewById(R.id.scanQR);
        qr = new QR();
        activity = getIntent().getStringExtra("activity");
        auth_token = getIntent().getStringExtra("auth_token");
        adminType.setText("Έλεγχος ακαδημαϊκής ταυτότητας");
        studentNumber = findViewById(R.id.searchStudent2);

        scan.setOnClickListener(v -> qr.scan_QR(scan.getContext()));

        BottomNavigationView bottomBar;
        bottomBar = findViewById(R.id.navbar);
        bottomBar.getMenu().getItem(0).setChecked(true);
        bottomBar.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.Settings) {
                startActivity(new Intent(getApplicationContext(), Settings.class).putExtra("user","admin").putExtra("auth_token",auth_token));
                overridePendingTransition(0, 0);
                return true;
            }
            if (item.getItemId() == R.id.Logout) {
                startActivity(new Intent(getApplicationContext(), Login_or_Reg.class));
                studentAdminRequest.logout(auth_token);
                overridePendingTransition(0, 0);
                return true;
            }
          
            return item.getItemId() == R.id.home;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (!data.getStringExtra("SCAN_RESULT").equals(null)) {
                Log.i("scan result", data.getStringExtra("SCAN_RESULT"));
                find_by_identifier(result.getContents());
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    public void search_by_studentNumber(View view){
        find_by_identifier(studentNumber.getText().toString());
    }

    public void find_by_identifier(String identifier) {
        try {
            JSONObject srtoken = new JSONObject().put("identifier", identifier);

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String postUrl ="https://3000-pink-dragon-w0hlrpcb.ws-eu09.gitpod.io/find/student";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    postUrl,
                    srtoken,
                    response -> {
                        Log.i("response", response.toString());
                        try {
                            String message = response.getString("message");

                            if (message.contains("Student found")) {
                                if (!response.getString("student_token").equals(null)) {
                                    JSONObject student = new JSONObject(response.getString("student_token"));
                                    StringBuilder info_str = new StringBuilder();
                                    info_str.append(student.getString("greekFname")).append(" ").append(student.getString("greekLname")).append("\n")
                                            .append(student.get("latinFname")).append(" ").append(student.get("latinLname"));
                                    startActivity(new Intent(this, QR_result_Activity.class)
                                            .putExtra("name", info_str.toString())
                                            .putExtra("photo", student.getString("photo"))
                                            .putExtra("address", student.getString("address"))
                                            .putExtra("academicAddress", student.getString("academicAddress"))
                                            .putExtra("entryDate", student.getString("entryDate"))
                                            .putExtra("department", student.getString("department"))
                                            .putExtra("studentshipType", student.getString("studentshipType"))
                                            .putExtra("institution", student.getString("institution"))
                                            .putExtra("studentNumber", student.getString("studentNumber"))
                                            .putExtra("auth_token", auth_token));
                                } else
                                    Toast.makeText(MainAdmin.this, getString(R.string.norsult), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    studentAdminRequest.errorListener){

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", auth_token);
                    return headers;
                }
            };
            requestQueue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView bottomBar;
        bottomBar = findViewById(R.id.navbar);
        bottomBar.getMenu().getItem(0).setChecked(true);
    }
}