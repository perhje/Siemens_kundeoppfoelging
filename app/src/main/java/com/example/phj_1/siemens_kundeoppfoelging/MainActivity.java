package com.example.phj_1.siemens_kundeoppfoelging;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    SurfaceView scanner;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    String serialNumber;
    Message message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCES", MODE_PRIVATE);
        /*if(sharedPreferences.getString("yourName", "").equals("")){
            launchSecondActivity(null);
        }else */if(true){
            setContentView(R.layout.activity_main);
            scanner =  findViewById(R.id.scanner);
            scanner.setZOrderOnTop(true);
            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                return;
            }
            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.VIBRATE}, 1);
                return;
            }
            this.qrScreen();
        }
    }

    public void launchSecondActivity(View view) {
        Intent intent = new Intent(this, ContactInfo.class);
        startActivity(intent);

    }

    @Override
   protected void onResume() {
        super.onResume();
        this.qrScreen();

        /*SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCES", MODE_PRIVATE);
        if(sharedPreferences.getString("yourName", "").equals("")){
            launchSecondActivity(null);
        }else{
            Intent intent = new Intent(this, QRScreen.class);
            startActivity(intent);
        }*/
    }

    public void menu(View view){
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }

    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message message) {
            Toast.makeText(getApplicationContext(),
                    getApplicationContext().getText(R.string.qrFail),Toast.LENGTH_SHORT).show();
        }
    };

    public void qrScreen(){
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource = new CameraSource.Builder(this,barcodeDetector)
                .setRequestedPreviewSize(640,480).build();
        scanner.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                    return;
                }
                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.VIBRATE}, 1);
                    return;
                }
                try {
                    cameraSource.start(holder);
                } catch (IOException e) {
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

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();
                if (qrCodes.size() != 0){
                    SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCES", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    // if(qrCodes.valueAt(0).displayValue.equals(serialNumber)) {

                    Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(100);
                    editor.putString("qrCode", qrCodes.valueAt(0).displayValue);
                    editor.apply();
                    Intent intent = new Intent(MainActivity.this, SystemServiceActivity.class);
                    startActivity(intent);
                    barcodeDetector.release();
                    //}
                       /* if(!qrCodes.valueAt(0).displayValue.equals(serialNumber)){
                            message = handler.obtainMessage();
                            message.sendToTarget();
                            editor.putString("qrCode", "feil qr");
                            editor.apply();
                        }*/
                }
            }
        });

    }
}
