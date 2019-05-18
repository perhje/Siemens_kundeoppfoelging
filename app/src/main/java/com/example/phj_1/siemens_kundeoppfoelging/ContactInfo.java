package com.example.phj_1.siemens_kundeoppfoelging;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class ContactInfo extends Activity implements AdapterView.OnItemClickListener {

    private EditText yourNameEditText;
    private EditText yourTelephoneEditText;
    private EditText yourEmailEditText;
    private EditText yourEmployeeIDEditText;
    private Button yourHospitalButton;
    private PopupWindow yourHospitalPopupWindow;
    private Button yourDepartmentButton;
    private PopupWindow yourDepartmentPopupWindow;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        yourNameEditText = findViewById(R.id.editText_contact_yourName);
        yourTelephoneEditText = findViewById(R.id.editText_contact_yourTelephone);
        yourEmailEditText = findViewById(R.id.editText_contact_yourEmail);
        yourEmployeeIDEditText = findViewById(R.id.editText_contact_yourEmployeeID);
        saveButton = findViewById(R.id.button_contact_save);
        yourHospitalButton = findViewById(R.id.button_contact_ShowDropDown_Hospitals);
        yourDepartmentButton = findViewById(R.id.button_contact_ShowDropDown_Departments);

        yourHospitalPopupWindow = popupWindowHospitals("http://student.cs.hioa.no/~s309856/jsonoutSykehus.php");

        View.OnClickListener handler = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button_contact_ShowDropDown_Hospitals:
                        yourHospitalPopupWindow.showAsDropDown(v, 0, 0);
                        break;
                }
                switch (v.getId()) {
                    case R.id.button_contact_ShowDropDown_Departments:
                        yourDepartmentPopupWindow.showAsDropDown(v, 0, 0);
                        break;
                }
            }
        };
        yourHospitalButton.setOnClickListener(handler);
        yourDepartmentButton.setOnClickListener(handler);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yourNameEditText.setText(yourNameEditText.getText().toString());
                yourTelephoneEditText.setText(yourTelephoneEditText.getText().toString());
                yourEmailEditText.setText(yourEmailEditText.getText().toString());
                yourEmployeeIDEditText.setText(yourEmployeeIDEditText.getText().toString());
                yourHospitalButton.setText(yourHospitalButton.getText().toString());
                yourDepartmentButton.setText(yourDepartmentButton.getText().toString());

                saveContactInfo();
                Intent intent = new Intent(ContactInfo.this, QRScreen.class);
                startActivity(intent);

                finish();
            }
        });
    }

    public PopupWindow popupWindowHospitals(String url) {
        ListView listView = new ListView(this);
        listView.setOnItemClickListener(this);
        return getPopupWindow(listView, url);
    }

    public PopupWindow popupWindowDepartments(String url) {
        ListView listView = new ListView(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context mContext = view.getContext();
                yourDepartmentPopupWindow.dismiss();
                String selectedItemText = (String) parent.getItemAtPosition(position);
                yourDepartmentButton.setText(selectedItemText);
                Toast.makeText(mContext, "Valgt avdeling: " + selectedItemText, Toast.LENGTH_SHORT).show();
            }
        });
        return getPopupWindow(listView, url);
    }

    @NonNull
    private PopupWindow getPopupWindow(ListView listView, String url) {
        PopupWindow popupWindow = new PopupWindow(this);

        GetJSON getJSON = new GetJSON();
        String[] urlStrings = new String[]{url};
        getJSON.execute(urlStrings);
        ListAdapter arrayAdapter = new ArrayAdapter<String>(
                ContactInfo.this,
                android.R.layout.simple_list_item_1,
                getJSON.getNames());
        listView.setAdapter(arrayAdapter);

        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(listView);

        return popupWindow;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Context mContext = view.getContext();
        yourHospitalPopupWindow.dismiss();
        String selectedItemText = (String) parent.getItemAtPosition(position);
        yourHospitalButton.setText(selectedItemText);
        Toast.makeText(mContext, "Valgt sykehus: " + selectedItemText, Toast.LENGTH_LONG).show();

        if (position == 0){
            yourDepartmentPopupWindow = popupWindowDepartments("http://student.cs.hioa.no/~s309856/jsonoutDepartment_Rikshospitalet.php");
        } else {
            yourDepartmentPopupWindow = popupWindowDepartments("http://student.cs.hioa.no/~s309856/jsonoutDepartment_Radiumhospitalet.php");
        }
    }

    public void saveContactInfo(){
        SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(getResources().getString(R.string.yourName), yourNameEditText.getText().toString());
        editor.putString(getResources().getString(R.string.yourTelephone), yourTelephoneEditText.getText().toString());
        editor.putString(getResources().getString(R.string.yourEmail), yourEmailEditText.getText().toString());
        editor.putString(getResources().getString(R.string.yourEmployeeID), yourEmployeeIDEditText.getText().toString());
        editor.putString(getResources().getString(R.string.yourHospital), yourHospitalButton.getText().toString());
        editor.putString(getResources().getString(R.string.yourDepartment), yourDepartmentButton.getText().toString());

        editor.apply();
    }

    @Override
    protected void onResume(){
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCES", MODE_PRIVATE);

        String yourName = sharedPreferences.getString(getResources().getString(R.string.yourName), "");
        String yourTelephone = sharedPreferences.getString(getResources().getString(R.string.yourTelephone), "");
        String yourEmail = sharedPreferences.getString(getResources().getString(R.string.yourEmail), "");
        String yourEmployeeID = sharedPreferences.getString(getResources().getString(R.string.yourEmployeeID), "");
        String yourHospital = sharedPreferences.getString(getResources().getString(R.string.yourHospital),getResources().getString(R.string.chooseHospital));
        String yourDepartment = sharedPreferences.getString(getResources().getString(R.string.yourDepartment),getResources().getString(R.string.chooseDepartment));

        yourNameEditText.setText(yourName);
        yourTelephoneEditText.setText(yourTelephone);
        yourEmailEditText.setText(yourEmail);
        yourEmployeeIDEditText.setText(yourEmployeeID);
        yourHospitalButton.setText(yourHospital);
        yourDepartmentButton.setText(yourDepartment);
    }

}
