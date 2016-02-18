package com.example.hi2.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.hi2.Hi2Application;
import com.example.hi2.app.ChatActivity;
import com.example.hi2.app.MainActivity;
import com.example.hi2.app.R;
import com.example.hi2.utils.SmackClient;
import org.jivesoftware.smackx.muc.RoomInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/17.
 */
public class ConversationAdapter extends BaseAdapter {
    private final static String TAG = "ConversationAdapter";
    private List<RoomInfo> normalList = new ArrayList<RoomInfo>();
    private List<RoomInfo> topList = new ArrayList<RoomInfo>();
    private Context context;
    private List<String> topMap;
    private LayoutInflater inflater;
    private SmackClient smackClient;

    public ConversationAdapter(Context context, List<RoomInfo> normalList, List<RoomInfo> topList, List<String> topMap){
        this.context = context;
        smackClient = Hi2Application.getInstance().getSmackClient();
//        this.topList = topList;
        this.topMap = topMap;
        this.normalList.addAll(loadConversationsWithRecentChat());
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return normalList.size()+topList.size();
    }

    @Override
    public RoomInfo getItem(int position) {
        if(position<topList.size()){
            return topList.get(position);
        }else {
            return normalList.get(position-topList.size());
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        boolean isGroup = true;

        final RoomInfo conversation = getItem(position);
        convertView = creatConvertView(1);
        // 初始化控件
        // 昵称
        holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        // 未读消息
        holder.tv_unread = (TextView) convertView.findViewById(R.id.tv_unread);
        // 最近一条消息
        holder.tv_content = (TextView) convertView
                .findViewById(R.id.tv_content);
        // 时间
        holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
        // 发送状态

        holder.msgState = (ImageView) convertView.findViewById(R.id.msg_state);
        if(!isGroup){

        }else {
            String name = conversation.getName();
            String desc = conversation.getDescription();
            holder.tv_name.setText(name);
            holder.tv_content.setText(desc);
            holder.iv_avatar1 = (ImageView) convertView
                    .findViewById(R.id.iv_avatar1);
        }

        if(false){
            // 显示与此用户的消息未读数
            holder.tv_unread.setText(String.valueOf(8));
            holder.tv_unread.setVisibility(View.VISIBLE);
        }else {
            holder.tv_unread.setVisibility(View.INVISIBLE);
        }

        final boolean isGroup_temp = isGroup;

        RelativeLayout re_parent = (RelativeLayout) convertView
                .findViewById(R.id.re_parent);
        re_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 进入聊天页面
                Intent intent = new Intent(context, ChatActivity.class);
                if (isGroup_temp) {
                    // it is group chat
                    intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                    intent.putExtra("roomName", conversation.getName());
                    intent.putExtra("room", conversation.getRoom());
                } else {
                    // it is single chat
                    intent.putExtra("user", "shnanyang");
                    intent.putExtra("nick", "shnanyang");
                    intent.putExtra("avatar", "shnanyang");
                }
                context.startActivity(intent);
            }
        });
        re_parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showMyDialog(conversation.getDescription(), conversation);
                return false;
            }
        });
        if(position<topList.size()){
            re_parent.setBackgroundColor(0xFFF5FFF1);
        }
        return convertView;
    }

    private static class ViewHolder {
        /** 和谁的聊天记录 */
        TextView tv_name;
        /** 消息未读数 */
        TextView tv_unread;
        /** 最后一条消息的内容 */
        TextView tv_content;
        /** 最后一条消息的时间 */
        TextView tv_time;
        /** 用户头像 */
        ImageView iv_avatar;
        ImageView iv_avatar1;
        ImageView iv_avatar2;
        ImageView iv_avatar3;
        ImageView iv_avatar4;
        ImageView iv_avatar5;
        ImageView msgState;

    }

    private View creatConvertView(int size) {
        View convertView;

        if (size == 0) {
            convertView = inflater.inflate(R.layout.item_conversation_single,
                    null, false);
        } else if (size == 1) {
            convertView = inflater.inflate(R.layout.item_conversation_group1,
                    null, false);
        } else if (size == 2) {
            convertView = inflater.inflate(R.layout.item_conversation_group2,
                    null, false);
        } else if (size == 3) {
            convertView = inflater.inflate(R.layout.item_conversation_group3,
                    null, false);
        } else if (size == 4) {
            convertView = inflater.inflate(R.layout.item_conversation_group4,
                    null, false);
        } else if (size > 4) {
            convertView = inflater.inflate(R.layout.item_conversation_group5,
                    null, false);
        } else {
            convertView = inflater.inflate(R.layout.item_conversation_group5,
                    null, false);
        }
        return convertView;
    }

    private void showMyDialog(String title, final RoomInfo conversation){
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.show();
        Window window = dlg.getWindow();

        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.alertdialog);

        window.findViewById(R.id.ll_title).setVisibility(View.VISIBLE);

        TextView tv_title = (TextView) window.findViewById(R.id.tv_title);
        tv_title.setText(title);

        TextView tv_content1 = (TextView) window.findViewById(R.id.tv_content1);

        if (topMap.contains(conversation.getName())) {
            tv_content1.setText("cancel top");
        } else {
            tv_content1.setText("to top");
        }

        tv_content1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
                dlg.cancel();
            }
        });

        TextView tv_content2 = (TextView) window.findViewById(R.id.tv_content2);
        tv_content2.setText("delete room");
        tv_content2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smackClient.destroyRoom(conversation.getRoom());

                refresh();
                dlg.cancel();
            }
        });
    }

    public void refresh(){
        normalList.clear();
        normalList.addAll(loadConversationsWithRecentChat());
        notifyDataSetChanged();
    }

    /**
     * 获取会议列表
     * @return
     */
    private List<RoomInfo> loadConversationsWithRecentChat(){
        List<RoomInfo> list = new ArrayList<RoomInfo>();
        List<RoomInfo> topList1 = new ArrayList<RoomInfo>();
        List<RoomInfo> list1 = smackClient.getHostRooms();
        for(RoomInfo roomInfo: list1){
            if(topMap.contains(roomInfo.getName())){
                topList1.add(roomInfo);
            }else {
                list.add(roomInfo);
            }
        }

        topList.clear();
        topList.addAll(topList1);
        return list;
    }
}
