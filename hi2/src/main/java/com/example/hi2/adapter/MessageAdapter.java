package com.example.hi2.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.hi2.app.R;
import com.example.hi2.db.HiMessage;
import com.example.hi2.utils.SmileUtils;
import org.jivesoftware.smack.packet.Message;

import java.util.*;


/**
 * Created by Administrator on 2016/2/2.
 */
public class MessageAdapter extends BaseAdapter {
    private final static String TAG = "MessageAdapter";

    private static final int MESSAGE_TYPE_RECV_TXT = 0;
    private static final int MESSAGE_TYPE_SENT_TXT = 1;
    private static final int MESSAGE_TYPE_SENT_IMAGE = 2;
    private static final int MESSAGE_TYPE_SENT_LOCATION = 3;
    private static final int MESSAGE_TYPE_RECV_LOCATION = 4;
    private static final int MESSAGE_TYPE_RECV_IMAGE = 5;
    private static final int MESSAGE_TYPE_SENT_VOICE = 6;
    private static final int MESSAGE_TYPE_RECV_VOICE = 7;
    private static final int MESSAGE_TYPE_SENT_VIDEO = 8;
    private static final int MESSAGE_TYPE_RECV_VIDEO = 9;
    private static final int MESSAGE_TYPE_SENT_FILE = 10;
    private static final int MESSAGE_TYPE_RECV_FILE = 11;
    private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 12;
    private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 13;

    public static final String IMAGE_DIR = "chat/image/";
    public static final String VOICE_DIR = "chat/audio/";
    public static final String VIDEO_DIR = "chat/video";

    private String from;
    private String to;
    private LayoutInflater inflater;
    private Activity activity;
    private HiMessage hiMessage;

    private Context context;
    private List<Map<String, String>> conversation;

    private Map<String, Timer> timers = new Hashtable<String, Timer>();

    public MessageAdapter(Context context, String from, String to){
        this.from = from;
        this.to = to;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (Activity) context;
        hiMessage = new HiMessage(context);
        conversation = hiMessage.getMessagesList(from, to);
    }

    @Override
    public int getCount() {
        return conversation.size();
    }

    public void refresh(){
        notifyDataSetChanged();
    }

    @Override
    public Map<String, String> getItem(int position) {
        return conversation.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public int getViewTypeCount() {
        return 14;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Map<String, String> message = getItem(position);
        String from = message.get("from");
        String to = message.get("to");
        String type = message.get("type");
        String text = message.get("text");
        String time = message.get("time");

        convertView = LayoutInflater.from(context).inflate(
                R.layout.social_chat_admin_item, null);
        TextView timestamp = (TextView) convertView
                .findViewById(R.id.tv_time);
        TextView tv_content = (TextView) convertView
                .findViewById(R.id.tv_content);
        timestamp.setText(time);
        Spannable span = SmileUtils.getSmiledText(context,
                text);
        // 设置内容
        tv_content.setText(span, TextView.BufferType.SPANNABLE);
        return convertView;
    }

    public static class ViewHolder {
        ImageView iv;
        TextView tv;
        ProgressBar pb;
        ImageView staus_iv;
        ImageView head_iv;
        TextView tv_userId;
        ImageView playBtn;
        TextView timeLength;
        TextView size;
        LinearLayout container_status_btn;
        LinearLayout ll_container;
        ImageView iv_read_status;
        // 显示已读回执状态
        TextView tv_ack;
        // 显示送达回执状态
        TextView tv_delivered;

        TextView tv_file_name;
        TextView tv_file_size;
        TextView tv_file_download_state;
    }
}
