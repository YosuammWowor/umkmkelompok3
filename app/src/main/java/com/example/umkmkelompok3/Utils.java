package com.example.umkmkelompok3;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.google.android.flexbox.FlexboxLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Utils {
    // Emulator
    // static String link = "http://10.0.2.2/MOBILE_PROGRAMMING/";
    // External Device
    static String link = "http://192.168.1.5/MOBILE_PROGRAMMING/";

    // Pass
    public static void replaceActivity(Activity currentActivity, Class<? extends Activity> targetActivity) {
        Intent intent = new Intent(currentActivity, targetActivity);
        currentActivity.startActivity(intent);
        currentActivity.finish();
    }

    // Pass
    public static void popUpLogin(Activity activity) {
        // Menampilkan pop up Login
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.login);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        // Aksi X mark dipojok kiri atas
        ImageView closeBtn = dialog.findViewById(R.id.btnClose);
        closeBtn.setOnClickListener(v -> dialog.dismiss());

        // Ambil field dari form login
        EditText emailField = dialog.findViewById(R.id.email);
        EditText passwordField = dialog.findViewById(R.id.password);
        Button loginButton = dialog.findViewById(R.id.btnSubmit);

        // Tombol Login
        loginButton.setOnClickListener(v -> logIn(activity, emailField, passwordField));

        // Pop up Daftar
        TextView daftarLink = dialog.findViewById(R.id.linkDaftar);
        daftarLink.setOnClickListener(v -> {
            dialog.dismiss();
            popUpDaftar(activity);
        });
    }

    // Pass
    public static void popUpDaftar(Activity activity) {
        // Menampilkan pop up Daftar
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.daftar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        // Aksi X mark dipojok kiri atas
        ImageView closeBtn = dialog.findViewById(R.id.btnClose);
        closeBtn.setOnClickListener(v -> dialog.dismiss());

        // Ambil field dari form daftar
        EditText emailField = dialog.findViewById(R.id.email);
        EditText usernameField = dialog.findViewById(R.id.username);
        EditText passwordField = dialog.findViewById(R.id.password);
        EditText deskripsiField = dialog.findViewById(R.id.deskripsiPengguna);
        Button daftarButton = dialog.findViewById(R.id.btnSubmit);

        // Tombol Daftar
        daftarButton.setOnClickListener(v -> signIn(activity, emailField, passwordField, usernameField, deskripsiField));

        // Pop up Login
        TextView loginLink = dialog.findViewById(R.id.linkLogin);
        loginLink.setOnClickListener(v -> {
            dialog.dismiss();
            popUpLogin(activity);
        });
    }

    // Pass
    public static void logIn(Context context, EditText emailField, EditText passwordField) {
        // Convert Object ke String
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        // Pengecekan
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Email dan Password wajib diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thread Baru untuk proses login di background
        // Tujuan : agar tidak mengganggu thread utama dan penting untuk operasi jaringan
        new Thread(() -> {
            try {
                // Persiapan Koneksi
                URL url = new URL(link + "Login.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                // Struktur data POST
                String postData = "email=" + URLEncoder.encode(email, "UTF-8") +
                        "&password=" + URLEncoder.encode(password, "UTF-8");

                // Mengirimkan data ke server
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(postData);
                writer.flush();
                writer.close();
                os.close();

                // Respon Server
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    in.close();

                    // fetch data dari Object JSON
                    JSONObject json = new JSONObject(response.toString());
                    String status = json.getString("status");
                    String message = json.getString("message");
                    String getEmail = json.getString("email");
                    String getUsername = json.getString("username");
                    String getDeskripsi = json.getString("deskripsi");

                    // Kembali ke UI Thread
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(() -> {
                        global app = (global) ((Activity) context).getApplication();
                        if (status.equals("success")) {
                            // Proses Debug Logcat
                            Log.d("Debug",message);

                            // Notifikasi Berhasil
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                            // Simpan informasi pengguna (Global Variabel)
                            app.setIsLogin(true);
                            app.setEmail(getEmail);
                            app.setUsername(getUsername);
                            app.setDeskripsiPengguna(getDeskripsi);

                            // Perpindahan Activity
                            Utils.replaceActivity((Activity) context, ProfilActivity.class);
                        } else {
                            // Notifikasi Gagal
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } catch (Exception e) {
                // Menangani Error / Exception
                Log.e("Error", String.valueOf(e));
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(context, "Terjadi kesalahan saat login", Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }

    // Pass
    public static void signIn(Context context, EditText emailField, EditText passwordField, EditText usernameField, EditText deskripsiField) {
        // Convert Object ke String
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String username = usernameField.getText().toString();
        String deskripsi = deskripsiField.getText().toString();

        // Pengecekan
        if (email.isEmpty() || password.isEmpty() || username.isEmpty() || deskripsi.isEmpty()) {
            Toast.makeText(context, "Semua field wajib diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thread Baru untuk proses login di background
        // Tujuan : agar tidak mengganggu thread utama dan penting untuk operasi jaringan
        new Thread(() -> {
            try {
                // Persiapan Koneksi
                URL url = new URL(link + "Daftar.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                // Struktur data POST
                String postData = "email=" + URLEncoder.encode(email, "UTF-8") +
                        "&password=" + URLEncoder.encode(password, "UTF-8") +
                        "&username=" + URLEncoder.encode(username, "UTF-8") +
                        "&deskripsi=" + URLEncoder.encode(deskripsi, "UTF-8");

                // Mengirimkan data ke server
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(postData);
                writer.flush();
                writer.close();
                os.close();

                // Respon Server
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    in.close();

                    // fetch data dari Object JSON
                    JSONObject json = new JSONObject(response.toString());
                    String status = json.getString("status");
                    String message = json.getString("message");
                    String getEmail = json.getString("email");
                    String getUsername = json.getString("username");
                    String getDeskripsi = json.getString("deskripsi");

                    // Kembali ke UI Thread
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(() -> {
                        if (status.equals("success")) {
                            // Proses Debug Logcat
                            Log.d("Debug", message);

                            // Notifikasi Berhasil
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                            // Simpan informasi pengguna (Global Variabel)
                            global app = (global) ((Activity) context).getApplication();
                            app.setIsLogin(true);
                            app.setEmail(getEmail);
                            app.setUsername(getUsername);
                            app.setDeskripsiPengguna(getDeskripsi);

                            // Perpindahan Activity
                            Utils.replaceActivity((Activity) context, ProfilActivity.class);
                        } else {
                            // Notifikasi Gagal
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } catch (Exception e) {
                // Menangani Error / Exception
                Log.e("Error", String.valueOf(e));
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(context, "Terjadi kesalahan saat mendaftar", Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }

    // Pass
    public static void fetchProducts(Context context, FlexboxLayout flexboxLayout, Class<?> detailActivityClass) {
        // Thread Baru
        new Thread(() -> {
            try {
                // Persiapan Koneksi
                URL url = new URL(link + "Produk.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(3000);
                conn.setReadTimeout(3000);
                conn.connect();

                // Respon server
                int responseCode = conn.getResponseCode();
                Log.i("Debug", "HTPP response code: " + responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    conn.disconnect();

                    // fetch data dari object JSON
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    JSONArray products = jsonResponse.getJSONArray("produk");

                    // Perulangan Setiap Produk
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject item = products.getJSONObject(i);
                        String image = item.getString("image");
                        String nama = item.getString("nama");
                        String deskripsi = item.getString("deskripsi");
                        String harga = item.getString("harga");

                        // Penambahan Produk ke UI
                        Handler mainHandler = new Handler(Looper.getMainLooper());
                        mainHandler.post(() ->
                                addProductCard(context, flexboxLayout, nama, deskripsi, harga, image, detailActivityClass)
                        );
                    }

                }

            } catch (Exception e) {
                // Menangani Error / Exception
                Log.e("ProdukError", "HTTP response code: " + e);
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(context, "Gagal Terhubung ke server", Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }

    // Pass
    public static void addProductCard(Context context, FlexboxLayout flexboxLayout, String nama, String deskripsi, String harga, String image, Class<?> detailActivityClass) {
        float density = context.getResources().getDisplayMetrics().density;

        // Card View Layout
        CardView card = new CardView(context);
        FlexboxLayout.LayoutParams cardParams = new FlexboxLayout.LayoutParams(
                (int) (160 * density),
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(
                (int) (10 * density),
                (int) (10 * density),
                (int) (10 * density),
                (int) (10 * density)
        );
        card.setLayoutParams(cardParams);
        card.setRadius(10 * density);
        card.setClickable(true);

        // Internal Layout
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        layout.setBackgroundColor(Color.WHITE);

        // ImageView (Gambar Produk)
        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int) (200 * density)
        );
        imageParams.gravity = Gravity.CENTER;
        imageView.setLayoutParams(imageParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(context.getResources().getIdentifier(
                image.toLowerCase(), "drawable", context.getPackageName()));
        layout.addView(imageView);

        // Title (Nama Produk)
        TextView title = new TextView(context);
        title.setText(nama);
        title.setTypeface(null, Typeface.BOLD);
        title.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        title.setTextColor(Color.BLACK);
        title.setPadding(0, (int) (10 * density), 0, 0);
        layout.addView(title);

        // Divider (Garis Pemisah)
        View divider = new View(context);
        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int) (1 * density)
        );
        dividerParams.setMargins(
                (int) (10 * density),
                0,
                (int) (10 * density),
                0
        );
        divider.setLayoutParams(dividerParams);
        divider.setBackgroundColor(context.getResources().getColor(R.color.brown));
        layout.addView(divider);

        // Description (Deskripsi Produk)
        TextView desc = new TextView(context);
        desc.setText("Lihat Selengkapnya");
        desc.setPadding((int) (5 * density), (int) (10 * density), (int) (5 * density), (int) (10 * density));
        desc.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
        desc.setGravity(Gravity.CENTER);
        layout.addView(desc);

        // Tambahkan Linear Layout ke Card View
        card.addView(layout);

        // Card Action
        card.setOnClickListener(v -> {
            Intent intent = new Intent(context, detailActivityClass);
            intent.putExtra("nama", nama);
            intent.putExtra("deskripsi", deskripsi);
            intent.putExtra("harga", harga);
            intent.putExtra("image", image);
            context.startActivity(intent);
        });

        // Menambahkan Card View ke Flexbox
        flexboxLayout.addView(card);
    }

    // Pass
    public static void addPemesanan(Activity activity, Context context) {
        Spinner Meja = activity.findViewById(R.id.spMeja);
        Spinner Hari = activity.findViewById(R.id.spHari);
        Spinner Waktu = activity.findViewById(R.id.spWaktu);
        Button Pesan = activity.findViewById(R.id.btnPesan);

        // Aksi Tombol Pesan
        Pesan.setOnClickListener(v -> {
            // Pengecekan
            if (Meja.getSelectedItemPosition() != 0 && Hari.getSelectedItemPosition() != 0 && Waktu.getSelectedItemPosition() != 0) {
                // Notifikasi Berhasil
                Toast.makeText(context,
                        "Pesanan kamu: " + Meja.getSelectedItem() + ", " + Hari.getSelectedItem() + ", " + Waktu.getSelectedItem(),
                        Toast.LENGTH_LONG).show();

                // Simpan Input User
                simpanPemesanan(context, Meja, Hari, Waktu);

                // Reset Selection
                Meja.setSelection(0);
                Hari.setSelection(0);
                Waktu.setSelection(0);

            } else {
                // Notifikasi Gagal
                Toast.makeText(context, "Mohon pastikan kembali pilihan anda", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Pass
    public static void simpanPemesanan(Context context, Spinner spMeja, Spinner spHari, Spinner spWaktu) {
        // Convert Object ke String
        String meja = spMeja.getSelectedItem().toString();
        String hari = spHari.getSelectedItem().toString();
        String waktu = spWaktu.getSelectedItem().toString();

        // Global Variabel
        global app = (global) ((Activity) context).getApplication();
        String username = app.getUsername();

        // Thread Baru
        new Thread(() -> {
            try {
                // Persiapan Koneksi
                URL url = new URL(link + "Order.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                // Struktur data POST
                String postData = "spMeja=" + URLEncoder.encode(meja, "UTF-8")
                        + "&spHari=" + URLEncoder.encode(hari, "UTF-8")
                        + "&spWaktu=" + URLEncoder.encode(waktu, "UTF-8")
                        + "&username=" + URLEncoder.encode(username, "UTF-8");

                // Mengirimkan data ke server
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(postData);
                writer.flush();
                writer.close();
                os.close();

                // Respon Server
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    in.close();

                    // fetch data dari Object JSON
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    String message = jsonResponse.getString("message");

                    // Kembali ke UI Thread
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(() ->
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(() -> {
                    Toast.makeText(context, "Terjadi kesalahan saat mengirim data", Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }

    public static void profilSection(Activity activity) {
        // Global Variabel
        global app = (global) activity.getApplication();

        // Set nama pengguna
        TextView namaPengguna = activity.findViewById(R.id.namaPengguna);
        namaPengguna.setText(app.getUsername());

        // Set deskripsi pengguna
        TextView deskripsiPengguna = activity.findViewById(R.id.deskripsiPengguna);
        deskripsiPengguna.setText(app.getDeskripsiPengguna());

        // Tombol Keluar Akun
        Button btnKeluar = activity.findViewById(R.id.btnKeluar);
        btnKeluar.setOnClickListener(v -> {
            // Default Data dalam global variabel
            app.setIsLogin(false);
            app.setEmail("");
            app.setDeskripsiPengguna("");
            app.setUsername("");

            // Redirect ke HomeActivity
            Utils.replaceActivity(activity, HomeActivity.class);
        });
    }
}