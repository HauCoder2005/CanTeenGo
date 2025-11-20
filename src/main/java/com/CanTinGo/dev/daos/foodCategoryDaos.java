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
import com.CanTinGo.dev.models.foodCategoryModels;

@Repository
public class foodCategoryDaos {
	private DataSource dataSource;
	public foodCategoryDaos() {
		super();
	}
	
	@Autowired 
	public foodCategoryDaos(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	// function create food category => createFoodCate
	public boolean createFoodCate(String category_name) {
	    String checkSql = "SELECT COUNT(*) FROM food_category WHERE category_name = ?";
	    try (Connection conn = dataSource.getConnection();
	         PreparedStatement psCheck = conn.prepareStatement(checkSql)) {
	        
	        psCheck.setString(1, category_name);
	        ResultSet rs = psCheck.executeQuery();
	        if (rs.next() && rs.getInt(1) > 0) {
	            return false;
	        }
	        
	        String sql = "INSERT INTO food_category (category_name) VALUES (?);";
	        try (PreparedStatement ps = conn.prepareStatement(sql)) {
	            ps.setString(1, category_name);
	            int rowsAffected = ps.executeUpdate();
	            return rowsAffected > 0;
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	// function get all category food => getAllCate
	public List<foodCategoryModels> getAllFoodCate() {
		String sql = "SELECT * FROM food_category";
		List<foodCategoryModels> catefoodList = new ArrayList<>();
		try(Connection conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery()) {
			while(rs.next()) {
				foodCategoryModels cate = new foodCategoryModels(
						rs.getInt("id"),
						rs.getString("category_name")
				);
				catefoodList.add(cate);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return catefoodList;
	}
	
	// function edit category food by id => editCateFoodByid
	public boolean editCateFood(foodCategoryModels cate) {
		String sql = "UPDATE food_category SET category_name = ? WHERE id = ?";
		try(Connection conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, cate.getCategory_name());
			ps.setInt(2, cate.getId());
			int rowsAffected = ps.executeUpdate();
			return rowsAffected > 0;
		}
		catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// function get cate food by id => getCateFoodById
	public foodCategoryModels getCateById(int id) {
	    String sql = "SELECT * FROM food_category WHERE id = ?";
	    try (Connection conn = dataSource.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setInt(1, id);
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            foodCategoryModels cate = new foodCategoryModels();
	            cate.setId(rs.getInt("id"));
	            cate.setCategory_name(rs.getString("category_name"));
	            return cate;
	        } else {
	            return null;
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	public void updateAvailable(int id, boolean available) {
	    String sql = "UPDATE food SET is_available = ? WHERE id = ?";
	    try (Connection conn = dataSource.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setBoolean(1, available);
	        ps.setInt(2, id);
	        ps.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
}
