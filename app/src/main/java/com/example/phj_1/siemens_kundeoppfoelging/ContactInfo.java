package com.example.phj_1.siemens_kundeoppfoelging;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactInfo extends Activity implements AdapterView.OnItemSelectedListener {

    ArrayList<String>a1 = new ArrayList<String>();
    private EditText yourNameEditText;
    private EditText yourTelephoneEditText;
    private EditText yourEmailEditText;
    private EditText yourEmployeeIDEditText;
    private Spinner yourDepartmentSpinner;
    private Spinner yourHospitalSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        yourNameEditText = (EditText) findViewById(R.id.editText_contact_yourName);
        yourTelephoneEditText = (EditText) findViewById(R.id.editText_contact_yourTelephone);
        yourEmailEditText = (EditText) findViewById(R.id.editText_contact_yourEmail);
        yourEmployeeIDEditText = (EditText) findViewById(R.id.editText_contact_yourEmployeeID);
        yourHospitalSpinner = (Spinner) findViewById(R.id.spinner_contact_yourHospital);
        yourDepartmentSpinner = (Spinner) findViewById(R.id.spinner_contact_yourDepartment);
        Button saveButton = (Button) findViewById(R.id.button_contact_save);

        GetJSON hospitalsGetJSON = new GetJSON();
        String[] hospitalsList = new String[]{"http://student.cs.hioa.no/~s309856/jsonoutSykehus.php"};
        String[] hospitalsList2 = {"riksen","ahus","ullevål"};
        hospitalsGetJSON.execute(hospitalsList);
        ArrayAdapter hospitalsArrayAdapter = new ArrayAdapter<String>(ContactInfo.this, android.R.layout.simple_spinner_item, hospitalsGetJSON.getNames());
        hospitalsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hospitalsArrayAdapter.notifyDataSetChanged();
        yourHospitalSpinner.setAdapter(hospitalsArrayAdapter);

        yourHospitalSpinner.setOnItemSelectedListener(this);

//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                int hospitalSpinner_Position = yourHospitalSpinner.getSelectedItemPosition();
////                switch (hospitalSpinner_Position){
////                    case 0:
////                        GetJSON getJSONDepartment_Rikshospitalet = new GetJSON();
////                        getJSONDepartment_Rikshospitalet.execute(new String[]{"http://student.cs.hioa.no/~s309856/jsonoutDepartment_Rikshospitalet.php"});
////                        yourDepartmentSpinner.setAdapter(new ArrayAdapter<String>(ContactInfo.this, android.R.layout.simple_spinner_dropdown_item, getJSONDepartment_Rikshospitalet.getNames()));
////                        break;
////                    case 1:
////                        GetJSON getJSONDepartment_Radiumhospitalet = new GetJSON();
////                        getJSONDepartment_Radiumhospitalet.execute(new String[]{"http://student.cs.hioa.no/~s309856/jsonoutDepartment_Radiumhospitalet.php"});
////                        yourDepartmentSpinner.setAdapter(new ArrayAdapter<String>(ContactInfo.this, android.R.layout.simple_spinner_dropdown_item, getJSONDepartment_Radiumhospitalet.getNames()));
////                        break;
////                }

//                String spinnerValue = (String)parent.getItemAtPosition(position);
//                StringBuffer stringBuffer_Rikshospitalet = new StringBuffer("Rikshospitalet");
//
////                spinnerValue.contentEquals(stringBuffer_Rikshospitalet)
////                if (parent.getId() == R.id.spinner_contact_yourLocation) {
//                    if (spinnerValue.matches("Rikshospitalet")) {
//                        GetJSON getJSONDepartment_Rikshospitalet = new GetJSON();
//                        getJSONDepartment_Rikshospitalet.execute(new String[]{"http://student.cs.hioa.no/~s309856/jsonoutDepartment_Rikshospitalet.php"});
//                        yourDepartmentSpinner.setAdapter(new ArrayAdapter<String>(ContactInfo.this, android.R.layout.simple_spinner_dropdown_item, getJSONDepartment_Rikshospitalet.getNames()));
//                        finish();
//                    } else {
//                        GetJSON getJSONDepartment_Rikshospitalet = new GetJSON();
//                        String[] stringURLDepartment_Rikshospitalet = new String[]{"http://student.cs.hioa.no/~s309856/jsonoutDepartment_Rikshospitalet.php"};
//                        System.out.println("size of Dep Riks: " + getJSONDepartment_Rikshospitalet.getNames().size());
//                        getJSONDepartment_Rikshospitalet.execute(stringURLDepartment_Rikshospitalet);
//                        ArrayAdapter<String> adapterForDepartment_Rikshospitalet;
//                        adapterForDepartment_Rikshospitalet = new ArrayAdapter<String>(ContactInfo.this, android.R.layout.simple_spinner_dropdown_item, getJSONDepartment_Rikshospitalet.getNames());
//                        yourDepartmentSpinner.setAdapter(adapterForDepartment_Rikshospitalet);
//                    }
//                }


//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yourNameEditText.setText(yourNameEditText.getText().toString());
                yourTelephoneEditText.setText(yourTelephoneEditText.getText().toString());
                yourEmailEditText.setText(yourEmailEditText.getText().toString());
                yourEmployeeIDEditText.setText(yourEmployeeIDEditText.getText().toString());

                saveContactInfo();
                Intent intent = new Intent(ContactInfo.this, MainActivity.class);
                startActivity(intent);

                finish();
            }
        });
    }

    public void saveContactInfo(){
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

        String yourName = sharedPreferences.getString(getResources().getString(R.string.yourName), "");
        String yourTelephone = sharedPreferences.getString("yourTelephone", "");
        String yourEmail = sharedPreferences.getString("yourEmail", "");
        String yourEmployeeID = sharedPreferences.getString("yourEmployeeID", "");

        yourNameEditText.setText(yourName);
        yourTelephoneEditText.setText(yourTelephone);
        yourEmailEditText.setText(yourEmail);
        yourEmployeeIDEditText.setText(yourEmployeeID);
    }

    String[] verdi = new String[3];

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Hospital hospital = (Hospital) parent.getSelectedItem();
        displayHospital(hospital);
        System.out.println("HER: " + hospital);

