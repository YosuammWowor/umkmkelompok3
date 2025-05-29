package com.example.umkmkelompok3;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        // Menu Navigasi
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.Profil);

        // Aksi Menu Navigasi
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

        // Profil Section
        Utils.profilSection(ProfilActivity.this);
    }
}
