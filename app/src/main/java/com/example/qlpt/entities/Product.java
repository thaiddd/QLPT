package com.example.qlpt.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Products")
public class Product {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public double price;
    public String description;
    public int categoryId;
}