package com.example.phj_1.siemens_kundeoppfoelging;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Menu extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
    }

    public void serviceRequest(View v){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void changeUserInformation(View v){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void youtubeChannel(View v){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void lifeNet(View v){
        Intent intent=new Intent(itemService.class);
        startActivity(intent);
    }
}
