package com.c4_hm_03_xml;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Xml;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText et_phone;
    private TextView tv_phone_number;
    private TextView tv_phone_location;
    private TextView tv_phone_jx;
    String phone_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        et_phone= (EditText) findViewById(R.id.et_phone);
        tv_phone_number= (TextView) findViewById(R.id.tv_phone_number);
        tv_phone_location= (TextView) findViewById(R.id.tv_phone_location);
        tv_phone_jx= (TextView) findViewById(R.id.tv_phone_jx);
    }

    String path="http://mobile.9om.com/"+phone_no.substring(0,6)+"/"+phone_no+".html";
    public void getJX(View view){
        phone_no=et_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone_no)){
            Toast.makeText(MainActivity.this,"手机号码错误",Toast.LENGTH_SHORT).show();
            return ;
        }

        new Thread(){
            @Override
            public void run() {
                try {
                    URL url=new URL(path);
                    HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    int code=conn.getResponseCode();
                    if (code==200){
                        InputStream is=conn.getInputStream();
                        XmlPullParser parser= Xml.newPullParser();
                        parser.setInput(is,"utf-8");
                        int eventType=parser.getEventType();
                        while (eventType!=XmlPullParser.END_DOCUMENT){
                            //javabean



                            eventType=parser.next();
                        }
                    }else {

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }

            }
        }.start();


    }
}
