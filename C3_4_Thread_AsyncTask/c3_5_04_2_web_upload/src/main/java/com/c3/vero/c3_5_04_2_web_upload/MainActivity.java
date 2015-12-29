package com.c3.vero.c3_5_04_2_web_upload;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private static final int FLAG_LOAD_IMAGE=1;
    private UserService service=new UserServiceImp();
    private String pathName;
    private Button btnSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init() {
        btnSelect=(Button)findViewById(R.id.btn_select);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //读取手机相册
                Intent i = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, FLAG_LOAD_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==FLAG_LOAD_IMAGE) {
            if (data == null) {
                Toast.makeText(this, "nononono", Toast.LENGTH_SHORT).show();
            } else {
                Uri uri =data.getData();
                if (uri==null){
                    Toast.makeText(this, "nononono", Toast.LENGTH_SHORT).show();
                }else {
                    String path=null;
                    String[] pojo={MediaStore.Images.Media.DATA};
                    Cursor cursor=getContentResolver().query(uri,pojo,null,null,null);
                    if (cursor!=null){
                        int columnIndex=cursor.getColumnIndexOrThrow(pojo[0]);
                        cursor.moveToFirst();
                        path=cursor.getString(columnIndex);
                        cursor.close();
                    }

                    if (path==null){
                        Toast.makeText(this, "图片物理路径获取失败", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, "图片物理路径:"+path, Toast.LENGTH_SHORT).show();

                        pathName=path;
                        new AlertDialog.Builder(this)
                                .setTitle("提示")
                                .setMessage("上传吗？")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        doUpload();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).create().show();
                    }
                }
            }
        }
    }

    private void doUpload(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //上传字符串+对象，对象：流，字符串：map
                    File file=new File(pathName);
                    Map<String,String> data=new HashMap<>();
                    data.put("Name","Vero");
                    data.put("Gender", "man");
                    final String result=service.upLoad(file,data);
                    //主线程显示返回值
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }
}
