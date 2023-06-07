package com.edelweiss.stationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;

public class Frecuencias extends AppCompatActivity {

    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);

        // Obtener la referencia al WebView
        webView = findViewById(R.id.web_view);

        // Habilitar el soporte de JavaScript
        webView.getSettings().setJavaScriptEnabled(true);

        // Cargar la URL en el WebView
        webView.loadUrl("https://edelweiss.fm/cobertura/");
    }

    // Para asegurarse de que se libera la memoria al salir de la actividad
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
