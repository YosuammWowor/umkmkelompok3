package com.example.umkmkelompok3;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SigninActivity {
    private Context context;
    private ExecutorService executor;
    private Handler handler;

    public SigninActivity(Context context) {
        this.context = context;
        this.executor = Executors.newSingleThreadExecutor();
        this.handler = new Handler(Looper.getMainLooper());
    }

    public void executeLogin(final String email, final String password) {
        executor.execute(() -> {
            String result = loginTask(email, password);

            handler.post(() -> {
                global app = (global) ((Activity) context).getApplication();

                if (result.equals("Login Failed") || result.equals("Missing username or password")) {
                    app.setIsLogin(false);
                    Log.d("Login", "Login Failed");
                } else {
                    app.setIsLogin(true);
                    Log.d("Login", "Login Success: " + result);

                    // Set informasi pengguna

                    // Optional: Navigate to Home/Profile/etc
                     Utils.replaceActivity((Activity) context, HomeActivity.class);
                }
            });
        });
    }

    private String loginTask(String username, String password) {
        try {
            String link = "http://10.0.2.2/MOBILE_PROGRAMMING/Login.php";
            String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")
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
            return "Exception: " + e.getMessage();
        }
    }
}
