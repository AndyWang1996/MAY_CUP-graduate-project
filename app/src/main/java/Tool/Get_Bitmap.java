package Tool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * author: WangYiKai
 * date: on 2019/4/15.
 * describe:下载图片的工具类
 */
public class Get_Bitmap {

    private static Get_Bitmap instance = new Get_Bitmap();

    private Get_Bitmap(){}

    public static Get_Bitmap getInstance(){
        return instance;
    }

    /*
     *    get image from network
     *    @param [String]imageURL1
     *    @return [BitMap]image
     */
    public Bitmap returnBitMap(String url){
        URL myFileUrl = null;
        Bitmap bitmap = null;
        url = url.replace("\\","");
        Log.i("imgURL",url);
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


}
