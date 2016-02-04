package com.example.hi2.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.example.hi2.app.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/2.
 */
public class CommonUtils {
    private static final String TAG = "CommonUtils";
    /**
     * ��������Ƿ����
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }

        return false;
    }

    /**
     * ���Sdcard�Ƿ����
     *
     * @return
     */
    public static boolean isExitsSdcard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }


    /**
     * ������Ϣ���ݺ���Ϣ���ͻ�ȡ��Ϣ������ʾ
     *
     * @param message
     * @param context
     * @return
     */
    public static String getMessageDigest(Map<String, String> message, Context context) {
        String digest = message.get("text");
        return digest;
    }

    static String getString(Context context, int resId){
        return context.getResources().getString(resId);
    }


    public static String getTopActivity(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

        if (runningTaskInfos != null)
            return runningTaskInfos.get(0).topActivity.getClassName();
        else
            return "";
    }

}
