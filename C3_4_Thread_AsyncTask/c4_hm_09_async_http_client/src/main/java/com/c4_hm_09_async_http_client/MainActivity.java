package com.c4_hm_09_async_http_client;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.Header;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;

import http.AsyncHttpClient;
import http.AsyncHttpResponseHandler;
import http.RequestParams;

public class MainActivity extends AppCompatActivity {
    public static final int SUCCESS=1;
    public static final int ERROR=0;
    private String path="http://113.55.71.193:8080/vero/LoginServlet?";
    private EditText et_number;
    private EditText et_pwd;
    private TextView tv_state;
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

    public void login(View view) {
        final String number = et_number.getText().toString().trim();
        final String pwd = et_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(number) || TextUtils.isEmpty(pwd)) {
            Toast.makeText(MainActivity.this, "不能为空", Toast.LENGTH_SHORT).show();
            return;
        }


        //get
//            doGet(number,pwd);
        //post
        doPost(number,pwd);
    }

    private void doGet(String number,String pwd){
        try {
            String newPath = path + "number=" + URLEncoder.encode(number, "UTF-8") + "&pwd=" + URLEncoder.encode(pwd, "UTF-8");
            AsyncHttpClient client=new AsyncHttpClient();
            client.get(newPath, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.i("doGet",new String(responseBody));
                    tv_state.setText(new String(responseBody));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    error.printStackTrace();
                    Toast.makeText(MainActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    private void doPost(String number,String pwd){
           AsyncHttpClient client=new AsyncHttpClient();
            //登录参数通过RequestParams设置
            RequestParams params=new RequestParams();
            params.add("number",number);
            params.add("pwd",pwd);
            client.post(path, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.i("doPost",new String(responseBody));
                    tv_state.setText(new String(responseBody));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        }
}

