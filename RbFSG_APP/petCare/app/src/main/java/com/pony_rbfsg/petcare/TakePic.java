package com.pony_rbfsg.petcare;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class TakePic extends Activity {

	SurfaceView sv;
	SurfaceHolder sh;
	Camera camera;
	int facing;
	TextView tv;
	Button takepicture, switchcamera, viewpicture;
	LinearLayout ll;
	SensorManager smgr;
	Sensor sensor;
	
	Display display;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		display = getWindowManager().getDefaultDisplay();

		Intent intent = getIntent();
		facing = intent.getIntExtra("FACING", CameraInfo.CAMERA_FACING_BACK);
		setContentView(R.layout.activity_surface);
		sv = (SurfaceView)findViewById(R.id.sv);
		sh = sv.getHolder();
		sh.addCallback(new MySHCallback());
		smgr = (SensorManager)getSystemService(SENSOR_SERVICE);
		sensor = smgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		LayoutInflater inflater = LayoutInflater.from(this);
		ll = (LinearLayout)inflater.inflate(R.layout.addviews, null);
		tv = (TextView)ll.findViewById(R.id.tv_camerastatus);
		takepicture = (Button)ll.findViewById(R.id.btn_tp);
		takepicture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {//照相
				if (camera != null) {
					camera.takePicture(null, null, jpeg);
					((Globalvar)getApplicationContext()).setCapture(1);
				}
			}
		});
		switchcamera = (Button)ll.findViewById(R.id.btn_sc);
		switchcamera.setOnClickListener(new OnClickListener() {//切換前鏡頭,後鏡頭
			@Override
			public void onClick(View v) {
				if (facing == CameraInfo.CAMERA_FACING_BACK)
					facing = CameraInfo.CAMERA_FACING_FRONT;
				else
					facing = CameraInfo.CAMERA_FACING_BACK;
				camera.stopPreview();
				camera.release();
				camera = null;
				Intent intent = new Intent();
				intent.setClass(TakePic.this,
						TakePic.class);
				intent.putExtra("FACING", facing);
				startActivity(intent);
				finish();
			}
		});
		viewpicture = (Button)ll.findViewById(R.id.btn_vp);
		viewpicture.setOnClickListener(new OnClickListener() {//回到Petinfo activity並將相片儲存到地圖上
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(TakePic.this, Petinfo.class);
				startActivity(intent);
			}
		});

		LayoutParams lp = new LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		addContentView(ll, lp);
	}
	
	PictureCallback jpeg = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			Bitmap bm = BitmapFactory.decodeByteArray(data,
					0, data.length);
			FileOutputStream fos = null;
			try {
				File sdroot = Environment.getExternalStorageDirectory();
				File file = new File(sdroot, ((Globalvar)getApplicationContext()).getpicnum()+".jpg");//本日第幾張照片,照片名就是第幾張.jpg
				fos = new FileOutputStream(file);
				BufferedOutputStream bos =
						new BufferedOutputStream(fos);
				bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
				bos.flush();
				bos.close();
				Toast.makeText(TakePic.this, "儲存成功",
						Toast.LENGTH_SHORT).show();
				((Globalvar)getApplicationContext()).addpicnum();
				camera.startPreview();
			}catch (Exception e) {
				Toast.makeText(TakePic.this, "儲存失敗",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();

	}

	class MySHCallback implements SurfaceHolder.Callback {
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			CameraInfo info = new CameraInfo();
			int ncamera = Camera.getNumberOfCameras();
			for (int i = 0; i < ncamera; i++) {
				Camera.getCameraInfo(i, info);
				if (info.facing == facing) {
					camera = Camera.open(i);
					break;
				}
			}
			if (camera == null) camera = Camera.open();
			
			if (camera == null) {
				Toast.makeText(TakePic.this,
						"無法開啟相機", Toast.LENGTH_SHORT).show();
				finish();
				return;
			}
			
			int degrees = 0;
			int rotation = display.getRotation();
			switch (rotation) {
			case Surface.ROTATION_0: degrees = 0; break;
			case Surface.ROTATION_90: degrees = 90; break;
			case Surface.ROTATION_180: degrees = 180; break;
			case Surface.ROTATION_270: degrees = 270; break;
			}

			int result;
			if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
				result = (info.orientation + degrees) % 360;
				result = (360 - result) % 360;  // compensate the mirror
			} else {  // back-facing
				result = (info.orientation - degrees + 360) % 360;
			}
			camera.setDisplayOrientation(result);

			Camera.Parameters params = camera.getParameters();
			params.setPictureFormat(ImageFormat.JPEG);
			params.setPictureSize(480, 320);
			camera.setParameters(params);

			try  {
				camera.setPreviewDisplay(sh);
			} catch (Exception e) {
				finish();
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder surfaceholder) {
			if (camera != null) {
				camera.stopPreview();
				camera.release();
			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder surfaceholder,
				int format , int w, int h) {
			if (camera != null) camera.startPreview();
		}
	}

}
