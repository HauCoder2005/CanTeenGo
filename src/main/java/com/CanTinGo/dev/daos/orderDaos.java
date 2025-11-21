package com.CanTinGo.dev.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.CanTinGo.dev.models.foodModels;
import com.CanTinGo.dev.models.orderItemModels;
import com.CanTinGo.dev.models.orderModels;
import com.CanTinGo.dev.models.userModels;

@Repository
public class orderDaos {

    @Autowired
    private DataSource dataSource;

    public orderModels getOrderById(int orderId) {
        String sqlOrder = "SELECT o.*, u.id AS user_id, u.username, u.first_name, u.last_name "
                        + "FROM orders o JOIN users u ON o.user_id = u.id WHERE o.id = ?";
        try (Connection conn = dataSource.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sqlOrder)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                orderModels order = new orderModels();
                userModels user = new userModels();
                user.setId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setFirst_name(rs.getString("first_name"));
                user.setLast_name(rs.getString("last_name"));
                order.setUsers(user);

                order.setId(rs.getInt("id"));
                order.setTotal_price(rs.getBigDecimal("total_price"));
                order.setStatus(rs.getString("status"));
                order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());

                order.setItems(getOrderItemsByOrderId(orderId));

                return order;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<orderItemModels> getOrderItemsByOrderId(int orderId) {
        String sql = "SELECT oi.*, f.id AS food_id, f.food_name, f.price AS food_price, f.image, f.available_quantity, f.is_available "
                   + "FROM order_item oi JOIN food f ON oi.food_id = f.id WHERE oi.order_id = ?";

        List<orderItemModels> items = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                orderItemModels item = new orderItemModels();
                foodModels food = new foodModels();
                food.setId(rs.getInt("food_id"));
                food.setFood_name(rs.getString("food_name"));
                food.setPrice(rs.getBigDecimal("food_price"));
                food.setImage(rs.getString("image"));
                food.setAvailable_quantity(rs.getInt("available_quantity"));
                food.setIsAvailable(rs.getBoolean("is_available"));

                item.setFoods(food);
                item.setQuantity(rs.getInt("quantity"));
                item.setPrice(rs.getBigDecimal("price"));

                items.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public void createOrderItems(int orderId, List<orderItemModels> items) {
        String sql = "INSERT INTO order_item (order_id, food_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (orderItemModels item : items) {
                ps.setInt(1, orderId);
                ps.setInt(2, item.getFoods().getId());
                ps.setInt(3, item.getQuantity());
                ps.setBigDecimal(4, item.getPrice());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int createOrder(orderModels order) {
        String sql = "INSERT INTO orders (user_id, total_price, payment_method, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, order.getUsers().getId());
            ps.setBigDecimal(2, order.getTotal_price());
            ps.setString(3, order.getPayment_method());
            ps.setString(4, order.getStatus() != null ? order.getStatus() : "pending");

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // trả về orderId vừa tạo
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
