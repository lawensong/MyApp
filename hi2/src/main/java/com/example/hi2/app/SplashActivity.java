package com.example.hi2.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.example.hi2.utils.FileService;
import com.example.hi2.utils.SmackClient;

import java.util.Map;


public class SplashActivity extends Activity {
    private static final String TAG = "SplashActivity";
    private static final int sleepTime = 2000;
    private FileService fileService;
    private SmackClient smackClient;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        smackClient = new SmackClient(this);
        fileService = new FileService(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        new Thread(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
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
                    try {
                        Map<String, String> map = fileService.getUserInfo("private.txt");
                        String smack_user = map.get("username");
                        String smack_pass = map.get("password");
                        Boolean result = false;
                        if(smack_user!=null && smack_pass!=null){
                            result = smackClient.login(smack_user, smack_pass);
                        }

                        if(result){
                            smackClient.addPacketListener();
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
