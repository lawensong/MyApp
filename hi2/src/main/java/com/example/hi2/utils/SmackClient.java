package com.example.hi2.utils;

/**
 * Created by Administrator on 2016/1/29.
 */
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.example.hi2.db.HiMessage;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.android.AndroidSmackInitializer;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.muc.*;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/1/28.
 */
public class SmackClient {
    private final static String TAG = "SmackClient";
    private String SERVER_HOST = "192.168.16.175";
    private int PORT = 5222;
    private String SERVER_NAME = "localhost";
    private static XMPPTCPConnection connection = null;
    private static VCard vCard;
    private static Context context;
    private static HiMessage hiMessage;
    private TConnectionListener tConnectionListener;
    private static final String TYPE_RECEIVE = "receive";
    private static final String TYPE_SEND = "send";

    synchronized public static XMPPTCPConnection getInstace(){
        return connection;
    }

    public SmackClient(){}

    public SmackClient(Context context, String address, String port){
        this.context = context;
        this.SERVER_HOST = address;
        this.PORT = Integer.parseInt(port);
    }

    public XMPPTCPConnection getConnection(){
        SmackConfiguration.DEBUG = true;
        if(connection == null){
            openConnection();
        }
        return connection;
    }

    /**
     * 初始化连接
     * @return
     */
    public void openConnection(){
        try {
            if(null == connection || !connection.isAuthenticated()){
                new AndroidSmackInitializer().initialize();

                XMPPTCPConnectionConfiguration conf = XMPPTCPConnectionConfiguration.builder()
                        .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                        .setHost(SERVER_HOST).setPort(PORT).setServiceName(SERVER_NAME).build();
                connection = new XMPPTCPConnection(conf);
                connection.connect();
                Log.d(TAG, "connection finish!");
            }
        }catch (Exception e){
//            e.printStackTrace();
            connection = null;
            Log.d(TAG, "--->>>openConnection error"+e);
        }
    }

    /**
     * 关闭连接
     * @return
     */
    public void closeConnection(){
        if(connection!=null){
            if(connection.isConnected()){
                connection.disconnect();
            }

            connection = null;
        }
    }

