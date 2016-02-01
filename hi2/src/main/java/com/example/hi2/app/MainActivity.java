package com.example.hi2.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/1/28.
 */
public class MainActivity extends BaseActivity{
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
}
