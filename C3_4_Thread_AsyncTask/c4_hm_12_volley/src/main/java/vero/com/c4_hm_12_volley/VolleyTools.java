package vero.com.c4_hm_12_volley;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by vero on 2016/6/5.
 * 单例化
 */
public class VolleyTools {
    RequestQueue mQueue;
    ImageLoader mLoader;
    ImageLoader.ImageCache mCache;

    private static VolleyTools instance;
    private VolleyTools(Context context){
        mQueue= Volley.newRequestQueue(context);
        mCache=new MyImageCache();
        mLoader=new ImageLoader(mQueue,mCache);
    }

    public static VolleyTools getInstance(Context context){
        if (instance==null){
            synchronized (VolleyTools.class){
                if (instance==null){
                    instance=new VolleyTools(context);
                }
            }
        }
        return instance;
    }

    public RequestQueue getmQueue() {
        return mQueue;
    }

    public ImageLoader getmLoader() {
        return mLoader;
    }

    public ImageLoader.ImageCache getmCache() {
        return mCache;
    }
}
