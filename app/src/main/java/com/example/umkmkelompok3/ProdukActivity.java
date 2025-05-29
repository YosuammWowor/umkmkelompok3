package com.example.umkmkelompok3;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProdukActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produk);

        // Daftar Produk
        FlexboxLayout flexboxLayout = findViewById(R.id.flexbox);
        Utils.fetchProducts(this, flexboxLayout, ProdukDetailActivity.class);

        // Menu Navigasi
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.Produk);

        // Aksi Menu Navigasi
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            global app = (global) getApplication();

            if (id == R.id.Produk) {
                // This Class
            } else if (id == R.id.Home) {
                Utils.replaceActivity(ProdukActivity.this, HomeActivity.class);
                overridePendingTransition(0, 0);
            } else if (id == R.id.Order) {
                if (app.getIsLogin()) {
                    Utils.replaceActivity(ProdukActivity.this, OrderActivity.class);
                    overridePendingTransition(0, 0);
                } else {
                    Utils.popUpLogin(ProdukActivity.this);
                }
            } else {
                if (app.getIsLogin()) {
                    Utils.replaceActivity(ProdukActivity.this, ProfilActivity.class);
                    overridePendingTransition(0, 0);
                } else {
                    Utils.popUpLogin(ProdukActivity.this);
                }
            }
            return true;
        });
    }
}
