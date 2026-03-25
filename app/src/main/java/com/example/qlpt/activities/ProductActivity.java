package com.example.qlpt.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlpt.R;
import com.example.qlpt.dal.AppDB;
import com.example.qlpt.entities.Order;
import com.example.qlpt.entities.OrderDetail;
import com.example.qlpt.entities.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    ListView lvProducts;
    AppDB db;
    List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        lvProducts = findViewById(R.id.lvProducts);
        db = AppDB.getInstance(this);

        // Kiểm tra xem có lọc theo Category không
        int categoryId = getIntent().getIntExtra("categoryId", -1);
        if (categoryId != -1) {
            productList = db.dao().getProductsByCategory(categoryId);
        } else {
            productList = db.dao().getAllProducts();
        }

        List<String> names = new ArrayList<>();
        for(Product p : productList){
            names.add(p.name + " - " + p.price + " VNĐ");
        }

        lvProducts.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                names
        ));

        lvProducts.setOnItemClickListener((parent, view, position, id) -> {
            Product p = productList.get(position);
            
            SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
            boolean isLogin = sp.getBoolean("isLogin", false);

            if(!isLogin){
                Toast.makeText(this, "Vui lòng đăng nhập để mua hàng", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.putExtra("pendingProductId", p.id); // Gửi ID sản phẩm sang Login
                startActivity(intent);
                return;
            }

            String username = sp.getString("username", "");
            addToCart(username, p);
            
            Toast.makeText(this, "Đã thêm " + p.name + " vào giỏ hàng", Toast.LENGTH_SHORT).show();
            
            // Theo luồng: Có tiếp tục chọn sản phẩm? 
            // Ở đây ta có thể ở lại trang hoặc sang giỏ hàng.
            // Để tiện cho người dùng, ta cho họ lựa chọn hoặc mặc định ở lại.
        });
    }

    private void addToCart(String username, Product p) {
        Order order = db.dao().getPendingOrder(username);
        if(order == null){
            order = new Order();
            order.username = username;
            order.orderDate = System.currentTimeMillis();
            order.status = "Pending";
            long oid = db.dao().insertOrder(order);
            order.id = (int) oid;
        }

        OrderDetail detail = db.dao().getOrderDetail(order.id, p.id);
        if(detail == null){
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
}