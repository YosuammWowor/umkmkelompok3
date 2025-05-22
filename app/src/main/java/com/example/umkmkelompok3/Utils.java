package com.example.umkmkelompok3;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.umkmkelompok3.databinding.ActivityMainBinding;

public class Utils {

    public static void replaceActivity(Activity currentActivity, Class<? extends Activity> targetActivity) {
        Intent intent = new Intent(currentActivity, targetActivity);
        currentActivity.startActivity(intent);
    }
    public static void popUpLogin(Activity activity) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.login);
        dialog.setCancelable(false);
        dialog.show();

        ImageView closeBtn = dialog.findViewById(R.id.btnClose);
        closeBtn.setOnClickListener(v -> dialog.dismiss());

        // Access views from dialog, NOT from a new inflated layout
        EditText emailField = dialog.findViewById(R.id.email);
        EditText passwordField = dialog.findViewById(R.id.password);
        Button loginButton = dialog.findViewById(R.id.submit);

        loginButton.setOnClickListener(v -> logIn(activity, emailField, passwordField));
    }


    public static void logIn(Context context, EditText emailField, EditText passwordField) {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        Log.d("Debug", "Email: " + email);
        Log.d("Debug", "Password: " + password);

        // Continue with login logic
        SigninActivity signinTask = new SigninActivity(context);
        signinTask.executeLogin(email, password);
    }




}
