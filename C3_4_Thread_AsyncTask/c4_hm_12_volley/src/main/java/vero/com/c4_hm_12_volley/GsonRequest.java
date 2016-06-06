package vero.com.c4_hm_12_volley;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;

/**
 * Created by vero on 2016/6/5.
 */
public class GsonRequest<T> extends Request<T>{
    Class<T> tClass;//具体Bean的类型
    private Response.Listener<T> mListener;

    public GsonRequest(int method, String url, Response.ErrorListener listener,Response.Listener<T> mListener,Class<T> tClass) {
        super(method, url, listener);
        this.tClass=tClass;
        this.mListener=mListener;
    }

    /**
     * 处理网络请求
     * @param response
     * @return
     */
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String jsonString;
        try {
            jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException var4) {
            jsonString = new String(response.data);
        }
        //jsonString----->XXX.bean
        try {
            Gson gson=new Gson();
            T obj=gson.fromJson(jsonString,tClass);
            //返回结果
            return Response.success(obj,HttpHeaderParser.parseCacheHeaders(response));
        }catch (JsonSyntaxException e){
            e.printStackTrace();
            return Response.error(new ParseError());
        }
      }
    /**
     * 传递响应结果
     * @param response
     */
    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }
}
