package com.example.administrator.c3_4_01_ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private final String imageUrl="http://img3.12349.net/13a1/12349.net_3tfq2z4obzm.jpg";
    private final String imageUrl2="http://imgs.91danji.com/Upload/2013115/20131151348202468432.jpg";
    private ImageView mImageView=null;
    private Button btn1_no_Thread=null;
    private Button btn2_Thread=null;
    private Button btn3_Thread_post=null;
    private Button btn4_no_Thread_syntask_exe=null;
    private ProgressDialog progressDialog=null;


    private class DownloadImageTask extends AsyncTask<String,Integer,Drawable>{
        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Drawable doInBackground(String... urls) {
            return loadImageFromNetWork(urls[0]);
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            mImageView.setImageDrawable(drawable);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView=(ImageView)findViewById(R.id.imageView);
        btn1_no_Thread=(Button)findViewById(R.id.btn1_no_Thread);
        btn2_Thread=(Button)findViewById(R.id.btn2_Thread);
        btn3_Thread_post=(Button)findViewById(R.id.btn3_Thread_post);
        btn4_no_Thread_syntask_exe=(Button)findViewById(R.id.btn4_no_Thread_syntask_exe);

        btn1_no_Thread.setOnClickListener(this);
        btn2_Thread.setOnClickListener(this);
        btn3_Thread_post.setOnClickListener(this);
        btn4_no_Thread_syntask_exe.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.btn1_no_Thread:
                //主线程不能访问网络
                Drawable drawable=loadImageFromNetWork(imageUrl);
                mImageView.setImageDrawable(drawable);
                break;
            case R.id.btn2_Thread:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //子线程不能操作UI
                        Drawable drawable=loadImageFromNetWork(imageUrl);
                        mImageView.setImageDrawable(drawable);
                    }
                }).start();
                break;
            case R.id.btn3_Thread_post:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Drawable drawable=loadImageFromNetWork(imageUrl);
                        mImageView.post(new Runnable() {
                            @Override
                            public void run() {
                                mImageView.setImageDrawable(drawable);
                            }
                        });
                    }
                }).start();
                break;
            case R.id.btn4_no_Thread_syntask_exe:
                new DownloadImageTask().execute(imageUrl2);
                break;
        }
    }

    public Drawable loadImageFromNetWork(String imageUrl){
        Drawable drawable=null;
        try {
            drawable =Drawable.createFromStream(new URL(imageUrl).openStream(), "image.gif");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (drawable==null){
            Log.i("test","null drawable----------vvvvvvvvvvvvv");
        }else {
            Log.i("test","drawable----------gooooooooooooooood");
        }
        return drawable;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
