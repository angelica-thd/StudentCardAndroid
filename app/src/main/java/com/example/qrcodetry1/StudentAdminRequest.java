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



    public StudentAdminRequest(Context context) {
        this.context = context;
    }

    // POST /auth/login
    public void auth_login(String email, String pass) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject postData = new JSONObject();
        try {
            postData.put("email", email);
            postData.put("password", pass);

        } catch (
                JSONException e) {
            e.printStackTrace();
        }
        if(!postUrl.toString().contains("auth/login")) postUrl = postUrl.append("auth/login");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                postUrl.toString(),
                postData,
                response -> {
                    Log.i("response",response.toString());
                    try {
                        if (response.has("auth_token")){
                            Log.i("response",response.toString());
                            String auth_token = response.getString("auth_token");
                            context.startActivity(new Intent(context, Main.class).
                                    putExtra("auth_token", auth_token).
                                    putExtra("email", email).
                                    putExtra("userType", response.getString("message")));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                login_errorListener);
        requestQueue.add(jsonObjectRequest);
    }

    //GET /me
    public void me(String auth_token){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        if(!postUrl.toString().contains("me")) postUrl =postUrl.append("me");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                postUrl.toString(),
                null,
                response -> {
                    Log.i("response", response.toString());
                    try {
                        JSONObject jsonObject = response.getJSONObject("credentials");
                        Log.i("cred", jsonObject.toString());
                        if(jsonObject.has("username")){
                            String username = jsonObject.getString("username");
                            String name = jsonObject.getString("name");
                            Log.i("json",name);
                            editor.putString("username",username).apply();
                            editor.putString("name",name).apply();

                        }
                        if (response.has("student_info")){
                            jsonObject = response.getJSONObject("student_info");
                            String department = jsonObject.getString("department");
                            String id_number = jsonObject.getString("id_number");
                            String university = jsonObject.getString("university");
                            Log.i("json",department);
                            editor.putString("department",department);
                            editor.putString("university",university);
                            editor.putString("id_number",id_number);
                            editor.apply();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                me_errorListener) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization",auth_token);
                return headers;
            }
        };

        requestQueue.add(request);
    }



    //POST /signup
    public void signup_user(String username, String name, String email, String password, String password_confirmation){
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();

        JSONObject postData = new JSONObject();
        try {
            postData.put("username", username);
            postData.put("name", name);
            Log.i("postname",name);
            postData.put("email", email);
            postData.put("password", password);
            postData.put("password_confirmation", password_confirmation);


        } catch (
                JSONException e) {
            e.printStackTrace();
        }
        if(!postUrl.toString().contains("signup")) postUrl =postUrl.append("signup");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                postUrl.toString(),
                postData,
                response -> {
                    Log.i("response",response.toString());
                    try {
                        if (response.has("auth_token")){
                            Log.i("response",response.toString());
                            String auth_token = response.getString("auth_token");

                            //create class to store instead
                            editor.putString("username", username);
                            editor.putString("auth_token", auth_token);
                            editor.putString("email", email);
                            editor.putString("name", name);
                            Log.i("name",name);
                            editor.putString("message", response.getString("message"));
                            editor.apply();

                            context.startActivity(new Intent(context,StudentInfoReg.class).putExtra("auth_token",auth_token));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                signup_errorListener);

        requestQueue.add(jsonObjectRequest);

    }

    public void signup_student(String auth_token, String name, String university, String department, String id_number, Student student){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject postData = new JSONObject();
        try {
            postData.put("name", name);
            postData.put("university", university);
            postData.put("department", department);
            postData.put("id_number", id_number);
        } catch (
                JSONException e) {
            e.printStackTrace();
        }
        if(!postUrl.toString().contains("signup/student")) postUrl =postUrl.append("signup/student");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                postUrl.toString(),
                postData,
                response -> {
                    Log.i("response",response.toString());
                    try {
                        if (response.has("message")){
                            //Log.i("response",response.toString());
                            String message = response.getString("message");
                            Log.i("signupSt",message);
                            if(message.contains("successfully")){
                                editor.putString("userType","Student user");
                                editor.putString("name",name);
                                editor.putString("department",department);
                                editor.putString("university",university);
                                editor.putString("id_number",id_number);
                                editor.apply();
                                Log.i("context",context.toString());
                                context.startActivity(new Intent(context,Main.class).putExtra("student",student).putExtra("activity","stu_signup"));
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                signup_errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization",auth_token);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);

    }

    Response.ErrorListener login_errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (error.networkResponse != null) {
                Log.i("response","Error Response code: " + error.networkResponse.statusCode);
                switch (error.networkResponse.statusCode){
                    case 404:
                        Toast.makeText(context,R.string.checkInternet,Toast.LENGTH_LONG).show();
                    case 401:
                        Toast.makeText(context,R.string.invalid_creds,Toast.LENGTH_LONG).show();
                    case 500:
                        Toast.makeText(context,R.string.error500,Toast.LENGTH_LONG).show();
                }
            }

        }
    };

    Response.ErrorListener me_errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (error.networkResponse != null) {
                Log.i("response","Error Response code: " + error.networkResponse.statusCode);
                switch (error.networkResponse.statusCode){
                    case 404:
                        Toast.makeText(context,R.string.checkInternet,Toast.LENGTH_LONG).show();
                    case 422:
                        Toast.makeText(context,R.string.authorization,Toast.LENGTH_LONG).show();
                    case 500:
                        Toast.makeText(context,R.string.error500,Toast.LENGTH_LONG).show();
                }
            }

        }
    };

    // fix error messages to user now that you can read them XD
    Response.ErrorListener signup_errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            String errorMessage = null;
            if (error.networkResponse != null) {
                Log.i("Errorresponse","Error Response code: " + error.networkResponse.statusCode);
                try {
                    errorMessage = new String(error.networkResponse.data,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

               Log.i("Errorresponse","Error Response message: " + errorMessage);

                switch (error.networkResponse.statusCode){
                    case 404:
                        Toast.makeText(context,R.string.checkInternet,Toast.LENGTH_LONG).show();
                    case 422:
                        if(errorMessage.contains("a user with this email or username already exists")){
                            Toast.makeText(context,R.string.alreadyexists,Toast.LENGTH_LONG).show();
                        }
                       else
                           Toast.makeText(context,R.string.validationfail,Toast.LENGTH_LONG).show();
                    case 500:
                        Toast.makeText(context,R.string.error500,Toast.LENGTH_LONG).show();
                }
            }

        }
    };
}
