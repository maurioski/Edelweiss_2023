package com.edelweiss.stationapp;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;


import java.util.Locale;

public class YourApplicationName extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex.install(getBaseContext());

       String country= Locale.getDefault().getDisplayLanguage();

       if (country.equalsIgnoreCase("Spanish")){
           Locale locale = new Locale("es");
           Locale.setDefault(locale);
           Configuration config = new Configuration();
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
               config.setLocale(locale);
           } else {
               config.locale = locale;
           }
           getResources().updateConfiguration(config,
                   getResources().getDisplayMetrics());
       }else {
           Locale locale = new Locale("en");
           Locale.setDefault(locale);
           Configuration config = new Configuration();
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
               config.setLocale(locale);
           } else {
               config.locale = locale;
           }
           getResources().updateConfiguration(config,
                   getResources().getDisplayMetrics());
       }
    }
}
