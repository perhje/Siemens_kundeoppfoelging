package com.example.phj_1.siemens_kundeoppfoelging;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetJSON extends AsyncTask<String, Void,String> {
    JSONObject jsonObject;

    List<String> names = new ArrayList<>();

    @Override
    protected String doInBackground(String... urls) {
        String retur = "";
        String s = "";
        String output = "";

        for (int i = 0; i < urls.length; i++) {
            try {
                URL urlen = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) urlen.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : " +
                            conn.getResponseCode());
                }

                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                System.out.println("Output from Server .... \n");
                while ((s = br.readLine()) != null) {
                    output = output + s;
                }
                conn.disconnect();

                try {
                        JSONArray locationJSONArray = new JSONArray(output);
                        for (int j = 0; j < locationJSONArray.length(); j++) {
                            JSONObject jsonobject = locationJSONArray.getJSONObject(j);
                            String name = jsonobject.getString("name");
                            System.out.println("name: " + name);
                            names.add(name);
                            for(String f : names){
                                System.out.println("de derre: "+f);
                            }
                            retur = retur + name + "\n";
                        }
                        return retur;
                }catch (JSONException e) {
                    System.out.println("err: " + e.getMessage());
                    e.printStackTrace();
                }
                return retur;
            } catch (Exception e) {
                System.out.println("error" + e.getMessage());
                return "error";
            }
        }
        return retur;
    }

    @Override
    protected void onPostExecute(String ss) {

    }

    public List<String> getNames() {
        return names;
    }



    public void setNames(List<String> names) {
        this.names = names;
    }
}
