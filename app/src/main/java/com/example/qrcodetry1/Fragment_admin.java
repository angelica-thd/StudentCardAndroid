package com.example.qrcodetry1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

public class Fragment_admin extends Fragment {
    FloatingActionButton fab;
    EditText email,username,name,pass,pass_conf;
    private StudentAdminRequest studentAdminRequest;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, container, false);
        Button button = (Button) view.findViewById(R.id.login_btn);
        button.setOnClickListener(v -> {
            startActivity(new Intent(view.getContext(), Login_or_Reg.class));
        });
        studentAdminRequest = new StudentAdminRequest(view.getContext());
        email = (EditText) view.findViewById(R.id.email_admin);
        username = (EditText) view.findViewById(R.id.username_admin);
        name = (EditText) view.findViewById(R.id.fname_admin);
        pass_conf = (EditText) view.findViewById(R.id.pass_conf_admin);
        pass = (EditText) view.findViewById(R.id.pass_admin);
        fab = (FloatingActionButton) view.findViewById(R.id.admin_fab);
        fab.setOnClickListener(v -> {
            Log.i("click","click");
            if(!email.getText().toString().equals("")
                    || !email.getText().toString().equals(null)
                    || !username.getText().toString().equals("")
                    || !username.getText().toString().equals(null)
                    || !name.getText().toString().equals("")
                    || !name.getText().toString().equals(null)
                    || !pass.getText().toString().equals("")
                    || !pass.getText().toString().equals(null)
                    || !pass.getText().toString().equals("")
                    || !pass_conf.getText().toString().equals(null)) {
                JSONObject postData = new JSONObject();
                try {
                    postData.put("username", username.getText().toString());
                    postData.put("name", name.getText().toString());
                    postData.put("email", email.getText().toString());
                    postData.put("password", pass.getText().toString());
                    postData.put("password_confirmation", pass_conf.getText().toString());
                } catch (
                        JSONException e) {
                    e.printStackTrace();
                }
                studentAdminRequest.signup_user(postData,true);

            }else {
                Toast.makeText(view.getContext(), R.string.blankField, Toast.LENGTH_SHORT).show();
            }

        });
        return view;
    }
}