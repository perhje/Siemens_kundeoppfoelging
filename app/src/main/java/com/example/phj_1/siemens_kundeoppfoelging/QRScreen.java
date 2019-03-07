package com.example.phj_1.siemens_kundeoppfoelging;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class QRScreen extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrscreen);
    }

    public void menu(View v){
        Intent intent=new Intent(this,Menu.class);
        startActivity(intent);
    }
}


