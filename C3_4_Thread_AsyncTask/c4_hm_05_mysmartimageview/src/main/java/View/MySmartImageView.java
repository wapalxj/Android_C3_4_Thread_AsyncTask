package View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by vero on 2016/5/23.
 * 写一个自己的SmartImageView原理--->代码不完整
 */
public class MySmartImageView extends ImageView{

    public static final int SUCCESS=1;
    public static final int ERROR=0;
    private Handler mHanlder=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SUCCESS:
                    Bitmap bitmap= (Bitmap) msg.obj;
                    break;
                case ERROR:
                    break;
                default:
                    break;
            }
        }
    };
    public MySmartImageView(Context context) {
        super(context);
    }

    public void setImageUrl(final String url){
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url2 =new URL(url);
                    HttpURLConnection conn= (HttpURLConnection) url2.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    int code=conn.getResponseCode();
                    if (code==200){
                        InputStream is=conn.getInputStream();
                        Bitmap bitmap=BitmapFactory.decodeStream(is);
                        Message msg=Message.obtain();
                        msg.obj=bitmap;
                        msg.what= SUCCESS;
                        mHanlder.sendMessage(msg);
                    }else {

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    Message msg=Message.obtain();
                    msg.what= ERROR;
                    mHanlder.sendMessage(msg);
                }
            }
        }.start();
    }
}
