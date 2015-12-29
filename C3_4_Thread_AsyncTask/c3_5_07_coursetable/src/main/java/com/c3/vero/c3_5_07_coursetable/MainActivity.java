package com.c3.vero.c3_5_07_coursetable;

import android.graphics.Bitmap;
import android.renderscript.Element;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Documented;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button down;
    private ImageView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view=(ImageView)findViewById(R.id.view);
        down=(Button)findViewById(R.id.down);
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downLoad();
//                test();
            }
        });
    }
    public void downLoad(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url=new URL("http://202.203.209.96/v5/");
                    HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(8000);
                    connection.setConnectTimeout(8000);
                    connection.setRequestProperty("Accept-Encoding", "identity");
                    connection.connect();

                    InputStream in=connection.getInputStream();
                    BufferedReader bis=new BufferedReader(new InputStreamReader(in));
                    char [] data=new char[10240];
                    StringBuilder sb=new StringBuilder();
                    int len;
                    while (-1!=(len=bis.read(data))){
                        sb.append(data);
                    }
                    String str=sb.toString();
                    Log.i("SSSSSSSSSSSSSSSSSS", str);
                    Document document=Jsoup.parse(str);
                    document.body().attr("ng-app");
                    Log.i("ttttttttttttttttt", document.body().attr("ng-app"));
//                    Elements divs=document.select(".col-xs-5");
//                    Document divscon= Jsoup.parse(divs.toString());
//                    Elements elements=divscon.getElementsByTag("img");
//                    String src=(elements.get(0)).attr("src");
//                    Log.i("sssssssssss", src);
                    bis.close();
                    in.close();
                    connection.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        view.setImageBitmap();
                    }
                });
            }
        }).start();
    }
   public void test(){
       new Thread(new Runnable() {
           @Override
           public void run() {
//                   Document doc = Jsoup.connect("http://wwww.baidu.com").post();
                   String doc="<html><head><title>ggggg</title><body><p id=\"ppp\"></p></body></head></html>";
                   Document document=Jsoup.parse(doc);
                   String title=document.getElementsByTag("p").attr("id");
                   Log.i("ttttttttttttttttt", "aaaaaaa:" + title);
           }
       }).start();
   }
}
