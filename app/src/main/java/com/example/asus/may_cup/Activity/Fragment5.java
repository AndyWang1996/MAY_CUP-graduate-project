package com.example.asus.may_cup.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.asus.may_cup.R;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import Adapters.ProductAdapter;
import Constructer.DataObject;
import Constructer.Product_Constructer;
import Constructer.raw_item;
import Tool.VImageUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment5.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment5#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment5 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ListView history_list;
    ImageView temp_view;
    private Bundle bundle;
    DataObject temp;
    private List<raw_item> data;
    private List<Product_Constructer> adapter_data = new ArrayList<>();

    public Fragment5() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment5.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment5 newInstance(String param1, String param2) {
        Fragment5 fragment = new Fragment5();
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
        View root = inflater.inflate(R.layout.fragment_5_cart, container, false);
        history_list = root.findViewById(R.id.HISTORY_LIST);
        temp_view  = root.findViewById(R.id.temp_view5);
        bundle = getActivity().getIntent().getBundleExtra("history3");
        temp = (DataObject) bundle.getSerializable("history2");
        data = temp.getMap().get("history1");

        history_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    mListener.onFragmentInteraction(Uri.parse(adapter_data.get(position).getProduct_url()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        load_image();

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

    private void refresh(List<Product_Constructer> l){
        ProductAdapter adapter = new ProductAdapter(getContext(),R.layout.fragment_5_cart_item,l);
        history_list.setAdapter(adapter);
    }

    private void load_image(){
        for(int i = 0; i<data.size(); i++){
            raw_item rawItem = data.get(i);
            VImageUtils.disPlay(temp_view, rawItem.getLink());
            Bitmap bp;
            bp = loadBitmapFromView(temp_view);
            adapter_data.add(new Product_Constructer(bp, rawItem.getName(), rawItem.getPrice(), rawItem.getUrl()));
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
}
