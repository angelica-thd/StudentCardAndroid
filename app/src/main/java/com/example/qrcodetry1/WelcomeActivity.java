package com.example.qrcodetry1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

public class WelcomeActivity extends AppCompatActivity {
    ConstraintLayout constraintLayout;
    ImageView imageView,logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        constraintLayout = findViewById(R.id.constraintLayout);
        constraintLayout.setBackgroundColor(Color.BLACK);
        imageView=findViewById(R.id.welcome_img);
        logo=findViewById(R.id.logoimg);
        AppCenter.start(getApplication(), "b34a3cf4-2f56-4600-afa4-e2ddb2e74e2b",
                Analytics.class, Crashes.class);

        imageView.setImageResource(R.drawable.logobackground);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        Animation fadeIn = AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.fade_in);

        logo.startAnimation(fadeOut);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                logo.startAnimation(fadeIn);
                logo.setBackgroundResource(R.drawable.checksudent_removebg_preview_2_);
                logo.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

        });

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                logo.startAnimation(fadeOut);
                logo.setBackgroundResource(R.drawable.scanqr_removebg_preview);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    public void to_main_activity(View view){
        Intent ma = new Intent(this, Login_or_Reg.class);
        startActivity(ma);
    }

}

