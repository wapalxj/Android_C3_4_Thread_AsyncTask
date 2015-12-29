package com.c3.vero.c3_5_05_web_download;

import android.app.ProgressDialog;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private Button download;
    private ProgressDialog dialog;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int pro=msg.arg1;
            int length=msg.arg2;
            dialog.setProgress((int) (pro / (length * 1.0) * 100));
            dialog.setMessage((int)(pro/(length*1.0)*100)+"%");//针对圆形进度显示百分比
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        download=(Button)findViewById(R.id.down);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doDownload();
            }
        });
    }

    private void doDownload() {
        dialog=new ProgressDialog(MainActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMax(100);
        dialog.setTitle("loading...");
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream in=null;
                OutputStream out=null;
                try {
                    URL url=new URL("http://192.168.56.1:8080/vero/download");
                    HttpURLConnection connection =(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Accept-Encoding", "identity");
//                    connection.setRequestProperty("Content-Type", "multipart/form-data");
                    connection.setDoOutput(true);
                    connection.setReadTimeout(8000);
                    connection.setConnectTimeout(8000);
                    connection.setUseCaches(false);
                    final int length=connection.getContentLength();
                    in=new BufferedInputStream(connection.getInputStream());
                    out= new BufferedOutputStream(
                            new FileOutputStream(
                                    new File(Environment.getExternalStorageDirectory()+"/download.jpg")));


                    byte []data=new byte[10240];
                    int len;
                    int perLength=0;
                    while (-1!=(len=in.read(data))){
                        Thread.sleep(200);
                        perLength+=len;
                        out.write(data, 0, len);

                        Message msg=Message.obtain();
                        msg.arg1=perLength;
                        msg.arg2=length;
                        mHandler.sendMessage(msg);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            Toast.makeText(MainActivity.this, "Goooood", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        out.close();
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

}
