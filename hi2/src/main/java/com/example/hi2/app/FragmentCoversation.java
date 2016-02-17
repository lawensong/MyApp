package com.example.hi2.app;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.hi2.Hi2Application;
import com.example.hi2.adapter.ConversationAdapter;
import com.example.hi2.utils.SmackClient;
import org.jivesoftware.smackx.muc.RoomInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/29.
 */
public class FragmentCoversation extends Fragment {
    private final static String TAG = "FragmentCoversation";
    private List<String> topMap = new ArrayList<String>();
    private ListView listView;
    private ConversationAdapter adapter;
    private List<RoomInfo> normalList = new ArrayList<RoomInfo>();
    private List<RoomInfo> topList = new ArrayList<RoomInfo>();
    private SmackClient smackClient;

    public RelativeLayout errorItem;
    public TextView errorText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null){
            return;
        }

        errorItem = (RelativeLayout) getView().findViewById(R.id.rl_error_item);
        errorText = (TextView) errorItem.findViewById(R.id.tv_connect_errormsg);
        smackClient = Hi2Application.getInstance().getSmackClient();

        normalList.addAll(loadConversationsWithRecentChat());
        listView = (ListView) getView().findViewById(R.id.list);
        adapter = new ConversationAdapter(getActivity(), normalList, topList, topMap);
        listView.setAdapter(adapter);
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

    /**
     * 刷新页面
     */
    public void refresh() {

    }
}