//        if (position == 0) {
//            verdi[0] = "Valgt element er: " + yourHospitalSpinner.getSelectedItem().toString();
//            GetJSON getJSONDepartment_Rikshospitalet = new GetJSON();
//            getJSONDepartment_Rikshospitalet.execute(new String[]{"http://student.cs.hioa.no/~s309856/jsonoutDepartment_Rikshospitalet.php"});
//            yourDepartmentSpinner.setAdapter(new ArrayAdapter<String>(ContactInfo.this, android.R.layout.simple_spinner_dropdown_item, getJSONDepartment_Rikshospitalet.getNames()));
//            System.out.println(verdi[1]);
//        }
//        else {
//            verdi[1] = "Valgt element er: " + yourHospitalSpinner.getSelectedItem().toString();
//            GetJSON getJSONDepartment_Radiumhospitalet = new GetJSON();
//            getJSONDepartment_Radiumhospitalet.execute(new String[]{"http://student.cs.hioa.no/~s309856/jsonoutDepartment_Radiumhospitalet.php"});
//            yourDepartmentSpinner.setAdapter(new ArrayAdapter<String>(ContactInfo.this, android.R.layout.simple_spinner_dropdown_item,getJSONDepartment_Radiumhospitalet.getNames() ));
//            System.out.println(verdi[1]);
//        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        System.out.println(verdi[0]+verdi[1]);
    }

    class Hospital{
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Hospital(String name) {
            this.name = name;
        }
    }
    public void getSelectedHospital(View view){
        Hospital hospital = (Hospital) yourHospitalSpinner.getSelectedItem();
        displayHospital(hospital);
    }

    public void displayHospital (Hospital hospital){
        String name = hospital.name;
        Toast.makeText(this, name, Toast.LENGTH_LONG);
    }

}
