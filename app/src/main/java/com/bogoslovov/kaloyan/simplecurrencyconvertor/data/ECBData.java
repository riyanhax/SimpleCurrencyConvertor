package com.bogoslovov.kaloyan.simplecurrencyconvertor.data;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Kaloyan on 15.11.2016 г..
 */

public class ECBData extends AsyncTask<Object, Object, Void> {
    public static final int CONNECTION_TIMEOUT= Integer.parseInt(System.getProperty("ECB.connection.timeout", "10000"));
    public static SharedPreferences sharedPreferences;


    protected Void doInBackground(Object... voids) {
        getData();
        return null;
    }

    protected void onPostExecute() {
    }

    public void getData() {
        String url = makeURL();
        URLConnection connection =setConnectionWithECB(url);
        getInformationFromECB(connection);
    }

    private String makeURL() {
        return "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
    }

    private URLConnection setConnectionWithECB(String url) {
        URLConnection connection = null;
        try {
            URL request = new URL(url);
            connection = request.openConnection();
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setReadTimeout(CONNECTION_TIMEOUT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private void getInformationFromECB(URLConnection connection) {
        try {
            InputStreamReader is = new InputStreamReader(connection.getInputStream());
            BufferedReader br = new BufferedReader(is);

            for (int i = 0; i < 7; i++) {
                br.readLine();
            }
            parseData(br);
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("getInformationFromECB exception");
        }
    }

    public void parseData(BufferedReader br) throws IOException {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("EUR", "1");
        String line = br.readLine();
        String date = line.substring(14, 24);
        String remaining;
        String symbol;
        String rate;
        for (int i = 0; i < 31; i++) {
            line = br.readLine();
            remaining = line.substring(19);
            symbol = remaining.substring(0,3);
            rate = remaining.substring(11, remaining.length()-3);
            editor.putString(symbol, rate);

        }
        System.out.println("data refreshed");
        editor.commit();
        br.close();
    }
}
