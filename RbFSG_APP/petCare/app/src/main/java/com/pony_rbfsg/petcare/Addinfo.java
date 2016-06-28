package com.pony_rbfsg.petcare;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Random;

/**
 * Rb-FSG
 * this class may get massage from Set, then present an interface to show the whole things you could use it.
 * continue....
 */

public class Addinfo extends Activity {

    private GoogleApiClient client;
    SharedPreferences pref;
    SharedPreferences.Editor preEdt;
    Button skip;
    ImageButton reminderButton, hospitalButton, laptopButton;
    Random random = new Random();
    int r = 0;

    private void setInit(){


        //@set button
        skip = (Button) findViewById(R.id.skip);

        //@set ImageButton
        reminderButton = (ImageButton) findViewById( R.id.r_phone );
        hospitalButton = (ImageButton) findViewById( R.id.hosipital );
        laptopButton = (ImageButton) findViewById( R.id.laptop );

        //@set SharedPreferences => pref (read) , preEdt( write )
        pref = getSharedPreferences("PREF_DATA", MODE_PRIVATE);//MODE_PRIVATE :only讓目前得app讀寫
        preEdt = pref.edit();
    }


    protected void onCreate(Bundle savedInstanceState) {

        //initiation
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addinfo);

        setInit();
        setEventListener();
    }

    public void setEventListener() {

        //@skip to next page of Petinfo
        skip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent();
                intent1.setClass(Addinfo.this, Petinfo.class);
                startActivity(intent1);
            }
        });

        //@Open reminder
        reminderButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

  //              r = random.nextInt(3)+1;
    //            String value = Integer.toString(r);
      //          preEdt.putString( "pic", value );
        //        preEdt.commit();// commit() 會直接將異動結果寫入檔案，同步！

                Intent intent2 = new Intent();
                intent2.setClass(Addinfo.this, ReminderInfo.class);
                startActivity(intent2);
            }
        });

        //@Open MedicalDiary
        hospitalButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent3 = new Intent();
                intent3.setClass(Addinfo.this, MedicalDiary.class);
                startActivity(intent3);
            }
        });

        //@Open ImageDirary
        laptopButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent4 = new Intent();
                intent4.setClass(Addinfo.this, ImageDirary.class);
                startActivity(intent4);
            }
        });

    }
}

