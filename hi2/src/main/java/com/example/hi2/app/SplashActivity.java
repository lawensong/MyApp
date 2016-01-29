package com.example.hi2.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.example.hi2.utils.SmackClient;


public class SplashActivity extends Activity {
    private static final int sleepTime = 2000;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();

        new Thread(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                SmackClient smackClient = new SmackClient();
                smackClient.getConnection();
                long costTime = System.currentTimeMillis() - start;
                if(sleepTime > costTime){
                    try {
                        Thread.sleep(sleepTime-costTime);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if(smackClient.getConnection() == null){
                    startActivity(new Intent(SplashActivity.this, SetActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }).start();
    }
}
