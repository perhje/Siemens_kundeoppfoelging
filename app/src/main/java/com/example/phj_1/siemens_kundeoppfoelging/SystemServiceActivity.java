package com.example.phj_1.siemens_kundeoppfoelging;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SystemServiceActivity extends AppCompatActivity {
    Button call;
    Button generateemail;
    Button back;
    TextView SystemId;
    EditText SystemInput;
    TextView problemdescription;
    EditText EditProblemdescription;
    String referanse;
    String body;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.systemserviceactivity);
        //-----------need to rewrite code when using QR CODE------
        SystemId = (TextView) findViewById(R.id.SystemId);
        SystemInput = (EditText) findViewById(R.id.SystemInput);
        //--------------------------------------------------------
        problemdescription = (TextView) findViewById(R.id.Problem);
        EditProblemdescription = (EditText) findViewById(R.id.ProblemInput);
        call = (Button) findViewById(R.id.callsupport);
        back = (Button) findViewById(R.id.back);

        SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCES", MODE_PRIVATE);
        String yourName = sharedPreferences.getString("yourName", "");
        String yourTelephone = sharedPreferences.getString("yourTelephone", "");
        String yourEmail = sharedPreferences.getString("yourEmail", "");
        String yourEmployeeID = sharedPreferences.getString("yourEmployeeID", "");
        referanse = getResources()
                .getString(R.string.yourName)+": " + yourName + "\n" + getResources()
                .getString(R.string.yourEmployeeID)+": " + yourEmployeeID + "\n" + getResources()
                .getString(R.string.yourTelephone)+": " + yourTelephone + "\n" + getResources()
                .getString(R.string.yourEmail)+": " + yourEmail;
        generateemail = (Button) findViewById(R.id.sendmail);
        generateemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                GenerateEmail();
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(getResources().getString(R.string.tel)+getResources().getString(R.string.support_phone)));

               if (ActivityCompat.checkSelfPermission(SystemServiceActivity.this,
                       Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                     ActivityCompat.requestPermissions(SystemServiceActivity.this,
                             new String[]{Manifest.permission.CALL_PHONE},1);
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
                    Intent call = new Intent(Intent.ACTION_CALL, Uri.parse(getResources().getString(R.string.tel)
                            + getResources().getString(R.string.support_phone)));

                    if (ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        this.GenerateEmail();
                        startActivity(call);
                    }
                }
            }
        }
    }


    public void GenerateEmail(){

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {

                    String sysName = String.valueOf(SystemInput.getText());
                    String sysProblem= String.valueOf(EditProblemdescription.getText());
                    String body="";
                    if(sysName.length()>0||sysProblem.length()>0) {
                         body = getResources().getString(R.string.hi) + "\n" + "\n" + sysName + " " +
                                getResources().getString(R.string.email_text) + "\n" + sysProblem + "\n" + "\n"
                                + getResources().getString(R.string.regards) + "\n" + referanse;
                    }else {
                        body = " System down, calling support ";
                    }

                    SystemMail sender=new SystemMail();
                    sender.sendMail("system down", body,
                            "phamducnguyen82@gmail.com");
                    Message message = handler.obtainMessage();
                    message.sendToTarget();
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }

        }).start();

    }

    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message message) {
            Toast.makeText(getApplicationContext(),
                    "service request sent",Toast.LENGTH_SHORT).show();
        }
    };


    public void goback(View v) {
        finish();
    }
}




