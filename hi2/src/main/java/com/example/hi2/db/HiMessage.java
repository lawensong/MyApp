package com.example.hi2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.*;

/**
 * Created by Administrator on 2016/2/2.
 */
public class HiMessage {
    public static final String TABLE_NAME = "message";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_FROM = "m_from";
    public static final String COLUMN_NAME_TO = "m_to";
    public static final String COLUMN_NAME_TYPE = "m_type";
    public static final String COLUMN_NAME_TEXT = "m_text";
    public static final String COLUMN_NAME_TIME = "m_time";

    private DbOpenHelper dbHelper;

    public HiMessage(Context context){
        dbHelper = DbOpenHelper.getInstance(context);
    }

    public void saveMessage(String from, String to, String content, String type){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(db.isOpen()){
            Date date = new Date();
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_FROM, from);
            values.put(COLUMN_NAME_TO, to);
            values.put(COLUMN_NAME_TYPE, type);
            values.put(COLUMN_NAME_TEXT, content);
            values.put(COLUMN_NAME_TIME, date.getTime());
            db.insert(TABLE_NAME, null, values);
        }
    }

    public List<Map<String, String>> getMessagesList(String from, String to){
        List<Map<String, String>> messageList = new ArrayList<Map<String, String>>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(db.isOpen()){
            Cursor cursor = db.rawQuery("select * from "+TABLE_NAME+" where "+COLUMN_NAME_FROM+"="+from+" and "+COLUMN_NAME_TO+"="+to
                    +" order by "+COLUMN_NAME_TIME+" desc", null);
            while (cursor.moveToNext()){
                Map<String, String> message = new HashMap<String, String>();
                String m_from = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_FROM));
                String m_to = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TO));
                String m_type = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TYPE));
                String m_text = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TEXT));
                String m_time = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TIME));
                message.put("from", m_from);
                message.put("to", m_to);
                message.put("type", m_type);
                message.put("text", m_text);
                message.put("time", m_time);

                messageList.add(message);
            }
            cursor.close();
        }

        return messageList;
    }
}
