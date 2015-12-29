package com.c3.vero.c3_5_01_web;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.conn.ConnectTimeoutException;

import java.lang.ref.WeakReference;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class Register_Activity extends AppCompatActivity implements View.OnClickListener{

    private EditText txt_register_username =null;
    private EditText txt_register_pwd =null;
    private Button btn_register_in =null;
    private Button btn_register_reset =null;
    private List<String> hobbies=null;
    private CheckBox chk_music=null;
    private CheckBox chk_coding=null;
    private CheckBox chk_lol=null;
    private UserService userService=new UserServiceImpl();
    private static ProgressDialog dialog;
    private static final int FLAG_Register_SUCCESS =1;
    private static final String MSG_Register_ERROR ="注册错误";
    private static final String MSG_Register_SUCCESS ="注册成功";
    public static final String MSG_Register_FAILED ="用户名或密码错误";
    public static final String SERVER_REQUEST_ERROR ="服务器请求错误";
    public static final String SERVER_REQUEST_TIMEOUT ="服务器请求超时";
    public static final String SERVER_RESPONSE_TIMEOUT="服务器响应超时";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);
        initView();
    }
    public void initView(){
        this.txt_register_username =(EditText)findViewById(R.id.txt_register_username);
        this.txt_register_pwd =(EditText)findViewById(R.id.txt_register_pwd);
        this.btn_register_in =(Button)findViewById(R.id.btn_register_in);
        this.btn_register_reset =(Button)findViewById(R.id.btn_register_reset);

        this.btn_register_in.setOnClickListener(this);
        this.btn_register_reset.setOnClickListener(this);

        this.chk_music=(CheckBox)findViewById(R.id.music);
        this.chk_coding=(CheckBox)findViewById(R.id.coding);
        this.chk_lol=(CheckBox)findViewById(R.id.LOL);

        this.chk_music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        this.chk_coding.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        this.chk_lol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.btn_register_in:
                //验证输入
                if (!confirm()) {return;}
                //loading
                showDialog();
                //连接
                connect();
                break;
            case R.id.btn_register_reset:
                this.txt_register_username.setText("");
                this.txt_register_pwd.setText("");
                break;
            default:
                break;
        }
    }
    //连接方法
    private void connect(){
        //引用类型用final :表示只能引用这一个对象
        final String registerName= txt_register_username.getText().toString();
        final String registerPassword= txt_register_pwd.getText().toString();
        //检测是否选中兴趣选项
        checkHobbies();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //连接
                    userService.UserRegister(registerName,registerPassword,hobbies);
                    handler.sendEmptyMessage(FLAG_Register_SUCCESS);//what=1
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
                    data.putSerializable("error", MSG_Register_ERROR);
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
        if (this.txt_register_username.getText().toString().equals("")){
            Toast.makeText(Register_Activity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }else if (this.txt_register_pwd.getText().toString().equals("")){
            Toast.makeText(Register_Activity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }else if (this.txt_register_username.getText().toString().length()<4){
            Toast.makeText(Register_Activity.this,"用户名不能小于4位",Toast.LENGTH_SHORT).show();
            return false;
        }else if (this.txt_register_pwd.getText().toString().length()<6){
            Toast.makeText(Register_Activity.this,"密码不能小于6位",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //提示信息
    private void showTip(String str){
        Toast.makeText(Register_Activity.this,str,Toast.LENGTH_SHORT).show();
    }
    private static class MyHandler extends Handler {
        private final WeakReference<Activity> mActivity;

        public MyHandler(Register_Activity activity) {
            this.mActivity = new WeakReference<Activity>(activity);
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
                    ((Register_Activity)mActivity.get()).showTip(error);
                    break;
                case FLAG_Register_SUCCESS:
                    ((Register_Activity)mActivity.get()).showTip(MSG_Register_SUCCESS);
                    break;
            }

        }
    }
    private MyHandler handler=new MyHandler(this);

    private void showDialog(){
        if (dialog==null){
            dialog=new ProgressDialog(Register_Activity.this);
        }
        dialog.setTitle("loading");
        dialog.setMessage("loading...");
        dialog.setCancelable(false);
        dialog.show();
    }

    private void checkHobbies(){
        hobbies=new ArrayList<>();
        if (chk_music.isChecked()){
            hobbies.add("music");
        }
        if (chk_coding.isChecked()){
            hobbies.add("coding");
        }
        if (chk_lol.isChecked()){
            hobbies.add("LOL");
        }
    }
}
