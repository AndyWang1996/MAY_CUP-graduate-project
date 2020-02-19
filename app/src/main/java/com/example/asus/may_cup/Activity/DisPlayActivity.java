package com.example.asus.may_cup.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.asus.may_cup.R;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import Algorithm.VectorBuilder;
import Constructer.DataObject;
import Constructer.Vector_module;
import Constructer.raw_item;
import Tool.PermissionsChecker;
import client.TestClient;

public class DisPlayActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener,
        Fragment1.OnFragmentInteractionListener,
        Fragment2.OnFragmentInteractionListener,
        Fragment3.OnFragmentInteractionListener,
        Fragment4.OnFragmentInteractionListener,
        Fragment5.OnFragmentInteractionListener,
        Tool.ThreadCallback.CallBack{

    static final String[] Permissions = {"android.permission.WRITE_EXTERNAL_STORAGE",
                                         "android.permission.READ_EXTERNAL_STORAGE",
                                         "android.permission.INTERNET"};
    private ArrayList<Fragment> fragment_group;
    private Vector<Double> user_vector = new Vector<>();
    private List<raw_item> info_list;
    private List<raw_item> history_list = new ArrayList<>();
    TestClient tc = new TestClient();
    BottomNavigationBar bnb;
    Bundle bundle = new Bundle();
    Bundle web_B = new Bundle();
    VectorBuilder vB = new VectorBuilder();

    @SuppressLint("HandlerLeak")
    Handler dataH = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            String path = "mnt/ext_sdcard/Download/data.json";
            Log.i("initialize",path);
            File file = new File(path);

            try {
                FileReader fr = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fr);
                String json_data;
                try {
                    json_data = bufferedReader.readLine();
                    json_data = json_data.substring(9,json_data.length()-2);
                    PrintStream ps = new PrintStream(new FileOutputStream(file));
                    ps.print(json_data);
                    ps.close();
                    bufferedReader.close();
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Intent i = new Intent(DisPlayActivity.this,USER_face.class);
            startActivity(i);
        }
    };

    @Override
    public void ThreadStratListener() {

    }

    @Override
    public void ThreadFinishListener() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dis_play);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //尝试获取应用权限
        get_Permissions();

        //读取preference用户数据，并且检测是否为初次启动
        load_Userdatas();

        //加载商品列表
        load_Productlist();


        bnb = findViewById(R.id.navigation_button_group);

        bnb
                .setAnimationDuration(100)
                .setMode(BottomNavigationBar.MODE_SHIFTING)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE)
                .addItem(new BottomNavigationItem(R.mipmap.a_ic_recommend_24dp,R.string.Recommend)).setActiveColor(R.color.SlateBlue)
                .addItem(new BottomNavigationItem(R.mipmap.b_ic_home_white_24dp,R.string.Product))
                .addItem(new BottomNavigationItem(R.mipmap.c_ic_find_replace_white_24dp,R.string.Search))
                .addItem(new BottomNavigationItem(R.mipmap.e_ic_blogs_white_24dp,R.string.Blog))
                .addItem(new BottomNavigationItem(R.mipmap.d_ic_product_cart_24dp_white,R.string.Cart))
                .setFirstSelectedPosition(0)
                .initialise();

        fragment_group = fragments_init();
        bundle.putSerializable("fragments",fragment_group);
        setDefault_Fragment();
        bnb.setTabSelectedListener(this);


    }

    public void change_position(){
        BottomNavigationBar bnb = findViewById(R.id.navigation_button_group);
        bnb.setFirstSelectedPosition(3);
    }





    private void load_Userdatas() {
        Log.i("initialize","start");
        SharedPreferences sp = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        Boolean FLAG_IS_FIRST_RUN = sp.getBoolean("FLAG_IS_FIRST_RUN",true);
        if (FLAG_IS_FIRST_RUN){

            Log.i("initialize","first");

            String path = "mnt/ext_sdcard/Download/";
            File file = new File(path); //判断文件夹是否存在,如果不存在则创建文件夹
            if (!file.exists()) { file.mkdir(); Log.i("initialize","mkdir");}

            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("FLAG_IS_FIRST_RUN",false);
            editor.putString("USER_VECTOR","0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0");
            editor.commit();

            get_Server_data(null,3);
        }else{

            Log.i("initialize","no");

            String raw_user_data = sp.getString("USER_VECTOR","0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0");
            StringTokenizer st = new StringTokenizer(raw_user_data,"|");
            while (st.hasMoreElements()){
                user_vector.add(Double.parseDouble(st.nextToken()));
            }
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
                dataH.sendMessage(msg);
                Log.i("initialize","data.json set");
            }
        }.start();
    }

    private void get_Permissions() {
        PermissionsChecker permissionsChecker = new PermissionsChecker(this);
        if(permissionsChecker.lacksPermissions(Permissions)){
            ActivityCompat.requestPermissions(DisPlayActivity.this,Permissions,666);
            Log.e("permission_hhh","check");
        }
    }

    private void load_Productlist(){

        try {
            vB.JsonReader(user_vector);
            info_list = vB.getRanked_result();
            DataObject dataObject = new DataObject();
            Map<String, List<raw_item>> temp = new HashMap<>();
            temp.put("all",info_list);
            dataObject.setMap(temp);
            bundle.putSerializable("map",dataObject);
            getIntent().putExtra("data",bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDefault_Fragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,Fragment1.newInstance("F1","1"));
        fragmentTransaction.commit();
    }

    private ArrayList<Fragment> fragments_init(){
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(Fragment1.newInstance("F1","1"));
        fragments.add(Fragment2.newInstance("F2","2"));
        fragments.add(Fragment3.newInstance("F3","3"));
        fragments.add(Fragment4.newInstance("F4","4"));
        fragments.add(Fragment5.newInstance("F5","5"));
        return fragments;
    }

    @Override
    public void onTabSelected(int position) {
        Log.i("SELECTION", Integer.toString(fragment_group.size()));
        Log.i("SELECTION", Integer.toString(position));
        if (fragment_group != null){
            Log.i("SELECTION", "position1");
            if (position < fragment_group.size()){
                Log.i("SELECTION", "position2");
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment current = fm.findFragmentById(R.id.container);
                if (current != null){
                    ft.hide(current);
                }
                Fragment temp = fragment_group.get(position);
                if (temp.isAdded()){
                    Log.i("SELECTION", "position3");
                    ft.show(temp);
                }else{
                    Log.i("SELECTION", "position4");
                    ft.add(R.id.container, temp);
                }
                ft.commitAllowingStateLoss();
            }else{
            }
        }else{
        }
    }

    @Override
    public void onTabUnselected(int position) {
        if (fragment_group != null){
            if (position < fragment_group.size()){
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment temp = fragment_group.get(position);
                ft.remove(temp);
                ft.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) throws JSONException {
        String call = uri.toString();
        Vector<Double> newVector = new Vector<>();
        if (call.contains("HISTORY")){
            String name = call.replace("HISTORY","");
            Vector_module v = vB.getP().get(name);
            history_list.add(new raw_item(v.getKey(),v.get_url(), v.get_pic(), v.get_pri()));
            DataObject dataObject = new DataObject();
            Map<String, List<raw_item>> temp = new HashMap<>();
            temp.put("history1",history_list);
            dataObject.setMap(temp);
            bundle.putSerializable("history2",dataObject);
            getIntent().putExtra("history3",bundle);
        }

        if (call.contains("MODV")){
            StringBuilder stringBuilder = new StringBuilder();
            VectorBuilder vectorBuilder = new VectorBuilder();
            Vector<Double> v = vectorBuilder.get_Product_vector(null,call);
            if (v.size() == user_vector.size()){
                if (call.contains("MODV+")){
                    for (int i = 0; i<user_vector.size(); i++){
                        newVector.add(user_vector.get(i) + v.get(i)*0.05);
                    }
                }else {
                    for (int i = 0; i < user_vector.size(); i++) {
                        newVector.add(user_vector.get(i) - v.get(i) * 0.05);
                    }
                }
                user_vector = newVector;
                for (int i = 0; i<user_vector.size(); i++){
                    stringBuilder.append(user_vector.get(i));
                    stringBuilder.append("|");
                }
                stringBuilder.deleteCharAt(stringBuilder.length()-1);
                Log.i("USERVECTORCHANGED",stringBuilder.toString());
                SharedPreferences sp = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("USER_VECTOR",stringBuilder.toString());
                editor.commit();
            }
        }else{
            web_B.putString("URL",call);
            getIntent().putExtra("web",web_B);
            bnb.selectTab(3);
        }
    }
}
