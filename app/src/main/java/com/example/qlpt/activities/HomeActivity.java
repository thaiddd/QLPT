package com.example.qlpt.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlpt.R;

public class HomeActivity extends AppCompatActivity {

    TextView tvWelcome;
    Button btnLogin, btnCategories, btnProducts, btnCart, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvWelcome = findViewById(R.id.tvWelcome);
        btnLogin = findViewById(R.id.btnLoginScreen);
        btnCategories = findViewById(R.id.btnCategories);
        btnProducts = findViewById(R.id.btnProducts);
        btnCart = findViewById(R.id.btnCart);
        btnLogout = findViewById(R.id.btnLogout);

        updateUI();

        btnLogin.setOnClickListener(v ->
                startActivity(new Intent(this, LoginActivity.class)));

        btnCategories.setOnClickListener(v ->
                startActivity(new Intent(this, CategoryActivity.class)));

        btnProducts.setOnClickListener(v ->
                startActivity(new Intent(this, ProductActivity.class)));

        btnCart.setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class)));

        btnLogout.setOnClickListener(v -> {
            SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.apply();
            updateUI();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin", false);
        String username = sp.getString("username", "");

        if (isLogin) {
            tvWelcome.setText("Xin chào: " + username);
            btnLogin.setVisibility(View.GONE);
            
            // Hiện các chức năng khác khi đã đăng nhập
            btnCategories.setVisibility(View.VISIBLE);
            btnProducts.setVisibility(View.VISIBLE);
            btnCart.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.VISIBLE);
        } else {
            tvWelcome.setText("Chào mừng bạn đến với QLPT");
            btnLogin.setVisibility(View.VISIBLE);
            
            // Ẩn các chức năng khác khi chưa đăng nhập
            btnCategories.setVisibility(View.GONE);
            btnProducts.setVisibility(View.GONE);
            btnCart.setVisibility(View.GONE);
            btnLogout.setVisibility(View.GONE);
        }
    }
}