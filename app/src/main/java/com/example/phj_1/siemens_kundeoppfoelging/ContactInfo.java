package com.example.phj_1.siemens_kundeoppfoelging;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ContactInfo extends Activity{
    private TextView yourNameTexView;
    private TextView yourTelephoneTexView;
    private TextView yourEmailTexView;
    private TextView yourEmployeeIDTexView;
    private EditText yourNameEditText;
    private EditText yourTelephoneEditText;
    private EditText yourEmailEditText;
    private EditText yourEmployeeIDEditText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        yourNameTexView = (TextView) findViewById(R.id.textView_contact_yourName);
        yourTelephoneTexView = (TextView) findViewById(R.id.textView_contact_yourTelephone);
        yourEmailTexView = (TextView) findViewById(R.id.textView_contact_yourEmail);
        yourEmployeeIDTexView = (TextView) findViewById(R.id.textView_contact_yourEmployeeID);
        yourNameEditText = (EditText) findViewById(R.id.editText_contact_yourName);
        yourTelephoneEditText = (EditText) findViewById(R.id.editText_contact_yourTelephone);
        yourEmailEditText = (EditText) findViewById(R.id.editText_contact_yourEmail);
        yourEmployeeIDEditText = (EditText) findViewById(R.id.editText_contact_yourEmployeeID);
        saveButton = (Button) findViewById(R.id.button_contact_save);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //yourNameTexView.setText(yourNameEditText.getText().toString());
                yourNameEditText.setText(yourNameEditText.getText().toString());
                yourTelephoneEditText.setText(yourTelephoneEditText.getText().toString());
                yourEmailEditText.setText(yourEmailEditText.getText().toString());
                yourEmployeeIDEditText.setText(yourEmployeeIDEditText.getText().toString());

                saveContactInfo();
                Intent intent = new Intent(ContactInfo.this, QRScreen.class);
                startActivity(intent);

            }
        });

    }

    public void saveContactInfo(){
        //super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(getResources().getString(R.string.yourName), yourNameEditText.getText().toString());
        editor.putString("yourTelephone", yourTelephoneEditText.getText().toString());
        editor.putString("yourEmail", yourEmailEditText.getText().toString());
        editor.putString("yourEmployeeID", yourEmployeeIDEditText.getText().toString());

        editor.apply();
    }
    @Override
    protected void onResume(){
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCES", MODE_PRIVATE);

        String yourName = sharedPreferences.getString("yourName", "");
        String yourTelephone = sharedPreferences.getString("yourTelephone", "");
        String yourEmail = sharedPreferences.getString("yourEmail", "");
        String yourEmployeeID = sharedPreferences.getString("yourEmployeeID", "");

        yourNameEditText.setText(yourName);
        yourTelephoneEditText.setText(yourTelephone);
        yourEmailEditText.setText(yourEmail);
        yourEmployeeIDEditText.setText(yourEmployeeID);

    }

}
