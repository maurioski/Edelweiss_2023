package com.edelweiss.stationapp;

import static com.edelweiss.stationapp.service.ExoService.exoPlayer;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.icu.util.Calendar;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.edelweiss.stationapp.NotificationService.SharedPrefManager;
import com.edelweiss.stationapp.service.ExoService;
import com.edelweiss.stationapp.service.Notification_Service;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AudioManager.OnAudioFocusChangeListener {


    private static final int NOTIFICATION_ID = 1;

    private static final String PREF_SHOW_DIALOG = "new_quality";

    private SharedPreferences sharedPreferences;


    private TextView clockTextView;

    private Runnable runnable;

    private static final int REQUEST_CODE_APP_UPDATE = 100;

    private static final String PREFS_NAME = "MyPrefs";

    private static final String UPDATE_DIALOG_SHOWN = "UpdateDialogShown";

    private AppUpdateManager appUpdateManager;
    private DatabaseReference programasRef;
    private TextView slogan;
    private Handler handler;
    private Runnable updateProgramRunnable;


    public static class Programa {
        private String hora_inicio;
        private String nombre;

        public Programa() {
            // Constructor sin argumentos requerido para Firebase
        }

        public String getHoraInicio() {
            return hora_inicio;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
    }


    int MAX_SLEEP_MODE = 120;
    int MIN_SLEEP_MODE = 5;
    int STEP_SLEEP_MODE = 5;
    private String url = "";
    private final String title = "";
    private String _facebook = "";
    private final String _Youtube = "";
    private String _whatsapp = "";
    private final String _web = "";
    private String m3u8 = "";
    private String _slogan = "";
    private String facebook_id = "";
    private String url_instagram = "";
    private final String url_twitter = "";
    private final String url_img = "";
    private final String cover_img = "";
    private final String admob_native_id = "";
    @SuppressLint("StaticFieldLeak")
    public static ImageView Share;
    @SuppressLint("StaticFieldLeak")
    public static ImageView iv_playpause;
    @SuppressLint("StaticFieldLeak")
    public static ImageView facebook;
    @SuppressLint("StaticFieldLeak")
    public static ImageView whatsapp;
    @SuppressLint("StaticFieldLeak")
    public static ImageView timer;
    @SuppressLint("StaticFieldLeak")
    public static ImageView web;
    @SuppressLint("StaticFieldLeak")
    public static ImageView AlbumArt;
    @SuppressLint("StaticFieldLeak")
    public static ImageView Tv;
    @SuppressLint("StaticFieldLeak")
    public static ImageView _insta;
    private SeekBar Vol_seekbar;
    private AudioManager audioManager = null;
    @SuppressLint("StaticFieldLeak")
    public static RelativeLayout backcolor;
    private EqualizerView equalizer;
    private TextView Tv_Artist_Name;
    public static Data_Reciever1 receiver1;
    public static Cross_Receiver reciever_cross;
    String start_color = "#000000";
    String end_color = "#007cf7";
    ImageView background_image;
    String refreshedToken,android_id;
    @SuppressLint("StaticFieldLeak")
    public static MainActivity inst;


    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }


    private void CallAPi2(String id) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        StringRequest strReq = new StringRequest(Constant.Noti_Url+"&device_id=" + android_id+"&device_token="+refreshedToken+"&appid="+id, response -> {
            Log.d("ghgrrrrrr", response);
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(response);
                jsonObject.getString("status");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
        });
        requestQueue.add(strReq);
    }

    @Override
    protected void onResume() {
        super.onResume();

        handler.postDelayed(runnable, 0); // Iniciar la actualización del reloj

        // Verificar si la instalación fue completada y realizar acciones adicionales
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(appUpdateInfo -> {
                    if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                        showUpdateCompleteDialog();
                    }
                });
    }

    private boolean isAppInstalled(String packageName) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); // Detener la actualización del reloj
    }

    private void updateClock() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(new Date());
        clockTextView.setText(currentTime);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint({"MissingInflatedId", "NonConstantResourceId", "PrivateResource", "HardwareIds"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean new_quality = sharedPreferences.getBoolean(PREF_SHOW_DIALOG, true);

        if (new_quality) {
            new_quality();
        }

        configureQualityButton();

        clockTextView = findViewById(R.id.clockTextView);

        // Inicializar el Handler y el Runnable para actualizar la hora
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                updateClock();
                handler.postDelayed(this, 1000); // Actualizar cada segundo
            }
        };

        // Inicializar el AppUpdateManager
        appUpdateManager = AppUpdateManagerFactory.create(this);

        // Registrar un listener para el estado de instalación
        appUpdateManager.registerListener(installStateUpdatedListener);

        // Comprobar si hay actualizaciones disponibles
        checkForUpdates();

        //revisar version app
        TextView versionTextView = findViewById(R.id.version_textview);
        String versionName = "";
        try {
            versionName = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        String versionText = "Version " + versionName;
        versionTextView.setText(versionText);

        //mensaje para calificar la app
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int contadorInicio = sharedPreferences.getInt("contadorInicio", 0);
        contadorInicio++;

        if (contadorInicio == 15) { //numero de aperturas para mostrar el mensaje
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Calificame!");
            builder.setMessage("Por favor, califica nuestra aplicación");

            // Agregar emoticones del sistema para representar estrellas amarillas
            TextView textView = new TextView(this);
            textView.setText(getString(R.string.five_star_emoji));
            textView.setTextSize(24);
            textView.setGravity(Gravity.CENTER);
            builder.setView(textView);

            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Abrir la página de calificaciones en Google Play Store
                    String packageName = getPackageName();
                    String playStoreUrl = "https://play.google.com/store/apps/details?id=" + packageName + "&reviewId=0";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(playStoreUrl));
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Cancelar", null);

            AlertDialog dialog = builder.create();
            dialog.show();

            // Establecer el fondo redondeado para el cuadro de diálogo
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corners);

            contadorInicio = 0; // Reiniciar el contador
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("contadorInicio", contadorInicio);
        editor.apply();



        // Iniciador OneSignal
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        String onesignalAppId = getString(R.string.onesignal_app_id);
        OneSignal.setAppId(onesignalAppId);
        OneSignal.promptForPushNotifications();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTheme(R.style.AppTheme_ActionBar);


        // Inicializa la Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Apply custom style
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        toolbar.setTitleTextAppearance(this, R.style.MyMenuStyle);

        // Configura el DrawerLayout y el ActionBarDrawerToggle
        @SuppressLint("CutPasteId") DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        //DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        @SuppressLint("CutPasteId") View mainContent = findViewById(R.id.drawer_layout);

        mainContent.setOnTouchListener(new View.OnTouchListener() {
            private final GestureDetector gestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.openDrawer(GravityCompat.START);
                    }
                    return true;
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        // Configuración del NavigationView
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            // Manejar los clicks en los items del NavigationView
            switch (item.getItemId()) {
                case R.id.facebook:
                    boolean isFacebookAppInstalled = isAppInstalled("com.facebook.katana");
                    if (isFacebookAppInstalled) {
                        String facebookId = "fb://profile/" + facebook_id;
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookId)));
                        } catch (Exception e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(_facebook)));
                        }
                    } else {
                        String message = getString(R.string.app_not_installed);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(_facebook)));
                    }
                    break;
                case R.id.qr:
                    showQrDialog();
                    break;
                case R.id.about1:
                    showDialog1();
                    break;
                case R.id.whatsapp:
                    try {
                        String text = getString(R.string.whatsapp_message);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("whatsapp://send?phone=" + _whatsapp + "&text=" + text));
                        PackageManager pm = getPackageManager();
                        List<ResolveInfo> resInfo = pm.queryIntentActivities(intent, 0);
                        if (resInfo.size() > 0) {
                            startActivity(intent);
                        } else {
                            String message = getString(R.string.app_not_installed);
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            String url = "https://api.whatsapp.com/send?phone=" + _whatsapp + "&text=" + text;
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.insta:
                    boolean isInstagramAppInstalled = isAppInstalled("com.instagram.android");
                    if (isInstagramAppInstalled) {
                        String instagramId = "https://instagram.com/_u/" + url_instagram;
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(instagramId)));
                        } catch (Exception e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url_instagram)));
                        }
                    } else {
                        String message = getString(R.string.app_not_installed);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url_instagram)));
                    }
                    break;
                case R.id.vivo:
                    if (exoPlayer != null) {
                        iv_playpause.setImageResource(R.drawable.play);
                        Constant.IS_PLAYING = false;
                        ExoService.getInstance().pause();
                        clearNotification();
                    }
                    if (m3u8 == null || m3u8.isEmpty()) {
                        Toast.makeText(MainActivity.this, "El archivo m3u8 no está disponible", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(MainActivity.this, TvActivity.class);
                        intent.putExtra("m3u8", m3u8);
                        // Ejecutar la tarea en el hilo principal
                        runOnUiThread(() -> startActivity(intent));
                    }
                    break;
                case R.id.telefono:
                    // Llamar a un número de teléfono
                    String telefono = getString(R.string.telefono);
                    Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                    dialIntent.setData(Uri.parse("tel:" + telefono));
                    startActivity(dialIntent);
                    break;
                case R.id.email:
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", getString(R.string.email_destino), null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Asunto del correo");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Contenido del correo");
                    startActivity(Intent.createChooser(emailIntent, "Enviar correo electrónico"));
                    break;
                //case R.id.web:
                //    Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_open)));
                //    startActivity(webIntent);
                //    break;
            }
            return false;
        });

        Share = findViewById(R.id.share);
        facebook = findViewById(R.id.facebook);
        whatsapp = findViewById(R.id.whatsapp);
        //web = findViewById(R.id.web);
        iv_playpause = findViewById(R.id.iv_playpause);
        Vol_seekbar = findViewById(R.id.seekBar);
        backcolor = findViewById(R.id.backcolor);
        equalizer = findViewById(R.id.equalizer);
        TextView tv_Song_Name = findViewById(R.id.song_name);
        _insta = findViewById(R.id.insta);
        tv_Song_Name.setSelected(true);
        AlbumArt = findViewById(R.id.albumart);
        Tv = findViewById(R.id.tv);
        Share = findViewById(R.id.share);
        timer = findViewById(R.id.timer);
        background_image = findViewById(R.id.background_image);

        refreshedToken = SharedPrefManager.getInstance(this).getDeviceToken();
        android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);


        iv_playpause.setOnClickListener(view -> play());
        Share.setOnClickListener(view -> _Share());

        start_color = getSharedPreferences("skin", MODE_PRIVATE).getString("start_color", "");
        end_color = getSharedPreferences("skin", MODE_PRIVATE).getString("end_color", "");
        url = getSharedPreferences("skin", MODE_PRIVATE).getString("radio_stream", "");
        m3u8 = getSharedPreferences("skin", MODE_PRIVATE).getString("tv_stream", "");
        _facebook = getSharedPreferences("skin", MODE_PRIVATE).getString("facebook", "");
        _slogan = getSharedPreferences("skin", MODE_PRIVATE).getString("slogan", "");
        facebook_id = getSharedPreferences("skin", MODE_PRIVATE).getString("facebook_id", "");
        url_instagram = getSharedPreferences("skin", MODE_PRIVATE).getString("url_instagram", "");
        _whatsapp = getSharedPreferences("skin", MODE_PRIVATE).getString("whatsapp", "");
        String url_img = getSharedPreferences("skin", MODE_PRIVATE).getString("background_image", "");
        Constant.admob_id = getSharedPreferences("skin", MODE_PRIVATE).getString("admob_id", "");
        String id = getSharedPreferences("skin", MODE_PRIVATE).getString("id", "");

        CallAPi2(id);

        Glide.with(getApplicationContext())
                .load(url_img)
                .dontTransform()
                .placeholder(R.drawable.splash)
                .into(background_image);

        //if (cover_img!=null) {
        //    Glide.with(getApplicationContext())
        //            .load(cover_img)
        //            .dontTransform()
        //            .placeholder(R.drawable.logo_icon)
        //            .into(AlbumArt);
        //}

        if (m3u8.equalsIgnoreCase("")) {
            Tv.setVisibility(View.GONE);
        } else {
            Tv.setVisibility(View.VISIBLE);
        }
        if (start_color.equalsIgnoreCase("") || end_color.equalsIgnoreCase("")) {

        } else {
            ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {
                @Override
                public Shader resize(int width, int height) {
                    return new LinearGradient(0, 0, width, height,
                            new int[]{
                                    Color.parseColor(start_color),
                                    Color.parseColor(end_color)
                            },
                            new float[]{0, 1},
                            Shader.TileMode.REPEAT);
                }
            };
            PaintDrawable paint = new PaintDrawable();
            paint.setShape(new RectShape());
            paint.setShaderFactory(shaderFactory);
            backcolor.setBackground(paint);
        }
        SetList(url);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        Vol_seekbar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        Vol_seekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        receiver1 = new Data_Reciever1();
        registerReceiver(receiver1, new IntentFilter(Constant.RECIEVER_NOTI_PLAYPAUSE1));

        reciever_cross = new Cross_Receiver();
        registerReceiver(reciever_cross, new IntentFilter(Constant.RECEIVER_NOTI_CROSS));

        Vol_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        timer.setOnClickListener(view -> showDialogSleepMode());

        Tv.setOnClickListener(view -> {
            if (exoPlayer != null) {
                iv_playpause.setImageResource(R.drawable.play);

                Constant.IS_PLAYING = false;
                ExoService.getInstance().pause();
                clearNotification();

                Intent intent = new Intent(MainActivity.this, TvActivity.class);
                intent.putExtra("m3u8", m3u8);
                startActivity(intent);

            } else {
                Intent intent = new Intent(MainActivity.this, TvActivity.class);
                intent.putExtra("m3u8", m3u8);
                startActivity(intent);
            }
        });

        slogan = findViewById(R.id.slogan);
        slogan.setText(_slogan);

        programasRef = FirebaseDatabase.getInstance().getReference("dias_de_semana");

        // Obtener el día de la semana actual
        Calendar calendar = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            calendar = Calendar.getInstance();
        }
        assert calendar != null;
        int diaSemana = calendar.get(Calendar.DAY_OF_WEEK);
        String[] diasSemana = {"", "domingo", "lunes", "martes", "miércoles", "jueves", "viernes", "sábado"};
        String diaActual = diasSemana[diaSemana];
        String sloganTimeStr = getSharedPreferences("skin", MODE_PRIVATE).getString("slogan_time", "60000");
        Log.d("SloganTime", "Valor de slogan_time: " + sloganTimeStr);
        long sloganTime = Long.parseLong(sloganTimeStr);
        String sloganText = getSharedPreferences("skin", MODE_PRIVATE).getString("slogan", "");

        Handler handler = new Handler();
        Runnable updateTextViewRunnable = new Runnable() {
            @Override
            public void run() {
                final String[] nombrePrograma = {""};
                programasRef.child(diaActual).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean programaEncontrado = false;
                        for (DataSnapshot programaSnapshot : dataSnapshot.getChildren()) {
                            String horaInicio = programaSnapshot.child("hora_inicio").getValue(String.class);
                            String horaFinal = programaSnapshot.child("hora_final").getValue(String.class);
                            nombrePrograma[0] = programaSnapshot.child("nombre").getValue(String.class);
                            Log.d("FirebaseDB", "Nombre Programa: " + nombrePrograma[0]);
                            assert horaInicio != null;
                            assert horaFinal != null;
                            if (cumpleHorario(horaInicio, horaFinal)) {
                                programaEncontrado = true;
                                break;
                            }
                        }
                        if (!programaEncontrado) {
                            slogan.setText(sloganText); // Establecer el texto del slogan si no se encuentra un programa
                        } else {
                            slogan.setText(nombrePrograma[0]); // Actualizar el TextView con el nombre del programa
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this, "Error al obtener datos de Firebase", Toast.LENGTH_SHORT).show();
                    }
                });
                handler.postDelayed(this, sloganTime);
            }
        };
        handler.post(updateTextViewRunnable);
    }

    private void new_quality() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_quality, null);
        dialogBuilder.setView(dialogView);

        RadioButton radioButtonNoMostrar = dialogView.findViewById(R.id.radioButtonNoMostrar);
        Button btnAceptar = dialogView.findViewById(R.id.btnAceptar);

        AlertDialog dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();

        btnAceptar.setOnClickListener(v -> {
            if (radioButtonNoMostrar.isChecked()) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(PREF_SHOW_DIALOG, false);
                editor.apply();
            }
            dialog.dismiss();
        });
    }

    private void checkForUpdates() {
        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build();
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(appUpdateInfo -> {
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                            && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                        showUpdateDialog();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Error al verificar las actualizaciones", Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_APP_UPDATE) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(MainActivity.this, "Error al completar la actualización", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void showUpdateDialog() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDialogShown = preferences.getBoolean(UPDATE_DIALOG_SHOWN, false);

        if (!isDialogShown) {
            appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
                LayoutInflater inflater = LayoutInflater.from(this);
                View dialogView = inflater.inflate(R.layout.dialog_update, null);
                Button cancelButton = dialogView.findViewById(R.id.cancelup);
                Button acceptButton = dialogView.findViewById(R.id.aceptup);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(dialogView);
                builder.setCancelable(false);
                AlertDialog updateDialog = builder.create();
                updateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                cancelButton.setOnClickListener(view -> {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(UPDATE_DIALOG_SHOWN, true);
                    editor.apply();

                    updateDialog.dismiss(); // Cerrar el diálogo después de hacer clic en el botón "Cancelar"
                });
                acceptButton.setOnClickListener(view -> {
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo,
                                AppUpdateType.IMMEDIATE,
                                MainActivity.this,
                                REQUEST_CODE_APP_UPDATE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                    updateDialog.dismiss(); // Cerrar el diálogo después de hacer clic en el botón "Aceptar"
                });

                updateDialog.show();
            }).addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Error al obtener información de actualización", Toast.LENGTH_SHORT).show());
        }
    }

    private void showUpdateCompleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Actualización completada");

        // Crear un nuevo TextView para personalizar el estilo del mensaje
        TextView messageTextView = new TextView(this);
        messageTextView.setText("La aplicación se ha actualizado exitosamente. Reinicia la aplicación para aplicar los cambios.");
        messageTextView.setTextColor(Color.BLACK);
        messageTextView.setTextSize(16f);
        messageTextView.setPadding(20, 20, 20, 20);
        builder.setView(messageTextView);

        builder.setPositiveButton("Reiniciar", (dialog, which) -> {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        builder.setCancelable(false);
        builder.show();
    }


    private final InstallStateUpdatedListener installStateUpdatedListener = installState -> {
        if (installState.installStatus() == InstallStatus.DOWNLOADED) {
            showUpdateCompleteDialog();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean cumpleHorario(String horaInicioPrograma, String horaFinalPrograma) {
        Calendar calendar = Calendar.getInstance();
        int horaActual = calendar.get(Calendar.HOUR_OF_DAY);
        int minutoActual = calendar.get(Calendar.MINUTE);

        String[] inicioParts = horaInicioPrograma.split(":");
        int horaInicio = Integer.parseInt(inicioParts[0]);
        int minutoInicio = Integer.parseInt(inicioParts[1]);

        String[] finParts = horaFinalPrograma.split(":");
        int horaFin = Integer.parseInt(finParts[0]);
        int minutoFin = Integer.parseInt(finParts[1]);

        if (horaActual > horaInicio && horaActual < horaFin)
        {
            return true;
        } else if (horaActual == horaInicio && minutoActual >= minutoInicio) {
            return true;
        } else return horaActual == horaFin && minutoActual < minutoFin;
    }

    private void showDialog1() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_test);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();

        Button btnok = dialog.findViewById(R.id.btnok);
        btnok.setOnClickListener(v -> dialog.dismiss());
    }


    public void openWebUrl(View view) {
        String url = "http://www.edelweiss.fm";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void showQrDialog() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.qr_diag, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btnok = view.findViewById(R.id.btnok);
        btnok.setOnClickListener(v -> {
            Toast.makeText(getBaseContext(),"❤️", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        if (focusChange <= 0) {
            if (exoPlayer != null) {
                ExoService.getInstance().pause();
                Intent kkk = new Intent(MainActivity.this, ExoService.class);
                kkk.setAction(Constant.ACTION_PAUSE_STICKING);
                startService(kkk);
                iv_playpause.setImageResource(R.drawable.play);

                Intent startIntent1 = new Intent(MainActivity.this, Notification_Service.class);
                startIntent1.setAction(Constant.ACTION_PAUSE_STICKING);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(startIntent1);
                } else {
                    startService(startIntent1);
                }

                Constant.IS_PLAYING = false;
            }
        } else {
            if (exoPlayer != null) {
                iv_playpause.setImageResource(R.drawable.pause);
                equalizer.animateBars();

                Intent kkk = new Intent(MainActivity.this, ExoService.class);
                kkk.setAction(Constant.ACTION_PLAY_STICKING);
                startService(kkk);

                Intent startIntent1 = new Intent(MainActivity.this, Notification_Service.class);
                startIntent1.setAction(Constant.ACTION_PLAY_STICKING);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(startIntent1);
                } else {
                    startService(startIntent1);
                }

                Constant.IS_PLAYING = true;
            }
        }
    }
    public void showDialogSleepMode() {
        try {
            View mView = LayoutInflater.from(this).inflate(R.layout.dialog_sleep_time, findViewById(android.R.id.content), false);
            TextView mTvInfo = mView.findViewById(R.id.tv_info);

            int sleepMode = RadioSettingManager.getSleepMode(this);
            if (sleepMode > 0) {
                mTvInfo.setText(getResources().getString(R.string.format_minutes, sleepMode));
            } else {
                mTvInfo.setText(R.string.title_off);
            }

            SeekArc mCircularVir = mView.findViewById(R.id.seek_sleep);
            mCircularVir.setProgressColor(ContextCompat.getColor(this, R.color.colorAccent));
            mCircularVir.setArcColor(ContextCompat.getColor(this, R.color.progress_gray));
            mCircularVir.setMax((MAX_SLEEP_MODE - MIN_SLEEP_MODE) / STEP_SLEEP_MODE + 1);
            mCircularVir.setProgressWidth(getResources().getDimensionPixelOffset(R.dimen.tiny_margin));
            mCircularVir.setProgress(sleepMode / STEP_SLEEP_MODE);
            mCircularVir.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {
                @Override
                public void onProgressChanged(SeekArc seekArc, int progress, boolean fromUser) {
                    try {
                        int newSleepMode = progress * STEP_SLEEP_MODE;
                        RadioSettingManager.setSleepMode(MainActivity.this, newSleepMode);

                        if (progress == 0) {
                            mTvInfo.setText(R.string.title_off);
                        } else {
                            mTvInfo.setText(String.format(getString(R.string.format_minutes), sleepMode));
                        }

                        Intent mIntent1 = new Intent(MainActivity.this, ExoService.class);
                        mIntent1.setAction(Constant.ACTION_UPDATE_SLEEP_MODE);
                        startService(mIntent1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekArc seekArc) {
                }

                @Override
                public void onStopTrackingTouch(SeekArc seekArc) {
                }
            });

            MaterialDialog.Builder mBuilder = createBasicDialogBuilder(R.string.title_sleep_mode, R.string.title_done, 0);
            mBuilder.customView(mView, false);
            mBuilder.onPositive((dialog, which) -> {
                // Acciones a realizar cuando se presione el botón positivo
            });

            MaterialDialog mDialog = mBuilder.build();
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public MaterialDialog.Builder createBasicDialogBuilder(int titleId, int resPositive, int resNegative) {
        int colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
        int colorTextRed = ContextCompat.getColor(this, R.color.colortextred);

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .backgroundColor(colorPrimary)
                .title(titleId)
                .titleColor(colorTextRed)
                .contentColor(colorTextRed)
                .positiveColor(colorTextRed)
                .negativeColor(colorTextRed)
                .autoDismiss(true);

        if (resPositive != 0 && !TextUtils.isEmpty(getString(resPositive))) {
            builder.positiveText(resPositive);
        }

        if (resNegative != 0 && !TextUtils.isEmpty(getString(resNegative))) {
            builder.negativeText(resNegative);
        }

        return builder;
    }

    private void _Share() {
        String link = "https://play.google.com/store/apps/details?id=" + getPackageName();
        String text1 = getString(R.string.listin) + " " + Constant.ALBUM_NAME + getResources().getString(R.string.app_name);
        String text2 = getString(R.string.you_to);

        Intent intent2 = new Intent(Intent.ACTION_SEND);
        intent2.setType("text/plain");
        intent2.putExtra(Intent.EXTRA_TEXT, text1 + text2 + link);
        intent2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent2, "Compartir via"));

    }

    private void play() {
        if (Constant.IS_PLAYING) {
            iv_playpause.setImageResource(R.drawable.play);

            Constant.IS_PLAYING = false;
            ExoService.getInstance().pause();

            Intent kk = new Intent(getApplicationContext(), Notification_Service.class);
            kk.setAction(Constant.ACTION_PLAY_STICKING);
            startService(kk);
            equalizer.stopBars();

        } else {
            iv_playpause.setImageResource(R.drawable.pause);
            equalizer.animateBars();
            Constant.IS_PLAYING = true;
            SetList(url);
        }
    }

    public void SetList(String url) {
        ExoService.initialize(getApplicationContext(), url);
        Constant.IS_PLAYING = true;
        Intent kkk = new Intent(getApplicationContext(), ExoService.class);
        kkk.setAction(Constant.ACTION_PLAY_STICKING);
        startService(kkk);

        Intent kk = new Intent(getApplicationContext(), Notification_Service.class);
        kk.setAction(Constant.ACTION_PLAY_STICKING);
        startService(kk);

        //SetMetaData();

        equalizer.animateBars(); // To start the animation

        iv_playpause.setImageResource(R.drawable.pause);
    }

    private void showQualitySelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Html.fromHtml("<font color='#000000'>Seleccione la calidad de transmisión</font>"));

        String radioStreamUrl = getSharedPreferences("skin", MODE_PRIVATE).getString("radio_stream", "");
        String radioStream64Url = getSharedPreferences("skin", MODE_PRIVATE).getString("radio_stream_64", "");

        final List<String> streamUrls = new ArrayList<>();
        final List<String> streamQualities = new ArrayList<>();

        if (!TextUtils.isEmpty(radioStreamUrl)) {
            streamUrls.add(radioStreamUrl);
            streamQualities.add("Calidad 192kb/s MP3");
        }

        if (!TextUtils.isEmpty(radioStream64Url)) {
            streamUrls.add(radioStream64Url);
            streamQualities.add("Calidad 64kb/s AAC");
        }

        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(RadioGroup.VERTICAL);

        for (int i = 0; i < streamQualities.size(); i++) {
            String quality = streamQualities.get(i);
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(quality);
            radioGroup.addView(radioButton);

            if (quality.equals(getSelectedQuality())) {
                radioButton.setChecked(true);
            }
        }

        builder.setView(radioGroup);
        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
            int selectedIndex = radioGroup.indexOfChild(radioGroup.findViewById(selectedRadioButtonId));

            if (selectedIndex != -1) {
                String selectedStreamUrl = streamUrls.get(selectedIndex);
                String selectedStreamQuality = streamQualities.get(selectedIndex);

                iv_playpause.setImageResource(R.drawable.pause);
                equalizer.animateBars();
                Constant.IS_PLAYING = true;
                SetList(selectedStreamUrl);

                setSelectedQuality(selectedStreamQuality);

                TextView kbpsTextView = findViewById(R.id.kbps);
                kbpsTextView.setText(selectedStreamQuality);
            }
        });

        builder.show();
    }

    private String getSelectedQuality() {
        return getSharedPreferences("skin", MODE_PRIVATE).getString("selected_quality", "");
    }

    private void setSelectedQuality(String quality) {
        SharedPreferences.Editor editor = getSharedPreferences("skin", MODE_PRIVATE).edit();
        editor.putString("selected_quality", quality);
        editor.apply();
    }


    private void configureQualityButton() {
        ImageView qualityButton = findViewById(R.id.quality);
        qualityButton.setOnClickListener(v -> showQualitySelectionDialog());
    }


    @SuppressLint("SetTextI18n")
    public void AlbumArtParsing(String artistName, String albumName) {

       Tv_Artist_Name.setText(artistName + " - " + albumName);

        String a = artistName.trim();
        String b = albumName.trim();
        try {
            artistName = URLEncoder.encode(a, "UTF-8");
            albumName = URLEncoder.encode(b, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        String apiUrl = "https://itunes.apple.com/search?media=music&limit=1&term=" + artistName + "-" + albumName;

        System.out.println("---apiUrl----" + apiUrl);
        Log.d("apiUrl", apiUrl);

        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        StringRequest strReq = new StringRequest(apiUrl, response -> {
            System.out.println("----response album art 111----" + response);
            try {
                // do code for album not found
                JSONObject jobj = new JSONObject(response);
                String resultCount = jobj.getString("resultCount");
                if (resultCount.equalsIgnoreCase("1")) {
                    JSONArray jsonArray = jobj.getJSONArray("results");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String artworkUrl30 = jsonObject1.getString("artworkUrl30");

                    Log.e("RESULT artworkUrl30", "" + artworkUrl30);
                    String arr_images = artworkUrl30.replace("30x30", "350x350");
                    Log.e("RESULT artworkUrl30", "" + arr_images);

                    if (arr_images.equalsIgnoreCase("")) {
                        Glide.with(MainActivity.this).load(cover_img).placeholder(R.drawable.logo_icon).into(AlbumArt);
                    } else {
                        System.out.println("----arr_images inside---" + arr_images);

                        if (arr_images.isEmpty()) {
                            // code for album not found
                            try {
                                Glide.with(MainActivity.this)
                                        .load(cover_img)
                                        .placeholder(R.drawable.logo_icon).into(AlbumArt);

                            } catch (OutOfMemoryError e) {
                                System.out.println(e);
                            }
                        } else {
                            Constant.IMAGE_URL = arr_images;
                            System.out.println("---album_image----" + arr_images);
                            try {
                                Glide.with(MainActivity.this).load(arr_images).placeholder(R.drawable.logo_icon).into(AlbumArt);

                                Intent startIntent1 = new Intent(MainActivity.this, Notification_Service.class);
                                startIntent1.setAction(Constant.ACTION_PLAY_STICKING);
                                startService(startIntent1);

                            } catch (OutOfMemoryError e) {
                                System.out.println(e);
                            }
                        }
                    }
                }else {
                    Glide.with(MainActivity.this).load(cover_img).placeholder(R.drawable.logo_icon).into(AlbumArt);

                }
            } catch (JSONException e) {
                e.printStackTrace();
                Glide.with(MainActivity.this).load(cover_img).placeholder(R.drawable.logo_icon).into(AlbumArt);
            }
        }, error -> {
        });
        requestQueue.add(strReq);
    }

    private class Data_Reciever1 extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            play();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Deregistrar el listener al destruir la actividad
        appUpdateManager.unregisterListener(installStateUpdatedListener);

        Intent ddd = new Intent(getApplicationContext(), ExoService.class);
        ddd.setAction(Constant.ACTION_PAUSE_STICKING);
        stopService(ddd);

        Intent dddd = new Intent(getApplicationContext(), Notification_Service.class);
        dddd.setAction(Constant.ACTION_PAUSE_STICKING);
        startService(dddd);

        unregisterReceiver(receiver1);
        unregisterReceiver(reciever_cross);
        //resetTimer();
        clearNotification();

        android.os.Process.killProcess(android.os.Process.myPid());

        // Detener la tarea de actualización del programa al destruir la actividad
        handler.removeCallbacks(updateProgramRunnable);

    }

    public class Cross_Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("----cross Event-----");

            Intent ddd = new Intent(getApplicationContext(), ExoService.class);
            ddd.setAction(Constant.ACTION_PAUSE_STICKING);
            stopService(ddd);

            Intent dddd = new Intent(getApplicationContext(), Notification_Service.class);
            dddd.setAction(Constant.ACTION_PAUSE_STICKING);
            stopService(dddd);

            unregisterReceiver(receiver1);
            unregisterReceiver(reciever_cross);

            clearNotification();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Showdialoge();
        }
    }


    //public void Showdialoge() {
    //    final Dialog dialog = new Dialog(this);
    //    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    //    dialog.setCanceledOnTouchOutside(false);
    //    dialog.setContentView(R.layout.dialog);
    //    RelativeLayout background = dialog.findViewById(R.id.background);
    //    Button tv_yes = dialog.findViewById(R.id.tv_yes);
    //    Button tv_no = dialog.findViewById(R.id.tv_no);
    //    Button minimize = dialog.findViewById(R.id.minimize);
