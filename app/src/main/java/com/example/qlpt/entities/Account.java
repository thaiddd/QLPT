package com.example.qlpt.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Accounts")
public class Account {
    @PrimaryKey
    @NonNull
    public String username;
    public String password;
    public String fullName;
}