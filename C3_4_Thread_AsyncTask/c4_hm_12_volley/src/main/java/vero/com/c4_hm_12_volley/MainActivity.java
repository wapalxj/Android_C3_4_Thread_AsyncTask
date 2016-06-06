package vero.com.c4_hm_12_volley;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_string;
    private Button btn_jsonO;
    private Button btn_jsonA;
    private Button btn_ImageR;
    private Button btn_niv;
    private Button btn_imgLoader;
    private Button btn_Gson;
    private ImageView iv;
    private NetworkImageView niv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    private void initView(){
        btn_string = (Button) findViewById(R.id.stringR);
        btn_jsonO = (Button) findViewById(R.id.JsonOR);
        btn_jsonA = (Button) findViewById(R.id.JsonAR);
        btn_ImageR= (Button) findViewById(R.id.ImageR);
        btn_niv= (Button) findViewById(R.id.btn_niv);
        btn_imgLoader= (Button) findViewById(R.id.btn_imgLoader);
        btn_Gson= (Button) findViewById(R.id.btn_Gson);
        iv= (ImageView) findViewById(R.id.iv);
        niv= (NetworkImageView) findViewById(R.id.niv);

        btn_string.setOnClickListener(this);
        btn_jsonO.setOnClickListener(this);
        btn_jsonA.setOnClickListener(this);
        btn_ImageR.setOnClickListener(this);
        btn_niv.setOnClickListener(this);
        btn_imgLoader.setOnClickListener(this);
        btn_Gson.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.stringR:
                initStringRequest();
                break;
            case R.id.JsonOR:
                initJsonObjectRequest();
                break;
            case R.id.JsonAR:
                initJsonArrayRequest();
                break;
            case R.id.ImageR:
                initImageRequest();
                break;
            case R.id.btn_niv:
                initNetWorkImageView();
                break;
            case R.id.btn_imgLoader:
                initImageLoader();
                break;
            case R.id.btn_Gson:
                initGsonRequest();
                break;
        }
    }



    /**
     *StringRequest:直接返回String=
     */
    private void initStringRequest(){
        //1.创建StringRequest对象
        String url="http://www.baidu.com";
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("onResponse------->",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("onErrorResponse------->",error.getMessage());

            }
        });
        //2.创建RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        //3.发起网络请求：把request放入requestQueue中
        requestQueue.add(stringRequest);


        //网络请求的取消
        stringRequest.cancel();//当请求的取消
        //队列中的所有请求取消
        requestQueue.cancelAll(null);
        //取消指定的请求,tag--->stringRequest.setTag("tag1");
        requestQueue.cancelAll("tag1");


    }
    /**
     * JsonObjectRequest:直接返回JsonObject
     */
    private void initJsonObjectRequest(){
        //1.创建jsonObjectRequest对象
        String url="http://113.55.41.20:8080/vero.json";
        JSONObject jsonRequest=null;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
//                        try {
//                            String vero=jsonObject.getString("hobbies");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                        String vero2=jsonObject.optString("hobbies");//不需要try,无法获取则返回null

                        Log.i("onResponse------->",vero2);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("onErrorResponse------->",volleyError.getMessage());
            }
        });
        //2.创建RequestQueue对象
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        //3.发起请求
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * JsonArrayRequest:直接返回JsonArray
     */
    private void initJsonArrayRequest(){
        //1.创建jsonObjectRequest对象
        String url="http://113.55.41.20:8080/veroArray.json";
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {

                Log.i("onResponse------->",jsonArray.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("onErrorResponse------->",volleyError.getMessage());
            }
        });

        //2.创建RequestQueue对象
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        //3.发起请求
        requestQueue.add(jsonArrayRequest);
    }

    /**
     * ImageRequest:直接返回Bitmap
     */
    private void initImageRequest(){
        //1.创建jsonObjectRequest对象
                    String url="http://113.55.41.20:8080/img/login.png";
                    ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            Log.i("onResponse------->",bitmap.getByteCount()+"");
                            iv.setImageBitmap(bitmap);
                        }
                    }, 0, 0, Bitmap.Config.ARGB_4444, new Response.ErrorListener() {
                        //0,0参数为宽高,0,0表示默认大小
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.i("onErrorResponse------->",volleyError.getMessage());
                        }
                    });

                    //2.创建RequestQueue对象
                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                    //3.发起请求
                    requestQueue.add(imageRequest);
                }

                /**
                 * 	NetWorkImageView:继承了ImageView
                 */
    private void initNetWorkImageView(){
        //1.设置默认和错误图片
        String url="http://113.55.41.20:8080/img/login.png";
        niv.setDefaultImageResId(R.drawable.default_img);
        niv.setErrorImageResId(R.drawable.error);
        //2.创建RequestQueue对象
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        //3.设置缓存
        ImageLoader.ImageCache cache=new MyImageCache();
        ImageLoader loader=new ImageLoader(requestQueue,cache);
        niv.setImageUrl(url,loader);
    }
    /**
     * 	ImageLoader:图片加载器
     */
    private void initImageLoader() {
        String url="http://113.55.41.20:8080/img/login.png";
//        ImageLoader.ImageCache cache=new MyImageCache();
//        RequestQueue queue=Volley.newRequestQueue(MainActivity.this);
//        ImageLoader loader=new ImageLoader(queue,cache);
        //使用单例化
        ImageLoader loader= VolleyTools.getInstance(MainActivity.this).getmLoader();
        loader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {

                Bitmap bitmap=imageContainer.getBitmap();
//                Log.i("onResponse------->",bitmap.getByteCount()+"");
                iv.setImageBitmap(bitmap);
            }
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("onErrorResponse------->",volleyError.getMessage());
            }
        },0,0);
    }

    /**
     * 	自定义请求：Gson
     */
    private void initGsonRequest() {
        String url="http://113.55.41.20:8080/vero.json";
        GsonRequest<Ip> gsonRequest = new GsonRequest<>(Request.Method.GET, url,
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error:" + error.getMessage());
                    }
                }, new Response.Listener<Ip>() {

            @Override
            public void onResponse(Ip ip) {
                System.out.println(ip.name);
            }
        }, Ip.class);

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(gsonRequest);
    }

}
