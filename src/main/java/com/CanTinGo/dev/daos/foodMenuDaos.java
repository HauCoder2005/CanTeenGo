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

    // 1. Get all foods with category name
    public List<foodModels> getAllFood() {
        String sql = """
                SELECT f.*, c.category_name 
                FROM food f 
                LEFT JOIN food_category c ON f.category_id = c.id 
                ORDER BY f.id DESC
                """;
        List<foodModels> list = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRowToFood(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Get foods by category id
    public List<foodModels> getAllFoodByIdCate(int id) {
        String sql = """
                SELECT f.*, c.category_name 
                FROM food f 
                LEFT JOIN food_category c ON f.category_id = c.id 
                WHERE f.category_id = ? 
                ORDER BY f.id DESC
                """;
        List<foodModels> list = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToFood(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. Get single food by id (for edit)
    public foodModels getFoodById(int id) {
        String sql = """
                SELECT f.*, c.category_name 
                FROM food f 
                LEFT JOIN food_category c ON f.category_id = c.id 
                WHERE f.id = ?
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToFood(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 4. Add new food
    public boolean addFood(foodModels food) {
        String sql = """
                INSERT INTO food (food_name, description, price, is_available, category_id, image) 
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, food.getFood_name());
            ps.setString(2, food.getDescription());
            ps.setDouble(3, food.getPrice());                    
            ps.setBoolean(4, food.getIsAvailable());          
            ps.setInt(5, food.getCategory().getId());          
            ps.setString(6, food.getImage());                   

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. Update existing food
    public boolean updateFood(foodModels food) {
        String sql = """
                UPDATE food 
                SET food_name = ?, description = ?, price = ?, 
                    is_available = ?, category_id = ?, image = ? 
                WHERE id = ?
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, food.getFood_name());
            ps.setString(2, food.getDescription());
            ps.setDouble(3, food.getPrice());
            ps.setBoolean(4, food.getIsAvailable());
            ps.setInt(5, food.getCategory().getId());
            ps.setString(6, food.getImage());
            ps.setInt(7, food.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 6. Delete food by id
    public boolean deleteFood(int id) {
        String sql = "DELETE FROM food WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Helper: map ResultSet → foodModels (exactly matches your current model)
    private foodModels mapRowToFood(ResultSet rs) throws SQLException {
        foodModels food = new foodModels();
        food.setId(rs.getInt("id"));
        food.setFood_name(rs.getString("food_name"));
        food.setDescription(rs.getString("description"));
        food.setPrice(rs.getDouble("price"));                    
        food.setIsAvailable(rs.getBoolean("is_available"));     

        foodCategoryModels category = new foodCategoryModels();
        category.setId(rs.getInt("category_id"));
        category.setCategory_name(rs.getString("category_name"));
        food.setCategory(category);

        food.setImage(rs.getString("image") != null ? rs.getString("image") : "");

        return food;
    }
}