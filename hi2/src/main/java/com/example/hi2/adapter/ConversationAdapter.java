package com.example.hi2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.hi2.app.R;
import org.jivesoftware.smackx.muc.RoomInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/17.
 */
public class ConversationAdapter extends BaseAdapter {
    private List<RoomInfo> normalList;
    private List<RoomInfo> topList;
    private Context context;
    private List<String> topMap;
    private LayoutInflater inflater;

    public ConversationAdapter(Context context, List<RoomInfo> normalList, List<RoomInfo> topList, List<String> topMap){
        this.context = context;
        this.normalList = normalList;
        this.topList = topList;
        this.topMap = topMap;
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
        // ��ʼ���ؼ�
        // �ǳ�
        holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        // δ����Ϣ
        holder.tv_unread = (TextView) convertView.findViewById(R.id.tv_unread);
        // ���һ����Ϣ
        holder.tv_content = (TextView) convertView
                .findViewById(R.id.tv_content);
        // ʱ��
        holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
        // ����״̬

        holder.msgState = (ImageView) convertView.findViewById(R.id.msg_state);
        if(!isGroup){

        }else {
            String desc = conversation.getDescription();
            holder.tv_name.setText(desc);
            holder.iv_avatar1 = (ImageView) convertView
                    .findViewById(R.id.iv_avatar1);
        }

        RelativeLayout re_parent = (RelativeLayout) convertView
                .findViewById(R.id.re_parent);
        re_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        re_parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
        if(position<topList.size()){
            re_parent.setBackgroundColor(0xFFF5FFF1);
        }
        return convertView;
    }

    private static class ViewHolder {
        /** ��˭�������¼ */
        TextView tv_name;
        /** ��Ϣδ���� */
        TextView tv_unread;
        /** ���һ����Ϣ������ */
        TextView tv_content;
        /** ���һ����Ϣ��ʱ�� */
        TextView tv_time;
        /** �û�ͷ�� */
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
}
