package com.example.hi2.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import com.example.hi2.utils.CommonUtils;

import java.util.Map;

/**
 * Created by Administrator on 2016/1/29.
 */
public class BaseActivity extends FragmentActivity {
    private final static String TAG = "BaseActivity";
    private static final int notifiId = 11;
    protected NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /**
     * ��Ӧ����ǰ̨ʱ�������ǰ��Ϣ�������ڵ�ǰ�Ự����״̬����ʾһ�� �������Ҫ��ע�͵�����
     *
     * @param message
     */
    protected void notifyNewMessage(Map<String, String> message) {
        Log.d(TAG, "-->> notifyNewMessage ");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(getApplicationInfo().icon)
                .setWhen(System.currentTimeMillis()).setAutoCancel(true);

        String ticker = CommonUtils.getMessageDigest(message, this);

        // ����״̬����ʾ
        mBuilder.setTicker(message.get("from") + ": " + ticker);

        // ��������pendingintent��������2.3�Ļ����ϻ���bug
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, notifiId,
                intent, PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(pendingIntent);

        Notification notification = mBuilder.build();
        notificationManager.notify(notifiId, notification);
        notificationManager.cancel(notifiId);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void back(View view) {
        finish();
    }
}
