package com.example.asus.may_cup.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.asus.may_cup.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import Adapters.DisplayList_adapter;
import Adapters.ProductAdapter;
import Constructer.Product_Constructer;
import Tool.VImageUtils;
import client.TestClient;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment3.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment3 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View root;
    SearchView sv;
    ListView slist;
    ImageView temp_view;
    private List<Product_Constructer> sever_data = new ArrayList<>();
    String download_path;
    TestClient tc = new TestClient();

    private OnFragmentInteractionListener mListener;

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

    public Fragment3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment3.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment3 newInstance(String param1, String param2) {
        Fragment3 fragment = new Fragment3();
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
        root = inflater.inflate(R.layout.fragment_3_search, container, false);

        sv = root.findViewById(R.id.search_bar_3);
        slist = root.findViewById(R.id.search_view_list_3);
        temp_view = root.findViewById(R.id.temp_view3);

        slist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    mListener.onFragmentInteraction(Uri.parse(sever_data.get(position).getProduct_name() + "HISTORY"));
                    mListener.onFragmentInteraction(Uri.parse(sever_data.get(position).getProduct_name() + "MODV+"));
                    mListener.onFragmentInteraction(Uri.parse(sever_data.get(position).getProduct_url()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i("SEARCH",query);
                get_Server_data(query,1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("SEARCH",newText);
                get_Server_data(newText,1);
                return false;
            }
        });

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
        ProductAdapter adapter = new ProductAdapter(getContext(),R.layout.fragment_5_cart_item,l);
        slist.setAdapter(adapter);
    }
}
