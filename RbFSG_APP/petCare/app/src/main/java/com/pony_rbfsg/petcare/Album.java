package com.pony_rbfsg.petcare;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;//做出列表般的效果,單純只想把一類項目列出來
import android.widget.GridView;
import android.widget.ImageView;
import java.io.File;//File info

/**
 * Created by Pony(Yin-Jie Wang)on 16/6/21.
 */

public class Album extends Activity {

    SharedPreferences pref;
    SharedPreferences.Editor preEdt;
    private String sd_Path;
    private String [] imgFiles ;
    private GridView gridView;


    private void setInit() {

        //set read and write instance
        pref = getSharedPreferences("PREF_DATA", MODE_PRIVATE);
        preEdt = pref.edit();

        //set gridView
        gridView = (GridView) findViewById(R.id.gridView1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.album);

        //設定列數
        gridView.setNumColumns(1);

        ////////////////////////////////相片來源,先顯示6張(read)
        imgFiles = new String[pref.getInt("photo_index",0)];

        File sdroot = Environment.getExternalStorageDirectory();//from SD card

        String sd_Path = sdroot.getPath();//get File(director path)

        for (int i = 0; i < imgFiles.length; i++) {

            imgFiles[i] = sd_Path+"/myapp/"+i+ ".jpg";
        }

        //要放置列表的事件中執行setAdapter。
        gridView.setAdapter(new ImageAdapter(this, imgFiles));
    }

    public class ImageAdapter extends ArrayAdapter<String> {

        private Context mCtx;

        public ImageAdapter(Context c, String[] imgFiles) {

            super(c, 0, imgFiles);
            mCtx = c;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            String filepath = getItem(position);
            Drawable drawable = Drawable.createFromPath(filepath);

            ImageView iv = new ImageView(mCtx);
            if (drawable == null) {
                iv.setImageResource(R.drawable.footprint);
            } else {
                iv.setImageDrawable(drawable);
            }
            iv.setAdjustViewBounds(true);
            return iv;
        }
    }
}
