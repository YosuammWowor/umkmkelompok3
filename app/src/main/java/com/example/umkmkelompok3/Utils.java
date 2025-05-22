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
import android.widget.TextView;

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
        dialog.show();

        ImageView closeBtn = dialog.findViewById(R.id.btnClose);
        closeBtn.setOnClickListener(v -> dialog.dismiss());

        // Login Button
        EditText emailField = dialog.findViewById(R.id.email);
        EditText passwordField = dialog.findViewById(R.id.password);
        Button loginButton = dialog.findViewById(R.id.btnSubmit);

        loginButton.setOnClickListener(v -> logIn(activity, emailField, passwordField));

        // Daftar Link
        TextView daftarLink = dialog.findViewById(R.id.linkDaftar);
        daftarLink.setOnClickListener(v -> {
            dialog.dismiss();
            popUpDaftar(activity);
        });

    }

    public static void popUpDaftar(Activity activity) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.daftar); // Gantilah dengan layout XML untuk form daftar
        dialog.show();

        ImageView closeBtn = dialog.findViewById(R.id.btnClose);
        closeBtn.setOnClickListener(v -> dialog.dismiss());

        // Ambil field dari form daftar
        EditText emailField = dialog.findViewById(R.id.email);
        EditText usernameField = dialog.findViewById(R.id.username);
        EditText passwordField = dialog.findViewById(R.id.password);
        Button daftarButton = dialog.findViewById(R.id.btnSubmit);

        daftarButton.setOnClickListener(v -> {
            // Lakukan validasi & proses registrasi (bisa kirim ke SignUpActivity atau PHP)
            String email = emailField.getText().toString();
            String username = usernameField.getText().toString();
            String password = passwordField.getText().toString();

            Log.d("Daftar", "Email: " + email + ", Username: " + username + ", Password: " + password);

            // TODO: Kirim data ke server (misalnya panggil SignUpActivity seperti login)
            dialog.dismiss(); // Tutup setelah daftar
        });

        // Login Link
        TextView loginLink = dialog.findViewById(R.id.linkLogin);
        loginLink.setOnClickListener(v -> {
            dialog.dismiss();
            popUpLogin(activity);
        });
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
