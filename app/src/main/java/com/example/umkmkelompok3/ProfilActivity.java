package com.example.umkmkelompok3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfilActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set item aktif sekarang
        bottomNavigationView.setSelectedItemId(R.id.Profil);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.Profil) {
                // This Class
            } else if (id == R.id.Produk) {
                Utils.replaceActivity(ProfilActivity.this, ProdukActivity.class);
                overridePendingTransition(0, 0);
            } else if (id == R.id.Order) {
                Utils.replaceActivity(ProfilActivity.this, OrderActivity.class);
                overridePendingTransition(0, 0);
            } else {
                Utils.replaceActivity(ProfilActivity.this, HomeActivity.class);
                overridePendingTransition(0, 0);
            }
            return true;
        });

        // Ambil data dari global variabel
        global app = (global) getApplication();

        // Set nama pengguna
        TextView namaPengguna = findViewById(R.id.namaPengguna);
        namaPengguna.setText(app.getUsername());

        // Set deskripsi pengguna
        TextView deskripsiPengguna = findViewById(R.id.deskripsiPengguna);
        deskripsiPengguna.setText(app.getDeskripsiPengguna());

        // Tombol Keluar Akum
        Button btnKeluar = findViewById(R.id.btnKeluar);
        btnKeluar.setOnClickListener(v -> {
            // Default Data dalam global variabel
            app.setIsLogin(false);
            app.setEmail("");
            app.setUsername("");

            // Redirect to HomeActivity
            Utils.replaceActivity(ProfilActivity.this, HomeActivity.class);
        });
    }
}
