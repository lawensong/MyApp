package com.example.hi2.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/28.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener{
    private final static String TAG = "MainActivity";

    private TextView unreadLabel;
    public TextView unreadAddressLable;

    private ImageView[] imagebuttons;
    private TextView[] textviews;
    private int index;
    private int currentTabIndex;

    private Fragment[] fragments;
    private FragmentCoversation fragmentCoversation;
    private FragmentFriends fragmentFriends;
    private FragmentFind fragmentFind;
    private FragmentProfile fragmentProfile;

    private ImageView iv_add;
    private ImageView iv_search;

    private NewMessageBroadcastReceiver msgReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initView();

        iv_add = (ImageView) findViewById(R.id.iv_add);
        iv_search = (ImageView) findViewById(R.id.iv_search);
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        updateUnreadLabel();
        updateUnreadAddressLable();
    }

    private void initView(){
        unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
        unreadAddressLable = (TextView) findViewById(R.id.unread_address_number);

        fragmentCoversation = new FragmentCoversation();
        fragmentFriends = new FragmentFriends();
        fragmentFind = new FragmentFind();
        fragmentProfile = new FragmentProfile();
        fragments = new Fragment[]{fragmentCoversation, fragmentFriends, fragmentFind, fragmentProfile};

        imagebuttons = new ImageView[4];
        imagebuttons[0] = (ImageView) findViewById(R.id.ib_weixin);
        imagebuttons[1] = (ImageView) findViewById(R.id.ib_contact_list);
        imagebuttons[2] = (ImageView) findViewById(R.id.ib_find);
        imagebuttons[3] = (ImageView) findViewById(R.id.ib_profile);
        imagebuttons[0].setSelected(true);

        textviews = new TextView[4];
        textviews[0] = (TextView) findViewById(R.id.tv_weixin);
        textviews[1] = (TextView) findViewById(R.id.tv_contact_list);
        textviews[2] = (TextView) findViewById(R.id.tv_find);
        textviews[3] = (TextView) findViewById(R.id.tv_profile);
        textviews[0].setTextColor(0xFF45C01A);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragmentCoversation)
                .add(R.id.fragment_container, fragmentFriends)
                .add(R.id.fragment_container, fragmentFind)
                .add(R.id.fragment_container, fragmentProfile)
                .hide(fragmentFriends).hide(fragmentFind)
                .hide(fragmentProfile).show(fragmentCoversation).commit();

        // 注册一个接收消息的BroadcastReceiver
        msgReceiver = new NewMessageBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter("message");
        intentFilter.setPriority(3);
        registerReceiver(msgReceiver, intentFilter);
    }

    public void onTabClicked(View view){
        switch (view.getId()){
            case R.id.re_weixin:
                index = 0;
                break;
            case R.id.re_contact_list:
                index = 1;
                break;
            case R.id.re_find:
                index = 2;
                break;
            case R.id.re_profile:
                index = 3;
                break;
        }

        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager()
                    .beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }

        imagebuttons[currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        imagebuttons[index].setSelected(true);
        textviews[currentTabIndex].setTextColor(0xFF999999);
        textviews[index].setTextColor(0xFF45C01A);
        currentTabIndex = index;
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 获取未读消息数
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        //TODO
        return unreadMsgCountTotal;
    }

    /**
     * 刷新未读消息数
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        if (count > 0) {
            unreadLabel.setText(String.valueOf(count));
            unreadLabel.setVisibility(View.VISIBLE);
        } else {
            unreadLabel.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 获取未读申请与通知消息
     *
     * @return
     */
    public int getUnreadAddressCountTotal() {
        int unreadAddressCountTotal = 0;
        //TODO
        return unreadAddressCountTotal;
    }

    /**
     * 刷新申请与通知消息数
     */
    public void updateUnreadAddressLable() {
        runOnUiThread(new Runnable() {
            public void run() {
                int count = getUnreadAddressCountTotal();
                if (count > 0) {
                    unreadAddressLable.setText(String.valueOf(count));
                    unreadAddressLable.setVisibility(View.VISIBLE);
                } else {
                    unreadAddressLable.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    /**
     * 新消息广播接收者
     *
     *
     */
    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 主页面收到消息后，主要为了提示未读，实际消息内容需要到chat页面查看
            Log.d(TAG, "-->> Main Receiver");
            String from = intent.getStringExtra("from");
            String to = intent.getStringExtra("to");
            String content = intent.getStringExtra("text");
            Map<String, String> message = new HashMap<String, String>();
            message.put("from", from);
            message.put("to", to);
            message.put("text", content);

            // 2014-10-22 修复在某些机器上，在聊天页面对方发消息过来时不立即显示内容的bug
            if (ChatActivity.activityInstance != null) {
                if (from.equals(ChatActivity.activityInstance.toUser))
                    return;
            }

            // 注销广播接收者，否则在ChatActivity中会收到这个广播
            abortBroadcast();

            notifyNewMessage(message);

            // 刷新bottom bar消息未读数
            updateUnreadLabel();
            if (currentTabIndex == 0) {
                // 当前页面如果为聊天历史页面，刷新此页面
                if (fragmentCoversation != null) {
                    fragmentCoversation.refresh();
                }
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销广播
        try {
            unregisterReceiver(msgReceiver);
            msgReceiver = null;
        } catch (Exception e) {
        }
    }
}
