package com.pony_rbfsg.petcare;

/**
 * Rb-FSG
 * Created by Pony(Yin-Jie Wang) on 2016/6/14.
 * this class is use to show the clould database then showing the image uploded.
 */
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

//#android WebView 可用於瀏灠網頁的 View
//#WebSettings 是用來設定 WebView 屬性的類別

public class Display extends Activity {

    //Bitnami LAMP sever
    String myURL = "http://52.163.210.37/o.php";
    WebView webView;
    WebSettings websettings;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        webView=(WebView)findViewById(R.id.webView);

        //set websetting
        websettings = webView.getSettings();
        websettings.setSupportZoom(true);
        websettings.setBuiltInZoomControls(true);
        websettings.setJavaScriptEnabled(true);

        //webView is created by auto --> using client
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(myURL);//調用web並close this activity
    }
}
