package com.example.qlpt.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "OrderDetails")
public class OrderDetail {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int orderId;
    public int productId;
    public int quantity;
    public double unitPrice;
}