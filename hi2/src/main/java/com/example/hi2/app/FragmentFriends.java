package com.example.hi2.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.hi2.adapter.ContactAdapter;
import com.example.hi2.domain.User;
import com.example.hi2.utils.SmackClient;
import com.example.hi2.widget.Sidebar;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.*;

/**
 * Created by Administrator on 2016/1/29.
 */
public class FragmentFriends extends Fragment {
    private final static String TAG = "FragmentFriends";
    private ContactAdapter adapter;
    private List<RosterEntry> contactList;
    private ListView listView;
    private boolean hidden;
    private Sidebar sidebar;

    private List<String> blackList;
    private TextView tv_unread;
    private TextView tv_total;
    private LayoutInflater infalter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contactlist, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (ListView) getView().findViewById(R.id.list);
        contactList = new ArrayList<RosterEntry>();
        getContactList();

        infalter=LayoutInflater.from(getActivity());
        View headView = infalter.inflate(R.layout.item_contact_list_header,
                null);
        listView.addHeaderView(headView);
        View footerView = infalter.inflate(R.layout.item_contact_list_footer,
                null);
        listView.addFooterView(footerView);
        sidebar = (Sidebar) getView().findViewById(R.id.sidebar);
        sidebar.setListView(listView);
        tv_unread = (TextView) headView.findViewById(R.id.tv_unread);

        if(((MainActivity)getActivity()).unreadAddressLable.getVisibility()==View.VISIBLE){
            tv_unread.setVisibility(View.VISIBLE);
            tv_unread.setText(((MainActivity)getActivity()).unreadAddressLable.getText());

        }else{
            tv_unread.setVisibility(View.GONE);
        }

        tv_total = (TextView) footerView.findViewById(R.id.tv_total);
        tv_total.setText(String.valueOf(contactList.size())+"位联系人");
        // 设置adapter
        adapter = new ContactAdapter(getActivity(), R.layout.item_contact_list,
                contactList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0&&position!=contactList.size()+1){
                    RosterEntry user=contactList.get(position-1);
                    String username = user.getUser();
                    startActivity(new Intent(getActivity(), UserInfoActivity.class).putExtra("user", user.getUser() )
                            .putExtra("nick", user.getUser() ).putExtra("avatar", user.getUser() ).putExtra("sex", user.getUser()));
                }
            }
        });

        RelativeLayout re_newfriends=(RelativeLayout) headView.findViewById(R.id.re_newfriends);
        re_newfriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if(!hidden){
            refresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!hidden){
            refresh();
        }
    }

    public void refresh(){
        try {
            // 可能会在子线程中调到这方法
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    getContactList();
                    adapter.notifyDataSetChanged();
                    tv_total.setText(String.valueOf(contactList.size())+" friends");
                    if(((MainActivity)getActivity()).unreadAddressLable.getVisibility()==View.VISIBLE){
                        tv_unread.setVisibility(View.VISIBLE);
                        tv_unread.setText(((MainActivity)getActivity()).unreadAddressLable.getText());

                    }else{
                        tv_unread.setVisibility(View.GONE);
                    }
                }
            });
        }catch (Exception e){
            Log.e(TAG, "friends get error: "+e);
        }
    }

    private void getContactList(){
        contactList.clear();

        // 获取本地好友列表
        SmackClient smackClient = new SmackClient();
        List<RosterEntry> rosterEntries = smackClient.getAllEntries();
        Iterator<RosterEntry> i = rosterEntries.iterator();
        while (i.hasNext()){
            contactList.add(i.next());
        }

        // 对list进行排序
        Collections.sort(contactList, new PinyinComparator() {
        });
    }

    @SuppressLint("DefaultLocale")
    public class PinyinComparator implements Comparator<RosterEntry> {

        @SuppressLint("DefaultLocale")
        @Override
        public int compare(RosterEntry o1, RosterEntry o2) {
            // TODO Auto-generated method stub
            String py1 = o1.getUser();
            String py2 = o2.getUser();
            if(py1 == null){

            }
            // 判断是否为空""
            if (isEmpty(py1) && isEmpty(py2))
                return 0;
            if (isEmpty(py1))
                return -1;
            if (isEmpty(py2))
                return 1;
            String str1 = "";
            String str2 = "";
            try {
                str1 = ((o1.getUser()).toUpperCase()).substring(0, 1);
                str2 = ((o2.getUser()).toUpperCase()).substring(0, 1);
            } catch (Exception e) {
                System.out.println("某个str为\" \" 空");
            }
            return str1.compareTo(str2);
        }
        private boolean isEmpty(String str) {
            return "".equals(str.trim());
        }
    }
}
