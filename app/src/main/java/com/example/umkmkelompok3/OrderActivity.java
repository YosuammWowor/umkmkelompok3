package com.example.umkmkelompok3;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class OrderActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set item aktif sekarang
        bottomNavigationView.setSelectedItemId(R.id.Order);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.Home) {
                startActivity(new Intent(this, HomeActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.Produk) {
                startActivity(new Intent(this, ProdukActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else {
                startActivity(new Intent(this, ProfilActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
        });
    }
}