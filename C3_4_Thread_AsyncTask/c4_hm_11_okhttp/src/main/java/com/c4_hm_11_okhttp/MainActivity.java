package com.c4_hm_11_okhttp;
/**
 * OKHTTP框架的GET和POST
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button doGet;
    private Button doPost;
    private String path="http://113.55.78.141:8080/news.xml";
    private OkHttpClient okHttpClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        okHttpClient=new OkHttpClient();
    }

    private void initView(){
        doGet= (Button) findViewById(R.id.doGet);
        doPost= (Button) findViewById(R.id.doPost);
        doGet.setOnClickListener(this);
        doPost.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.doGet:
                doGet();
                break;
            case R.id.doPost:
                doPost();
                break;
            default:
                break;
        }
    }

    private void doGet() {
        new Thread(){
            @Override
            public void run() {
                //get为同步响应
                try {
                    Request request=new Request.Builder().url(path).build();
                    Response response=okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()){
                        String resBody=response.body().string();
                        Log.i("okHttp--get--response",resBody);
                    }else {
                        Log.i("okHttp--get--failed","failed");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
    private void doPost() {
        new Thread(){
            @Override
            public void run() {
                //post为异步响应
                    //有请求参数的时候：new FormBody.Builder().add("name","xxx").build();
                    FormBody formBody=new FormBody.Builder().build();
                    Request request=new Request.Builder().url(path).post(formBody).build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.i("okHttp--post--failed","failed");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String resBody=response.body().string();
                            Log.i("okHttp--post--response",resBody);
                        }
                    });
            }
        }.start();
    }


}
