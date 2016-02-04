package com.example.hi2;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.example.hi2.utils.FileService;
import com.example.hi2.utils.LocalUserInfo;
import com.example.hi2.utils.SmackClient;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.security.PublicKey;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/4.
 */
public class Hi2Application extends Application {
    private final static String TAG = "Hi2Application";
    public static Context applicationContext;
    public static Hi2Application instance;
    public String username;
    public String password;
    private static String ADDRESS = "address";
    private static String PORT = "port";
    private static FileService fileService;
    private static SmackClient smackClient;
    private LocalUserInfo localUserInfo;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        instance = this;

        fileService = new FileService(this);
        try {
            Map<String, String> map = fileService.getUserInfo("private.txt");
            username = map.get("username");
            password = map.get("password");
        }catch (Exception e){

        }

        localUserInfo = LocalUserInfo.getInstance(this);
        if(localUserInfo.getUserInfo(ADDRESS).equals("") || localUserInfo.getUserInfo(PORT).equals("")){
            smackClient = new SmackClient(this, "192.168.16.175", "5200");
        }else {
            smackClient = new SmackClient(this, localUserInfo.getUserInfo(ADDRESS), localUserInfo.getUserInfo(PORT));
        }
    }

    public static Hi2Application getInstance(){
        return instance;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public SmackClient getSmackClient(){
        return smackClient;
    }

    public void setUsernamePassword(String username, String password){
        try {
            boolean result = fileService.saveToRom(username, password, "private.txt");
            if(result){
                Toast.makeText(getApplicationContext(), "save success", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(), "save fail", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Log.d(TAG, "save error: " + e);
            Toast.makeText(getApplicationContext(), "save error", Toast.LENGTH_SHORT).show();
        }
        this.username = username;
        this.password = password;
    }

    public XMPPTCPConnection getConnection(){
        return smackClient.getConnection();
    }

    public boolean login(){
        if(username!=null && password!=null){
            return smackClient.login(username, password);
        }else {
            return false;
        }
    }

    public void addPacketListener(){
        smackClient.addPacketListener();
    }
}
