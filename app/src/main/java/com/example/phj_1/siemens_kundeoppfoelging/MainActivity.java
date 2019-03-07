package com.example.phj_1.siemens_kundeoppfoelging;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=new Intent(this,QRScreen.class);
        startActivity(intent);
    }

    public void launchSecondActivity(View view) {
        Intent intent = new Intent(this, ContactInfo.class);
        startActivity(intent);


    }

}
