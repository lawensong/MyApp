package com.example.hi2.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.*;
import com.example.hi2.app.ChatActivity;
import com.example.hi2.app.FXAlertDialog;
import com.example.hi2.app.R;
import com.example.hi2.db.Constant;
import com.example.hi2.db.HiMessage;
import com.example.hi2.utils.DateUtils;
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

    private static final String TYPE_RECEIVE = "receive";
    private static final String TYPE_SEND = "send";

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
        Log.d(TAG, "adapter start to refresh");
        conversation = hiMessage.getMessagesList(from, to);
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
        Map<String, String> message = conversation.get(position);
        return message.get("type").equals(TYPE_RECEIVE) ? MESSAGE_TYPE_RECV_TXT
                : MESSAGE_TYPE_SENT_TXT;
    }

    public int getViewTypeCount() {
        return 14;
    }

    @SuppressLint("InflateParams")
    private View createViewByMessage(String type) {
            return type.equals(TYPE_RECEIVE) ? inflater
                    .inflate(R.layout.row_received_message, null) : inflater
                    .inflate(R.layout.row_sent_message, null);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final char messageType = '1';//"txt";
        Map<String, String> message = getItem(position);
        String from = message.get("from");
        String to = message.get("to");
        String type = message.get("type");
        String text = message.get("text");
        String time = message.get("time");

        String fromusernick = "0000";
        String fromuseravatar = "0000";

        if(from.contains("admin") || to.contains("admin")){
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.social_chat_admin_item, null);
            TextView timestamp = (TextView) convertView
                    .findViewById(R.id.tv_time);
            TextView tv_content = (TextView) convertView
                    .findViewById(R.id.tv_content);
            timestamp.setText(DateUtils.getTime(time));
            Spannable span = SmileUtils.getSmiledText(context,
                    text);
            // 设置内容
            tv_content.setText(span, TextView.BufferType.SPANNABLE);
            return convertView;
        }else {
            final ViewHolder holder;
            if(convertView == null){
                convertView = createViewByMessage(type);
                holder = new ViewHolder();
                try {
                    holder.pb = (ProgressBar) convertView
                            .findViewById(R.id.pb_sending);
                    holder.staus_iv = (ImageView) convertView
                            .findViewById(R.id.msg_status);
                    holder.head_iv = (ImageView) convertView
                            .findViewById(R.id.iv_userhead);
                    // 这里是文字内容
                    holder.tv = (TextView) convertView
                            .findViewById(R.id.tv_chatcontent);
                    holder.tv_userId = (TextView) convertView
                            .findViewById(R.id.tv_userid);
                }catch (Exception e){

                }

                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            if(type.equals(TYPE_SEND)){
                holder.tv_ack = (TextView) convertView
                        .findViewById(R.id.tv_ack);
                holder.tv_delivered = (TextView) convertView
                        .findViewById(R.id.tv_delivered);
                if (holder.tv_ack != null) {
                    if (holder.tv_delivered != null) {
                        holder.tv_delivered.setVisibility(View.INVISIBLE);
                    }
                    holder.tv_ack.setVisibility(View.VISIBLE);
                }
            }

            switch (messageType){
                case '1':
                    handleTextMessage(message, holder, position);
                    break;
                default:
                    break;
            }

            if(type.equals(TYPE_SEND)){
                View statusView = convertView.findViewById(R.id.msg_status);
                statusView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // 显示重发消息的自定义alertdialog
                        Intent intent = new Intent(activity,
                                FXAlertDialog.class);
                        intent.putExtra("msg",
                                activity.getString(R.string.confirm_resend));
                        intent.putExtra("title",
                                activity.getString(R.string.resend));
                        intent.putExtra("cancel", true);
                        intent.putExtra("position", position);
                        if (messageType == '1')
                            activity.startActivityForResult(intent,
                                    ChatActivity.REQUEST_CODE_TEXT);
                    }
                });
            }else {
                // 长按头像，移入黑名单
                holder.head_iv
                    .setOnLongClickListener(new View.OnLongClickListener() {

                        @Override
                        public boolean onLongClick(View v) {
                            Intent intent = new Intent(activity,
                                    FXAlertDialog.class);
                            intent.putExtra("msg", "移入到黑名单？");
                            intent.putExtra("cancel", true);
                            intent.putExtra("position", position);
                            activity.startActivityForResult(
                                    intent,
                                    ChatActivity.REQUEST_CODE_ADD_TO_BLACKLIST);
                            return true;
                        }
                    });
            }

            TextView timestamp = (TextView) convertView
                    .findViewById(R.id.timestamp);
            if(position==0){
                timestamp.setText(DateUtils.getTime(time));
                timestamp.setVisibility(View.VISIBLE);
            }else {
                if(DateUtils.isCloseEnough(Long.parseLong(time), Long.parseLong(conversation.get(position-1).get("time")))){
                    timestamp.setVisibility(View.GONE);
                }else {
                    timestamp.setText(DateUtils.getTime(time));
                    timestamp.setVisibility(View.VISIBLE);
                }
            }

            if(type.equals(TYPE_RECEIVE)){
                // 对方的头像值： fromuseravatar
                final String avater = Constant.URL_Avatar + fromuseravatar;
                holder.head_iv.setTag(avater);
                if(avater!=null && !avater.equals("")){

                }
            }else {
                // 设置自己本地的头像

                final String avater = Constant.URL_Avatar + fromuseravatar;
                holder.head_iv.setTag(avater);
                if(avater!=null && !avater.equals("")){

                }
            }
        }
        return convertView;
    }

    private void handleTextMessage(final Map<String, String> message, final ViewHolder holder, final int position){
        Spannable span = SmileUtils
                .getSmiledText(context, message.get("text"));
        // 设置内容
        holder.tv.setText(span, TextView.BufferType.SPANNABLE);
        // 设置长按事件监听
        holder.tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                activity.startActivityForResult((new Intent(activity,
                                ContextMenu.class)).putExtra("position", position)
                                .putExtra("type", message.get("type")),
                        ChatActivity.REQUEST_CODE_CONTEXT_MENU);
                return true;
            }
        });
        if(message.get("type").equals(TYPE_SEND)){
            holder.pb.setVisibility(View.GONE);
            holder.staus_iv.setVisibility(View.GONE);
        }
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
