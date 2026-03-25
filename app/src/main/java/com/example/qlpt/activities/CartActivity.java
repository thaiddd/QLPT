package com.example.qlpt.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlpt.R;
import com.example.qlpt.dal.AppDB;
import com.example.qlpt.entities.CartView;
import com.example.qlpt.entities.Order;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    ListView lvCart;
    TextView tvTotal;
    Button btnCheckout;
    AppDB db;
    Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        lvCart = findViewById(R.id.lvCart);
        tvTotal = findViewById(R.id.tvTotal);
        btnCheckout = findViewById(R.id.btnCheckout);

        db = AppDB.getInstance(this);

        SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
        String username = sp.getString("username", "");

        order = db.dao().getPendingOrder(username);
        if(order == null){
            Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
            return;
        }

        List<CartView> list = db.dao().getCartView(order.id);
        List<String> data = new ArrayList<>();

        for(CartView c : list){
            data.add(c.productName + " | SL: " + c.quantity + " | Giá: " + c.unitPrice + " | Thành tiền: " + c.total);
        }

        lvCart.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                data
        ));

        Double total = db.dao().getOrderTotal(order.id);
        tvTotal.setText("Tổng tiền: " + (total == null ? 0 : total));

        btnCheckout.setOnClickListener(v -> {
            order.status = "Paid";
            db.dao().updateOrder(order);

            Intent intent = new Intent(this, InvoiceActivity.class);
            intent.putExtra("orderId", order.id);
            startActivity(intent);
            finish();
        });
    }
}