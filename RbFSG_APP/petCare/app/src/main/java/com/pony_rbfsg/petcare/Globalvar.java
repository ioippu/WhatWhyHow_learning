package com.pony_rbfsg.petcare;

import android.app.Application;

/**
 為了判斷目前有沒有新的照片,使用全域變數capture來做紀錄,有新的照片才會定位在地圖上
 */
public class Globalvar extends Application {

    private int capture=0;
    private int pic_num=0;
    private int touch=0;

    public int getCapture() {

        return capture;
    }

    public void setCapture(int data) {

        this.capture = data;
    }

    public int getpicnum() {

        return pic_num;
    }

    public void addpicnum() {

        this.pic_num = pic_num+1;
    }

    public void addtouch() {

        if(touch<3){
            this.touch = touch+1;
        }
        else{
            this.touch = 0;
        }
        return;
    }

    public int gettouch() {

        return touch;
    }
}
