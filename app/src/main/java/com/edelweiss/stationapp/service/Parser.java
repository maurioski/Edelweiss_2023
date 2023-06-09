package com.edelweiss.stationapp.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ST_002 on 24-08-2016.
 */
public class Parser {

    public static String parse(String urlM3u) {

        String ligne = null;

        try {
            URL urlPage = new URL(urlM3u);
            HttpURLConnection connection = (HttpURLConnection) urlPage.openConnection();
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            //StringBuffer stringBuffer = new StringBuffer();

            while ((ligne = bufferedReader.readLine()) != null) {
                if (ligne.contains("https")) {
                    connection.disconnect();
                    bufferedReader.close();
                    inputStream.close();
                    return ligne;
                }else {
                    connection.disconnect();
                    bufferedReader.close();
                    inputStream.close();
                    return ligne;
                }
            }

            connection.disconnect();
            bufferedReader.close();
            inputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
