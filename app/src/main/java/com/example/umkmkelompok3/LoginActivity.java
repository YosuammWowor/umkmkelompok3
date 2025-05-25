package com.example.umkmkelompok3;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

// Librari JSON
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity {
    private Context context;
    private ExecutorService executor;
    private Handler handler;

    public LoginActivity(Context context) {
        this.context = context;
        this.executor = Executors.newSingleThreadExecutor();
        this.handler = new Handler(Looper.getMainLooper());
    }

    public void executeLogin(final String email, final String password) {
        executor.execute(() -> {
            String result = loginTask(email, password);

            handler.post(() -> {
                global app = (global) ((Activity) context).getApplication();

                try {
                    JSONObject json = new JSONObject(result);
                    String status = json.getString("status");

                    if (status.equals("success")) {
                        Log.d("Login", "Login Success: " + json.toString());

                        // Simpan informasi pengguna
                        app.setIsLogin(true);
                        app.setEmail(json.getString("email"));
                        app.setUsername(json.getString("username"));
                        app.setDeskripsiPengguna(json.getString("deskripsi"));

                        Utils.replaceActivity((Activity) context, ProfilActivity.class);
                    } else {
                        app.setIsLogin(false);
                        Log.d("Login", "Login Failed: " + json.getString("message"));
                    }
                } catch (Exception e) {
                    app.setIsLogin(false);
                    Log.e("Login", "Parsing error: " + e.getMessage());
                }
            });
        });
    }


    private String loginTask(String email, String password) {
        try {
            // Emulator
            String link = "http://10.0.2.2/MOBILE_PROGRAMMING/Login.php";

            // External Device
            // String link = "http:192.168.1.5/MOBILE_PROGRAMMING/Login.php";
            String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8")
                    + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            wr.close();
            reader.close();

            return sb.toString();
        } catch (Exception e) {
            return "{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}";
        }
    }

}
