package Tool;

import android.graphics.Bitmap;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class MemoryCacheUtils {

    private static  MemoryCacheUtils mInstance;
    private ImageCache mImageCache;

    private MemoryCacheUtils(){
        Map<String, SoftReference<Bitmap>> cacheMap = new HashMap<>();
        mImageCache = new ImageCache(cacheMap);

    }
    public static MemoryCacheUtils getInstance(){
        if(mInstance == null){
            synchronized (MemoryCacheUtils.class) {
                if (mInstance == null) {
                    mInstance = new MemoryCacheUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 从内存中读图片
     * @param url
     */
    public Bitmap getBitmapFromMemory(String url) {
        Bitmap bitmap = mImageCache.get(url);
        // 如果图片不存在强引用中，则去软引用（SoftReference）中查找
        if(bitmap == null){
            Map<String, SoftReference<Bitmap>> cacheMap = mImageCache.getCacheMap();
            SoftReference<Bitmap> softReference = cacheMap.get(url);
            if(softReference!=null){
                bitmap = softReference.get();
                //重新放入强引用缓存中
                mImageCache.put(url,bitmap);
            }
        }
        return bitmap;

    }

    /**
     * 往内存中写图片
     * @param url
     * @param bitmap
     */
    public void setBitmapToMemory(String url, Bitmap bitmap) {
        mImageCache.put(url,bitmap);
    }

}
