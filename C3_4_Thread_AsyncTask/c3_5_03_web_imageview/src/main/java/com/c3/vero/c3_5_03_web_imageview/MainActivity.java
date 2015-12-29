package com.c3.vero.c3_5_03_web_imageview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageView;
    private Button down;
    private Bitmap bitmap;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            bitmap=(Bitmap)msg.obj;
            if (bitmap!=null){
                imageView.setImageBitmap(bitmap);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=(ImageView)findViewById(R.id.image);
        imageView.setImageResource(R.drawable.i2);
        down=(Button)findViewById(R.id.down);
        down.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bitmap=downImage();
                Log.i("bbb",bitmap.toString());

                Message msg=Message.obtain();
                msg.obj=bitmap;
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    private Bitmap downImage() {
        Bitmap bitmap=null;
        try {
            //GET
//            String strUrl="http://192.168.56.1:8080/vero/GetImage.jpeg?id=2";
            //POST
            String strUrl="http://192.168.56.1:8080/vero/GetImage.jpeg";
            URL url=new URL(strUrl);
            HttpURLConnection connection= (HttpURLConnection)url.openConnection();
//            connection.setRequestMethod("GET");
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept-Encoding", "identity");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setReadTimeout(8000);
            connection.setConnectTimeout(8000);
            connection.setChunkedStreamingMode(0);
            connection.setUseCaches(false);
            connection.connect();

            OutputStream os=connection.getOutputStream();

//POST方式需要传递参数
            PrintWriter pw=new PrintWriter(os);
            pw.print("id=1");
            pw.flush();
            pw.close();
            os.close();

            InputStream is=connection.getInputStream();
            BufferedInputStream bis=new BufferedInputStream(is);
            bitmap= BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
