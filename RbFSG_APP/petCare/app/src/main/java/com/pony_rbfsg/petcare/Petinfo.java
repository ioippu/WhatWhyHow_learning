package com.pony_rbfsg.petcare;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Pony(Yin-Jie Wang)
 * Rb-FSG
 * this class may get the intent message then set to it interface.
 * It shows four function to using it.
 * 1. Reminder could remind the parent to deal with what they wanna do for their pets.
 * 2. diary for living
 * 3. medical diary is for pets records
 * 4. add+ is for anding the other pets you raise.
 */
public class Petinfo extends Activity {

    private int[] List = {
            R.drawable.reminder,
            R.drawable.diary,
            R.drawable.medical,
            R.drawable.add_info
    };

    ImageButton[] ListButtons = new ImageButton[4];

    ImageButton reminderButton, diaryButton, medicalButton, addInfo_Button;
    String animal, name, birthday, sex, path;
    SharedPreferences pref;
    ImageView sex_image, headImage;
    TextView text;

    private Uri ImagePath;
    Intent intent = new Intent();

    private void setInit() {

        //@initialization
        pref = getSharedPreferences("PREF_DATA", MODE_PRIVATE);

        //@ImageView
        sex_image = (ImageView) findViewById(R.id.sex_image);
        headImage = (ImageView) findViewById(R.id.big);

        //@TextView
        text = (TextView) findViewById(R.id.text);

        //@ImageButton
        reminderButton = (ImageButton) findViewById(R.id.reminder);
        diaryButton = (ImageButton) findViewById(R.id.diary);
        medicalButton = (ImageButton) findViewById(R.id.medical);
        addInfo_Button = (ImageButton) findViewById(R.id.add_info);

        //ImageButton List
        ListButtons[0] = reminderButton;
        ListButtons[1] = diaryButton;
        ListButtons[2] = medicalButton;
        ListButtons[3] = addInfo_Button;

    }

    public void getTransmit(){

        //@String
        animal = pref.getString("animal", " ");
        name = pref.getString("name", " ");
        birthday = pref.getString("birthday", " ");
        sex = pref.getString("sex", " ");
    }

    public void setButtonPic( int id_ ){

        for( int Id = 0; Id < ListButtons.length; Id++ ){

            if ( Id != id_ ) {

                ListButtons[Id].setImageResource( List[Id] );
            }else;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petinfo);

        //initialization
        this.setInit();

        //get transmit from last
        getTransmit();

        //Listener
        setEventListener();

        //info
        text.setText("毛星人名:"+name+"\n生日:  "+birthday+" \n");

        //sex = three type
        ImageIndex ImagePic = new ImageIndex();
        sex_image = ImagePic.setImageView( sex_image, sex );

        //set Pic photo
        path = pref.getString("Bighead_path","");
        if( path == "" ){

            headImage.setImageResource(R.drawable.frameq);
        }
        else{
            headImage.setImageURI(Uri.parse(path));
        }

    }

    public void setEventListener(){

        //@Open add new
        addInfo_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //maintenance
                setButtonPic( 3 );

                //@change
                addInfo_Button.setImageResource(R.drawable.add_info_dark);
                //intent.setClass(Petinfo.this, Petinfo.class);
                //startActivity(intent);
            }
        });

        //@Open reminder
        reminderButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //@maintenance
                setButtonPic( 0 );

                //@change
                reminderButton.setImageResource(R.drawable.reminder_dark);

                Intent intent1 = new Intent();
                intent1.setClass(Petinfo.this, ReminderInfo.class);
                startActivity(intent1);
            }
        });

        //@Open Medical Diary
        medicalButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //@maintenance
                setButtonPic( 2 );

                //@change
                medicalButton.setImageResource(R.drawable.medical_dark);

                Intent intent2 = new Intent();
                intent2.setClass(Petinfo.this, MedicalDiary.class);
                startActivity(intent2);
            }
        });

        //@Open Diary
        diaryButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //@maintenance
                setButtonPic( 1 );

                //@change
                diaryButton.setImageResource(R.drawable.diary_dark);

                Intent intent3 = new Intent();
                intent3.setClass(Petinfo.this, ImageDirary.class);
                startActivity(intent3);
            }
        });
    }
}

