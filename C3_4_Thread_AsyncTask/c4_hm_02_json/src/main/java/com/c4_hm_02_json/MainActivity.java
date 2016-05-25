package com.c4_hm_02_json;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 通过JSON获取天气信息
 */
public class MainActivity extends AppCompatActivity {

    public static final int SUCCESS=1;
    public static final int INVALID_CITY=2;
    private EditText et_city;
    private TextView city_result1;
    private TextView city_result2;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SUCCESS:
                    try {
                        JSONArray days= (JSONArray) msg.obj;
                        String day01=days.getString(0);
                        String day02=days.getString(1);
                        city_result1.setText(day01);
                        city_result2.setText(day02);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case INVALID_CITY:
                    Toast.makeText(MainActivity.this,"城市错误",Toast.LENGTH_SHORT).show();
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
        private void initView(){
            et_city= (EditText) findViewById(R.id.et_city);
            city_result1= (TextView) findViewById(R.id.city_result1);
            city_result2= (TextView) findViewById(R.id.city_result2);
        }

        String path="http://wthrcdn.etouch.cn/weather_mini?city=";
        String city=null;
        public void search(View v){
//        city+=et_city.getText().toString().trim();
            city="深圳";
            if (TextUtils.isEmpty(city)){
                Toast.makeText(MainActivity.this, "地址错误", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                path= path+URLEncoder.encode(city,"UTF-8");
                new Thread(){
                    @Override
                    public void run() {
                        try{
                            URL url=new URL(path);
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

                                String data=bos.toString();
                                Log.i("bbbbbbbbbbbos",data);
                                JSONObject jsonObject=new JSONObject(data);
                                String result =jsonObject.getString("desc");
                                if ("OK".equals(result)){
                                    //城市有效
                                    JSONObject dataObj=jsonObject.getJSONObject("data");
                                    JSONArray jsonArray=dataObj.getJSONArray("forecast");
//                                String value1=jsonArray.getString(0);
//                                String value2=jsonArray.getString(1);
//                                Log.i("bbbbbbbbbbbos",value1+"----"+value2);
                                    Message msg=Message.obtain();
                                    msg.what= SUCCESS;
                                    msg.obj=jsonArray;
                                    mHandler.sendMessage(msg);
                                }else {
                                    //城市无效
                                    Message msg=Message.obtain();
                                    msg.what= INVALID_CITY;
                                    mHandler.sendMessage(msg);
                                }

                            }else {

                            }

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }
}
