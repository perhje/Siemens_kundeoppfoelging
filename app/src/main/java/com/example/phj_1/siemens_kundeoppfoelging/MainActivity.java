package com.example.phj_1.siemens_kundeoppfoelging;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=new Intent(this,QRScreen.class);
        startActivity(intent);
    }

}
