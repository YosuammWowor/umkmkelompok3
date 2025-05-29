package com.example.umkmkelompok3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProdukDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produk_detail);

        // Inisialisasi Data
        ImageView image;
        TextView name, desc, price;
        name = findViewById(R.id.detailName);
        desc = findViewById(R.id.detailTime);
        price = findViewById(R.id.detailIngredients);
        image = findViewById(R.id.detailImage);

        // Set data dari Intent
        Intent intent = getIntent();
        name.setText(intent.getStringExtra("nama"));
        desc.setText(intent.getStringExtra("deskripsi"));
        price.setText(intent.getStringExtra("harga"));
        image.setImageResource(getResources().getIdentifier(
                intent.getStringExtra("image"), "drawable", getPackageName()));
    }
}