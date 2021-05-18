package com.example.qrcodetry1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class StudentAdminRequest {
    private Context context;
    private StringBuilder postUrl = new StringBuilder().append("https://3000-sapphire-porcupine-rq946s26.ws-eu04.gitpod.io/");
    private String photourl;

    public StudentAdminRequest(Context context) {
        this.context = context;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    // POST /auth/login
    public void auth_login(JSONObject postData) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        if (!postUrl.toString().contains("auth/login")) postUrl = postUrl.append("auth/login");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                postUrl.toString(),
                postData,
                response -> {
                    Log.i("response", response.toString());
                    try {
                        if (response.getString("message").contains("Basic"))
                            context.startActivity(new Intent(context, MainAdmin.class).
                                    putExtra("activity","login").
                                    putExtra("auth_token", response.getString("auth_token")).
                                    putExtra("email", response.getString("user")).
                                    putExtra("userType", response.getString("message")));
                        else if (response.getString("message").contains("Student")) {
                            context.startActivity(new Intent(context, MainStudent.class).
                                    putExtra("activity","login").
                                    putExtra("auth_token", response.getString("auth_token")).
                                    putExtra("email", response.getString("user")).
                                    putExtra("userType", response.getString("message")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                errorListener);
        requestQueue.add(jsonObjectRequest);
    }

    //GET /me
    public void me(String auth_token) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        Log.i("me","here");
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        if (!postUrl.toString().contains("me")) postUrl = postUrl.append("me");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                postUrl.toString(),
                null,
                response -> {
                    Log.i("me response", response.toString());
                    try {
                        JSONObject jsonObject = response.getJSONObject("credentials");
                        if (jsonObject.has("username")) {
                            String username = jsonObject.getString("username");
                            String name = jsonObject.getString("name");
                            editor.putString("username", username).apply();
                            editor.putString("name", name).apply();

                        }
                        if (response.has("student_info")) {
                            jsonObject = response.getJSONObject("student_info");
                            Log.i("me", jsonObject.toString());
                            editor.putString("student_info", jsonObject.toString());
                            editor.apply();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                errorListener) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", auth_token);
                return headers;
            }
        };

        requestQueue.add(request);
    }


    //POST /signup
    public void signup_user(JSONObject postData, Boolean admin) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        Log.i("postData",postData.toString());

        if (!postUrl.toString().contains("signup")) postUrl = postUrl.append("signup");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                postUrl.toString(),
                postData,
                response -> {
                    Log.i("response", response.toString());
                    try {
                        if (response.has("auth_token")) {
                            Log.i("response", response.toString());
                            String auth_token = response.getString("auth_token");

                            //create class to store instead
                            editor.putString("username", postData.getString("username"));
                            editor.putString("auth_token", auth_token);
                            editor.putString("email", postData.getString("email"));
                            editor.putString("name", postData.getString("name"));
                            editor.putString("message", response.getString("message"));
                            editor.apply();

                            if(admin)
                               context.startActivity(new Intent(context,MainAdmin.class).putExtra("auth_token",auth_token));
                            else
                                context.startActivity(new Intent(context, StudentInfoReg.class).putExtra("auth_token", auth_token));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                errorListener);

        requestQueue.add(jsonObjectRequest);

    }

    public void signup_student(String auth_token, JSONObject postData) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        if (!postUrl.toString().contains("signup/student"))  postUrl = postUrl.append("signup/student");
        Log.i("postdata", postData.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                postUrl.toString(),
                postData,
                response -> {
                    Log.i("response", response.toString());
                    try {
                        if (response.has("message")) {
                            String message = response.getString("message");
                            if (message.contains("successfully")) {
                                Log.i("context", context.toString());
                                context.startActivity(new Intent(context, MainStudent.class)
                                        .putExtra("auth_token",auth_token)
                                        .putExtra("photourl",this.getPhotourl())
                                        .putExtra("activity", "stu_signup")
                                        .putExtra("userType", "Student user"));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", auth_token);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);

    }


        // fix error messages to user now that you can read them XD
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = null;
                if (error.networkResponse != null) {
                    Log.i("Errorresponse", "Error Response code: " + error.networkResponse.statusCode);
                    try {
                        errorMessage = new String(error.networkResponse.data, "UTF-8");
                        Log.i("Errorresponse", "Error Response message: " + errorMessage);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    switch (error.networkResponse.statusCode) {
                        case 404:
                            Toast.makeText(context, R.string.checkInternet, Toast.LENGTH_LONG).show();

                        case 401:
                            Toast.makeText(context, R.string.invalid_creds, Toast.LENGTH_LONG).show();

                        case 422:
                            if (errorMessage.contains("a user with this email or username already exists")) {
                                Toast.makeText(context, R.string.useralreadyexists, Toast.LENGTH_LONG).show();
                            }
                            else if (errorMessage.contains("a student with this student number already exists")){
                                Toast.makeText(context, R.string.stalreadyexists, Toast.LENGTH_LONG).show();
                            }
                            else if (errorMessage.contains("Missing token")) {
                                Toast.makeText(context, R.string.authorization, Toast.LENGTH_LONG).show();
                            }
                            else if (errorMessage.contains("Validation failed")) {
                               if(errorMessage.contains("Email is invalid"))
                                    Toast.makeText(context, R.string.invalidEmail, Toast.LENGTH_LONG).show();

                                else if (errorMessage.contains("Password confirmation doesn't match Password"))
                                    Toast.makeText(context, R.string.invalid_pass, Toast.LENGTH_LONG).show();

                                Toast.makeText(context, R.string.validationfail, Toast.LENGTH_LONG).show();
                            }
                        case 500:
                            Toast.makeText(context, R.string.error500, Toast.LENGTH_LONG).show();
                    }
                }

            }
        };
}