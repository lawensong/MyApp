package com.example.hi2.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2016/2/18.
 */
public class AddPopWindow extends PopupWindow {
    private View conentView;

    public AddPopWindow(final Activity context){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.popupwindow_add, null);

        // ����SelectPicPopupWindow��View
        this.setContentView(conentView);
        // ����SelectPicPopupWindow��������Ŀ�
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        // ����SelectPicPopupWindow��������ĸ�
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // ����SelectPicPopupWindow��������ɵ��
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // ˢ��״̬
        this.update();
        // ʵ����һ��ColorDrawable��ɫΪ��͸��
        ColorDrawable dw = new ColorDrawable(0000000000);
        // ��back���������ط�ʹ����ʧ,������������ܴ���OnDismisslistener �����������ؼ��仯�Ȳ���
        this.setBackgroundDrawable(dw);

        // ����SelectPicPopupWindow�������嶯��Ч��
        this.setAnimationStyle(R.style.AnimationPreview);

        RelativeLayout re_addfriends =(RelativeLayout) conentView.findViewById(R.id.re_addfriends);
        RelativeLayout re_chatroom =(RelativeLayout) conentView.findViewById(R.id.re_chatroom);
        RelativeLayout create_chatroom =(RelativeLayout) conentView.findViewById(R.id.create_chatroom);

        re_addfriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        re_chatroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        create_chatroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, CreateRoomActivity.class));
                AddPopWindow.this.dismiss();
            }
        });
    }

    /**
     * ��ʾpopupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // ��������ʽ��ʾpopupwindow
            this.showAsDropDown(parent, 0, 0);
        } else {
            this.dismiss();
        }
    }
}
