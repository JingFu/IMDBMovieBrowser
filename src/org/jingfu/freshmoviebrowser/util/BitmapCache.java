package org.jingfu.freshmoviebrowser.util;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class BitmapCache extends LruCache<String, Bitmap> implements ImageCache {

	public BitmapCache(int size) {
		super(size);
	}

	public BitmapCache() {
		super(getDefaultLruCacheSize());
	}

	public static int getDefaultLruCacheSize() {
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory / 8;
		return cacheSize;
	}

	public Bitmap getBitmap(String url) {
		Log.d("BitmapCache", "getBitmap : " + url);

		Bitmap b = get(url);
		if (b != null) {
			Log.d("BitmapCache", "getBitmap : " + b.getWidth() + " x " + b.getHeight());
		}
		return get(url);
	}

	public void putBitmap(String url, Bitmap bitmap) {
		Log.d("BitmapCache", "putBitmap : " + url);

		Log.d("BitmapCache", "putBitmap : " + bitmap.getWidth() + " x " + bitmap.getHeight());
		put(url, bitmap);
	}
}
