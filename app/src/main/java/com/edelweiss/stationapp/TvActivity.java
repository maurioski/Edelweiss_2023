package com.edelweiss.stationapp;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;


public class TvActivity extends AppCompatActivity implements Player.Listener {

    private SimpleExoPlayer player;
    PlayerView playerView;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private String url;
    String start_color = "";
    String end_color = "";
    private LinearLayout background;
    boolean rotation = false;
    ProgressBar progressBar;
    Handler handler = new Handler();
    //WebView wv1;

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //First Hide other objects (listview or recyclerview), better hide them using Gone.
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            playerView.setLayoutParams(params);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //unhide your objects here.
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            playerView.setLayoutParams(params);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv);
        playerView = findViewById(R.id.exoplayerView);
        url = getIntent().getStringExtra("m3u8");

        background = findViewById(R.id.background);
        progressBar = findViewById(R.id.progressBar);
        ImageView back = findViewById(R.id.back);
        ImageView Iv_video_resize = findViewById(R.id.Iv_video_resize);

       /* wv1=findViewById(R.id.webview);
        WebSettings settings = wv1.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        wv1.setVerticalScrollBarEnabled(true);
        wv1.setHorizontalScrollBarEnabled(true);
        wv1.setWebViewClient(new MyBrowser());
        wv1.loadData("", "text/html", "UTF-8");
        wv1.loadUrl("https://radiogratitud.com/chat/");*/


        handler.postDelayed(() -> {
            back.setVisibility(View.GONE);
        }, 5000);

        playerView.setOnClickListener(view -> {
            back.setVisibility(View.VISIBLE);

            handler.postDelayed(() -> {
                back.setVisibility(View.GONE);
            }, 5000);
        });

        start_color = getSharedPreferences("skin", MODE_PRIVATE).getString("start_color", "");
        end_color = getSharedPreferences("skin", MODE_PRIVATE).getString("end_color", "");

        ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {
                LinearGradient linearGradient = new LinearGradient(0, 0, width, height,
                        new int[]{
                                Color.parseColor("#000000"),
                                Color.parseColor("#000000")
                        },
                        new float[]{0, 1},
                        Shader.TileMode.REPEAT);
                return linearGradient;
            }
        };
        PaintDrawable paint = new PaintDrawable();
        paint.setShape(new RectShape());
        paint.setShaderFactory(shaderFactory);
        background.setBackground(paint);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        rotation = true;


        back.setOnClickListener(view -> {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            releasePlayer();
            onBackPressed();
        });

        Iv_video_resize.setOnClickListener(view -> {
            if (rotation == true) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                rotation = false;
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                rotation = true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        releasePlayer();
    }

    private static class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }

    public void initializePlayer() {

        if (player == null) {
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(this);
            trackSelector.setParameters(trackSelector.buildUponParameters().setMaxVideoSizeSd());
            player = new SimpleExoPlayer.Builder(this).setTrackSelector(trackSelector).build();
        }

        Log.e("eeeee", url);

        Uri uri = Uri.parse(url);

        HttpDataSource.Factory httpDataSourceFactory = new DefaultHttpDataSource.Factory().
                setUserAgent(getString(R.string.app_name)).setAllowCrossProtocolRedirects(true);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(),
                httpDataSourceFactory);

        player.addListener(this);
        MediaSource videoSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(uri));
        player.prepare(videoSource);
        playerView.setPlayer(player);

        player.setPlayWhenReady(playWhenReady);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, int reason) {
    }

    @Override
    public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
    }

    @Override
    public void onIsLoadingChanged(boolean isLoading) {
    }

    @Override
    public void onPlaybackStateChanged(int state) {
        if (state == Player.STATE_BUFFERING) {
            progressBar.setVisibility(View.VISIBLE);
        } else if (state == Player.STATE_READY) {
            progressBar.setVisibility(View.GONE);
            playerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPlayWhenReadyChanged(boolean playWhenReady, int reason) {
    }

    @Override
    public void onPlaybackSuppressionReasonChanged(int playbackSuppressionReason) {
    }

    @Override
    public void onIsPlayingChanged(boolean isPlaying) {
    }

}