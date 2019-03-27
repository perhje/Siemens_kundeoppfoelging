package com.example.phj_1.siemens_kundeoppfoelging;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

public class QRScreen extends AppCompatActivity{

    Camera camera;
    FrameLayout scanner;
    ShowCamera showCamera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrscreen);
        scanner = (FrameLayout)findViewById(R.id.scanner);
        if (ActivityCompat.checkSelfPermission(QRScreen.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(QRScreen.this, new String[]{Manifest.permission.CAMERA},1);
            return;
        }
        camera = Camera.open();

        showCamera = new ShowCamera(this, camera);
        scanner.addView(showCamera);

    }

    public void menu(View v){
        Intent intent=new Intent(this,Menu.class);
        startActivity(intent);
    }
}


