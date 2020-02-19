package Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.may_cup.R;

import java.util.List;

import Constructer.Product_Constructer;

public class ProductAdapter extends ArrayAdapter<Product_Constructer>{

    private int mResourceId;

    public ProductAdapter(Context context, int textViewResourceId, List<Product_Constructer> objects) {
        super(context, textViewResourceId, objects);
        // TODO Auto-generated constructor stub
        this.mResourceId = textViewResourceId;
    }

    public View getView(int position, View converView, ViewGroup parent){
        Product_Constructer product_constructer = getItem(position);
        Log.i("DATACHECK",product_constructer.getProduct_name());
        Log.i("DATACHECK",product_constructer.getProduct_price());
        Log.i("DATACHECK",product_constructer.getProduct_url());
        View view;

        if (converView == null) {
            view = LayoutInflater.from(getContext()).inflate(mResourceId, parent, false);
            view.setTag(view);
        } else {
            view = converView;
        }

        ImageView imageView = view.findViewById(R.id.fragment_5_cart_product_item);
        imageView.setImageBitmap(product_constructer.getProduct_image());
        TextView p_name = view.findViewById(R.id.fragment_5_cart_product_item_name);
        p_name.setText(product_constructer.getProduct_name());
        TextView p_price = view.findViewById(R.id.fragment_5_cart_product_item_price);
        p_price.setText("¥" + product_constructer.getProduct_price());

        return view;
    }
}
