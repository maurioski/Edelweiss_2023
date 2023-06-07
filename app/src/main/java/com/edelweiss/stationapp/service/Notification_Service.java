package com.edelweiss.stationapp.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.edelweiss.stationapp.Constant;
import com.edelweiss.stationapp.MainActivity;
import com.edelweiss.stationapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class Notification_Service extends Service {


    public static Notification_Service service;
    int icon;

    @Override
    public void onCreate() {
        super.onCreate();

        MediaSessionCompat mediaSession = new MediaSessionCompat(this, getClass().getSimpleName());
        //transportControls = mediaSession.getController().getTransportControls();
        mediaSession.setActive(true);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                //.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, Constant.ARTIST_NAME)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, Constant.ARTIST_NAME)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, Constant.ALBUM_NAME)
                .build());
        // mediaSession.setCallback(mediasSessionCallback);

    }

    static public Notification_Service getInstance() {
        if (service == null) {
            service = new Notification_Service();
        }
        return service;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final String action = intent == null ? null : intent.getAction();
        Log.d("APP", "service action:" + action);
        if (Constant.ACTION_PLAY_STICKING.equals(action)) {
            CustomNotification();

        } else if (Constant.ACTION_PAUSE_STICKING.equals(action)) {
            stopForeground(true);
            stopSelf();
        }


        return START_NOT_STICKY;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }


    @SuppressLint("UnspecifiedImmutableFlag")
    public void CustomNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Bitmap largeIcon;
        if (Constant.IMAGE_URL == null) {
            largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo_round);
        } else if (Constant.IMAGE_URL.equalsIgnoreCase("")) {
            largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.noti2);
        } else {
            largeIcon = getBitmapFromURL(Constant.IMAGE_URL);
        }

        Intent layout = new Intent(getApplicationContext(), MainActivity.class);
        layout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingLayoutIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingLayoutIntent = PendingIntent.getActivity(this, 0, layout, PendingIntent.FLAG_MUTABLE);
        } else {
            pendingLayoutIntent = PendingIntent.getActivity(this, 0, layout, PendingIntent.FLAG_ONE_SHOT);
        }

        int icon;
        //for playpause button click
        Intent playpauseButton = new Intent(Constant.RECIEVER_NOTI_PLAYPAUSE1);
        playpauseButton.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingPlayPauseIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingPlayPauseIntent = PendingIntent.getBroadcast(this, 0, playpauseButton, PendingIntent.FLAG_MUTABLE);
        } else {
            pendingPlayPauseIntent = PendingIntent.getBroadcast(this, 0, playpauseButton, PendingIntent.FLAG_ONE_SHOT);
        }

        if (Constant.IS_PLAYING) {
            icon = android.R.drawable.ic_media_pause;
        } else {
            icon = android.R.drawable.ic_media_play;
        }

        Intent crossButton = new Intent(Constant.RECEIVER_NOTI_CROSS);
        crossButton.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent crossButtonIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            crossButtonIntent = PendingIntent.getBroadcast(this, 0, crossButton, PendingIntent.FLAG_MUTABLE);
        } else {
            crossButtonIntent = PendingIntent.getBroadcast(this, 0, crossButton, PendingIntent.FLAG_ONE_SHOT);
        }

        MediaSessionCompat mediaSession = new MediaSessionCompat(this, getClass().getSimpleName());
        mediaSession.setActive(true);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, Constant.ARTIST_NAME)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, Constant.ALBUM_NAME)
                .build());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel notificationChannel = new NotificationChannel("c_id", getString(R.string.app_name), importance);
            notificationChannel.enableVibration(false);
            notificationChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "c_id")
                .setContentTitle(Constant.ALBUM_NAME)
                .setContentText(Constant.ARTIST_NAME)
                .setLargeIcon(largeIcon)
                .setContentIntent(pendingLayoutIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.logo_icon)
                .addAction(icon, "pause", pendingPlayPauseIntent)
                .addAction(R.drawable.ic_close_black, "stop", crossButtonIntent)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setVibrate(new long[]{0L})
                .setWhen(System.currentTimeMillis())
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.getSessionToken())
                        .setShowActionsInCompactView(0, 1)
                        .setCancelButtonIntent(crossButtonIntent));

        Notification notification = builder.build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(1, notification);
        } else {
            startForeground(1, notification);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
