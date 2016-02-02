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
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

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

    synchronized public static XMPPTCPConnection getInstace(){
        return connection;
    }

    public SmackClient(){}

    public SmackClient(Context context){
        this.context = context;
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
            getConnection().login(account, password);

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
                    getHiMessage().saveMessage(packet.getFrom().split("@")[0], packet.getTo().split("@")[0], message.getBody(), "receive");

                    Intent broadcastIntent = new Intent("message");
                    broadcastIntent.putExtra("from", packet.getFrom().split("@")[0]);
                    broadcastIntent.putExtra("to", packet.getTo().split("@")[0]);
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