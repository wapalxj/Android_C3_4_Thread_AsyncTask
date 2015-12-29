package com.c3.vero.c3_5_02_web_listview;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.conn.ConnectTimeoutException;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int UPDATE_SUCCESS=1;
    private static final int UPDATE_FIALED=2;
    private static final int SERVER_REQUEST_TIMEOUT=3;
    private static final int SERVER_RESPONSE_TIMEOUT=4;
    private ProgressDialog dialog;
    private UserService userService;
    private List<Student> students;
    private ArrayAdapter<Student> adapter;
    private ListView listView;
    private Button btn;
    private  Handler mHandler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            dialog.dismiss();
            switch (msg.what){
                case UPDATE_SUCCESS:
                    adapter.notifyDataSetChanged();
                    showTip("获取成功");
                    break;
                case UPDATE_FIALED:
                    showTip("获取失败：连接出错");
                    break;
                case SERVER_REQUEST_TIMEOUT:
                    showTip("获取失败：请求出错");
                    break;
                case SERVER_RESPONSE_TIMEOUT:
                    showTip("获取失败：响应出错");
                    break;
                case 0:
                    showTip("获取失败：我也不知道怎么了");
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    @Override
    public void onClick(View v) {
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    userService.connect(students);
                    Message message = Message.obtain();
                    message.what = UPDATE_SUCCESS;
                    mHandler.sendMessage(message);
                } catch (ConnectTimeoutException e) {
                    //服务器请求超时异常，可关闭服务器测试
                    e.printStackTrace();
                    Message msg = Message.obtain();
                    msg.what = SERVER_REQUEST_TIMEOUT;
                    mHandler.sendMessage(msg);
                } catch (SocketTimeoutException e) {
                    //服务器响应超时异常
                    e.printStackTrace();
                    Message msg = Message.obtain();
                    msg.what = SERVER_RESPONSE_TIMEOUT;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {//获取失败
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.what = UPDATE_FIALED;
                    mHandler.sendMessage(message);
                }
            }

        }).start();
    }
    //提示信息
    private void showTip(String str){
        Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
    }
    public void init(){
        btn=(Button)findViewById(R.id.btn);
        btn.setOnClickListener(this);
        listView=(ListView)findViewById(R.id.listview);
        dialog=new ProgressDialog(MainActivity.this);
        dialog.setTitle("请等待");
        dialog.setMessage("loading....");
        students=new ArrayList<>();
        adapter=new StudentAdapter(MainActivity.this,R.layout.student_item,students);
        listView.setAdapter(adapter);
        userService=new UserServiceImp();
    }


}
