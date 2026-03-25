package com.example.shoppingmanager.dal;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.qlpt.entities.Account;
import com.example.qlpt.entities.Category;
import com.example.qlpt.entities.Order;
import com.example.qlpt.entities.Product;

@Database(entities = {
        Account.class,
        Category.class,
        Product.class,
        Order.class,
        OrderDetail.class
}, version = 1)
public abstract class AppDB extends RoomDatabase {
    public abstract DAO dao();

    private static AppDB INSTANCE;

    public static AppDB getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDB.class,
                    "ShoppingDB"
            ).allowMainThreadQueries().build();
        }
        return INSTANCE;
    }
}