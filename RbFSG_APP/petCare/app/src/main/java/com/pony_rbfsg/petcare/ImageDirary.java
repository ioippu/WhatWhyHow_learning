package com.pony_rbfsg.petcare;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ImageDirary extends Activity {

    //declare
    SharedPreferences pref;
    SharedPreferences.Editor preEdt;
    private MyLocationListener mll;
    private LocationManager mgr;
    private String best;
    private TextView text;
    private GeoPoint startPoint;
    private MapView map;

    //for uploading photos ================================================================
    public static final String UPLOAD_URL = "http://52.163.210.37/upload.php";
    public static final String UPLOAD_KEY = "image";
    public static final String TAG = "MY MESSAGE";
    private int PICK_IMAGE_REQUEST = 1;

    //end for uplaoding photos ==================================================================
    //for touch
    ImageButton uploadCloudFileButton, showCloudFileButton, cameraButton;
    private Bitmap bitmap;
    private Uri filePath;

    private void setInit(){

        pref = getSharedPreferences("PREF_DATA", MODE_PRIVATE);
        preEdt = pref.edit();

        //set ImageButton
        cameraButton = (ImageButton) findViewById(R.id.camera);
        uploadCloudFileButton = (ImageButton) findViewById(R.id.cloud);
        showCloudFileButton = (ImageButton) findViewById(R.id.choosefile);

        //開啟gps
        mgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        mll = new MyLocationListener();

    }

    public void setButtonEventListener(){

        //choose
        showCloudFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ImageDirary.this, Display.class);
                startActivity(intent);
            }
        });

        //choose image,then upload to cloud database
        uploadCloudFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("QAQ", "ygyvv");
                showFileChooser();//then uplod to cloud database
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() { //按下相機圖事後,開啟TakePic activity
            @Override
            public void onClick(View v) {

                //Intent cameraIntent = new Intent();
                //cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                int photo_index = pref.getInt("photo_index",0);
                preEdt.putInt("photo_index",photo_index+1);
                preEdt.commit();

                String strImage = Environment.getExternalStorageDirectory().getAbsolutePath()+"/myapp";
                File myImage = new File(strImage);
                if(!myImage.exists())  myImage.mkdir();

                strImage = Environment.getExternalStorageDirectory().getAbsolutePath()+"/myapp/"+Integer.toString(photo_index)+".jpg";
                myImage = new File(strImage);
                Uri uriMyImage = Uri.fromFile(myImage);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriMyImage);
                startActivityForResult(intent, 3);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagediary);

        this.setInit();
        this.setButtonEventListener();


        //osm基本設定
        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        startPoint = new GeoPoint(24.60, 120.84);//因為DEMO時,室// 內抓不到GPS位置,所以相簿已經預設在新竹囉
        IMapController mapController = map.getController();

        mapController.setZoom(8);//放大8倍
        mapController.setCenter(startPoint);//將畫面中心設定為startPoint

        ////////////////////////////DEMO用的相簿定位,因為室內抓不到GPS,無法動態生成相簿><
        //加Marker在地圖上
        Marker startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(startMarker);
        map.invalidate();
        startMarker.setIcon(getResources().getDrawable(R.drawable.footprint));

        //設定marker內的文字資料為系統年月日
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy" + "\n" + "MM/dd");
        Date curDate = new Date(System.currentTimeMillis()); // 獲取當前時間
        String str = formatter.format(curDate);
        startMarker.setTitle(str);

        //按下Maker後的相片內容(為最後一張相片路徑)
        try {

            File sdroot = Environment.getExternalStorageDirectory();
            File file = new File(sdroot, ((Globalvar) getApplicationContext()).getpicnum()-1 + ".jpg");
            FileInputStream fis = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            Drawable drawable = new BitmapDrawable(bitmap);
            fis.close();
            startMarker.setImage(drawable);
        } catch (Exception e) {
        }

        startMarker.setOnMarkerClickListener(click); //按下Marker會做的事情  第一下開啟infomation  第二下 開啟相簿 第三下關閉infomation
        ((Globalvar) getApplicationContext()).setCapture(0);//設定完相片後即沒有新的照片
        /////////////////////////////////////////////////////////////////////////////////////////////////////////
    }


    @Override
    protected void onResume () {
        super.onResume();

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        best = mgr.getBestProvider(criteria, true);//find provider

        if (best != null) {//have no provider

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //request present or upadated location
            mgr.requestLocationUpdates(best, 1000, 1, mll);

        } else//have provider
        {
            //request present or upadated location
            mgr.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 1000, 1, mll);
        }
    }

    @Override
    protected void onPause () {//leave
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mgr.removeUpdates(mll);
    }


    //upload photo =================================================================================
    private void showFileChooser() {

        Log.i("QAQ", "ygyvv");
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==3){

        }
        else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                uploadImage();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;//bmp to string , prepare to upload by string foromat
    }

    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show( ImageDirary.this, "Uploading Image", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();
                data.put(UPLOAD_KEY, uploadImage);

                String result = rh.sendPostRequest(UPLOAD_URL,data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }
    //upload photo =================================================================================

    class MyLocationListener implements LocationListener {//只有在目前有新照片並且有抓到GPS位置的情況下,才會在畫面上定位新的相簿,但目前只做到只能做出一個相簿,多個相簿目前難產,就當DEMO用吧!
        @Override
        public void onLocationChanged(Location location) {
            if (location != null && ((Globalvar) getApplicationContext()).getCapture() == 1) { //((Globalvar)getApplicationContext()).getCapture()==1表示目前有新照片
                //建立新的地圖座標點
                GeoPoint startPoint = new GeoPoint(location.getLatitude(), location.getAltitude());//經緯度設定
                IMapController mapController = map.getController();
                mapController.setZoom(9);
                mapController.setCenter(startPoint);

                //加Marker在地圖上
                Marker startMarker = new Marker(map);
                startMarker.setPosition(startPoint);
                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                map.getOverlays().add(startMarker);
                map.invalidate();
                startMarker.setIcon(getResources().getDrawable(R.drawable.footprint));
                //設定marker內的文字資料為系統年月日
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy" + "\n" + "MM/dd");
                Date curDate = new Date(System.currentTimeMillis()); // 獲取當前時間
                String str = formatter.format(curDate);
                startMarker.setTitle(str);

                //按下Maker後的相片內容(為剛照完的相片路徑)

                startMarker.setOnMarkerClickListener(click); //按下Marker會做的事情  第一下開啟infomation  第二下 開啟相簿 第三下關閉infomation
                ((Globalvar) getApplicationContext()).setCapture(0);//設定完相片後即沒有新的照片

            }
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }


    Marker.OnMarkerClickListener click = new Marker.OnMarkerClickListener() {  //按下狗腳印做的事情

        @Override
        public boolean onMarkerClick(Marker item, MapView arg1) {
            ((Globalvar) getApplicationContext()).addtouch();
            if (((Globalvar) getApplicationContext()).gettouch() == 0) {
                item.closeInfoWindow();
            } else if (((Globalvar) getApplicationContext()).gettouch() == 1) {
                item.showInfoWindow();
            } else if (((Globalvar) getApplicationContext()).gettouch() == 2) {
                Intent intent = new Intent();
                intent.setClass(ImageDirary.this, Album.class);
                startActivity(intent);
                return true;
            }
            return true;
        }
    };
}

