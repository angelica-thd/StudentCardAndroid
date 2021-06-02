package com.example.qrcodetry1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainAdmin extends AppCompatActivity {
    private StudentAdminRequest studentAdminRequest;
    private TextView info,adminType;
    private ImageButton scan;
    private QR qr;
    private String activity,auth_token,userType;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        Log.i("mainadmin","admin");
        studentAdminRequest = new StudentAdminRequest(this);
        info = findViewById(R.id.QRresult);
        adminType = findViewById(R.id.user);
        scan = findViewById(R.id.scanQR);
        qr = new QR();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        activity = getIntent().getStringExtra("activity");
        auth_token = getIntent().getStringExtra("auth_token");
        userType = getIntent().getStringExtra("userType");
        adminType.setText(userType);

        scan.setOnClickListener(v -> qr.scan_QR(scan.getContext()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            if(!data.getStringExtra("SCAN_RESULT").equals(null)){
                Log.i("scan result",data.getStringExtra("SCAN_RESULT"));
                try {
                    JSONObject jsonresult = new JSONObject(result.getContents());
                    if(jsonresult.has("srtoken")) {
                        JSONObject srtoken = new JSONObject().put("identifier", jsonresult.getString("srtoken"));

                        RequestQueue requestQueue = Volley.newRequestQueue(this);
                        StringBuilder postUrl = new StringBuilder().append(studentAdminRequest.getPostURL());

                        if (!postUrl.toString().contains("find/student"))
                            postUrl = postUrl.append("find/student");
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                                postUrl.toString(),
                                srtoken,
                                response -> {
                                    Log.i("response", response.toString());
                                    try {
                                        String message = response.getString("message");
                                        if (message.contains("Student found")) {
                                            if(!response.getString("student_token").equals(null) ){
                                                info.setText(getString(R.string.QRresult));
                                                info.append("\n\n");
                                                info.append(jsonresult.getString("greekFname"));
                                                info.append(" ");
                                                info.append(jsonresult.getString("greekLname"));
                                                info.append("\nο οποίος φοιτά στο ");
                                                info.append(jsonresult.getString("institution"));
                                                info.append(" με αριθμό μητρώου ");
                                                info.append(jsonresult.getString("studentNumber"));
                                            }else
                                                Toast.makeText(this,getString(R.string.norsult),Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                },
                                studentAdminRequest.errorListener) {

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> headers = new HashMap<>();
                                headers.put("Authorization", auth_token);
                                return headers;
                            }
                        };
                        requestQueue.add(request);
                    }
                       else{
                        Toast.makeText(this,getString(R.string.norsult),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    info.setText(getString(R.string.norsult));
                }
            }else{
                Toast.makeText(this,getString(R.string.norsult),Toast.LENGTH_LONG).show();
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}