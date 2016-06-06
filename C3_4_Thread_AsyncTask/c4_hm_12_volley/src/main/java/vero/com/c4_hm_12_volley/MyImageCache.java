package vero.com.c4_hm_12_volley;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by vero on 2016/6/5.
 */
public class MyImageCache implements ImageLoader.ImageCache {

    LruCache<String,Bitmap> lruCache;
    private int maxSize=5*1024*1024;//5242880 byte
    public MyImageCache(){
        lruCache=new LruCache<String, Bitmap>(maxSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //返回每一个entry大小
                //具体大小和定义的maxSize单位统一
                return value.getByteCount();//父类为1byte
            }
        };
    }
    @Override
    public Bitmap getBitmap(String url) {
        return lruCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        lruCache.put(url,bitmap);
    }
}
