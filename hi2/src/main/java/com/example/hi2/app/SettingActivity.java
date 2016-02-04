package com.example.hi2.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.example.hi2.Hi2Application;
import com.example.hi2.utils.LocalUserInfo;

/**
 * Created by Administrator on 2016/2/4.
 */
public class SettingActivity extends Activity implements View.OnClickListener {
    private final static String TAG = "SettingActivity";

    /**
     * ��������Ϣ֪ͨ����
     */
    private RelativeLayout rl_switch_notification;
    /**
     * ������������
     */
    private RelativeLayout rl_switch_sound;
    /**
     * �����𶯲���
     */
    private RelativeLayout rl_switch_vibrate;
    /**
     * ��������������
     */
    private RelativeLayout rl_switch_speaker;

    /**
     * ������Ϣ֪ͨimageView
     */
    private ImageView iv_switch_open_notification;
    /**
     * �ر�����Ϣ֪ͨimageview
     */
    private ImageView iv_switch_close_notification;
    /**
     * ��������ʾimageview
     */
    private ImageView iv_switch_open_sound;
    /**
     * �ر�������ʾimageview
     */
    private ImageView iv_switch_close_sound;
    /**
     * ����Ϣ����ʾ
     */
    private ImageView iv_switch_open_vibrate;
    /**
     * �ر���Ϣ����ʾ
     */
    private ImageView iv_switch_close_vibrate;
    /**
     * ����������������
     */
    private ImageView iv_switch_open_speaker;
    /**
     * �ر���������������
     */
    private ImageView iv_switch_close_speaker;

    /**
     * �˳���ť
     */
    private Button logoutBtn;
    private LocalUserInfo localUserInfo;

    private String NOTIFICATION = "notification";
    private String NOTICE_BY_SOUND = "noticeBySound";
    private String NOTICE_BY_VIBRATE = "noticeByVibrate";
    private String USER_SPEAKER = "userSpeaker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        rl_switch_notification = (RelativeLayout) this.findViewById(R.id.rl_switch_notification);
        rl_switch_sound = (RelativeLayout) this.findViewById(R.id.rl_switch_sound);
        rl_switch_vibrate = (RelativeLayout) this.findViewById(R.id.rl_switch_vibrate);
        rl_switch_speaker = (RelativeLayout) this.findViewById(R.id.rl_switch_speaker);

