package com.example.asus.may_cup.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.may_cup.R;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Constructer.DataObject;
import Constructer.Product_Constructer;
import Constructer.raw_item;
import Tool.Get_Bitmap;
import Tool.MD5;
import Tool.VImageUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment1 extends Fragment implements Tool.ThreadCallback.CallBack{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Fragment1() {
        // Required empty public constructor
    }

    private static final String CACHE_PATH = "mnt/ext_sdcard/VImageUtils";

    private View root;

    private ImageButton left;
    private ImageButton right;
    private ImageButton user;
    public static int pointer = 0;
    public static int ctr = 0;
    public static int end = 0;

    private Bundle bundle;
    DataObject temp;
    private List<raw_item> data;
    private List<Product_Constructer> adapter_data = new ArrayList<>();

    ImageView temp_view;
    ImageView product_graph;
    TextView P_name;
    TextView P_price;
    Button like;
    Button ignore;


    private void init(){
        left = root.findViewById(R.id.left_button);
        right = root.findViewById(R.id.right_button);
        user = root.findViewById(R.id.user_graph);
        product_graph = root.findViewById(R.id.float_window_image);
        temp_view = root.findViewById(R.id.temp_view);
        P_name = root.findViewById(R.id.float_window_p_name);
        P_price = root.findViewById(R.id.float_window_p_price);
        like = root.findViewById(R.id.fragment_1_like);
        ignore = root.findViewById(R.id.fragment_1_dislike);

        View.OnClickListener C = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.right_button){
                    inflate_Recommend_item(1);
                }else if (v.getId() == R.id.left_button){
                    inflate_Recommend_item(-1);
                }else if (v.getId() == R.id.fragment_1_like){
                    Toast.makeText(getActivity(),"???",Toast.LENGTH_LONG).show();
                    try {
                        mListener.onFragmentInteraction(Uri.parse(adapter_data.get(pointer).getProduct_name() + "HISTORY"));
                        mListener.onFragmentInteraction(Uri.parse(adapter_data.get(pointer).getProduct_name() + "MODV+"));
                        mListener.onFragmentInteraction(Uri.parse(adapter_data.get(pointer).getProduct_url()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (v.getId() == R.id.fragment_1_dislike){
                    Toast.makeText(getActivity(),"!!!",Toast.LENGTH_LONG).show();
                    try {
                        mListener.onFragmentInteraction(Uri.parse(adapter_data.get(pointer).getProduct_name() + "MODV-"));
                        adapter_data.remove(pointer);
                        inflate_Recommend_item(1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        View.OnTouchListener T = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.left_button){
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            left.setImageResource(R.drawable.left_pressed);
                            break;
                        case MotionEvent.ACTION_UP:
                            left.setImageResource(R.drawable.left);
                            break;
                    }
                }else if (v.getId() == R.id.right_button){
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            right.setImageResource(R.drawable.right_pressed);
                            break;
                        case MotionEvent.ACTION_UP:
                            right.setImageResource(R.drawable.right);
                            break;
                    }
                }else if (v.getId() == R.id.fragment_1_like){
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            like.setBackgroundResource(R.drawable.button_pressed);
                            break;
                        case MotionEvent.ACTION_UP:
                            like.setBackgroundResource(R.drawable.button);
                            break;
                    }
                }else if (v.getId() == R.id.fragment_1_dislike){
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            ignore.setBackgroundResource(R.drawable.button_pressed);
                            break;
                        case MotionEvent.ACTION_UP:
                            ignore.setBackgroundResource(R.drawable.button);
                            break;
                    }
                }
                return false;
            }
        };

        left.setOnClickListener(C);
        right.setOnClickListener(C);
        left.setOnTouchListener(T);
        right.setOnTouchListener(T);
        like.setOnClickListener(C);
        like.setOnTouchListener(T);
        ignore.setOnClickListener(C);
        ignore.setOnTouchListener(T);

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), USER_face.class);
                startActivity(i);
            }
        });

        if (getBitmapFromLocal("user_face") != null){
            user.setImageBitmap(getBitmapFromLocal("user_face"));
        }
    }

    public Bitmap getBitmapFromLocal(String name) {
        String fileName = name;
        try {
            File file = new File(CACHE_PATH, fileName);

            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));

            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void load_image(){
        end += 11;
        bundle = getActivity().getIntent().getBundleExtra("data");
        temp = (DataObject) bundle.getSerializable("map");
        data = temp.getMap().get("all");
        while (ctr < end) {
            raw_item rawItem = data.get(ctr);
            VImageUtils.disPlay(temp_view, rawItem.getLink());
            Bitmap bp;
            bp = loadBitmapFromView(temp_view);
            adapter_data.add(new Product_Constructer(bp, rawItem.getName(), rawItem.getPrice(), rawItem.getUrl()));
            ctr++;
        }
    }


    private Bitmap loadBitmapFromView(View v) {
        int w = 640;
        int h = 640;
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */

        v.layout(0, 0, w, h);
        v.draw(c);

        return bmp;
    }

    @Override
    public void ThreadStratListener(){

    }

    @Override
    public void ThreadFinishListener(){

    }

    /**
     *
     * @param movement 只允许1，-1,0代表在list中pointer的移动方向
     */
    private void inflate_Recommend_item(int movement){
        Log.i("position",ctr+ "|" +end + "|" + pointer);
        if (pointer >= adapter_data.size()){
            load_image();
        }
        if (pointer + movement + 2 == adapter_data.size()){
            load_image();
            pointer += movement;
            Product_Constructer P = adapter_data.get(pointer);
            product_graph.setImageBitmap(P.product_image);
            P_price.setText(P.product_price);
            P_name.setText(P.product_name);
        }else if (pointer + movement < 0){
            Product_Constructer P = adapter_data.get(0);
            product_graph.setImageBitmap(P.product_image);
            P_price.setText(P.product_price);
            P_name.setText(P.product_name);
        }else {
            pointer += movement;
            Product_Constructer P = adapter_data.get(pointer);
            product_graph.setImageBitmap(P.product_image);
            P_price.setText(P.product_price);
            P_name.setText(P.product_name);
        }

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment1 newInstance(String param1, String param2) {
        Fragment1 fragment = new Fragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_1_recommend, container, false);

        init();

        load_image();

        inflate_Recommend_item(0);

        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            try {
                mListener.onFragmentInteraction(uri);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri) throws JSONException;
    }

}
