package com.example.umkmkelompok3;

import static com.google.android.material.internal.ViewUtils.dpToPx;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.flexbox.FlexboxLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        // Ambil field dari form login
        EditText emailField = dialog.findViewById(R.id.email);
        EditText passwordField = dialog.findViewById(R.id.password);
        Button loginButton = dialog.findViewById(R.id.btnSubmit);

        // Login Button
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
        EditText deskripsiField = dialog.findViewById(R.id.deskripsiPengguna);
        Button daftarButton = dialog.findViewById(R.id.btnSubmit);

        daftarButton.setOnClickListener(v -> signIn(activity, emailField, passwordField, usernameField, deskripsiField));

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
        LoginActivity loginTask = new LoginActivity(context);
        loginTask.executeLogin(email, password);
    }

    public static void signIn(Context context, EditText emailField, EditText passwordField, EditText usernameField, EditText deskripsiField) {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String username = usernameField.getText().toString();
        String deskripsi = deskripsiField.getText().toString();

        Log.d("Email", "Email: " + email);
        Log.d("Password", "Password: " + password);
        Log.d("Username", "Username: " + username);
        Log.d("Deskripsi", "Deskripsi: " + deskripsi);

        // Continue with login logic
        SigninActivity signinTask = new SigninActivity(context);
        signinTask.executeSignin(email, password, username, deskripsi);
    }

    public static void fetchProducts(Context context, FlexboxLayout flexboxLayout, Class<?> detailActivityClass) {
        RequestQueue queue = Volley.newRequestQueue(context);
        // Emulator
//        String url = "http://10.0.2.2/MOBILE_PROGRAMMING/Produk.php";

        // External Device
         String url = "http:192.168.1.5/MOBILE_PROGRAMMING/Produk.php";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray products = response.getJSONArray("produk");
                        Log.d("ProdukResponse", response.toString());

                        for (int i = 0; i < products.length(); i++) {
                            JSONObject item = products.getJSONObject(i);
                            int id = item.getInt("id_produk");
                            String image = item.getString("image");
                            String nama = item.getString("nama");
                            String deskripsi = item.getString("deskripsi");
                            String harga = item.getString("harga");

                            Produk product = new Produk(id, nama, deskripsi, harga, image);
                            addProductCard(context, flexboxLayout, product, detailActivityClass);
                        }
                    } catch (JSONException e) {
                        Log.e("ProdukError", "Volley error: " + e);
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);

        queue.add(request);
    }

    public static void addProductCard(Context context, FlexboxLayout flexboxLayout, Produk  product, Class<?> detailActivityClass) {
        // Create CardView
        CardView card = new CardView(context);
        FlexboxLayout.LayoutParams cardParams = new FlexboxLayout.LayoutParams(
                (int) dpToPx(context, 160),
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(
                (int) dpToPx(context, 10),
                (int) dpToPx(context, 10),
                (int) dpToPx(context, 10),
                (int) dpToPx(context, 10)
        );
        card.setLayoutParams(cardParams);
        card.setRadius(dpToPx(context, 10));
        card.setClickable(true);

        // Create LinearLayout
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        layout.setBackgroundColor(Color.WHITE);

        // ImageView
        ImageView image = new ImageView(context);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int) dpToPx(context, 200)
        );
        imageParams.gravity = Gravity.CENTER;
        image.setLayoutParams(imageParams);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        image.setImageResource(context.getResources().getIdentifier(
                product.image.toLowerCase(), "drawable", context.getPackageName()));
        layout.addView(image);


        // Title TextView
        TextView title = new TextView(context);
        title.setText(product.nama);
        title.setTypeface(null, Typeface.BOLD);
        title.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        title.setTextColor(Color.BLACK);
        title.setPadding(0, (int) dpToPx(context, 10), 0, 0);
        layout.addView(title);

        // Divider View
        View divider = new View(context);
        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int) dpToPx(context, 1)
        );
        dividerParams.setMargins(
                (int) dpToPx(context, 10),
                0,
                (int) dpToPx(context, 10),
                0
        );
        divider.setLayoutParams(dividerParams);
        divider.setBackgroundColor(context.getResources().getColor(R.color.red));
        layout.addView(divider);

        // Description TextView
        TextView desc = new TextView(context);
        desc.setText("Lihat Selengkapnya");
        desc.setPadding((int) dpToPx(context, 5), (int) dpToPx(context, 10), (int) dpToPx(context, 5), (int) dpToPx(context, 10));
        desc.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
        desc.setGravity(Gravity.CENTER);
        layout.addView(desc);

        // Add layout to card
        card.addView(layout);

        // OnClick to open detail activity
        card.setOnClickListener(v -> {
            Intent intent = new Intent(context, detailActivityClass);
            intent.putExtra("nama", product.nama);
            intent.putExtra("deskripsi", product.deskripsi);
            intent.putExtra("harga", product.harga);
            intent.putExtra("image", product.image);
            context.startActivity(intent);
        });

        // Add card to FlexboxLayout
        flexboxLayout.addView(card);
    }

    private static float dpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    /*public static void addProductCard(Context context, FlexboxLayout flexboxLayout, Produk product, Class<?> detailActivityClass) {
        CardView card = new CardView(context);
        card.setLayoutParams(new ViewGroup.LayoutParams(500, ViewGroup.LayoutParams.WRAP_CONTENT));
        card.setRadius(10);
        card.setClickable(true);
        card.setForeground(context.getResources().getDrawable(android.R.drawable.list_selector_background, null));

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);

        TextView title = new TextView(context);
        title.setText(product.nama);
        title.setTypeface(null, Typeface.BOLD);
        layout.addView(title);

        TextView price = new TextView(context);
        price.setText("Harga: " + product.harga);
        layout.addView(price);

        card.addView(layout);

        card.setOnClickListener(v -> {
            Intent intent = new Intent(context, detailActivityClass);
            intent.putExtra("nama", product.nama);
            intent.putExtra("deskripsi", product.deskripsi);
            intent.putExtra("harga", product.harga);
            context.startActivity(intent);
        });

        flexboxLayout.addView(card);
    }*/
}