package vero.com.c4_hm_01_web;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    public static final int SUCCESS=1;
    public static final int ERROR=0;
    public static final int NETWORK_ERROR = -1;
    private EditText ed_path;
    private TextView tv;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SUCCESS:
                    tv.setText(msg.obj.toString());
                    break;
                case ERROR:
                    Toast.makeText(getApplicationContext(),"错误",Toast.LENGTH_SHORT).show();
                    break;
                case NETWORK_ERROR:
                    Toast.makeText(getApplicationContext(),"网络错误",Toast.LENGTH_SHORT).show();
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
        ed_path= (EditText) findViewById(R.id.ed_path);
        tv= (TextView) findViewById(R.id.tv);
    }

    String path=null;
    public void viewPageSource(View v){
        path=ed_path.getText().toString().trim();

        if (TextUtils.isEmpty(path)){
            Toast.makeText(getApplicationContext(),"路径不对",Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(){
            @Override
            public void run() {
                try {
                    URL url=new URL(path);
                    HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setRequestMethod("GET");

                    //构建PC端请求头
//                    connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko)");

                    String type=connection.getContentType();
                    int code=connection.getResponseCode();

                    if (code==200){
                        InputStream is=connection.getInputStream();
                        ByteArrayOutputStream bos=new ByteArrayOutputStream();
                        int len=0;
                        byte [] buff=new byte[1024];
                        while ((len=is.read(buff))>0){
                            bos.write(buff,0,len);
                        }
                        Message msg=Message.obtain();
                        String data=bos.toString();
                        msg.what= SUCCESS;
                        msg.obj=data;
                        mHandler.sendMessage(msg);

                    }else {
                        Message msg=Message.obtain();
                        msg.what= ERROR;
                        mHandler.sendMessage(msg);
                    }

                }catch (IOException e) {
                    e.printStackTrace();
                    Message msg=Message.obtain();
                    msg.what=NETWORK_ERROR;
                    mHandler.sendMessage(msg);
                }

            }
        }.start();
    }


}
