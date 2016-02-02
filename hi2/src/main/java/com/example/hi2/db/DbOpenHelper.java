package com.example.hi2.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/2/2.
 */
public class DbOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;
    private static DbOpenHelper instance;

    private static final String TOPUSER_TABLE_CREATE = "CREATE TABLE "
            + TopUserDao.TABLE_NAME + " ("
            + TopUserDao.COLUMN_NAME_TIME +" TEXT, "
            + TopUserDao.COLUMN_NAME_IS_GOUP +" TEXT, "
            + TopUserDao.COLUMN_NAME_ID + " TEXT PRIMARY KEY);";

    private static final String USERNAME_TABLE_CREATE = "CREATE TABLE "
            + UserDao.TABLE_NAME + " ("
            + UserDao.COLUMN_NAME_NICK + " TEXT, "
            + UserDao.COLUMN_NAME_AVATAR + " TEXT, "
            + UserDao.COLUMN_NAME_ID + " TEXT PRIMARY KEY);";

    private static final String INIVTE_MESSAGE_TABLE_CREATE = "CREATE TABLE "
            + InviteMessgeDao.TABLE_NAME + " ("
            + InviteMessgeDao.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + InviteMessgeDao.COLUMN_NAME_FROM + " TEXT, "
            + InviteMessgeDao.COLUMN_NAME_GROUP_ID + " TEXT, "
            + InviteMessgeDao.COLUMN_NAME_GROUP_Name + " TEXT, "
            + InviteMessgeDao.COLUMN_NAME_REASON + " TEXT, "
            + InviteMessgeDao.COLUMN_NAME_STATUS + " INTEGER, "
            + InviteMessgeDao.COLUMN_NAME_ISINVITEFROMME + " INTEGER, "
            + InviteMessgeDao.COLUMN_NAME_TIME + " TEXT); ";


    private static final String CREATE_PREF_TABLE = "CREATE TABLE "
            + UserDao.PREF_TABLE_NAME + " ("
            + UserDao.COLUMN_NAME_DISABLED_GROUPS + " TEXT, "
            + UserDao.COLUMN_NAME_DISABLED_IDS + " TEXT);";

    private static final String CREATE_MESSAGE_TABLE = "CREATE TABLE "
            + HiMessage.TABLE_NAME + " ("
            + HiMessage.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + HiMessage.COLUMN_NAME_FROM + " TEXT, "
            + HiMessage.COLUMN_NAME_TO + " TEXT, "
            + HiMessage.COLUMN_NAME_TYPE + " TEXT, "
            + HiMessage.COLUMN_NAME_TEXT + " TEXT, "
            + HiMessage.COLUMN_NAME_TIME + " TEXT);";

    private DbOpenHelper(Context context) {
        super(context, getUserDatabaseName(), null, DATABASE_VERSION);
    }

    public static DbOpenHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DbOpenHelper(context.getApplicationContext());
        }
        return instance;
    }

    private static String getUserDatabaseName() {
        return  "hi_demo.db";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(USERNAME_TABLE_CREATE);
//        db.execSQL(INIVTE_MESSAGE_TABLE_CREATE);
//        db.execSQL(CREATE_PREF_TABLE);
        db.execSQL(CREATE_MESSAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL(TOPUSER_TABLE_CREATE);
//        if(oldVersion < 2){
//            db.execSQL("ALTER TABLE "+ UserDao.TABLE_NAME +" ADD COLUMN "+
//                    UserDao.COLUMN_NAME_AVATAR + " TEXT ;");
//        }
//
//        if(oldVersion < 3){
//            db.execSQL(CREATE_PREF_TABLE);
//        }
    }

    public void closeDB() {
        if (instance != null) {
            try {
                SQLiteDatabase db = instance.getWritableDatabase();
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            instance = null;
        }
    }

}