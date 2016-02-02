package com.example.hi2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.example.hi2.domain.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/2.
 */
public class UserDao {
    public static final String TABLE_NAME = "uers";
    public static final String COLUMN_NAME_ID = "username";
    public static final String COLUMN_NAME_NICK = "nick";
    public static final String COLUMN_NAME_AVATAR = "avatar";

    public static final String PREF_TABLE_NAME = "pref";
    public static final String COLUMN_NAME_DISABLED_GROUPS = "disabled_groups";
    public static final String COLUMN_NAME_DISABLED_IDS = "disabled_ids";

    private DbOpenHelper dbHelper;

    public UserDao(Context context) {
        dbHelper = DbOpenHelper.getInstance(context);
    }

    /**
     * 保存好友list
     *
     * @param contactList
     */
    public void saveContactList(List<User> contactList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(TABLE_NAME, null, null);
            for (User user : contactList) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_NAME_ID, user.getUsername());
                if(user.getNick() != null)
                    values.put(COLUMN_NAME_NICK, user.getNick());
                if(user.getAvatar() != null)
                    values.put(COLUMN_NAME_AVATAR, user.getAvatar());
                db.replace(TABLE_NAME, null, values);
            }
        }
    }

    /**
     * 获取好友list
     *
     * @return
     */
    public Map<String, User> getContactList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Map<String, User> users = new HashMap<String, User>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME /* + " desc" */, null);
            while (cursor.moveToNext()) {
                String username = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID));
                String nick = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NICK));
                String avatar = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_AVATAR));

                User user = new User();
                user.setUsername(username);
                user.setNick(nick);
                user.setAvatar(avatar);
                String headerName = null;
                if (!TextUtils.isEmpty(user.getNick())) {
                    headerName = user.getNick();
                } else {
                    headerName = user.getUsername();
                }

                if (username.equals(Constant.NEW_FRIENDS_USERNAME) || username.equals(Constant.GROUP_USERNAME)
                        || username.equals(Constant.CHAT_ROOM)) {
                    user.setHeader("");
                } else {
                    user.setHeader("#");
                }
                users.put(username, user);
            }
            cursor.close();
        }
        return users;
    }

    /**
     * 删除一个联系人
     * @param username
     */
    public void deleteContact(String username){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(db.isOpen()){
            db.delete(TABLE_NAME, COLUMN_NAME_ID + " = ?", new String[]{username});
        }
    }

    /**
     * 保存一个联系人
     * @param user
     */
    public void saveContact(User user){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_ID, user.getUsername());
        if(user.getNick() != null)
            values.put(COLUMN_NAME_NICK, user.getNick());
        if(user.getAvatar() != null)
            values.put(COLUMN_NAME_AVATAR, user.getAvatar());
        if(db.isOpen()){
            db.replace(TABLE_NAME, null, values);
        }
    }

    public void setDisabledGroups(List<String> groups){
        setList(COLUMN_NAME_DISABLED_GROUPS, groups);
    }

    public List<String>  getDisabledGroups(){
        return getList(COLUMN_NAME_DISABLED_GROUPS);
    }

    public void setDisabledIds(List<String> ids){
        setList(COLUMN_NAME_DISABLED_IDS, ids);
    }

    public List<String> getDisabledIds(){
        return getList(COLUMN_NAME_DISABLED_IDS);
    }

    private void setList(String column, List<String> strList){
        StringBuilder strBuilder = new StringBuilder();

        for(String hxid:strList){
            strBuilder.append(hxid).append("$");
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(column, strBuilder.toString());

            db.update(PREF_TABLE_NAME, values, null,null);
        }
    }

    private List<String> getList(String column){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + column + " from " + PREF_TABLE_NAME,null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }

        String strVal = cursor.getString(0);
        if (strVal == null || strVal.equals("")) {
            return null;
        }

        cursor.close();

        String[] array = strVal.split("$");

        if(array != null && array.length > 0){
            List<String> list = new ArrayList<String>();
            for(String str:array){
                list.add(str);
            }

            return list;
        }

        return null;
    }
}
