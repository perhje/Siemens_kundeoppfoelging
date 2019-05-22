package com.example.phj_1.siemens_kundeoppfoelging;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    SurfaceView scanner;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    Message message;
    String dataString = "";
    String[] table;
    String url = "http://student.cs.hioa.no/~s309856/jsonoutSystems.php";
    String result;
    MachineJSON machineJSON = new MachineJSON();

    //Calls the values from the database, check for
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            result = machineJSON.execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
            return;
        }
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.VIBRATE}, 1);
            return;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCES", MODE_PRIVATE);
        if(sharedPreferences.getString(getResources().getString(R.string.hint_contact_name), "").equals("") ||
                sharedPreferences.getString(getResources().getString(R.string.hint_contact_Telephone), "").equals("") ||
                sharedPreferences.getString(getResources().getString(R.string.hint_contact_email), "").equals(""))
        {
            launchSecondActivity(null);
        }
        dataString = sharedPreferences.getString("table", "");
        if(!dataString.equals("")){
            table = dataString.split("\n");
        }
        setContentView(R.layout.activity_main);
        scanner = findViewById(R.id.scanner);
        scanner.setZOrderOnTop(true);
        this.qrScreen();
    }

    //Runs the QR-screen to make it ready for scanning
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCES", MODE_PRIVATE);
        if(sharedPreferences.getString(getResources().getString(R.string.hint_contact_name), "").equals("") ||
                sharedPreferences.getString(getResources().getString(R.string.hint_contact_Telephone), "").equals("") ||
                sharedPreferences.getString(getResources().getString(R.string.hint_contact_email), "").equals(""))
        {
            launchSecondActivity(null);
        }
        dataString = sharedPreferences.getString("table", "");
        if(!dataString.equals("")){
            table = dataString.split("\n");
        }
        this.qrScreen();
    }

    //Launches the contactInfo
    public void launchSecondActivity(View view) {
        Intent intent = new Intent(this, ContactInfo.class);
        startActivity(intent);
    }

    //Sends the user to the menu
    public void menu(View view) {
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }

    //Makes a toast that tells the user to
    Handler systemToast = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            Toast.makeText(getApplicationContext(),
                    getApplicationContext().getText(R.string.qrFail), Toast.LENGTH_SHORT).show();
        }
    };

    //Does the scanning of the code and sends to SystemServiceActivity if getting a hit.
    public void qrScreen() {
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480).build();
        scanner.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                    return;
                }
                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.VIBRATE}, 1);
                    return;
                }
                try {
                    cameraSource.start(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }
            //Take in the QR-code, check if system is in database and stops it to avoid multiple intents
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();
                if (qrCodes.size() != 0) {
                    for(int i = 0; i < table.length; i++) {
                        String[] temp = table[i].split("-");
                        if (qrCodes.valueAt(0).displayValue.equals(temp[0])) {
                            Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(100);
                            Intent intent = new Intent(MainActivity.this, SystemServiceActivity.class);
                            intent.putExtra( "machine",temp);
                            startActivity(intent);
                            barcodeDetector.release();

                        }else {
                            message = systemToast.obtainMessage();
                            message.sendToTarget();
                        }
                    }
                }
            }
        });

    }

    //Gets the data from the database and puts the data as an easy handleable string in shared preferences
    private class MachineJSON extends AsyncTask<String, Void, String> {
        static final String REQUEST_METHOD = "GET";
        static final int READ_TIMEOUT = 15000;
        static final int CONNECTION_TIMEOUT = 15000;
        @Override
        protected String doInBackground(String... params) {
            String stringUrl = params[0];
            String machine = "";
            String inputLine;
            String output = "";
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                connection.connect();
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                while ((inputLine = reader.readLine()) != null) {
                    output = output + inputLine;
                }
                connection.disconnect();
                reader.close();
                streamReader.close();
                result = stringBuilder.toString();
                try {
                    JSONArray jsonArray = new JSONArray(output);
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jsonobject = jsonArray.getJSONObject(j);
                        String serial_number = jsonobject.getString("serial_number");
                        String hospital = jsonobject.getString("hospital");
                        String department = jsonobject.getString("department");
                        if(machine.equals("")){
                            machine = serial_number + "-" + hospital + "-" + department;
                        }else {
                            machine +=  "\n" +serial_number + "-" + hospital + "-" + department;
                        }

                    }
                    return machine;


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return machine;
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return machine;
        }

            @Override
            protected void onPostExecute(String result){
            SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCES", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("table", result);
            editor.apply();
            super.onPostExecute(result);
        }
    }

}

