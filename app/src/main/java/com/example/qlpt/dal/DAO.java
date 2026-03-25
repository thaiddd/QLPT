package com.example.qlpt.dal;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.qlpt.entities.CartView;
import com.example.qlpt.entities.Category;
import com.example.qlpt.entities.Order;
import com.example.qlpt.entities.Product;

import java.util.List;

@Dao
public interface DAO {

    // ---------------- ACCOUNT ----------------
    @Insert
    void insertAccount(Account account);

    @Query("SELECT * FROM Accounts WHERE username = :u AND password = :p")
    Account login(String u, String p);

    @Query("SELECT * FROM Accounts WHERE username = :u")
    Account getAccount(String u);

    // ---------------- CATEGORY ----------------
    @Insert
    void insertCategory(Category category);

    @Query("SELECT * FROM Categories")
    List<Category> getAllCategories();

    // ---------------- PRODUCT ----------------
    @Insert
    void insertProduct(Product product);

    @Query("SELECT * FROM Products")
    List<Product> getAllProducts();

    @Query("SELECT * FROM Products WHERE categoryId = :cid")
    List<Product> getProductsByCategory(int cid);

    @Query("SELECT * FROM Products WHERE id = :id")
    Product getProductById(int id);

    // ---------------- ORDER ----------------
    @Insert
    long insertOrder(Order order);

    @Update
    void updateOrder(Order order);

    @Query("SELECT * FROM Orders WHERE username = :u AND status = 'Pending' LIMIT 1")
    Order getPendingOrder(String u);

    @Query("SELECT * FROM Orders WHERE id = :id")
    Order getOrderById(int id);

    // ---------------- ORDER DETAIL ----------------
    @Insert
    void insertOrderDetail(OrderDetail detail);

    @Query("SELECT * FROM OrderDetails WHERE orderId = :oid AND productId = :pid LIMIT 1")
    OrderDetail getOrderDetail(int oid, int pid);

    @Query("UPDATE OrderDetails SET quantity = :q WHERE id = :id")
    void updateQuantity(int id, int q);

    @Query("SELECT p.name as productName, d.quantity as quantity, d.unitPrice as unitPrice, " +
            "(d.quantity * d.unitPrice) as total " +
            "FROM OrderDetails d JOIN Products p ON d.productId = p.id " +
            "WHERE d.orderId = :oid")
    List<CartView> getCartView(int oid);

    @Query("SELECT SUM(quantity * unitPrice) FROM OrderDetails WHERE orderId = :oid")
    Double getOrderTotal(int oid);
}