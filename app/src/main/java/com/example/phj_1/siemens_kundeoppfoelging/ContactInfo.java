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
    private boolean hospitalChoosed = false;
    private int position_Rikshospitalet = 0;
    private int position_Radiumhospitalet = 1;
    private int position_GaustadSykehus = 2;
    private int position_AkerSykehus = 3;
    private int position_UllevålSykehus = 4;
    private int position_GeilomoBarnesykehus = 5;
    private int position_DikemarkSykehus = 6;
    private int position_AkershusUniversitetsSykehus = 7;
    private int position_SkiSykehus = 8;
    private int position_KongsvingerSykehus = 9;
    private int position_DiakonhjemmetSykehus = 10;
    private int position_LovisenbergDiakonaleSykehus = 11;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        yourNameEditText = findViewById(R.id.editText_contact_yourName);
        yourTelephoneEditText = findViewById(R.id.editText_contact_yourTelephone);
        yourEmailEditText = findViewById(R.id.editText_contact_yourEmail);
        yourEmployeeIDEditText = findViewById(R.id.editText_contact_yourEmployeeID);
        yourHospitalButton = findViewById(R.id.button_contact_ShowDropDown_Hospitals);
        yourDepartmentButton = findViewById(R.id.button_contact_ShowDropDown_Departments);
        saveButton = findViewById(R.id.button_contact_save);

        yourHospitalPopupWindow = popupWindowHospitals("http://student.cs.hioa.no/~s309856/jsonoutHospitals.php");

        View.OnClickListener handler = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button_contact_ShowDropDown_Hospitals:
                        yourHospitalPopupWindow.showAsDropDown(v, 0, 0);
                        break;
                }
                switch (v.getId()) {
                    case R.id.button_contact_ShowDropDown_Departments:
                        if (hospitalChoosed) {
                            yourDepartmentPopupWindow.showAsDropDown(v, 0, 0);
                        }
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
                Intent intent = new Intent(ContactInfo.this, MainActivity.class);
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
        hospitalChoosed = true;
        Context mContext = view.getContext();
        yourHospitalPopupWindow.dismiss();
        String selectedItemText = (String) parent.getItemAtPosition(position);
        yourHospitalButton.setText(selectedItemText);

        if (position == position_Rikshospitalet){
            yourDepartmentPopupWindow = popupWindowDepartments(
                    "http://student.cs.hioa.no/~s309856/jsonoutDepartment_Rikshospitalet.php"
            );
        } else if(position == position_Radiumhospitalet) {
            yourDepartmentPopupWindow = popupWindowDepartments(
                    "http://student.cs.hioa.no/~s309856/jsonoutDepartment_Radiumhospitalet.php"
            );
        }
        else if(position == position_GaustadSykehus) { }
        else if(position == position_AkerSykehus) { }
        else if(position == position_UllevålSykehus) { }
        else if(position == position_GeilomoBarnesykehus) { }
        else if(position == position_DikemarkSykehus) { }
        else if(position == position_AkershusUniversitetsSykehus) { }
        else if(position == position_SkiSykehus) { }
        else if(position == position_KongsvingerSykehus) { }
        else if(position == position_DiakonhjemmetSykehus) { }
        else if(position == position_LovisenbergDiakonaleSykehus) { }

    }

    public void saveContactInfo(){
        SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(getResources().getString(R.string.hint_contact_name), yourNameEditText.getText().toString());
        editor.putString(getResources().getString(R.string.hint_contact_Telephone), yourTelephoneEditText.getText().toString());
        editor.putString(getResources().getString(R.string.hint_contact_email), yourEmailEditText.getText().toString());
        editor.putString(getResources().getString(R.string.hint_contact_employeeID), yourEmployeeIDEditText.getText().toString());
        editor.putString(getResources().getString(R.string.chooseHospital), yourHospitalButton.getText().toString());
        editor.putString(getResources().getString(R.string.chooseDepartment), yourDepartmentButton.getText().toString());

        editor.apply();
    }

    @Override
    protected void onResume(){
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCES", MODE_PRIVATE);

        String yourName = sharedPreferences.getString(getResources().getString(R.string.hint_contact_name), "");
        String yourTelephone = sharedPreferences.getString(getResources().getString(R.string.hint_contact_Telephone), "");
        String yourEmail = sharedPreferences.getString(getResources().getString(R.string.hint_contact_email), "");
        String yourEmployeeID = sharedPreferences.getString(getResources().getString(R.string.hint_contact_employeeID), "");
        String yourHospital = sharedPreferences.getString(getResources().getString(R.string.chooseHospital),getResources().getString(R.string.chooseHospital));
        String yourDepartment = sharedPreferences.getString(getResources().getString(R.string.chooseDepartment),getResources().getString(R.string.chooseDepartment));

        yourNameEditText.setText(yourName);
        yourTelephoneEditText.setText(yourTelephone);
        yourEmailEditText.setText(yourEmail);
        yourEmployeeIDEditText.setText(yourEmployeeID);
        yourHospitalButton.setText(yourHospital);
        yourDepartmentButton.setText(yourDepartment);
    }

}
