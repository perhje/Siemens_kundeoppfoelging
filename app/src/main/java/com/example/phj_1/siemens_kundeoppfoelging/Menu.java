package com.example.phj_1.siemens_kundeoppfoelging;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class Menu extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
    }

    //Sends the user to window SystemServiceActivity
    public void systemServiceActivity(View v){
        Intent intent=new Intent(this,SystemServiceActivity.class);
        startActivity(intent);
    }

    //Sends the user to window ContactInfo
    public void changeUserInformation(View v){
        Intent intent=new Intent(this,ContactInfo.class);
        startActivity(intent);
    }

    /*Starting the youtubechannel of Siemens Healthineers if Youtube is installed on the device,
    * if not, it will install it.*/
    public void youtubeChannel(View v){
        if(installedCheck(getResources().getString(R.string.packageYoutube))) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage(getResources().getString(R.string.packageYoutube));
            intent.setData(Uri.parse(getResources().getString(R.string.healthineers)));
            startActivity(intent);
        }else if(installer(getResources().getString(R.string.packageYoutube))){
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
        }else{

        }
    }

    //Starting lifeNet, if not installed, it will install it.
    public void lifeNet(View v){
        if(installedCheck(getResources().getString(R.string.packageLifenet))) {
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(getResources().getString(R.string.packageLifenet));
            startActivity( launchIntent );
        }else if(installer(getResources().getString(R.string.packageLifenet))){
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
        }else{

        }
    }

    //Sends the user back to the MainActivity/Qrscreen
    public void back(View v){
        finish();
    }

    public boolean installedCheck(String s){ //checks if app is installed
        PackageManager packageManager = getPackageManager();
        try{
            packageManager.getPackageInfo(s,PackageManager.GET_ACTIVITIES);
            return true;
        }catch (PackageManager.NameNotFoundException e){

        }
        return false;
    }

    public boolean installer(String s){
        try{//checks if google play app is installed and uses that to install
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.googleplay) + s)));
            return true;
        }catch (android.content.ActivityNotFoundException e){//uses web browser to install if google play app is not installed
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.googleplayweb) + s)));
        }
        return false;
    }
}
