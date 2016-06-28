package com.pony_rbfsg.petcare;

import android.widget.ImageButton;
import android.widget.ImageView;

/**
 *  lalala~
 */
public class ImageIndex {

    public ImageButton button;
    public ImageView imageView;
    private String type;

    public ImageIndex(){}

    public ImageIndex( ImageButton button_, String type_ )
    {
        this.button = button_;
        this.type = type_;
    }

    public void setImageView( ImageView view ){

        this.imageView = view;
    }

    public void setInit( ImageButton button_, String type_ )
    {
        this.button = button_;
        this.type = type_;
    }

    public String getType(){

        return type;
    }

    public ImageButton setImageButton(ImageButton button, String type ) {

        //@animal
        if (type.equals("cat")) {

            button.setImageResource(R.drawable.bighead_cat);
        } else {

            button.setImageResource(R.drawable.bighead_dog);
        }
        return button;
    }

    public ImageView setImageView( ImageView imageView, String type ) {

        //@sex
        if (type.equals("male")) {

            imageView.setImageResource(R.drawable.male);

        } else if (type.equals("female")) {

            imageView.setImageResource(R.drawable.female);
        } else {

            imageView.setImageResource(R.drawable.both_sex);
        }
        return imageView;
    }

    public ImageView setImageHead( ImageView imageView, String type ) {

        //@animal
        if (type.equals("cat")) {

            imageView.setImageResource(R.drawable.bighead_cat);
        } else {

            imageView.setImageResource(R.drawable.bighead_dog);
        }
        return imageView;
    }

}
