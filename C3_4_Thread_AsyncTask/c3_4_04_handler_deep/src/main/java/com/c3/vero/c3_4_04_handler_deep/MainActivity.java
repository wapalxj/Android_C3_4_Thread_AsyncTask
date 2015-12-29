package com.c3.vero.c3_4_04_handler_deep;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Handler mHandler=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new ConnectionThread().start();
    }

    public void sendMsg(View view){
        if (mHandler!=null){
            Message msg=Message.obtain();
            msg.obj="vero";
            //向实例化handler的线程发送消息
            mHandler.sendMessage(msg);
        }

    }

    class ConnectionThread extends Thread{
        @Override
        public void run() {
            //创建looper
            Looper.prepare();
            //1.实例化handler
            mHandler=new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    String obj=(String)msg.obj;
                    Log.i("aaaaaaaaaaaaaa",new Date()+"111111111");
                    Toast.makeText(MainActivity.this,obj,Toast.LENGTH_SHORT).show();
                }
            };
            //循环读取Message对象
            Looper.loop();
        }
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
