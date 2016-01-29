package com.example.hi2.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/1/28.
 */
public class MainActivity extends BaseActivity{
    private final static String TAG = "MainActivity";

    private Fragment[] fragments;
    private FragmentCoversation fragmentCoversation;
    private FragmentFriends fragmentFriends;
    private FragmentFind fragmentFind;
    private FragmentProfile fragmentProfile;

    private ImageView iv_add;
    private ImageView iv_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        iv_add = (ImageView) findViewById(R.id.iv_add);
        iv_search = (ImageView) findViewById(R.id.iv_search);
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