//
    //    if (start_color.equalsIgnoreCase("")){
    //        start_color = "#000000";
    //    }else if (end_color.equalsIgnoreCase("")){
    //        end_color = "#007cf7";
    //    }
//
    //    ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {
    //        @Override
    //        public Shader resize(int width, int height) {
    //            int[] colors = new int[2];
    //            float[] positions = new float[2];
    //            if (!TextUtils.isEmpty(start_color)) {
    //                colors[0] = Color.parseColor(start_color);
    //                positions[0] = 0;
    //            }
    //            if (!TextUtils.isEmpty(end_color)) {
    //                colors[1] = Color.parseColor(end_color);
    //                positions[1] = 1;
    //            }
    //            return new LinearGradient(0, 0, width, height, colors, positions, Shader.TileMode.REPEAT);
    //        }
    //    };
    //    PaintDrawable paint = new PaintDrawable();
    //    paint.setShape(new RectShape());
    //    paint.setShaderFactory(shaderFactory);
    //    background.setBackground(paint);
//
//
    //    dialog.show();
//
    //    tv_yes.setOnClickListener(v -> {
//
    //        onDestroy();
    //        //  Showdialoge();
    //        Log.d("TAG", "The interstitial wasn't loaded yet.");
//
    //        dialog.dismiss();
    //    });
