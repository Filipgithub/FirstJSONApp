package com.example.uino.firstjsonapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> countryString;
    ArrayAdapter<String> adapter;
    GetListOfCountryNames countryNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list);

        countryString = new ArrayList<>();

         countryNames= new GetListOfCountryNames();
            countryNames.execute();

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, countryString);

        listView.setAdapter(adapter);





    }

    private class GetListOfCountryNames extends AsyncTask<String, Void, String> {

        /**
         * Retrieving data in background.
         */
        @Override
        protected String doInBackground(String... params) {

            String responseData = "";

            try {

                // Creating http request
                URL obj = new URL("http://services.groupkt.com/country/get/all");
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                //add request header
                con.setRequestMethod("GET");

                // Send post request
//                con.setDoOutput(true);
//                con.setDoInput(true);
//
//                String urlParameters = "num1=" + params[0] + "&num2=" + params[1];
//
//                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//                wr.writeBytes(urlParameters);
//                wr.flush();
//                wr.close();

                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                while ((line = br.readLine()) != null)
                    responseData += line;
                con.disconnect();

                int responseCode = con.getResponseCode();

                System.out.println("Response Code : " + responseCode);
                System.out.println("Response Data: " + responseData);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseData;
        }

        /**
         * Checks for response code, if its 200 - finish this activity, report for connection lost otherwise
         * @param result Result of doInBackground for checking response code
         */
        @Override
        protected void onPostExecute(String result) {

            try {

                JSONObject response = new JSONObject(result);
                JSONObject restResponse = response.getJSONObject("RestResponse");

                JSONArray countries = restResponse.getJSONArray("result");

                for (int i = 0; i < countries.length(); i++) {

                    JSONObject country = countries.getJSONObject(i);
                    String countryName = country.getString("name");
                    countryString.add(countryName);
                }
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
