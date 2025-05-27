package com.example.umkmkelompok3;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class OrderActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        Spinner spMeja = findViewById(R.id.spMeja);
        Spinner spHari = findViewById(R.id.spHari);
        Spinner spWaktu = findViewById(R.id.spWaktu);
        Button btnPesan = findViewById(R.id.btnPesan);

        Utils utils = new Utils();
        utils.addPemesanan(this, spMeja, spHari, spWaktu, btnPesan);

        // Set item aktif sekarang
        bottomNavigationView.setSelectedItemId(R.id.Order);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            global app = (global) getApplication();

            if (id == R.id.Order) {
                // This Class
            } else if (id == R.id.Produk) {
                Utils.replaceActivity(OrderActivity.this, ProdukActivity.class);
                overridePendingTransition(0, 0);
            } else if (id == R.id.Home) {
                Utils.replaceActivity(OrderActivity.this, HomeActivity.class);
                overridePendingTransition( 0, 0);
            } else {
                if (app.getIsLogin()) {
                    Utils.replaceActivity(OrderActivity.this, ProfilActivity.class);
                    overridePendingTransition(0, 0);
                } else {
                    Utils.popUpLogin(OrderActivity.this); // Sekarang ini fungsi dari parent
                }
            }
            return true;
        });
    }
}
