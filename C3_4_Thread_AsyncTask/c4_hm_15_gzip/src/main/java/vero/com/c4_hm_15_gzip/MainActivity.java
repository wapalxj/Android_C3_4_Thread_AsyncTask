package vero.com.c4_hm_15_gzip;

import android.net.http.Headers;
import android.preference.PreferenceActivity;
import android.support.v4.util.LongSparseArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_initGZIP;
    private Button btn_post_key_value;
    private Button btn_post_json;
    private Button btn_post_file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btn_initGZIP = (Button) findViewById(R.id.btn_initGZIP);
        btn_post_key_value= (Button) findViewById(R.id.btn_post_key_value);
        btn_post_json= (Button) findViewById(R.id.btn_post_json);
        btn_post_file= (Button) findViewById(R.id.btn_post_file);
        btn_initGZIP.setOnClickListener(this);
        btn_post_key_value.setOnClickListener(this);
        btn_post_json.setOnClickListener(this);
        btn_post_file.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_initGZIP:
                initGzip();
                break;
            case R.id.btn_post_key_value:
                Map<String,String> parmasMap=new HashMap<>();
                parmasMap.put("vero","vero");
                init_Post_kv(parmasMap);
                break;
            case R.id.btn_post_json:
                String json="{\"vero\":\"123\"}";
                init_json(json);
                break;
            case R.id.btn_post_file:
                //单张图片
                File file=new File("");
                init_post_file(file);
                //多张图片
                File actImgFile=null;
                File listImgFile=null;
                Map<String,File> map=new HashMap<>();
                map.put("actimg",actImgFile);
                map.put("listimg",listImgFile);
                init_post_files(map);
                break;
            default:
                break;
        }
    }

    private void initGzip(){
        new Thread(new Runnable() {
            @Override
            public void run() {
              try {
                  boolean isGzip=false;
                  DefaultHttpClient httpClient =new DefaultHttpClient();
                  HttpGet get=new HttpGet("http://mobileif.maizuo.com/city");
                  //添加gzip请求头
                  get.addHeader("Accept-Encoding","gzip");
                  HttpResponse response=httpClient.execute(get);


                  if (response.getStatusLine().getStatusCode()==200){
                      //检测有无gzip响应头
                      Header[] headerses=response.getHeaders("Content-Encoding");
                      for (Header header:headerses){
                          String value=header.getValue();
                          if (value.equals("gzip")){
                              isGzip=true;
                          }
                      }
                      HttpEntity entity=response.getEntity();

                      //解压
                      String result;
                      if (isGzip){
                          //gzip解压
                          InputStream is=entity.getContent();
                          GZIPInputStream gzipInputStream=new GZIPInputStream(is);
                          //inputStream---->string
                          result=convertStreamToString(gzipInputStream);
                      }else {
                          //标准解压
                          result=EntityUtils.toString(entity);
                      }
                      //响应结果
                      Log.i("result",result);
                  }

              } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    /**
     * stream--->string
     * @param is
     * @return
     * @throws IOException
     */
    public static String convertStreamToString(InputStream is) throws IOException {
        try {
            if (is != null) {
                StringBuilder sb = new StringBuilder();
                String line;
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
                    // BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    while ((line = reader.readLine()) != null) {
                        // sb.append(line);
                        sb.append(line).append("\n");
                    }
                } finally {
                    is.close();
                }
                return sb.toString();
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * http----post传key--value
     * @param parmasMap
     */
    private void init_Post_kv(final Map<String,String> parmasMap){
        //parmasMap用来封装key_value
       new Thread(new Runnable() {
           @Override
           public void run() {
               try {
                   DefaultHttpClient httpClient=new DefaultHttpClient();
                   HttpPost post=new HttpPost("http://httpbin.org/post");
                   post.addHeader("Content-Type","application/x-www-form-urlencoded");

                   List<BasicNameValuePair> parameters=new ArrayList<>();
                   for (Map.Entry<String,String> pairs:parmasMap.entrySet()){
                       String key=pairs.getKey();
                       String value=pairs.getValue();
                       BasicNameValuePair basicNameValuePair=new BasicNameValuePair(key,value);
                       parameters.add(basicNameValuePair);
                   }

                   UrlEncodedFormEntity reqEntity =new UrlEncodedFormEntity(parameters);

                   //提交表单类型的参数
                   post.setEntity(reqEntity);

                   HttpResponse response=httpClient.execute(post);

                   if (response.getStatusLine().getStatusCode()==200){
                       HttpEntity resEntity =response.getEntity();
                       String result=EntityUtils.toString(resEntity);
                       Log.i("result",result);
                   }
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }).start();
    }

    private void init_json(final String json){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DefaultHttpClient httpClient=new DefaultHttpClient();
                    HttpPost post=new HttpPost("http://httpbin.org/post");

                    //提交json参数
                    post.setEntity(new StringEntity(json));
                    HttpResponse response=httpClient.execute(post);
                    if (response.getStatusLine().getStatusCode()==200){
                        HttpEntity resEntity =response.getEntity();
                        String result=EntityUtils.toString(resEntity);
                        Log.i("result",result);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 单张图片的上传
     * @param file
     */
    private void init_post_file(final File file){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DefaultHttpClient httpClient=new DefaultHttpClient();
                    HttpPost post=new HttpPost("http://httpbin.org/post");

                    //提交文件参数httpClient+httpMime
                    MultipartEntity entity =new MultipartEntity();
                    ContentBody body=new FileBody(file);
                    entity.addPart("actimg",body);
                    post.setEntity(entity);
                    HttpResponse response=httpClient.execute(post);
                    if (response.getStatusLine().getStatusCode()==200){
                        HttpEntity resEntity =response.getEntity();
                        String result=EntityUtils.toString(resEntity);
                        Log.i("result",result);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    /**
     * 多张图片的上传
     * @param fileMap
     */
    private void init_post_files(final Map<String,File> fileMap){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DefaultHttpClient httpClient=new DefaultHttpClient();
                    HttpPost post=new HttpPost("http://httpbin.org/post");

                    //提交文件参数httpClient+httpMime
                    MultipartEntity entity =new MultipartEntity();
                    for (Map.Entry<String,File> info:fileMap.entrySet()){
                        String key=info.getKey();
                        File file=info.getValue();
                        ContentBody body=new FileBody(file);
                        entity.addPart(key,body);
                    }

                    post.setEntity(entity);
                    HttpResponse response=httpClient.execute(post);
                    if (response.getStatusLine().getStatusCode()==200){
                        HttpEntity resEntity =response.getEntity();
                        String result=EntityUtils.toString(resEntity);
                        Log.i("result",result);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
