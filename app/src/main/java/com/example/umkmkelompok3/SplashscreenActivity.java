package com.example.umkmkelompok3;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashscreenActivity extends AppCompatActivity {

    // Waktu tampilan splash screen (dalam milidetik)
    private static final int SPLASH_INTERVAL = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splashscreen);

        new Handler().postDelayed(() -> {
            // Pindah ke MainActivity setelah splash screen selesai
            Intent intent = new Intent(SplashscreenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_INTERVAL);
    }
}