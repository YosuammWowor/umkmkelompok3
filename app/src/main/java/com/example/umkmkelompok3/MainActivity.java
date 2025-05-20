package com.example.umkmkelompok3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.umkmkelompok3.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceActivity(HomeActivity.class);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.Home) {
                replaceActivity(HomeActivity.class);
            } else if (item.getItemId() == R.id.Produk) {
                replaceActivity(ProdukActivity.class);
            } else if (item.getItemId() == R.id.Order) {
                replaceActivity(OrderActivity.class);
            } else {
                replaceActivity(ProfilActivity.class);
            }

            return true;
        });
    }

protected void replaceActivity(Class<? extends Activity> activityClass) {
    Intent a = new Intent(this, activityClass);
    a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(a);
}

}