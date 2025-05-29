package com.example.umkmkelompok3;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class OrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // Fungsi Pemesanan
        Utils.addPemesanan(OrderActivity.this, this);

        // Menu Navigasi
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.Order);

        // Aksi Menu Navigasi
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.Order) {
                // This Class
            } else if (id == R.id.Produk) {
                Utils.replaceActivity(OrderActivity.this, ProdukActivity.class);
                overridePendingTransition(0, 0);
            } else if (id == R.id.Home) {
                Utils.replaceActivity(OrderActivity.this, HomeActivity.class);
                overridePendingTransition( 0, 0);
            } else {
                Utils.replaceActivity(OrderActivity.this, ProfilActivity.class);
                overridePendingTransition(0, 0);
            }
            return true;
        });
    }
}
