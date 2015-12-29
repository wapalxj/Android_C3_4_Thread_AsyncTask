package com.c3.vero.c3_5_01_web;
/**
 * login
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.conn.ConnectTimeoutException;

import java.lang.ref.WeakReference;
import java.net.SocketTimeoutException;

public class Login_Activity extends AppCompatActivity implements View.OnClickListener{
    private EditText txt_login_username=null;
    private EditText txt_login_pwd=null;
    private Button btn_login_in=null;
    private Button btn_login_reset=null;
    private UserService userService=new UserServiceImpl();
    private static ProgressDialog dialog;
    private static final int FLAG_LOGIN_SUCCESS=1;
    private static final String MSG_LOGIN_ERROR="登录错误";
    private static final String MSG_LOGIN_SUCCESS="登录成功";
    public static final String MSG_LOGIN_FAILED="用户名或密码错误";
    public static final String SERVER_REQUEST_ERROR ="服务器请求错误";
    public static final String SERVER_REQUEST_TIMEOUT ="服务器请求超时";
    public static final String SERVER_RESPONSE_TIMEOUT="服务器响应超时";
    private Button btn_register=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    public void initView(){
        this.txt_login_username=(EditText)findViewById(R.id.txt_login_username);
        this.txt_login_pwd=(EditText)findViewById(R.id.txt_login_pwd);
        this.btn_login_in=(Button)findViewById(R.id.btn_login_in);
        this.btn_login_reset=(Button)findViewById(R.id.btn_login_reset);
        this.btn_register=(Button)findViewById(R.id.btn_register);
        this.btn_login_in.setOnClickListener(this);
        this.btn_login_reset.setOnClickListener(this);
        this.btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.btn_login_in:
                //验证输入
                if (!confirm()) {return;}
                //loading
                showDialog();
                //连接
                connect();
                break;
            case R.id.btn_login_reset:
                this.txt_login_username.setText("");
                this.txt_login_pwd.setText("");
                break;
            //注册
            case R.id.btn_register:
                Intent reg=new Intent(Login_Activity.this,Register_Activity.class);
                startActivity(reg);
                break;
            default:
                break;
        }
    }
    //连接方法
    private void connect(){
        //引用类型用final :表示只能引用这一个对象
        final String loginName=txt_login_username.getText().toString();
        final String loginPassword=txt_login_pwd.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //连接
                    userService.UserLogin(loginName,loginPassword);
                    handler.sendEmptyMessage(FLAG_LOGIN_SUCCESS);//what=1
                }catch (ConnectTimeoutException e){
                    //post设置参数方式(方式3)的捕捉--服务器请求超时异常，可关闭服务器测试
                    e.printStackTrace();
                    Message msg=Message.obtain();
                    Bundle data=new Bundle();
                    data.putSerializable("error", SERVER_REQUEST_TIMEOUT);
                    msg.setData(data);
                    handler.sendMessage(msg);
                }
                catch (SocketTimeoutException e){
                    //post设置参数方式(方式3)的捕捉--服务器响应超时异常
                    e.printStackTrace();
                    Message msg=Message.obtain();
                    Bundle data=new Bundle();
                    data.putSerializable("error", SERVER_RESPONSE_TIMEOUT);
                    msg.setData(data);
                    handler.sendMessage(msg);
                }

                catch (ServiceRuleException e){//what=0
                    //验证出错、请求出错
                    e.printStackTrace();
                    Message msg=Message.obtain();
                    Bundle data=new Bundle();
                    data.putSerializable("error", e.getMessage());
                    msg.setData(data);
                    handler.sendMessage(msg);
                }
                catch (Exception e){//what=0
                    //登录错误--代码出错--空指针异常等
                    e.printStackTrace();
                    Message msg=Message.obtain();
                    Bundle data=new Bundle();
                    data.putSerializable("error",MSG_LOGIN_ERROR);
                    msg.setData(data);
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

    /**
     * 验证输入是否合法
     */
    public boolean confirm(){
        if (this.txt_login_username.getText().toString().equals("")){
            Toast.makeText(Login_Activity.this,"用户名不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }else if (this.txt_login_pwd.getText().toString().equals("")){
            Toast.makeText(Login_Activity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }else if (this.txt_login_username.getText().toString().length()<4){
            Toast.makeText(Login_Activity.this,"用户名不能小于4位",Toast.LENGTH_SHORT).show();
            return false;
        }else if (this.txt_login_pwd.getText().toString().length()<6){
            Toast.makeText(Login_Activity.this,"密码不能小于6位",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //提示信息
    private void showTip(String str){
        Toast.makeText(Login_Activity.this,str,Toast.LENGTH_SHORT).show();
    }
    private static class MyHandler extends Handler{
        private final WeakReference<Activity> mActivity;

        public MyHandler(Login_Activity ativity) {
            this.mActivity = new WeakReference<Activity>(ativity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (dialog!=null){
                dialog.dismiss();
            }
            int flag=msg.what;
            switch (flag){
                case 0:
                    String error=(String)msg.getData().getSerializable("error");
                    ((Login_Activity)mActivity.get()).showTip(error);
                    break;
                case FLAG_LOGIN_SUCCESS:
                    ((Login_Activity)mActivity.get()).showTip(MSG_LOGIN_SUCCESS);
                    break;
            }

        }
    }
    private MyHandler handler=new MyHandler(this);

    private void showDialog(){
        if (dialog==null){
            dialog=new ProgressDialog(Login_Activity.this);
        }
        dialog.setTitle("loading");
        dialog.setMessage("loading...");
        dialog.setCancelable(false);
        dialog.show();
    }

}
