package com.example.hi2.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.example.hi2.Hi2Application;
import com.example.hi2.utils.FileService;
import com.example.hi2.utils.SmackClient;

import java.util.Map;


public class SplashActivity extends Activity {
    private static final String TAG = "SplashActivity";
    private static final int sleepTime = 2000;
    private Hi2Application hi2Application;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        hi2Application = Hi2Application.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        new Thread(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                hi2Application.getConnection();
                long costTime = System.currentTimeMillis() - start;
                if(sleepTime > costTime){
                    try {
                        Thread.sleep(sleepTime-costTime);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if(hi2Application.getConnection() == null){
                    startActivity(new Intent(SplashActivity.this, SetActivity.class));
                    finish();
                }else {
                    try {
                        Boolean result = hi2Application.login();

                        if(result){
                            hi2Application.addPacketListener();
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        }else {
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            finish();
                        }
                    }catch (Exception e){
                        Log.d(TAG, "default login fail: "+e);
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            }
        }).start();
    }
}
