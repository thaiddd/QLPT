package com.example.qlpt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlpt.R;
import com.example.qlpt.dal.AppDB;
import com.example.qlpt.entities.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    ListView lvCategories;
    AppDB db;
    List<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        lvCategories = findViewById(R.id.lvCategories);
        db = AppDB.getInstance(this);

        categoryList = db.dao().getAllCategories();
        List<String> names = new ArrayList<>();

        for (Category c : categoryList) {
            names.add(c.name);
        }

        lvCategories.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                names
        ));

        lvCategories.setOnItemClickListener((parent, view, position, id) -> {
            Category selected = categoryList.get(position);
            Intent intent = new Intent(this, ProductActivity.class);
            intent.putExtra("categoryId", selected.id);
            startActivity(intent);
        });
    }
}