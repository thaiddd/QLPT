package com.example.qlpt.entities; // Đảm bảo thư mục lưu file là .../activities/InvoiceActivity.java

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlpt.R;
import com.example.qlpt.dal.AppDB;
import com.example.qlpt.entities.CartView;

import java.text.DecimalFormat;
import java.util.List;

public class InvoiceActivity extends AppCompatActivity {

    private TextView tvInvoice;
    private AppDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        // 1. Ánh xạ view
        tvInvoice = findViewById(R.id.tvInvoice);
        db = AppDB.getInstance(this);

        // 2. Lấy dữ liệu từ Intent
        int orderId = getIntent().getIntExtra("orderId", -1);

        if (orderId == -1) {
            Toast.makeText(this, "Không tìm thấy mã hóa đơn!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 3. Truy vấn dữ liệu
        List<CartView> list = db.dao().getCartView(orderId);
        Double total = db.dao().getOrderTotal(orderId);

        // 4. Hiển thị dữ liệu
        displayInvoice(list, total);
    }

    private void displayInvoice(List<CartView> list, Double total) {
        if (list == null || list.isEmpty()) {
            tvInvoice.setText("Hóa đơn trống hoặc không tồn tại.");
            return;
        }

        // Định dạng số (Ví dụ: 1,000,000)
        DecimalFormat formatter = new DecimalFormat("#,###");

        StringBuilder sb = new StringBuilder();
        sb.append("---------- HÓA ĐƠN ----------\n\n");

        for (CartView c : list) {
            sb.append("Sản phẩm: ").append(c.productName).append("\n")
                    .append("SL: ").append(c.quantity)
                    .append(" x Đơn giá: ").append(formatter.format(c.unitPrice)).append("\n")
                    .append("Thành tiền: ").append(formatter.format(c.total)).append("\n")
                    .append("-----------------------------\n");
        }

        double finalTotal = (total != null) ? total : 0;
        sb.append("\nTỔNG CỘNG: ").append(formatter.format(finalTotal)).append(" VNĐ");

        tvInvoice.setText(sb.toString());
    }
}