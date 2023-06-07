package com.edelweiss.stationapp.service;

import static com.edelweiss.stationapp.IXRadioSettingConstants.ONE_MINUTE;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.edelweiss.stationapp.Constant;
import com.edelweiss.stationapp.MainActivity;
import com.edelweiss.stationapp.R;
import com.edelweiss.stationapp.RadioSettingManager;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.util.Util;


import java.util.ArrayList;
import java.util.Arrays;


public class ExoService extends Service {

    private static Context context;
    private static String station;
    private static ExoService service;
    public static ExoPlayer exoPlayer;
    private WifiManager.WifiLock wifiLock;
    private Handler mHandlerSleep = new Handler();
    private int mMinuteCount;


    static public void initialize(Context context, String station) {
        ExoService.context = context;
        ExoService.station = station;
    }

    static public ExoService getInstance() {
        if (service == null) {
            service = new ExoService();
        }
        return service;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public Player.Listener onCompletionListener = new Player.Listener() {

        @Override
        public void onMetadata(@NonNull Metadata metadata) {
            new Handler().postDelayed(() -> getMetadata(metadata), 1000);
            Log.d("METADATA_TAG", "onMetadata : " + metadata);
        }

        @Override
        public void onPlayerError(PlaybackException error) {
            Player.Listener.super.onPlayerError(error);
            Toast.makeText(getApplicationContext(), "The radio station is not available at the moment, please try again later.\n", Toast.LENGTH_LONG).show();

        }

        @Override
        public void onPlaybackStateChanged(int playbackState) {
            if (playbackState == Player.STATE_ENDED) {
                Log.e("---state----", "get" + exoPlayer.getPlaybackState());
                Log.e("---current pos----", "get" + exoPlayer.getCurrentPosition());
                Log.e("---Duration pos----", "get" + exoPlayer.getDuration());
                //ExoService.exoPlayer.removeListener(this);
            } else if (playbackState == Player.STATE_READY) {
                Log.e("---Duration pos----", "" + exoPlayer.getDuration());
                Log.e("---current pos----", "" + exoPlayer.getCurrentPosition());
                //Constant.finalTime = exoPlayer.getDuration();
                //myHandler.postDelayed(MainActivity.UpdateSongTime, 100);
            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {
        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
        }

        @Override
        public void onPlaybackParametersChanged(@NonNull PlaybackParameters playbackParameters) {
        }
    };

    private void getMetadata(Metadata metadata) {

        if (!metadata.get(0).toString().equals("")) {
            String data = metadata.get(0).toString().replace("ICY: ", "");
            ArrayList<String> arrayList = new ArrayList(Arrays.asList(data.split(",")));
            String[] mediaMetadata = arrayList.get(0).split("=");

            String streamTitle, title;
            if (arrayList.get(0).contains("null")) {
                streamTitle = "";
                Constant.ARTIST_NAME = "";
                Constant.ALBUM_NAME = "";

                MainActivity.inst.AlbumArtParsing(Constant.ARTIST_NAME, Constant.ALBUM_NAME);

            } else {
                streamTitle = mediaMetadata[1].replace("\"", "");
            }
            Log.d("METADATA_TAG", "Metadata Info : " + streamTitle);


            if (streamTitle.equalsIgnoreCase("")) {
                //((MainActivity) context).changeSongName(obj.category_name);
                Constant.ARTIST_NAME = "";
                Constant.ALBUM_NAME = "";

                MainActivity.inst.AlbumArtParsing(Constant.ARTIST_NAME, Constant.ALBUM_NAME);

            } else {

                if (streamTitle.contains("http")) {
                    title = streamTitle.replaceAll("http?://\\S+\\s?", "");

                    Log.d("title", title);
                    if (title.contains("StreamUrl=")) {

                        title = title.replace("StreamUrl=", "");
                        Log.d("title1", title);
                    }
                } else if (streamTitle.contains("StreamUrl=")) {
                    title = streamTitle.replace("StreamUrl=", "");
                } else {
                    title = streamTitle;
                }

                if (title.contains("-")) {
                    String artist_name = title.substring(0, title.indexOf('-'));
                    String album_name = title.substring(title.indexOf('-') + 1);
                    Constant.ARTIST_NAME = artist_name.trim();
                    Constant.ALBUM_NAME = album_name.trim();

                    MainActivity.inst.AlbumArtParsing(artist_name, album_name);

                }
            }
        }
    }


    @Override
    public void onCreate() {
        try {
            exoPlayer = new ExoPlayer.Builder(context).build();
            exoPlayer.addListener(onCompletionListener);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final String action = intent == null ? null : intent.getAction();
        Log.d("APP", "service action:" + action);

        if (Constant.ACTION_PLAY_STICKING.equals(action)) {
            doInBackground();

        } else if (Constant.ACTION_PAUSE_STICKING.equals(action)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                stopForeground(Service.STOP_FOREGROUND_DETACH);
            } else {
                stopForeground(true);
            }
            stopSelf();

        } else if (Constant.ACTION_UPDATE_SLEEP_MODE.equals(action)) {
            startSleepMode();
        }

        return START_STICKY;
    }

    private void startSleepMode() {
        try {
            int minute = RadioSettingManager.getSleepMode(this);
            mHandlerSleep.removeCallbacksAndMessages(null);
            if (minute > 0) {
                this.mMinuteCount = minute * ONE_MINUTE;
                startCountSleep();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startCountSleep() {
        try {
            if (mMinuteCount > 0) {
                mHandlerSleep.postDelayed(() -> {
                    mMinuteCount = mMinuteCount - 1000;
                    //sendMusicBroadcast(Constant.ACTION_UPDATE_SLEEP_MODE,mMinuteCount);
                    if (mMinuteCount <= 0) {
                        mHandlerSleep.removeCallbacksAndMessages(null);
                        //releaseMedia(true);
                        //sendMusicBroadcast(ACTION_STOP,-1);
                        //updateSleepMode(0);
                        clearNotification();
                        stop();

                    } else {
                        startCountSleep();
                    }
                }, 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void resetTimer() {
        try {
            RadioSettingManager.setSleepMode(this, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void pause() {
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);
        }
    }

    public void start() {
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(true);
        }
    }

    public void onDestroy() {
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.stop();
            resetTimer();
            clearNotification();
            Log.e("Destroyed", "Called");
        }
    }

    public void stop() {
        if (exoPlayer != null && exoPlayer.getPlayWhenReady()) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
            wifiLock.release();
            stopForeground(true);
            stopSelf();
            resetTimer();

            //MainActivity.slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            clearNotification();

        }
    }

    public void clearNotification() {
        Intent k = new Intent(context, Notification_Service.class);
        k.setAction(Constant.ACTION_PAUSE_STICKING);
        context.stopService(k);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
    }


    private void doInBackground() {
        try {
            wifiLock = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE))
                    .createWifiLock(WifiManager.WIFI_MODE_FULL, "RadiophonyLock");
            wifiLock.acquire();
            Uri uri = Uri.parse(station);

            DataSource.Factory dataSourceFactory = new DefaultHttpDataSource.Factory()
                    .setUserAgent(Util.getUserAgent(getApplicationContext(), getString(R.string.app_name)));

            if (station.endsWith(".m3u8")) {
                HlsMediaSource hlsMediaSource = new HlsMediaSource.Factory(dataSourceFactory)
                        .setAllowChunklessPreparation(true)
                        .createMediaSource(MediaItem.fromUri(uri));

                exoPlayer.setMediaSource(hlsMediaSource);
            } else {
                MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(MediaItem.fromUri(uri));
                exoPlayer.setMediaSource(mediaSource);
            }

            exoPlayer.prepare();
            exoPlayer.setPlayWhenReady(true);
        } catch (IllegalArgumentException | SecurityException | IllegalStateException e1) {
            // Manejar la excepción adecuadamente
        }
    }
}
