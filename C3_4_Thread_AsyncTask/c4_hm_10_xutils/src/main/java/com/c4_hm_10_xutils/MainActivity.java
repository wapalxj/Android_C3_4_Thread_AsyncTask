package com.c4_hm_10_xutils;

/**
 * 开源框架XUtils--->暂未测试成功
 *
 */

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;



import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;




public class MainActivity extends AppCompatActivity {
    //	private static String path="http://127.0.0.1:8080/file.txt";
    //注意Android不能使用127.0.0.1
    private static String path="http://113.55.50.139:8080/ff.exe";
    private EditText et_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        x.Ext.init(getApplication());
    }

    public void initView(){
        et_path= (EditText) findViewById(R.id.et_path);
    }
    public void download(View v){
        et_path.getText().toString().trim();

        //XUtils2
//        HttpUtils utils=new HttpUtils();
//        utils.download(path,
//                Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"vero.exe",
//                true,//自动断点续传
//                new RequestCallBack<File>() {
//                    @Override
//                    public void onStart() {
//                        System.out.println("下载开始");
//                    }
//
//                    @Override
//                    public void onLoading(long total, long current, boolean isUploading) {
//                        System.out.println("下载进行中");
//                    }
//
//                    @Override
//                    public void onSuccess(ResponseInfo<File> responseInfo) {
//                        System.out.println("下载成功");
//                    }
//
//                    @Override
//                    public void onFailure(HttpException error, String msg) {
//                        System.out.println("下载失败");
//                    }
//                }
//
//        );

        //XUtils3
//        RequestParams params = new RequestParams(path);
//        params.addQueryStringParameter("wd", "xUtils");
//        x.http().get(params, new Callback.CommonCallback<String>() {
//
//            @Override
//            public void onSuccess(String result) {
//                Toast.makeText(x.app(), result, Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        });

    }


}

