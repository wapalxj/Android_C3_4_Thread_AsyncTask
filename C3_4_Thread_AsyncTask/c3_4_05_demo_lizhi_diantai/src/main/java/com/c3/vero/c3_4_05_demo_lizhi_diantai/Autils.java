package com.c3.vero.c3_4_05_demo_lizhi_diantai;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by vero on 2015/12/2.
 */
public class Autils {
    public static final int TYPE_TXT=0;//类型
    public static final int TYPE_IMG=1;//数据
    //线程池
    public static ExecutorService executorService= Executors.newFixedThreadPool(5);

    private Handler mHandler=null;
    public Autils(Handler handler) {
        this.mHandler=handler;
    }

    public void getAsync(final int type,final String url){
//        HttpURLConnection connection=null;
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL mUrl=new URL(url);
                    HttpURLConnection connection=(HttpURLConnection)mUrl.openConnection();
                    connection.setRequestProperty("Accept-Encoding", "identity");//这行必须有，不然进度条不会动
//                    long length=connection.getContentLength();
                    connection.setRequestMethod("GET");
                    InputStream is=connection.getInputStream();
                    ByteArrayOutputStream bos=new ByteArrayOutputStream();
                    byte []bytes=new byte[1024];

                    int len=-1;
                    while (-1!=(len=is.read(bytes))){
                            bos.write(bytes,0,len);
                    }
                    bytes=bos.toByteArray();

                    Message msg=Message.obtain();
                    msg.what=type;

                    if (type==TYPE_TXT){
                        String txt=new String(bytes,"utf-8");
                        msg.obj=txt;
                    }else {
                        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);

                        msg.obj=bitmap;

                        //msg封装url
                        Bundle data=new Bundle();
                        data.putString("url",url);
                        msg.setData(data);
                    }

                    //向主线程发送数据
                    mHandler.sendMessage(msg);


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
