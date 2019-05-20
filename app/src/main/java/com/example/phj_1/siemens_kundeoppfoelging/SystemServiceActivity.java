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
    TextView Systemlocation;
    EditText LocationInput;
    TextView problemdescription;
    EditText EditProblemdescription;
    String referanse;



/*create the scene for systemservice activity*/
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.systemserviceactivity);
        SystemId = (TextView) findViewById(R.id.SystemId);
        SystemInput = (EditText) findViewById(R.id.SystemInput);
        Systemlocation= (TextView) findViewById(R.id.sysLoc);
        LocationInput = (EditText) findViewById(R.id.LocationInput);
        problemdescription = (TextView) findViewById(R.id.Problem);
        EditProblemdescription = (EditText) findViewById(R.id.ProblemInput);
        call = (Button) findViewById(R.id.callsupport);
        back = (Button) findViewById(R.id.back);

        Intent k=getIntent();
        String [] machine= k.getStringArrayExtra("machine");
        if(machine!=null){
            SystemInput.setText(machine[0]);
            LocationInput.setText(machine[1]+", "+machine[2]);
        }


        SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCES", MODE_PRIVATE);
        String yourName = sharedPreferences.getString(getResources().getString(R.string.hint_contact_name), "");
        String yourTelephone = sharedPreferences.getString(getResources().getString(R.string.hint_contact_Telephone), "");
        String yourEmail = sharedPreferences.getString(getResources().getString(R.string.hint_contact_email), "");
        String yourEmployeeID = sharedPreferences.getString(getResources().getString(R.string.hint_contact_employeeID), "");
        String yourEmployeeHospital = sharedPreferences.getString(getResources().getString(R.string.chooseHospital),"");
        String yourEmployeeDepartment= sharedPreferences.getString(getResources().getString(R.string.chooseDepartment),"");

        referanse = getResources()
                .getString(R.string.yourName)+": " + yourName + "\n" + getResources()
                .getString(R.string.yourEmployeeID)+": " + yourEmployeeID + "\n" + getResources()
                .getString(R.string.yourTelephone)+": " + yourTelephone + "\n" + getResources()
                .getString(R.string.yourEmail)+": " + yourEmail+ "\n" + getResources()
                .getString(R.string.hospital)+": " + yourEmployeeHospital+ "\n" + getResources()
                .getString(R.string.department)+": " + yourEmployeeDepartment;
        generateemail = (Button) findViewById(R.id.sendmail);
        generateemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                GenerateEmail();
            }
        });

        /*setonclicklistener for button call. This fire off the phone call. */
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
/*** This method check the permisssion before making phone call and will ask user for permission if its not permitted
 * in advance, then makes the call and in the same time call to method generate email to generate new email and send it to support***/
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

/**This method generate email with information from contact info in a separate thread and send the email.
 * It aslo get the information from the QR-scan function.
 * **/
    public void GenerateEmail(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    String sysName = String.valueOf(SystemInput.getText());
                    String sysProblem= String.valueOf(EditProblemdescription.getText());
                    String body="";
                    if(sysName.length()>0||sysProblem.length()>0) {
                         body = getResources().getString(R.string.hi) + "\n" + "\n" + sysName + " " +getResources().getString(R.string.at)+" "+LocationInput.getText()+" "+
                                getResources().getString(R.string.email_text) + "\n" + sysProblem + "\n" + "\n"
                                + getResources().getString(R.string.regards) + "\n" + referanse;
                    }else if(sysName.length()>0||sysProblem.length()<0){
                        body = " Hi! \n\n System: "+sysName+"\n calling support "
                                + getResources().getString(R.string.regards) + "\n" + referanse;;
                    } else {
                        body = " Hi! \n\n System down, calling support "
                                + getResources().getString(R.string.regards) + "\n" + referanse;;
                    }

                    SystemMail sender=new SystemMail();
                    sender.sendMail("system down", body,
                            getResources().getString(R.string.support_email));
                    Message message = handler.obtainMessage();
                    message.sendToTarget();
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }

        }).start();

    }
/*handler request from thread to inform user*/
    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message message) {
            Toast.makeText(getApplicationContext(),
                    "service request sent",Toast.LENGTH_SHORT).show();
        }
    };

/**This method kill the activity**/
    public void goback(View v) {
        finish();
    }
}




