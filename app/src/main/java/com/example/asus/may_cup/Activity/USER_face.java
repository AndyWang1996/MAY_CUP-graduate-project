package com.example.asus.may_cup.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.asus.may_cup.R;

import java.io.File;
import java.io.FileOutputStream;

import Tool.Imagebuilder;
import Tool.MD5;

public class USER_face extends AppCompatActivity {

    public int[] Face_Array = new int[8];
    public int[] Item_ID_Array = new int[8];
    private static final String CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/VImageUtils";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_face);

        final ImageView USER_FACE = findViewById(R.id.USER_FACE);
        final Bitmap BOTTOM_LAYER = ((BitmapDrawable)USER_FACE.getDrawable()).getBitmap();

        for(int i = 0; i < 8; i++){
            Face_Array[i] = 0;
            Item_ID_Array[i] = R.drawable.default_empty;
        }

        final SeekBar face = findViewById(R.id.seekBar_face);
        final SeekBar eyebrows = findViewById(R.id.seekBar_eyebrows);
        final SeekBar fronthair = findViewById(R.id.seekBar_front_hair);
        final SeekBar rearhair = findViewById(R.id.seekBar_rear_hair);
        final SeekBar nose = findViewById(R.id.seekBar_nose);
        final SeekBar mouth = findViewById(R.id.seekBar_mouth);
        final SeekBar eyes = findViewById(R.id.seekBar_eyes);
        final SeekBar eyeball = findViewById(R.id.seekBar_eyeball);

