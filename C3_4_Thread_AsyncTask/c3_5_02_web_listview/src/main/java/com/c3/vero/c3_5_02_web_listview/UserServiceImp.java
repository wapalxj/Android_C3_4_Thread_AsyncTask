package com.c3.vero.c3_5_02_web_listview;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by vero on 2015/12/10.
 */
public class UserServiceImp implements UserService {
    @Override
    public void connect(List<Student> students) throws Exception {
        String urlStr="http://192.168.56.1:8080/vero/GetStudents";
        URL url=new URL(urlStr);

        HttpURLConnection connection=(HttpURLConnection)url.openConnection();
        connection.setRequestProperty("Accept-Encoding", "identity");
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(3000);
        connection.setReadTimeout(3000);
        connection.connect();

        InputStream is=connection.getInputStream();
        BufferedInputStream bis=new BufferedInputStream(is);
        byte [] data =new byte[1024];
        bis.read(data);
        String result =new String(data);
        bis.close();
        is.close();

        connection.disconnect();

        JSONArray array=new JSONArray(result);
        Log.i("aaaaaaaaaaaa",array.toString());

        for (int i=0;i<array.length();i++){
            JSONObject obj=array.getJSONObject(i);
            Log.i(i+"aaaaaaaaaaaa",obj.toString());

            String sid=obj.getString("sid");
            String name=obj.getString("name");
            int age=obj.getInt("age");

            Student temp=new Student(sid,name,age);
            students.add(temp);
        }

    }
}
