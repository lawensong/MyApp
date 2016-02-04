package com.example.hi2.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.hi2.Hi2Application;

/**
 * Created by Administrator on 2016/1/29.
 */
public class FragmentProfile extends Fragment {
    private final static String TAG = "FragmentProfile";
    private TextView tv_name;
    private TextView tv_fxid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RelativeLayout re_myinfo = (RelativeLayout) getView().findViewById(
                R.id.re_myinfo);
        tv_name = (TextView) re_myinfo.findViewById(R.id.tv_name);
        tv_fxid = (TextView) re_myinfo.findViewById(R.id.tv_fxid);
        tv_name.setText(Hi2Application.getInstance().getUsername());
        tv_fxid.setText(Hi2Application.getInstance().getUsername());

        RelativeLayout re_setting = (RelativeLayout) getView().findViewById(
                R.id.re_setting);
        re_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