        Button skip = findViewById(R.id.skip);
        Button next = findViewById(R.id.confirm);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder();
                sb.append(face.getProgress());
                sb.append("|0|");
                sb.append(eyebrows.getProgress());
                sb.append("|0|");
                sb.append(fronthair.getProgress());
                sb.append("|0|");
                sb.append(rearhair.getProgress());
                sb.append("|0|");
                sb.append(nose.getProgress());
                sb.append("|0|");
                sb.append(mouth.getProgress());
                sb.append("|0|");
                sb.append(eyes.getProgress());
                sb.append("|0|");
                sb.append(eyeball.getProgress());
                sb.append("|0|");
                sb.append("0|");
                sb.append("0|");
                sb.append("0|");
                sb.append("0|");
                sb.append("0");
                SharedPreferences sp = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("USER_VECTOR",sb.toString());
                editor.commit();
                Intent I = new Intent(USER_face.this,DisPlayActivity.class);
                startActivity(I);
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder();
                sb.append("0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0");
                SharedPreferences sp = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("USER_VECTOR",sb.toString());
                editor.commit();
                Intent I = new Intent(USER_face.this,DisPlayActivity.class);
                startActivity(I);
            }
        });

        SeekBar.OnSeekBarChangeListener Face_Change_Listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar.getId() == R.id.seekBar_face){
                    Face_Array[0] = seekBar.getProgress();
                    Log.i("face",Integer.toString(seekBar.getProgress()));
                }
                else if (seekBar.getId() == R.id.seekBar_eyebrows){
                    Face_Array[1] = seekBar.getProgress();
                    Log.i("eyebrows",Integer.toString(seekBar.getProgress()));
                }
                else if (seekBar.getId() == R.id.seekBar_front_hair){
                    Face_Array[2] = seekBar.getProgress();
                    Log.i("front_hair",Integer.toString(seekBar.getProgress()));
                }
                else if (seekBar.getId() == R.id.seekBar_rear_hair){
                    Face_Array[3] = seekBar.getProgress();
                    Log.i("rear_hair",Integer.toString(seekBar.getProgress()));
                }
                else if (seekBar.getId() == R.id.seekBar_nose){
                    Face_Array[4] = seekBar.getProgress();
                    Log.i("nose",Integer.toString(seekBar.getProgress()));
                }
                else if (seekBar.getId() == R.id.seekBar_mouth){
                    Face_Array[5] = seekBar.getProgress();
                    Log.i("mouth",Integer.toString(seekBar.getProgress()));
                }
                else if (seekBar.getId() == R.id.seekBar_eyes){
                    Face_Array[6] = seekBar.getProgress();
                    Log.i("eyes",Integer.toString(seekBar.getProgress()));
                }
                else if (seekBar.getId() == R.id.seekBar_eyeball){
                    Face_Array[7] = seekBar.getProgress();
                    Log.i("eyeball",Integer.toString(seekBar.getProgress()));
                }
                else {
                    Log.e("ERROR","UNKNOWN ERROR");
                }
                ID_ARRAY_Handler(Face_Array,BOTTOM_LAYER,USER_FACE);
            }
        };

        face.setOnSeekBarChangeListener(Face_Change_Listener);
        eyebrows.setOnSeekBarChangeListener(Face_Change_Listener);
        fronthair.setOnSeekBarChangeListener(Face_Change_Listener);
        rearhair.setOnSeekBarChangeListener(Face_Change_Listener);
        nose.setOnSeekBarChangeListener(Face_Change_Listener);
        mouth.setOnSeekBarChangeListener(Face_Change_Listener);
        eyes.setOnSeekBarChangeListener(Face_Change_Listener);
        eyeball.setOnSeekBarChangeListener(Face_Change_Listener);


    }

    private void ID_ARRAY_Handler(int[] selection_array,Bitmap Bottom,ImageView USER_FACE){

        switch (selection_array[0]){
            case 0:
                Item_ID_Array[0] = R.drawable.default_empty;
                break;
            case 1:
                Item_ID_Array[0] = R.drawable.fg_face_p01_c1_m001;
                break;
            case 2:
                Item_ID_Array[0] = R.drawable.fg_face_p02_c1_m001;
                break;
            case 3:
                Item_ID_Array[0] = R.drawable.fg_face_p03_c1_m001;
                break;
            case 4:
                Item_ID_Array[0] = R.drawable.fg_face_p04_c1_m001;
                break;
            case 5:
                Item_ID_Array[0] = R.drawable.fg_face_p05_c1_m001;
                break;
            case 6:
                Item_ID_Array[0] = R.drawable.fg_face_p06_c1_m001;
                break;
            case 7:
                Item_ID_Array[0] = R.drawable.fg_face_p07_c1_m001;
                break;
            default:
                Log.e("Check1","default");
                Item_ID_Array[0] = R.drawable.default_empty;
                break;
        }

        switch (selection_array[1]){
            case 0:
                Item_ID_Array[1] = R.drawable.default_empty;
                break;
            case 1:
                Item_ID_Array[1] = R.drawable.fg_eyebrows_p01_c1_m003;
                break;
            case 2:
                Item_ID_Array[1] = R.drawable.fg_eyebrows_p01_c2_m001;
                break;
            case 3:
                Item_ID_Array[1] = R.drawable.fg_eyebrows_p02_c1_m003;
                break;
            case 4:
                Item_ID_Array[1] = R.drawable.fg_eyebrows_p02_c2_m001;
                break;
            case 5:
                Item_ID_Array[1] = R.drawable.fg_eyebrows_p03_c1_m003;
                break;
            case 6:
                Item_ID_Array[1] = R.drawable.fg_eyebrows_p03_c2_m001;
                break;
            case 7:
                Item_ID_Array[1] = R.drawable.fg_eyebrows_p04_c1_m003;
                break;
            default:
                Log.e("Check2","default");
                Item_ID_Array[1] = R.drawable.default_empty;
        }

        switch (selection_array[2]){
            case 0:
                Item_ID_Array[2] = R.drawable.default_empty;
                break;
            case 1:
                Item_ID_Array[2] = R.drawable.fg_fronthair_p01_c1_m003;
                break;
            case 2:
                Item_ID_Array[2] = R.drawable.fg_fronthair_p02_c1_m003;
                break;
            case 3:
                Item_ID_Array[2] = R.drawable.fg_fronthair_p03_c1_m003;
                break;
            case 4:
                Item_ID_Array[2] = R.drawable.fg_fronthair_p04_c1_m003;
                break;
            case 5:
                Item_ID_Array[2] = R.drawable.fg_fronthair_p05_c1_m003;
                break;
            case 6:
                Item_ID_Array[2] = R.drawable.fg_fronthair_p06_c1_m003;
                break;
            case 7:
                Item_ID_Array[2] = R.drawable.fg_fronthair_p07_c1_m003;
                break;
            default:
                Log.e("Check3","default");
                Item_ID_Array[2] = R.drawable.default_empty;
        }

        switch (selection_array[3]){
            case 0:
                Item_ID_Array[3] = R.drawable.default_empty;
                break;
            case 1:
                Item_ID_Array[3] = R.drawable.fg_rearhair1_p01_c1_m003;
                break;
            case 2:
                Item_ID_Array[3] = R.drawable.fg_rearhair1_p02_c1_m003;
                break;
            case 3:
                Item_ID_Array[3] = R.drawable.fg_rearhair1_p03_c1_m003;
                break;
            case 4:
                Item_ID_Array[3] = R.drawable.fg_rearhair1_p04_c1_m003;
                break;
            case 5:
                Item_ID_Array[3] = R.drawable.fg_rearhair1_p05_c1_m003;
                break;
            case 6:
                Item_ID_Array[3] = R.drawable.fg_rearhair1_p06_c1_m003;
                break;
            case 7:
                Item_ID_Array[3] = R.drawable.fg_rearhair1_p07_c1_m003;
                break;
            default:
                Log.e("Check4","default");
                Item_ID_Array[3] = R.drawable.default_empty;
                break;
        }

        switch (selection_array[4]){
            case 0:
                Item_ID_Array[4] = R.drawable.default_empty;
                break;
            case 1:
                Item_ID_Array[4] = R.drawable.fg_nose_p01_c1_m001;
                break;
            case 2:
                Item_ID_Array[4] = R.drawable.fg_nose_p02_c1_m001;
                break;
            case 3:
                Item_ID_Array[4] = R.drawable.fg_nose_p03_c1_m001;
                break;
            case 4:
                Item_ID_Array[4] = R.drawable.fg_nose_p04_c1_m001;
                break;
            case 5:
                Item_ID_Array[4] = R.drawable.fg_nose_p05_c1_m001;
                break;
            case 6:
                Item_ID_Array[4] = R.drawable.fg_nose_p06_c1_m001;
                break;
            case 7:
                Item_ID_Array[4] = R.drawable.fg_nose_p07_c1_m001;
                break;
            default:
                Log.e("Check5","default");
                Item_ID_Array[4] = R.drawable.default_empty;
                break;
        }

        switch (selection_array[5]){
            case 0:
                Item_ID_Array[5] = R.drawable.default_empty;
                break;
            case 1:
                Item_ID_Array[5] = R.drawable.fg_mouth_p01_c1_m001;
                break;
            case 2:
                Item_ID_Array[5] = R.drawable.fg_mouth_p02_c1_m001;
                break;
            case 3:
                Item_ID_Array[5] = R.drawable.fg_mouth_p03_c1_m001;
                break;
            case 4:
                Item_ID_Array[5] = R.drawable.fg_mouth_p04_c1_m001;
                break;
            case 5:
                Item_ID_Array[5] = R.drawable.fg_mouth_p05_c1_m001;
                break;
            case 6:
                Item_ID_Array[5] = R.drawable.fg_mouth_p06_c1_m001;
                break;
            case 7:
                Item_ID_Array[5] = R.drawable.fg_mouth_p07_c1_m001;
                break;
            default:
                Log.e("Check6","default");
                Item_ID_Array[5] = R.drawable.default_empty;
                break;
        }

        switch (selection_array[6]){
            case 0:
                Item_ID_Array[6] = R.drawable.default_empty;
                break;
            case 1:
                Item_ID_Array[6] = R.drawable.fg_eyes_p01_c2;
                break;
            case 2:
                Item_ID_Array[6] = R.drawable.fg_eyes_p02_c2;
                break;
            case 3:
                Item_ID_Array[6] = R.drawable.fg_eyes_p03_c2;
                break;
            case 4:
                Item_ID_Array[6] = R.drawable.fg_eyes_p04_c2;
                break;
            case 5:
                Item_ID_Array[6] = R.drawable.fg_eyes_p05_c2;
                break;
            case 6:
                Item_ID_Array[6] = R.drawable.fg_eyes_p06_c2;
                break;
            case 7:
                Item_ID_Array[6] = R.drawable.fg_eyes_p07_c2;
                break;
            default:
                Log.e("Check7","default");
                Item_ID_Array[6] = R.drawable.default_empty;
                break;
        }

        switch (selection_array[7]){
            case 0:
                Item_ID_Array[7] = R.drawable.default_empty;
                break;
            case 1:
                Item_ID_Array[7] = R.drawable.fg_eyes_p01_c1_m002;
                break;
            case 2:
                Item_ID_Array[7] = R.drawable.fg_eyes_p02_c1_m002;
                break;
            case 3:
                Item_ID_Array[7] = R.drawable.fg_eyes_p03_c1_m002;
                break;
            case 4:
                Item_ID_Array[7] = R.drawable.fg_eyes_p04_c1_m002;
                break;
            case 5:
                Item_ID_Array[7] = R.drawable.fg_eyes_p05_c1_m002;
                break;
            case 6:
                Item_ID_Array[7] = R.drawable.fg_eyes_p06_c1_m002;
                break;
            case 7:
                Item_ID_Array[7] = R.drawable.fg_eyes_p07_c1_m002;
                break;
            default:
                Log.e("Check8","default");
                Item_ID_Array[7] = R.drawable.default_empty;
                break;
        }

        RefreshImage(Item_ID_Array,Bottom,USER_FACE);

    }

    private void RefreshImage(int[] ID_Array,Bitmap BOTTOM_LAYER,ImageView USER_FACE){

        Bitmap tempLayer = BOTTOM_LAYER;

        for(int i = 0; i<8; i++){
            tempLayer = Imagebuilder.bitmapBuilder(tempLayer, BitmapFactory.decodeResource(this.getResources(),ID_Array[i]));
        }

        tempLayer = Imagebuilder.bitmapBuilder(tempLayer,BitmapFactory.decodeResource(this.getResources(),R.drawable.fg_ears_p01_c1_m001));
        USER_FACE.setImageBitmap(tempLayer);

        setBitmapToLocal("user_face",tempLayer);
    }

    public void setBitmapToLocal(String name, Bitmap bitmap) {
        try {
            String fileName = name;
            File file = new File(CACHE_PATH, fileName);

            //通过得到文件的父文件,判断父文件是否存在
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            //把图片保存至本地
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
