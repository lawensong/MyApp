package com.example.hi2.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.hi2.utils.LoadUserAvatar;

/**
 * Created by Administrator on 2016/2/1.
 */
public class UserInfoActivity extends Activity {
    private LoadUserAvatar avatarLoader;
    boolean is_friend = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        avatarLoader = new LoadUserAvatar(this, "/sdcard/hi2/");
        Button btn_sendmsg = (Button) this.findViewById(R.id.btn_sendmsg);
        ImageView iv_avatar = (ImageView) this.findViewById(R.id.iv_avatar);
        ImageView iv_sex = (ImageView) this.findViewById(R.id.iv_sex);
        TextView tv_name = (TextView) this.findViewById(R.id.tv_name);
        final String user = this.getIntent().getStringExtra("user");
        final String nick = this.getIntent().getStringExtra("nick");
        final String avatar = this.getIntent().getStringExtra("avatar");
        String sex = this.getIntent().getStringExtra("sex");
        if (nick != null && avatar != null && sex != null) {
            tv_name.setText(nick);
            if (sex.equals("1")) {
                iv_sex.setImageResource(R.drawable.ic_sex_male);
            } else if (sex.equals("2")) {
                iv_sex.setImageResource(R.drawable.ic_sex_female);
            } else {
                iv_sex.setVisibility(View.GONE);
            }
            is_friend = true;
            btn_sendmsg.setText("send message");

            showUserAvatar(iv_avatar, avatar);
        }

        btn_sendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_friend) {
                    Intent intent = new Intent();
                    intent.putExtra("user", user);
                    intent.putExtra("nick", nick);
                    intent.putExtra("avatar", avatar);

                    intent.setClass(UserInfoActivity.this, ChatActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("user", user);
                    intent.setClass(UserInfoActivity.this,
                            AddFriendsFinalActivity.class);
                    startActivity(intent);
                }
            }

        });
        refresh();
    }

    private void showUserAvatar(ImageView iamgeView, String avatar) {

    }

    public void back(View view) {
        finish();
    }

    private void refresh(){

    }
}
