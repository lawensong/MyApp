package com.example.hi2.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Administrator on 2016/2/1.
 */
public class ChatActivity extends Activity implements View.OnClickListener {
    private final static String TAG = "ChatActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final String user = this.getIntent().getStringExtra("user");
        final String nick = this.getIntent().getStringExtra("nick");
        final String avatar = this.getIntent().getStringExtra("avatar");

        initView();
        setUpView();
    }

    private void initView(){

    }

    private void setUpView(){

    }

    @Override
    public void onClick(View v) {

    }
}
