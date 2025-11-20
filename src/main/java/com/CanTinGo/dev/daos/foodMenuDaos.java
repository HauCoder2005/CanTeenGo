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

                // Tạo category model
                foodCategoryModels cate = new foodCategoryModels(
                        rs.getInt("cate_id"),
                        rs.getString("category_name")
                );

                foodModels food = new foodModels();
                food.setId(rs.getInt("id"));
                food.setFood_name(rs.getString("food_name"));
                food.setDescription(rs.getString("description"));
                food.setPrice(rs.getDouble("price"));
                food.setIsAvailable(rs.getBoolean("is_available"));
                food.setImage(rs.getString("image"));
                food.setAvailable_quantity(rs.getInt("available_quantity"));
                
                food.setFoodCategory(cate);

                listFood.add(food);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi JOIN lấy danh sách món ăn & category: " + e.getMessage());
        }

        return listFood;
    }
}