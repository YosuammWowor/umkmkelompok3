package com.example.umkmkelompok3;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.replaceActivity(MainActivity.this, HomeActivity.class);
    }

}
