package com.example.hi2.utils;

import android.content.Context;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/1.
 */
public class FileService {
    private Context context;

    public FileService(Context context){
        this.context = context;
    }

    public boolean saveToRom(String username, String password, String filename) throws Exception{
        FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
        String result = username+":"+password;
        fos.write(result.getBytes());
        fos.flush();
        fos.close();
        return true;
    }

    public Map<String, String> getUserInfo(String filename) throws Exception{
        FileInputStream fis = context.openFileInput(filename);

//        File file = new File(context.getFilesDir()+filename);
//        FileInputStream fis = new FileInputStream(file);
        byte[] data = getBytes(fis);
        String result = new  String(data);
        String results[] = result.split(":");
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", results[0]);
        map.put("password", results[1]);
        return map;
    }

    public byte[] getBytes(InputStream is) throws Exception{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len=is.read(buffer)) != -1){
            baos.write(buffer, 0, len);
        }
        baos.flush();
        baos.close();
        return baos.toByteArray();
    }
}
