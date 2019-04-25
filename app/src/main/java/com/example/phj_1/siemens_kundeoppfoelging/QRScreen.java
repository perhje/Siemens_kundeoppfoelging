package com.example.phj_1.siemens_kundeoppfoelging;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.IOException;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;


public class QRScreen extends AppCompatActivity {
    SurfaceView scanner;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrscreen);
        scanner = (SurfaceView) findViewById(R.id.scanner);
        if (ActivityCompat.checkSelfPermission(QRScreen.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(QRScreen.this, new String[]{Manifest.permission.CAMERA}, 1);
            return;
        }
        if (ActivityCompat.checkSelfPermission(QRScreen.this,
                Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(QRScreen.this, new String[]{Manifest.permission.VIBRATE}, 1);
            return;
        }
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource = new CameraSource.Builder(this,barcodeDetector)
                .setRequestedPreviewSize(640,480).build();

        scanner.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(QRScreen.this, new String[]{Manifest.permission.CAMERA}, 1);
                    return;
                }
                if (ActivityCompat.checkSelfPermission(QRScreen.this,
                        Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(QRScreen.this, new String[]{Manifest.permission.VIBRATE}, 1);
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
                    Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(100);
                    SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCES", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("qrCode", qrCodes.valueAt(0).displayValue);
                    editor.apply();
                    Intent intent = new Intent(QRScreen.this,ServiceRequest.class);
                    startActivity(intent);
                }
            }
        });

    }

    public void menu(View view){
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }
}


