package com.example.phj_1.siemens_kundeoppfoelging;

import android.app.Activity;
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
                yourNameTexView.setText(yourNameEditText.getText().toString());
                yourTelephoneTexView.setText(yourTelephoneEditText.getText().toString());
                yourEmailTexView.setText(yourEmailEditText.getText().toString());
                yourEmployeeIDTexView.setText(yourEmployeeIDEditText.getText().toString());

                saveContactInfo();
            }
        });

    }

    public void saveContactInfo(){
        //super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("yourName", yourNameTexView.getText().toString());
        editor.putString("yourTelephone", yourTelephoneTexView.getText().toString());
        editor.putString("yourEmail", yourEmailTexView.getText().toString());
        editor.putString("yourEmployeeID", yourEmployeeIDTexView.getText().toString());

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

        yourNameTexView.setText(yourName);
        yourTelephoneTexView.setText(yourTelephone);
        yourEmailTexView.setText(yourEmail);
        yourEmployeeIDTexView.setText(yourEmployeeID);

    }

}
