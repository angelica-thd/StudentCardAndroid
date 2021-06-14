package com.example.qrcodetry1;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

public class Settings extends AppCompatActivity {
    private BottomNavigationView bottomBar;
    private String user,auth_token,name,username,email;
    private StudentAdminRequest studentAdminRequest;
    private FloatingActionButton fab;
    EditText email_txt,username_txt,name_txt,pass_txt,pass_conf_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        fab = findViewById(R.id.settings_fab);
        email_txt =  findViewById(R.id.email_st2);
        username_txt =  findViewById(R.id.username_st2);
        name_txt =  findViewById(R.id.fname_st2);
        pass_conf_txt =  findViewById(R.id.pass_st2);
        pass_txt =  findViewById(R.id.pass_conf_st2);


        user = getIntent().getStringExtra("user");
        auth_token = getIntent().getStringExtra("auth_token");
        studentAdminRequest = new StudentAdminRequest(this);

        studentAdminRequest.me(auth_token);

        name = preferences.getString("name", "name");
        username = preferences.getString("username", "username");
        email = preferences.getString("email", "email");
        email_txt.setText(email);
        username_txt.setText(username);
        name_txt.setText(name);


        bottomBar = findViewById(R.id.settings_menu);
        bottomBar.getMenu().getItem(1).setChecked(true);
        bottomBar.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.Home) {
                startActivity(new Intent(getApplicationContext(), MainAdmin.class));
                overridePendingTransition(0, 0);
                return true;
            }
            if (item.getItemId() == R.id.Logout) {
                startActivity(new Intent(getApplicationContext(), Login_or_Reg.class));
                studentAdminRequest.logout(auth_token);
                overridePendingTransition(0, 0);
                return true;
            }
            return item.getItemId() == R.id.Settings;
        });

        fab.setOnClickListener(v -> {
            Log.i("fab","fab");
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
            builder.setMessage(R.string.update_check)
                    .setPositiveButton(R.string.update, (dialog, id) -> {
                        Log.i("click","click");
                        JSONObject postData = new JSONObject();
                        try{
                            if(!email_txt.getText().toString().equals("")|| !email_txt.getText().toString().equals(null))
                                postData.put("email", email_txt.getText().toString());

                            if(!username_txt.getText().toString().equals("")|| !username_txt.getText().toString().equals(null))
                                postData.put("username", username_txt.getText().toString());

                            if(!name_txt.getText().toString().equals("")|| !name_txt.getText().toString().equals(null))
                                postData.put("name", name_txt.getText().toString());

                            if(!pass_txt.getText().toString().equals("")|| !pass_txt.getText().toString().equals(null)
                                    ||!pass_conf_txt.getText().toString().equals("")|| !pass_conf_txt.getText().toString().equals(null)
                                    && !pass_txt.getText().toString().equals(pass_conf_txt.getText().toString()))
                                postData.put("password", pass_txt.getText().toString());
                            else {
                                Toast toast = Toast.makeText(this, R.string.invalid_pass, Toast.LENGTH_LONG);
                                View view = toast.getView();
                                view.setBackgroundResource(R.drawable.error_toast);
                                TextView text = (TextView) toast.getView().findViewById(android.R.id.message);
                                text.setTextColor(Color.RED);
                                toast.show();
                            }

                            studentAdminRequest.update(postData, auth_token);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    })
                    .setNegativeButton(R.string.cancel, (dialog, id) -> {
                       dialog.cancel();
                    });
            builder.create();
            builder.show();
            String message =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("message","fail");
            if( message.contains("succcess")){
                finish();
                startActivity(getIntent());
            }
        });
    }


}
