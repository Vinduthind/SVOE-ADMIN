package co.techxpose.firebaseui;


import android.content.Context;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;

import android.hardware.Camera;
import android.os.Bundle;
import android.os.Vibrator;

import android.support.transition.Visibility;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class CameraScanner extends AppCompatActivity {

    SurfaceView camerapreview;
    String txtscanner;
    TextView txtcamera,historytxt;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    MyDataBaseforHistory db;
    Button refresh_button;
    Camera mcamera;
    Camera.Parameters mparameters;
    boolean isflash=true;
    boolean ison=false;

    SurfaceView camera_no_need;
    final int RequestCameraPermissionId = 1001;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_scanner);


        db=new MyDataBaseforHistory(this);


        camera_no_need=(SurfaceView)findViewById(R.id.cameranoneed);
        camerapreview = (SurfaceView) findViewById(R.id.camerapreview);
        txtcamera = (TextView) findViewById(R.id.scannertext);





        //************************for window landscape mode**********************************
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //**********************************************************************************

        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        //barcodeDetectorno = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(24)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1920,1024).build();



        // get camera source
        getcamerasource();
        //for refresh activity



        //get data from qr scanner and vibrate
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcode =detections.getDetectedItems();
                if (qrcode.size() !=0){
                    txtcamera.post(new Runnable() {
                        @Override
                        public void run() {
                            // here first vibrate

                            Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(100);
                            txtcamera.setText(qrcode.valueAt(0).displayValue);
                            cameraSource.stop();
                            txtscanner = txtcamera.getText().toString();
                            db.saveData(txtscanner);


                                Intent in = new Intent(getApplicationContext(),FinalOutputActivity.class);
                                in.putExtra("h_id", txtscanner);

                                startActivity(in);




                        finish();

                        }

                    });

                }

            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera_scanner, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_history) {
            Cursor res=db.getalldata();
            if (res.getCount()==0)
            {

                showmessage("error","Nothing found");

            }
            StringBuffer buffer=new StringBuffer();
            while(res.moveToNext()){
                buffer.append("scan no. :"+res.getString(0)+"\n");
                buffer.append("Text     :"+res.getString(1)+"\n....................................................\n");

            }

            showmessage("Data",buffer.toString());
        }


        else if (id == R.id.action_history_icon) {
            Cursor res=db.getalldata();
            if (res.getCount()==0)
            {

                showmessage("error","Nothing found");

            }
            StringBuffer buffer=new StringBuffer();
            while(res.moveToNext()){
                buffer.append("scan no. :"+res.getString(0)+"\n");
                buffer.append("Text     :"+res.getString(1)+"\n....................................................\n");

            }

            showmessage("Data",buffer.toString());
        }


        else if (id == R.id.action_flashlight_off) {






        }


        else if (id == R.id.action_exit) {
            Intent in = new Intent(getApplicationContext(),FirstActivityView.class);
            startActivity(in);
            finish();
            Toast.makeText(this, "Qr is not Scanned no data recieve", Toast.LENGTH_SHORT).show();
            return true;
        }

        else if (id == R.id.action_refresh) {

            Intent intent = getIntent();

            finish();
            startActivity(intent);


            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStop() {
        super.onStop();

        if(mcamera!=null)
        {
            mcamera.release();
            mcamera=null;
        }
    }

    public void getcamerasource()
    {


        //add events camera action
        camerapreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //request permission
                    ActivityCompat.requestPermissions(CameraScanner.this,new String[]{android.Manifest.permission.CAMERA},RequestCameraPermissionId);
                    return;
                }try {

                    cameraSource.start(camerapreview.getHolder());
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

                cameraSource.stop();
            }
        });

        //add no need camera here
        camera_no_need.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder1) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //request permission
                    ActivityCompat.requestPermissions(CameraScanner.this, new String[]{android.Manifest.permission.CAMERA}, RequestCameraPermissionId);
                    return;
                }


                try {

                    cameraSource.start(camera_no_need.getHolder());
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

                cameraSource.stop();
            }
        });

    }


    public  void showmessage(String title,String message){

        AlertDialog.Builder alert = new  AlertDialog.Builder(this);
        alert.setCancelable(true);
        alert.setTitle(title) ;
        alert.setMessage(message);


        alert.show();

    }



    @Override
    public void onBackPressed() {
        Intent in = new Intent(getApplicationContext(),FirstActivityView.class);
        startActivity(in);
        super.onBackPressed();
    }

}


