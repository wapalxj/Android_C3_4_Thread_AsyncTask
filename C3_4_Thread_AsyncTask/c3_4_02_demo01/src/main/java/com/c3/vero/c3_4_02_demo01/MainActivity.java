package com.c3.vero.c3_4_02_demo01;


import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button start=null;
    private Button stop=null;
    private TextView timer=null;
    private Handler mHandler=null;
    private boolean isStop=false;
    private int count =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view();
        stop.setEnabled(false);
    }
    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.start:
                v.setEnabled(false);
                stop.setEnabled(true);
                isStop=false;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        int count =0;
                        while (!isStop){
                            Message message=Message.obtain();
                            message.what=count++;
                            try {
                                mHandler.sendMessage(message);
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }).start();

                break;
            case R.id.stop:
                start.setEnabled(true);
                v.setEnabled(false);
                isStop=true;
            break;
            default:
            break;
        }
    }
    private void view(){
        start=(Button)findViewById(R.id.start);
        stop=(Button)findViewById(R.id.stop);
        timer=(TextView)findViewById(R.id.timer);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        mHandler=new Handler() {
            @Override
            public void handleMessage(Message msg) {
                timer.setText("send: "+msg.what);
            }
        };
    }
}