//
    //    minimize.setOnClickListener(view -> {
    //        dialog.dismiss();
    //        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
    //        homeIntent.addCategory(Intent.CATEGORY_HOME);
    //        startActivity(homeIntent);
    //    });
//
    //    tv_no.setOnClickListener(v -> dialog.dismiss());
//
    //}

    public void Showdialoge() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_close);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();

        Button cancelButton = dialog.findViewById(R.id.nocerrar);
        Button minimizeButton = dialog.findViewById(R.id.minimizar);
        Button closeButton = dialog.findViewById(R.id.sicerrar);

        cancelButton.setOnClickListener(v -> {
            // Lógica para no cerrar la aplicación
            dialog.dismiss(); // Cerrar el diálogo después de hacer clic en el botón
        });

        minimizeButton.setOnClickListener(v -> {
            // Lógica para minimizar la aplicación
            moveTaskToBack(true); // Minimizar la aplicación
            dialog.dismiss(); // Cerrar el diálogo después de hacer clic en el botón
        });

        closeButton.setOnClickListener(v -> {
            // Lógica para cerrar la aplicación
            finish(); // Cerrar la actividad actual
            dialog.dismiss(); // Cerrar el diálogo después de hacer clic en el botón
        });
    }




    public void clearNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            int index = Vol_seekbar.getProgress();
            Vol_seekbar.setProgress(index + 1);

            if (Vol_seekbar.getProgress() > 0) {
                //Mute.setImageResource(R.drawable.highvolume_icon);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            int index = Vol_seekbar.getProgress();
            Vol_seekbar.setProgress(index - 1);

            if (Vol_seekbar.getProgress() == 0) {
                //Mute.setImageResource(R.drawable.mute_icon);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

}