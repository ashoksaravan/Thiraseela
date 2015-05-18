package com.ashoksm.thiraseela.wsclient;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class WSClient {

    public static String execute(String json, String strUrl) {
        try {
            URL url = new URL(strUrl);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json; charset=utf8");

            // POST Data
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json);
            wr.flush();

            // Read Server Response
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            Log.e("WSClient", e.getLocalizedMessage());
        }
        return "";
    }
}
