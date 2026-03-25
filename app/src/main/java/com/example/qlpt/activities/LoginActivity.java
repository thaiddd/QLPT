package com.example.qlpt.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlpt.R;
import com.example.qlpt.dal.AppDB;
import com.example.qlpt.entities.Account;
import com.example.qlpt.entities.Category;
import com.example.qlpt.entities.Order;
import com.example.qlpt.entities.OrderDetail;
import com.example.qlpt.entities.Product;

public class LoginActivity extends AppCompatActivity {

    EditText edtUser, edtPass;
    Button btnLogin;
    AppDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        btnLogin = findViewById(R.id.btnLogin);

        db = AppDB.getInstance(this);
        seedData();

        btnLogin.setOnClickListener(v -> {
            String u = edtUser.getText().toString().trim();
            String p = edtPass.getText().toString().trim();

            Account acc = db.dao().login(u, p);

            if(acc != null){
                SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("username", u);
                editor.putBoolean("isLogin", true);
                editor.apply();

                // Kiểm tra xem có sản phẩm chờ được thêm vào giỏ hàng không
                int pendingProductId = getIntent().getIntExtra("pendingProductId", -1);
                if (pendingProductId != -1) {
                    addProductToCart(u, pendingProductId);
                    startActivity(new Intent(this, CartActivity.class));
                } else {
                    startActivity(new Intent(this, HomeActivity.class));
                }
                finish();
            } else {
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addProductToCart(String username, int productId) {
        Product p = db.dao().getProductById(productId);
        if (p == null) return;

        Order order = db.dao().getPendingOrder(username);
        if (order == null) {
            order = new Order();
            order.username = username;
            order.orderDate = System.currentTimeMillis();
            order.status = "Pending";
            long oid = db.dao().insertOrder(order);
            order.id = (int) oid;
        }

        OrderDetail detail = db.dao().getOrderDetail(order.id, p.id);
        if (detail == null) {
            detail = new OrderDetail();
            detail.orderId = order.id;
            detail.productId = p.id;
            detail.quantity = 1;
            detail.unitPrice = p.price;
            db.dao().insertOrderDetail(detail);
        } else {
            db.dao().updateQuantity(detail.id, detail.quantity + 1);
        }
    }

    private void seedData(){
        if(db.dao().getAccount("admin") == null){
            Account a = new Account();
            a.username = "admin";
            a.password = "123";
            a.fullName = "Administrator";
            db.dao().insertAccount(a);

            Category c1 = new Category();
            c1.name = "Điện thoại";
            db.dao().insertCategory(c1);

            Category c2 = new Category();
            c2.name = "Laptop";
            db.dao().insertCategory(c2);

            Product p1 = new Product();
            p1.name = "iPhone 15";
            p1.price = 22000000;
            p1.description = "Điện thoại Apple";
            p1.categoryId = 1;
            db.dao().insertProduct(p1);

            Product p2 = new Product();
            p2.name = "Samsung S24";
            p2.price = 19000000;
            p2.description = "Điện thoại Samsung";
            p2.categoryId = 1;
            db.dao().insertProduct(p2);

            Product p3 = new Product();
            p3.name = "MacBook Air M3";
            p3.price = 30000000;
            p3.description = "Laptop Apple";
            p3.categoryId = 2;
            db.dao().insertProduct(p3);
        }
    }
}