package com.example.asus.may_cup.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.asus.may_cup.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import Adapters.DisplayList_adapter;
import Adapters.ProductAdapter;
import Constructer.DataObject;
import Constructer.Product_Constructer;
import Constructer.Vector_module;
import Constructer.raw_item;
import Tool.VImageUtils;
import client.TestClient;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment2 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View root;

    ImageView temp_view;
    private ListView list;
    Button Recommend;
    Button M;
    Button S;
    Button E;
    Button B;
    private Bundle bundle;
    DataObject temp;
    private List<raw_item> data;
    private List<Product_Constructer> adapter_data = new ArrayList<>();
    private List<Product_Constructer> sever_data = new ArrayList<>();
    TestClient tc = new TestClient();
    String download_path;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String path = data.getString("filepath");
            download_path = path;
            Log.i("PATH",path);

            sever_data.clear();
            Log.i("READ","start");
            try {
                FileReader fReader = null;
                try {
                    fReader = new FileReader(new File(download_path));
                    Log.i("ALGORITHM OK","ok");
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Reader reader = new InputStreamReader(new FileInputStream(new File(download_path)),"gbk");
                try {
                    int ch = 0;
                    StringBuffer sb = new StringBuffer();
                    while ((ch = reader.read()) != -1) {
                        sb.append((char) ch);
                    }
                    JSONObject jsonObject = new JSONObject(sb.toString());

//                    Log.i("TAG",jsonObject.getJSONArray("list").get(0).toString());
                    reader.close();
                    fReader.close();
                    for (int i = 0; i<11;i++){
                        JSONObject item = new JSONObject(jsonObject.getJSONArray("list").get(i).toString());
                        String imglink = item.getString("imagelink").replace("\\","");
                        String plink = item.getString("productlink").replace("\\","");
                        Log.i("TAG",plink);
                        VImageUtils.disPlay(temp_view, imglink);
                        Bitmap bp = loadBitmapFromView(temp_view);
                        Product_Constructer p = new Product_Constructer(bp,
                                item.getString("name"),
                                item.getString("price"),
                                plink);
                        sever_data.add(p);
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            refresh(sever_data);
        }
    };

    public static int ctr = 0;
    public static int end = 0;

    private void init(){
        Recommend = root.findViewById(R.id.left_list_button1);
        M = root.findViewById(R.id.left_list_button2);
        S = root.findViewById(R.id.left_list_button3);
        E = root.findViewById(R.id.left_list_button4);
        B = root.findViewById(R.id.left_list_button5);

        Recommend.setBackgroundResource(R.drawable.button);
        M.setBackgroundResource(R.drawable.button);
        E.setBackgroundResource(R.drawable.button);
        S.setBackgroundResource(R.drawable.button);
        B.setBackgroundResource(R.drawable.button);

        temp_view = root.findViewById(R.id.temp_view2);

        list = root.findViewById(R.id.right_list_view);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    mListener.onFragmentInteraction(Uri.parse(adapter_data.get(position).getProduct_name() + "HISTORY"));
                    mListener.onFragmentInteraction(Uri.parse(adapter_data.get(position).getProduct_name() + "MODV+"));
                    mListener.onFragmentInteraction(Uri.parse(adapter_data.get(position).getProduct_url()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        View.OnTouchListener T = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (v.getId()){
                    case R.id.left_list_button1:
                        switch (event.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                Recommend.setBackgroundResource(R.drawable.button_pressed);
                                M.setBackgroundResource(R.drawable.button);
                                S.setBackgroundResource(R.drawable.button);
                                E.setBackgroundResource(R.drawable.button);
                                B.setBackgroundResource(R.drawable.button);
                                break;
                            case MotionEvent.ACTION_UP:
//                                Recommend.setBackgroundResource(R.drawable.button);
                                break;
                        }
                        break;
                    case R.id.left_list_button2:
                        switch (event.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                M.setBackgroundResource(R.drawable.button_pressed);
                                Recommend.setBackgroundResource(R.drawable.button);
                                S.setBackgroundResource(R.drawable.button);
                                E.setBackgroundResource(R.drawable.button);
                                B.setBackgroundResource(R.drawable.button);
                                break;
                            case MotionEvent.ACTION_UP:
//                                M.setBackgroundResource(R.drawable.button);
                                break;
                        }
                        break;
                    case R.id.left_list_button3:
                        switch (event.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                S.setBackgroundResource(R.drawable.button_pressed);
                                Recommend.setBackgroundResource(R.drawable.button);
                                M.setBackgroundResource(R.drawable.button);
                                E.setBackgroundResource(R.drawable.button);
                                B.setBackgroundResource(R.drawable.button);
                                break;
                            case MotionEvent.ACTION_UP:
//                                S.setBackgroundResource(R.drawable.button);
                                break;
                        }
                        break;
                    case R.id.left_list_button4:
                        switch (event.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                E.setBackgroundResource(R.drawable.button_pressed);
                                Recommend.setBackgroundResource(R.drawable.button);
                                M.setBackgroundResource(R.drawable.button);
                                S.setBackgroundResource(R.drawable.button);
                                B.setBackgroundResource(R.drawable.button);
                                break;
                            case MotionEvent.ACTION_UP:
//                                E.setBackgroundResource(R.drawable.button);
                                break;
                        }
                        break;
                    case R.id.left_list_button5:
                        switch (event.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                B.setBackgroundResource(R.drawable.button_pressed);
                                Recommend.setBackgroundResource(R.drawable.button);
                                M.setBackgroundResource(R.drawable.button);
                                S.setBackgroundResource(R.drawable.button);
                                E.setBackgroundResource(R.drawable.button);
                                break;
                            case MotionEvent.ACTION_UP:
//                                B.setBackgroundResource(R.drawable.button);
                                break;
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        };

        View.OnClickListener C = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.left_list_button1:
//                        Toast.makeText(getActivity(),"!!!",Toast.LENGTH_LONG).show();
                        load_image();
                        break;
                    case R.id.left_list_button2:
//                        Toast.makeText(getActivity(),"!!!",Toast.LENGTH_LONG).show();
                        get_Server_data("彩妆",2);
                        break;
                    case R.id.left_list_button3:
//                        Toast.makeText(getActivity(),"!!!",Toast.LENGTH_LONG).show();
                        get_Server_data("护肤",2);
                        break;
                    case R.id.left_list_button4:
//                        Toast.makeText(getActivity(),"!!!",Toast.LENGTH_LONG).show();
                        get_Server_data("工具",2);
                        break;
                    case R.id.left_list_button5:
//                        Toast.makeText(getActivity(),"!!!",Toast.LENGTH_LONG).show();
                        get_Server_data("兰蔻",1);
                        break;
                    default:
                        break;
                }
            }
        };

        Recommend.setOnTouchListener(T);
        Recommend.setOnClickListener(C);
        M.setOnTouchListener(T);
        M.setOnClickListener(C);
        E.setOnTouchListener(T);
        E.setOnClickListener(C);
        S.setOnTouchListener(T);
        S.setOnClickListener(C);
        B.setOnTouchListener(T);
        B.setOnClickListener(C);

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

    private void get_Server_data(final String query,final int flag){
        new Thread(){

            @Override
            public void run(){
                String path = tc.getSocket(query,flag);
                Message msg = new Message();
                Bundle temp = new Bundle();
                temp.putString("filepath",path);
                msg.setData(temp);
                handler.sendMessage(msg);
            }
        }.start();
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

    private void refresh(List<Product_Constructer> l){
        DisplayList_adapter adapter = new DisplayList_adapter(getContext(),R.layout.fragment_2_3_item,l);
        list.setAdapter(adapter);
    }

    public Fragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment2 newInstance(String param1, String param2) {
        Fragment2 fragment = new Fragment2();
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
        root = inflater.inflate(R.layout.fragment_2_product, container, false);

        init();

        load_image();

        Log.i("DATACHECK",adapter_data.size()+"");

        refresh(adapter_data);

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
