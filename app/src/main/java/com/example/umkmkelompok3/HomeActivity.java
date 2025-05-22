package com.example.umkmkelompok3;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set item aktif sekarang
        bottomNavigationView.setSelectedItemId(R.id.Home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            global app = (global) getApplication();

            if (id == R.id.Home) {
                // This Class
            } else if (id == R.id.Produk) {
                Utils.replaceActivity(HomeActivity.this, ProdukActivity.class);
                overridePendingTransition(0, 0);
            } else if (id == R.id.Order) {
                if (app.getIsLogin()) {
                    Utils.replaceActivity(HomeActivity.this, OrderActivity.class);
                    overridePendingTransition(0, 0);
                } else {
                    Utils.popUpLogin(HomeActivity.this); // Sekarang ini fungsi dari parent
                }
            } else {
                if (app.getIsLogin()) {
                    Utils.replaceActivity(HomeActivity.this, ProfilActivity.class);
                    overridePendingTransition(0, 0);
                } else {
                    Utils.popUpLogin(HomeActivity.this); // Sekarang ini fungsi dari parent
                }
            }
            return true;
        });

        global app = (global) getApplication();
        Log.d("Debug", app.getIsLogin().toString());
        Log.d("Debug", app.getEmail());
        Log.d("Debug", app.getUsername());

    }
}
