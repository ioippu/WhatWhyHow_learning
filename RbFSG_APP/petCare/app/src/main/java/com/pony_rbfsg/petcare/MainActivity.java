package com.pony_rbfsg.petcare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Pony(Yin-Jie Wang)
 * Rb-FSG
 * this class is mainActivity, then send the intent from this to Set.
 * the interface is created for user to choose which type of pets they have it( dog or cat ... )
 */

public class MainActivity extends Activity {

    ImageButton cat, dog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dog = (ImageButton) findViewById(R.id.dog);
        cat = (ImageButton) findViewById(R.id.cat);

        dog.setOnClickListener(dogListener);
        cat.setOnClickListener(catListener);

    }

    private View.OnClickListener dogListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            dog.setImageResource(R.drawable.dog_dark);
            transmit( "dog" );
        }
    };

    private View.OnClickListener catListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            cat.setImageResource(R.drawable.cat_dark);
            transmit( "cat" );
        }
    };

    public void transmit( String animal_ ){

        //intent 1)用來跳轉Activity，從 A class 到 B class，2)在兩個Activity間傳遞參數。
        //intent + Bundle --> from MainActivity to Set
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, Set.class);

        //new一個Bundle物件，並將要傳遞的資料傳入
        Bundle bundle = new Bundle();
        bundle.putString( "Animal", animal_ );

        //將Bundle物件assign給intent
        intent.putExtras(bundle);

        //開始跳往要去的Activity
        startActivity(intent);
    }
}
