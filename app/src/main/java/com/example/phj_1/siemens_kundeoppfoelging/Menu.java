package com.example.phj_1.siemens_kundeoppfoelging;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
        /*Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.android.gallery");
        startActivity( launchIntent );*/
        /*Intent intent = new  Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setPackage("com.android.gallery");
        intent.setData(Uri.parse("https://www.youtube.com/siemenshealthineers"));
        startActivity(intent);*/
        Intent intent = new  Intent(Intent.ACTION_VIEW);
        intent.setPackage("com.google.android.youtube");
        intent.setData(Uri.parse("https://www.youtube.com/siemenshealthineers"));//funker ikke på emulator, skal funke så lenge youtube er der
        startActivity(intent);

    }

    public void lifeNet(View v){
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.android.gallery");
        startActivity( launchIntent );
    }
}
