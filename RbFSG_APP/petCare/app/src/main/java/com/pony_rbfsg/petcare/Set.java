package com.pony_rbfsg.petcare;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
//在 Android 平台上一個 SharedPreferences 物件會對應到一個檔案，這個檔案中儲存 key/value 的對應資料，而 SharedPreferences 物件提供了一些對應的方法來讀寫這些資料。
//修改 SharedPreferences 裡面的資料 (將資料存入或刪除) 必須透過 SharedPreferences.Editor 物件來達成

/**
 * Created by Pony(Yin-Jie Wang)
 * Rb-FSG
 * this class may get massage from mainActivity, then send the intent to Addinfo
 * this interface is created to seting the information about our pets.
 */

public class Set extends Activity {

    //declare member variable
    private final static int PICK_IMAGE_REQUEST = 1;
    String animal, path;
    EditText name, birthday;
    SharedPreferences pref;
    SharedPreferences.Editor preEdt;
    ImageButton bigHead, saveButton, maleButton, femaleButton, dualButton;
    ImageIndex ImagePic = new ImageIndex();
    Intent intent = new Intent();
    private Uri ImagePath;


    private void setInit(){

        //@set EditText
        name = (EditText)findViewById(R.id.name);
        birthday = (EditText)findViewById(R.id.birthday);

        //@set ImageButton
        bigHead = (ImageButton) findViewById(R.id.big_head);
        saveButton = (ImageButton) findViewById(R.id.save);
        maleButton = (ImageButton) findViewById(R.id.male);
        femaleButton = (ImageButton) findViewById(R.id.female);
        dualButton = (ImageButton) findViewById(R.id.both_sex);

        //@set SharedPreferences => pref (read) , preEdt( write )
        pref = getSharedPreferences("PREF_DATA", MODE_PRIVATE);//MODE_PRIVATE :only讓目前得app讀寫
        preEdt = pref.edit();
    }

    //write
    public void writeData( String key, String value ){

        preEdt.putString( key, value );
        preEdt.commit();// commit() 會直接將異動結果寫入檔案，同步！
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        setInit();

        //@listener
        setEventListener();

        //set the big head pic --> @pass parameter( key/value )
        Bundle bundle = this.getIntent().getExtras(); //@intent傳來的bundle
        animal = bundle.getString("Animal");//key = "Animal" -> value?

        //@put key and value to preEdt
        writeData( "animal", animal );

        //bigHead = ImagePic.setImageButton( bigHead , animal );
    }


        //@set bigHead pic by animal
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        //藉由requestCode判斷是否為開啟相機或開啟相簿而呼叫的，且data不為null
        if ( requestCode == PICK_IMAGE_REQUEST && data != null)
        {
            ImagePath = data.getData();//get照片路徑uri
            writeData( "Bighead_path", ImagePath.toString() );

            //set Pic photo
            path = pref.getString("Bighead_path","");
            if( path == "" ){

                bigHead.setImageResource(R.drawable.frameq);
            }
            else{
                bigHead.setImageURI(Uri.parse(path));
            }
        }
        else;
    }

    public void setEventListener(){

          //bigHead.setOnClickListener(bigHead_Linsener);
          bigHead.setOnClickListener(new View.OnClickListener() {

              @Override
              public void onClick(View v) {

                  intent.setType("image/*");
                  intent.setAction(Intent.ACTION_GET_CONTENT);//使用Intent的ACTION_GET_CONTENT，系統就會幫使用者找到裝置內合適的App來取得指定MIME類型的內容
                  startActivityForResult(intent, PICK_IMAGE_REQUEST);
                  //使用startActivityForResult => pass parameter => 必須複寫Activity的onActivityResult函式才能有作用
            }
          });

          maleButton.setOnClickListener(new View.OnClickListener() {

              @Override
              public void onClick(View v) {

                  maleButton.setImageResource(R.drawable.male_dark);
                  writeData( "sex", "male");
              }
          });

          femaleButton.setOnClickListener( new View.OnClickListener() {

              @Override
              public void onClick(View v) {

                  femaleButton.setImageResource(R.drawable.female_dark);
                  writeData( "sex", "female" );
              }
          });

          dualButton.setOnClickListener(new View.OnClickListener() {

              @Override
              public void onClick(View v) {

                  dualButton.setImageResource(R.drawable.both_sex_dark);
                  writeData( "sex", "both_sex");
              }
          });

          saveButton.setOnClickListener( new View.OnClickListener() {

              @Override
              public void onClick(View v) {

                  writeData( "name", name.getText().toString() );
                  writeData( "birthday", birthday.getText().toString() );

                  intent.setClass(Set.this, Addinfo.class);//@@
                  startActivity(intent);
              }
          });
    }
}
