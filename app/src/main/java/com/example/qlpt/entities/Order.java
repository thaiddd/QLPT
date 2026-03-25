package com.example.qlpt.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Orders")
public class Order {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String username;
    public long orderDate;
    public String status; // Pending / Paid
}