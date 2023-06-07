package com.edelweiss.stationapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.edelweiss.stationapp.NotificationService.SharedPrefManager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String refreshedToken,android_id;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        sharedPreferences = getSharedPreferences("skin", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        refreshedToken = SharedPrefManager.getInstance(this).getDeviceToken();
        android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);


        CallAPi();
    }

    private void CallAPi() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        StringRequest strReq = new StringRequest(Constant.BaseUrl, response -> {
            Log.d("ghgrrrrrr", response);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                String sucess = jsonObject.getString("status");
                if (sucess.equalsIgnoreCase("success")) {

                    JSONArray result = jsonObject.getJSONArray("app");
                    JSONObject obj = result.getJSONObject(0);

                    String id = obj.getString("id");
                    String slogan = obj.getString("slogan");
                    String slogan_time = obj.getString("slogan_time");
                    String radio_stream = obj.getString("radio_stream");
                    String radio_stream_64 = obj.getString("radio_stream_64");
                    String tv_stream = obj.getString("tv_stream");
                    String website = obj.getString("website");
                    String facebook = obj.getString("facebook");
                    String whatsapp = obj.getString("whatsapp");
                    String youtube = obj.getString("youtube");
                    String url_instagram = obj.getString("instagram");
                    String url_tiktok = obj.getString("tiktok");
                    String url_twitter = obj.getString("twitter");
                    String admob_id = obj.getString("admob_id");
                    String admob_banner_id = obj.getString("admob_banner_id");
                    String admob_interstitial_id = obj.getString("admob_interstitial_id");
                    String admob_native_id = obj.getString("admob_native_id");
                    String start_color = obj.getString("start_color");
                    String end_color = obj.getString("end_color");
                    String background_image = obj.getString("background_image");
                    String about_us = obj.getString("about_us");
                    String our_service = obj.getString("our_service");
                    String cover_image = obj.getString("cover_image");
                    String privacy_policy = obj.getString("privacy_policy");
                    String facebook_id = obj.getString("facebook_id");

                    editor.putString("id", id);
                    editor.putString("slogan", slogan);
                    editor.putString("slogan_time", slogan_time);
                    editor.putString("radio_stream", radio_stream);
                    editor.putString("radio_stream_64", radio_stream_64);
                    editor.putString("tv_stream", tv_stream);
                    editor.putString("website", website);
                    editor.putString("facebook", facebook);
                    editor.putString("whatsapp", whatsapp);
                    editor.putString("youtube", youtube);
                    editor.putString("url_instagram", url_instagram);
                    editor.putString("url_tiktok", url_tiktok);
                    editor.putString("url_twitter", url_twitter);
                    editor.putString("admob_id", admob_id);
                    editor.putString("admob_banner_id", admob_banner_id);
                    editor.putString("admob_interstitial_id", admob_interstitial_id);
                    editor.putString("admob_native_id", admob_native_id);
                    editor.putString("start_color", start_color);
                    editor.putString("end_color", end_color);
                    editor.putString("background_image", background_image);
                    editor.putString("about_us", about_us);
                    editor.putString("our_service", our_service);
                    editor.putString("cover_image", cover_image);
                    editor.putString("privacy_policy", privacy_policy);
                    editor.putString("facebook_id", facebook_id);
                    editor.commit();

                    // editor.putString("facebook_id", facebook_id);
                     CallAPi2(id);
                    // }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
        });
        requestQueue.add(strReq);
    }

    private void CallAPi2(String theme) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        StringRequest strReq = new StringRequest(Constant.Noti_Url+"&device_id=" + android_id+"&device_token="+refreshedToken+"&appid="+theme, response -> {
            Log.d("ghgrrrrrr", response);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                String sucess = jsonObject.getString("status");
                if (sucess.equalsIgnoreCase("1")) {
                   /* JSONArray array = jsonObject.getJSONArray("theme");
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject obj = array.getJSONObject(i);
                        String grad_start_color = obj.getString("grad_start_color");
                        String grad_end_color = obj.getString("grad_end_color");

                        editor.putString("start_color", grad_start_color);
                        editor.putString("end_color", grad_end_color);



                    }*/


                }
                Intent qi = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(qi);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
        });
        requestQueue.add(strReq);
    }

}