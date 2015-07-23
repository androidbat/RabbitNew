package com.m.rabbit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        View log_iv = findViewById(R.id.log_iv);
        log_iv.setVisibility(View.VISIBLE);
        ScaleAnimation sa = new ScaleAnimation(0.3f,1f,0.3f,1f, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        sa.setFillAfter(true);
        sa.setDuration(1000);
        log_iv.startAnimation(sa);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }, 2000);
        EventBus.getDefault().register(new Object(){
            @Subscribe
            public void onEvent(String aa){
                Toast.makeText(getApplicationContext(),aa,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
