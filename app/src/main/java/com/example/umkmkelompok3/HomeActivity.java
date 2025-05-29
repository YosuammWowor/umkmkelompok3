package com.example.umkmkelompok3;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Inisialisasi Global Variabel
        global app = (global) getApplication();

        // Menu Navigasi
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.Home);

        // Aksi Menu Navigasi
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            // Perpindahan
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
                    Utils.popUpLogin(HomeActivity.this);
                }
            } else {
                if (app.getIsLogin()) {
                    Utils.replaceActivity(HomeActivity.this, ProfilActivity.class);
                    overridePendingTransition(0, 0);
                } else {
                    Utils.popUpLogin(HomeActivity.this);
                }
            }
            return true;
        });

        // Log Debugging
        Log.d("Login Status", app.getIsLogin().toString());
        Log.d("Email", app.getEmail());
        Log.d("Username", app.getUsername());

    }
}
