package Constructer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.widget.ImageView;

import java.net.URL;

public class Product_Constructer {

    public Bitmap product_image;
    public String product_name;
    public String product_price;
    public String product_url;

    public Product_Constructer(Bitmap b, String n, String p, String u){
        if (n == null) {this.product_name = "UNKNOWN_NAME";}
        else {this.product_name = n;}

        if (p == null){this.product_price = "UNKNOWN_PRICE";}
        else {this.product_price = p;}

        this.product_url = u;

        this.product_image = b;
    }

    public Bitmap getProduct_image() {
        return product_image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public String getProduct_url() { return product_url; }

}
