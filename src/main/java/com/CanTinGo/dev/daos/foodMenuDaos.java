package com.CanTinGo.dev.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.CanTinGo.dev.models.foodModels;
import com.CanTinGo.dev.models.foodCategoryModels;

@Repository
public class foodMenuDaos {
    @Autowired
    private DataSource dataSource;

    // Get all food items with their categories and images
    public List<foodModels> getAllFoodWithCategory() {
        String sql = """
                SELECT f.id, f.food_name, f.description, f.price, 
                       f.is_available, f.image, f.available_quantity,
                       c.id AS cate_id, c.category_name
                FROM food f
                JOIN food_category c ON f.category_id = c.id
                """;

        List<foodModels> listFood = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Create category object
                foodCategoryModels cate = new foodCategoryModels(
                        rs.getInt("cate_id"),
                        rs.getString("category_name")
                );

                // Create food object
                foodModels food = new foodModels();
                food.setId(rs.getInt("id"));
                food.setFood_name(rs.getString("food_name"));
                food.setDescription(rs.getString("description"));
                food.setPrice(rs.getBigDecimal("price"));
                food.setIsAvailable(rs.getBoolean("is_available"));
                food.setImage(rs.getString("image")); // add image
                food.setAvailable_quantity(rs.getInt("available_quantity"));
                food.setFoodCategory(cate);

                listFood.add(food);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error while joining food and category: " + e.getMessage());
        }

        return listFood;
    }

    // Insert new food item with image
    public boolean createDataFood(foodModels food) {
        String sql = """
                INSERT INTO food(food_name, description, price, available_quantity, is_available, category_id, image)
                VALUES (?, ?, ?, ?, ?, ?, ?);
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, food.getFood_name());
            ps.setString(2, food.getDescription());
            ps.setBigDecimal(3, food.getPrice());
            ps.setInt(4, food.getAvailable_quantity());
            ps.setBoolean(5, food.getIsAvailable());
            ps.setInt(6, food.getFoodCategory().getId());
            ps.setString(7, food.getImage()); // add image

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete food by ID if not available
    public boolean deleteFoodByIdAndCategory(int id) {
        String sql = "DELETE FROM food WHERE id = ? AND is_available = 0";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update the availability status of a food item
    public void updateAvailable(int id, boolean available) {
        int value = available ? 1 : 0;
        String sql = "UPDATE food SET is_available = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, value);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update food item by ID, including image
    public boolean updateFoodById(foodModels food) {
        String sql = """
            UPDATE food
            SET food_name = ?, 
                description = ?, 
                price = ?, 
                available_quantity = ?, 
                is_available = ?, 
                category_id = ?,
                image = ?
            WHERE id = ?;
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, food.getFood_name());
            ps.setString(2, food.getDescription());
            ps.setBigDecimal(3, food.getPrice());
            ps.setInt(4, food.getAvailable_quantity());
            ps.setBoolean(5, food.getIsAvailable());
            ps.setInt(6, food.getFoodCategory().getId());
            ps.setString(7, food.getImage()); // add image
            ps.setInt(8, food.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get food by ID including category and image
    public foodModels getFoodById(int id) {
        String sql = "SELECT f.*, f.image, c.id AS cate_id, c.category_name " +
                     "FROM food f " +
                     "JOIN food_category c ON f.category_id = c.id " +
                     "WHERE f.id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                foodModels food = new foodModels();

                food.setId(rs.getInt("id"));
                food.setFood_name(rs.getString("food_name"));
                food.setDescription(rs.getString("description"));
                food.setPrice(rs.getBigDecimal("price"));
                food.setAvailable_quantity(rs.getInt("available_quantity"));
                food.setIsAvailable(rs.getBoolean("is_available"));
                food.setImage(rs.getString("image")); // add image

                foodCategoryModels cate = new foodCategoryModels();
                cate.setId(rs.getInt("cate_id"));
                cate.setCategory_name(rs.getString("category_name"));

                food.setFoodCategory(cate);
                return food;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
 // function get food by active = true
    public List<foodModels> getAllFoodByActive() {
        List<foodModels> listFood = new ArrayList<>();
        String sql = "SELECT f.*, f.image, c.id AS cate_id, c.category_name " +
                     "FROM food f " +
                     "JOIN food_category c ON f.category_id = c.id " +
                     "WHERE f.is_available = 1"; // chỉ lấy món đang bán

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                foodModels food = new foodModels();
                food.setId(rs.getInt("id"));
                food.setFood_name(rs.getString("food_name"));
                food.setDescription(rs.getString("description"));
                food.setPrice(rs.getBigDecimal("price"));
                food.setAvailable_quantity(rs.getInt("available_quantity"));
                food.setIsAvailable(rs.getBoolean("is_available"));
                food.setImage(rs.getString("image"));

                foodCategoryModels cate = new foodCategoryModels();
                cate.setId(rs.getInt("cate_id"));
                cate.setCategory_name(rs.getString("category_name"));

                food.setFoodCategory(cate);

                listFood.add(food);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listFood;
    }
    
    public void updateQuantity(int foodId, int newQuantity) {
        String sql = "UPDATE food SET available_quantity = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newQuantity);
            ps.setInt(2, foodId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int countAllFood() {
        String sql = "SELECT COUNT(*) AS total FROM food";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getInt("total");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


}