        iv_switch_open_notification = (ImageView) this.findViewById(R.id.iv_switch_open_notification);
        iv_switch_close_notification = (ImageView) this.findViewById(R.id.iv_switch_close_notification);
        iv_switch_open_sound = (ImageView) this.findViewById(R.id.iv_switch_open_sound);
        iv_switch_close_sound = (ImageView) this.findViewById(R.id.iv_switch_close_sound);
        iv_switch_open_vibrate = (ImageView) this.findViewById(R.id.iv_switch_open_vibrate);
        iv_switch_close_vibrate = (ImageView) this.findViewById(R.id.iv_switch_close_vibrate);
        iv_switch_open_speaker = (ImageView) this.findViewById(R.id.iv_switch_open_speaker);
        iv_switch_close_speaker = (ImageView) this.findViewById(R.id.iv_switch_close_speaker);
        logoutBtn = (Button) this.findViewById(R.id.btn_logout);
        rl_switch_notification.setOnClickListener(this);
        rl_switch_sound.setOnClickListener(this);
        rl_switch_vibrate.setOnClickListener(this);
        rl_switch_speaker.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);

        localUserInfo = LocalUserInfo.getInstance(this);
        if (localUserInfo.getUserInfo(NOTIFICATION).equals("1")) {
            iv_switch_open_notification.setVisibility(View.VISIBLE);
            iv_switch_close_notification.setVisibility(View.INVISIBLE);
        } else {
            iv_switch_open_notification.setVisibility(View.INVISIBLE);
            iv_switch_close_notification.setVisibility(View.VISIBLE);
        }
        if (localUserInfo.getUserInfo(NOTICE_BY_SOUND).equals("1")) {
            iv_switch_open_sound.setVisibility(View.VISIBLE);
            iv_switch_close_sound.setVisibility(View.INVISIBLE);
        } else {
            iv_switch_open_sound.setVisibility(View.INVISIBLE);
            iv_switch_close_sound.setVisibility(View.VISIBLE);
        }
        if (localUserInfo.getUserInfo(NOTICE_BY_VIBRATE).equals("1")) {
            iv_switch_open_vibrate.setVisibility(View.VISIBLE);
            iv_switch_close_vibrate.setVisibility(View.INVISIBLE);
        } else {
            iv_switch_open_vibrate.setVisibility(View.INVISIBLE);
            iv_switch_close_vibrate.setVisibility(View.VISIBLE);
        }

        if (localUserInfo.getUserInfo(USER_SPEAKER).equals("1")) {
            iv_switch_open_speaker.setVisibility(View.VISIBLE);
            iv_switch_close_speaker.setVisibility(View.INVISIBLE);
        } else {
            iv_switch_open_speaker.setVisibility(View.INVISIBLE);
            iv_switch_close_speaker.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_switch_notification:
                if (iv_switch_open_notification.getVisibility() == View.VISIBLE) {
                    iv_switch_open_notification.setVisibility(View.INVISIBLE);
                    iv_switch_close_notification.setVisibility(View.VISIBLE);
                    rl_switch_sound.setVisibility(View.GONE);
                    rl_switch_vibrate.setVisibility(View.GONE);

                    localUserInfo.setUserInfo(NOTIFICATION, "0");
                } else {
                    iv_switch_open_notification.setVisibility(View.VISIBLE);
                    iv_switch_close_notification.setVisibility(View.INVISIBLE);
                    rl_switch_sound.setVisibility(View.VISIBLE);
                    rl_switch_vibrate.setVisibility(View.VISIBLE);

                    localUserInfo.setUserInfo(NOTIFICATION, "1");
                }
                break;
            case R.id.rl_switch_sound:
                if (iv_switch_open_sound.getVisibility() == View.VISIBLE) {
                    iv_switch_open_sound.setVisibility(View.INVISIBLE);
                    iv_switch_close_sound.setVisibility(View.VISIBLE);
                    localUserInfo.setUserInfo(NOTICE_BY_SOUND, "0");
                } else {
                    iv_switch_open_sound.setVisibility(View.VISIBLE);
                    iv_switch_close_sound.setVisibility(View.INVISIBLE);
                    localUserInfo.setUserInfo(NOTICE_BY_SOUND, "1");
                }
                break;
            case R.id.rl_switch_vibrate:
                if (iv_switch_open_vibrate.getVisibility() == View.VISIBLE) {
                    iv_switch_open_vibrate.setVisibility(View.INVISIBLE);
                    iv_switch_close_vibrate.setVisibility(View.VISIBLE);
                    localUserInfo.setUserInfo(NOTICE_BY_VIBRATE, "0");
                } else {
                    iv_switch_open_vibrate.setVisibility(View.VISIBLE);
                    iv_switch_close_vibrate.setVisibility(View.INVISIBLE);
                    localUserInfo.setUserInfo(NOTICE_BY_VIBRATE, "1");
                }
                break;
            case R.id.rl_switch_speaker:
                if (iv_switch_open_speaker.getVisibility() == View.VISIBLE) {
                    iv_switch_open_speaker.setVisibility(View.INVISIBLE);
                    iv_switch_close_speaker.setVisibility(View.VISIBLE);
                    localUserInfo.setUserInfo(USER_SPEAKER, "0");
                } else {
                    iv_switch_open_speaker.setVisibility(View.VISIBLE);
                    iv_switch_close_speaker.setVisibility(View.INVISIBLE);
                    localUserInfo.setUserInfo(USER_SPEAKER, "1");
                }
                break;
            case R.id.btn_logout: //�˳���½
                logout();
                break;

            default:
                break;
        }
    }

    private void logout(){
        Hi2Application.getInstance().setUsernamePassword("", "");
        startActivity(new Intent(SettingActivity.this, LoginActivity.class));
        finish();
    }

    public void back(View view){
        finish();
    }
}