    /**
     * 登陆
     * @return
     */
    public boolean login(String account, String password){
        try {
            if(connection==null){
                return false;
            }
            try {
                getConnection().login(account, password);
            }catch (Exception e){
                e.printStackTrace();
            }

            Presence presence = new Presence(Presence.Type.available);
            getConnection().sendStanza(presence);

            tConnectionListener = new TConnectionListener();
            getConnection().addConnectionListener(tConnectionListener);

            vCard = new VCard();
            vCard.load(connection);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更改用户状态
     * @return
     */
    public void setPresence(int code){
        XMPPTCPConnection conn = getConnection();
        if(conn == null){
            return;
        }
        Presence presence;
        try{
            switch (code){
                case 0:
                    presence = new Presence(Presence.Type.available);
                    conn.sendStanza(presence);
                    System.out.println("online");
                    System.out.println(presence.toXML());
                    break;
                case 1:
                    presence = new Presence(Presence.Type.available);
                    presence.setMode(Presence.Mode.chat);
                    conn.sendStanza(presence);
                    System.out.println("chat");
                    System.out.println(presence.toXML());
                    break;
                case 2:
                    presence = new Presence(Presence.Type.available);
                    presence.setMode(Presence.Mode.away);
                    conn.sendStanza(presence);
                    System.out.println("away");
                    System.out.println(presence.toXML());
                    break;
                case 3:
                    presence = new Presence(Presence.Type.available);
                    presence.setMode(Presence.Mode.dnd);
                    conn.sendStanza(presence);
                    System.out.println("busy");
                    System.out.println(presence.toXML());
                    break;
                case 4:
                    Roster roster = Roster.getInstanceFor(conn);
                    Collection<RosterEntry> entries = roster.getEntries();
                    for(RosterEntry entry: entries){
                        presence = new Presence(Presence.Type.available);
                        presence.setFrom(conn.getUser());
                        presence.setTo(entry.getUser());
                        conn.sendStanza(presence);
                        System.out.println(presence.toXML());
                    }
                    break;
                case 5:
                    presence = new Presence(Presence.Type.unavailable);
                    conn.sendStanza(presence);
                    System.out.println("unavailable");
                    break;
                default:
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<RosterEntry> getAllEntries(){
        List<RosterEntry> rosterEntries = new ArrayList<RosterEntry>();

        Roster roster = Roster.getInstanceFor(getConnection());
        Collection<RosterEntry> entries = roster.getEntries();
        Iterator<RosterEntry> i = entries.iterator();
        while (i.hasNext()){
            rosterEntries.add(i.next());
        }

        return rosterEntries;
    }

    public boolean sendMessage(String msg){
        try {
            Chat chat = ChatManager.getInstanceFor(getConnection()).createChat("sh@localhost/PC-20150119JKD1", null);
            chat.sendMessage(msg);
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    private HiMessage getHiMessage(){
        if(hiMessage==null){
            hiMessage = new HiMessage(context);
        }
        return hiMessage;
    }

    /**
     * 初始化会议室列表
     * @return
     */
    public List<RoomInfo> getHostRooms(){
        List<RoomInfo> roominfos = new ArrayList<RoomInfo>();
        Collection<HostedRoom> hostedRooms = null;
        try {
            ServiceDiscoveryManager.getInstanceFor(getConnection());
            MultiUserChatManager multiUserChatManager = MultiUserChatManager.getInstanceFor(getConnection());
//            hostedRooms = MultiUserChatManager.getInstanceFor(getConnection()).getHostedRooms(getConnection().getServiceName());
            hostedRooms = multiUserChatManager.getHostedRooms("conference.localhost");
            for(HostedRoom entry: hostedRooms){
//                roominfos.add(entry);
//                System.out.println("get rooms "+entry.getJid());
                roominfos.add(getRoomInfo(entry.getJid()));
//                destroyRoom(entry.getJid());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return roominfos;
    }

    /**
     * 得到房间信息
     * @param room
     * @return
     */
    public RoomInfo getRoomInfo(String room){
        MultiUserChatManager multiUserChatManager = MultiUserChatManager.getInstanceFor(getConnection());
        RoomInfo roomInfo=null;
        try {
            roomInfo = multiUserChatManager.getRoomInfo(room);
        }catch (Exception e){
            e.printStackTrace();
        }

        return roomInfo;
    }

    /**
     * 删除房间
     */
    public void destroyRoom(String room){
        MultiUserChat muc = null;
        try {
            muc = MultiUserChatManager.getInstanceFor(getConnection())
                    .getMultiUserChat(room);
            muc.destroy("destroy", "shnanyang@localhost");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 创建房间
     * @param user
     * @param roomName
     * @param password
     * @return
     */
    public MultiUserChat createRoom(String user, String roomName, String password, String description){
        MultiUserChat muc = null;
        try {
            muc = MultiUserChatManager.getInstanceFor(getConnection())
                    .getMultiUserChat(roomName + "@conference." + getConnection().getServiceName());
            muc.create(roomName);

            Form form = muc.getConfigurationForm();
            Form submitForm = form.createAnswerForm();
            List<FormField> formFields = form.getFields();
            Iterator<FormField> fields = formFields.iterator();
            while (fields.hasNext()){
                FormField field = (FormField)fields.next();
                if(!FormField.FORM_TYPE.equals(field.getType()) && field.getVariable()!=null){
                    submitForm.setDefaultAnswer(field.getVariable());
                }
            }

//            List<String> owners = new ArrayList<String>();
//            owners.add(getConnection().getUser());
//            submitForm.setAnswer("muc#roomconfig_roomowners", owners);
            // 设置聊天室是持久聊天室，即将要被保存下来
            submitForm.setAnswer("muc#roomconfig_persistentroom", true);
            // 房间仅对成员开放
            submitForm.setAnswer("muc#roomconfig_membersonly", false);
            // 允许占有者邀请其他人
            submitForm.setAnswer("muc#roomconfig_allowinvites", true);
            if (!password.equals("")) {
                // 进入是否需要密码
                submitForm.setAnswer("muc#roomconfig_passwordprotectedroom",
                        true);
                // 设置进入密码
                submitForm.setAnswer("muc#roomconfig_roomsecret", password);
            }
            //muc#roomconfig_changesubject
            submitForm.setAnswer("muc#roomconfig_roomdesc", description);
//             能够发现占有者真实 JID 的角色
//             submitForm.setAnswer("muc#roomconfig_whois", "anyone");
//             登录房间对话
//            submitForm.setAnswer("muc#roomconfig_enablelogging", true);
            // 仅允许注册的昵称登录
//            submitForm.setAnswer("x-muc#roomconfig_reservednick", true);
            // 允许使用者修改昵称
//            submitForm.setAnswer("x-muc#roomconfig_canchangenick", false);
            // 允许用户注册房间
//            submitForm.setAnswer("x-muc#roomconfig_registration", false);
            muc.sendConfigurationForm(submitForm);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return muc;
    }

    /**
     * 加入会议室
     * @param user
     * @param roomsName
     * @param password
     * @return
     */
    public MultiUserChat joinMultiUserChat(String user, String roomsName, String password){
        try {
            MultiUserChat muc = MultiUserChatManager.getInstanceFor(getConnection())
                    .getMultiUserChat(roomsName + "@conference." + getConnection().getServiceName());
            DiscussionHistory history = new DiscussionHistory();
            history.setMaxChars(0);
            muc.join(user, password, history, SmackConfiguration.getDefaultPacketReplyTimeout());
            return muc;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询会议室成员名字
     * @param muc
     * @return
     */
    public List<String> findMultiUser(MultiUserChat muc){
        List<String> listUser = muc.getOccupants();
        return listUser;
    }

    /**
     * 添加监听消息
     * @return
     */
    public boolean addPacketListener(){
        StanzaListener stanzaListener = new StanzaListener() {
            @Override
            public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                if(packet.getClass() == Message.class){
                    Message message = (Message)packet;
                    System.out.println("receive Message--->>>From:" + packet.getFrom().split("@")[0] + " message:" + message.getBody());
                    getHiMessage().saveMessage(packet.getFrom().split("@")[0], packet.getTo().split("@")[0], message.getBody(), TYPE_RECEIVE);

                    Intent broadcastIntent = new Intent("message");
                    broadcastIntent.putExtra("from", packet.getFrom().split("@")[0]);
                    broadcastIntent.putExtra("to", packet.getTo().split("@")[0]);
                    broadcastIntent.putExtra("text", message.getBody());
                    broadcastIntent.putExtra("type", "receive");
                    context.sendOrderedBroadcast(broadcastIntent, null);
                }else if(packet.getClass() == Presence.class){
                    Presence presence = (Presence)packet;
                    System.out.println("receive Presence--->>>From: "+presence.getType());
                } else{
                    System.out.println("receive packet:unknown "+packet);
                }
            }
        };
        getConnection().addAsyncStanzaListener(stanzaListener, null);
        return true;
    }
}