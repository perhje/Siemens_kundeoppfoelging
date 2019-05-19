package com.example.phj_1.siemens_kundeoppfoelging;

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
import java.util.ArrayList;
import java.util.List;
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
import java.util.ArrayList;
import java.util.List;

public class MachineJSON {

        List<Machine> machineArrayList = new ArrayList<Machine>();
        List<String> stringArrayList_Machine = new ArrayList<String>();

        public String chooseCard() {
            String returned = "";
            String s = "";
            String output = "";

            URL url = null;
            try {
                url = new URL("http://student.cs.hioa.no/~s309856/jsonoutSystems.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                }
                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                java.lang.System.out.println("Output from Server .... \n");
                while ((s = br.readLine()) != null) {
                    output = output + s;
                }
                conn.disconnect();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return output;
        }

        public List<Machine> getMachineList() throws JSONException {
            JSONArray jsonArray = new JSONArray(chooseCard());

            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject jsonobject = jsonArray.getJSONObject(j);
                String serial_number = jsonobject.getString("serial_number");
                String hospital = jsonobject.getString("hospital");
                String department = jsonobject.getString("department");
                Machine machine = new Machine();
                machine.setSerialNumber(serial_number);
                machine.setHospital(hospital);
                machine.setDepartment(department);
                machineArrayList.add(machine);
            }
            System.out.println(machineArrayList);
            return machineArrayList;
        }

        public List<String> getMachineStrings() throws JSONException {
            String machine = "";
            JSONArray jsonArray = new JSONArray(chooseCard());

            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject jsonobject = jsonArray.getJSONObject(j);
                String serial_number = jsonobject.getString("serial_number");
                String hospital = jsonobject.getString("hospital");
                String department = jsonobject.getString("department");
                machine += serial_number + "-" + hospital + "-" + department;
                stringArrayList_Machine.add(machine);
            }
            System.out.println(stringArrayList_Machine);
            return stringArrayList_Machine;
        }


    }

    class Machine {
        String serialNumber;
        String hospital;
        String department;

        public Machine(String serialNumber, String hospital, String department) {
            this.serialNumber = serialNumber;
            this.hospital = hospital;
            this.department = department;
        }

        public Machine() {
        }

        public String getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }

        public String getHospital() {
            return hospital;
        }

        public void setHospital(String hospital) {
            this.hospital = hospital;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        @Override
        public String toString() {
            return "Machine{" +
                    "serialNumber='" + serialNumber + '\'' +
                    ", hospital='" + hospital + '\'' +
                    ", department='" + department + '\'' +
                    '}';
        }
}
