package com.example.qlpt.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlpt.R;
import com.example.qlpt.dal.AppDB;
import com.example.qlpt.entities.CartView;

import java.util.List;

public class InvoiceActivity extends AppCompatActivity {

    TextView tvInvoice;
    AppDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        tvInvoice = findViewById(R.id.tvInvoice);
        db = AppDB.getInstance(this);

        int orderId = getIntent().getIntExtra("orderId", -1);

        List<CartView> list = db.dao().getCartView(orderId);
        Double total = db.dao().getOrderTotal(orderId);

        StringBuilder sb = new StringBuilder();
        sb.append("HÓA ĐƠN\n\n");

        for(CartView c : list){
            sb.append(c.productName)
                    .append(" - SL: ").append(c.quantity)
                    .append(" - Giá: ").append(c.unitPrice)
                    .append(" - TT: ").append(c.total)
                    .append("\n");
        }

        sb.append("\nTổng cộng: ").append(total == null ? 0 : total);
        tvInvoice.setText(sb.toString());
    }
}