package com.example.phj_1.siemens_kundeoppfoelging;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ServiceRequest extends AppCompatActivity{
Button back;
Button call;
Button sendemail;
EditText editObject;
TextView viewObject;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service);

        viewObject=(TextView)findViewById(R.id.object);
        editObject=(EditText)findViewById(R.id.inputobject);
        back = (Button) findViewById(R.id.back2menu);
        call=(Button)findViewById(R.id.callsupport);
        sendemail=(Button)findViewById(R.id.sendmail);
        call.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(getResources().getString(R.string.tel)+getResources().getString(R.string.support_phone)));

                if (ActivityCompat.checkSelfPermission(ServiceRequest.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ServiceRequest.this, new String[]{Manifest.permission.CALL_PHONE},1);
                    return;
                }
                startActivity(callIntent);
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1 : {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent call = new Intent(Intent.ACTION_CALL, Uri.parse(getResources().getString(R.string.tel) + getResources().getString(R.string.support_phone)));

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        startActivity(call);
                    }
                }
            }
        }
    }

    public void createemail(View v){
        Intent intent=new Intent(this, SystemServiceActivity.class);
        startActivity(intent);

    }


    public void goback(View v) {
        finish();
    }
}
