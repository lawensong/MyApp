package com.example.hi2.db;

/**
 * Created by Administrator on 2016/2/2.
 */
public class TopUser {

    private long  time;
    private String userName;
    private int is_group;

    public void setTime(long  time){
        this.time=time;
    }
    public long getTime(){
        return time;
    }

    public void setUserName(String userName){
        this.userName=userName;
    }
    public String getUserName(){
        return userName;
    }
    public void setType(int  is_group){
        this.is_group=is_group;
    }
    public int getType(){
        return is_group;
    }

}
