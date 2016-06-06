package com.leslie.volleylistviewdemo;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

/**
 * 自定义一个内存缓存,基于的LRU算法的内存缓存
 * @author Administrator
 *
 */
public class MyImageCache implements ImageCache {
	LruCache<String, Bitmap> caches;
	//1.定义缓存的空间大小
	int maxSize = 4 * 1024 * 1024;//单位是byte-->4194304byte

	//	int maxSize = 4;//单位是m-->4M

	//缓存的最大值 4m 4*1024*1024kb ,是空间大小.不是元素个数

	public MyImageCache() {
		caches = new LruCache<String, Bitmap>(maxSize) {
			//2.重载sizeOf
			@Override
			protected int sizeOf(String key, Bitmap value) {
				// TODO
				//返回bitmap这个entry的大小,统一计算单位
				//				return value.getByteCount() / 1024 / 1024;//一张图片,占了多少M
				return value.getByteCount();//一张图片,占了多少bytes
			}
		};
	}

	/**
	 * 从缓存里面取图片
	 */
	@Override
	public Bitmap getBitmap(String url) {
		System.out.println("--------------从缓存中加载--------------");
		return caches.get(url);
	}

	/**
	 * 放到缓存里面去
	 */
	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		System.out.println("--------------放置到缓存--------------");
		caches.put(url, bitmap);

	}

}
