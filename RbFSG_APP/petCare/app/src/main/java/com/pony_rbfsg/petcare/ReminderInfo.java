package com.pony_rbfsg.petcare;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;

/**
 *by Pony(Yin-Jie Wang)on 16/6/21.
 */
public class ReminderInfo extends Activity {

    SharedPreferences pref;
    ImageView panpan;
    String extra;

    //preEdt.putString( "pic", r );

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminderinfo);
/*
        //@ImageView
        panpan = (ImageView) findViewById(R.id.pp);
        Bundle bundle = this.getIntent().getExtras(); //@intent傳來的bundle
        extra = bundle.getString("pic");//key = "Animal" -> value?
        int r = Integer.parseInt(extra);
        System.out.println(r);

        switch(3){
            case 1:
                panpan.setImageResource(R.drawable.p1);
            case 2:
                panpan.setImageResource(R.drawable.p2);
            case 3:
                panpan.setImageResource(R.drawable.p3);
            case 4:
                panpan.setImageResource(R.drawable.p4);
            default:

        }
*/    }
}
