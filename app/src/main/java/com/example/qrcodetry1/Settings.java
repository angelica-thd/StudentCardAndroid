package com.example.qrcodetry1;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Settings extends AppCompatActivity {
    private BottomNavigationView bottomBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        bottomBar = findViewById(R.id.bottombar_sett);
        bottomBar.getMenu().getItem(1).setChecked(true);
        bottomBar.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.Home) {
                startActivity(new Intent(getApplicationContext(), Main.class));
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
            return item.getItemId() == R.id.Settings;
        });
    }


}
