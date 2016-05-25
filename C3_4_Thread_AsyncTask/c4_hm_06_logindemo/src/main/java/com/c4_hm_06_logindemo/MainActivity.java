package com.c4_hm_06_logindemo;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    public static final int SUCCESS=1;
    public static final int ERROR=0;
    private String path="http://113.55.71.193:8080/vero/LoginServlet?";
    private EditText et_number;
    private EditText et_pwd;
    private TextView tv_state;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SUCCESS:
                    String data= (String) msg.obj;
                    tv_state.setText(data);
                    break;
                case ERROR:
                    tv_state.setText("连接错误");
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void initView(){
        et_number= (EditText) findViewById(R.id.et_qq);
        et_pwd= (EditText) findViewById(R.id.et_pwd);
        tv_state= (TextView) findViewById(R.id.login_state);
    }

    public void login(View view){
        final String number=et_number.getText().toString().trim();
        final String pwd=et_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(number)||TextUtils.isEmpty(pwd)){
            Toast.makeText(MainActivity.this,"不能为空",Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(){
            @Override
            public void run() {
                try{
                    final String newPath;
                    newPath = path+"number="+ URLEncoder.encode(number,"UTF-8")+"&pwd="+URLEncoder.encode(pwd,"UTF-8");
                    URL url=new URL(newPath);
                    HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    int code=conn.getResponseCode();
                    if (code==200){
                        InputStream is=conn.getInputStream();
                        ByteArrayOutputStream bos=new ByteArrayOutputStream();
                        int len=0;
                        byte [] buff=new byte[1024];
                        while ((len=is.read(buff))>0){
                            bos.write(buff,0,len);
                        }
                        Message msg=Message.obtain();
                        String data=bos.toString();
                        msg.what= SUCCESS;
                        msg.obj=data;
                        mHandler.sendMessage(msg);

                    }else {
                        Message msg=Message.obtain();
                        msg.what= ERROR;
                        mHandler.sendMessage(msg);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
