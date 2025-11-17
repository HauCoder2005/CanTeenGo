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
import com.CanTinGo.dev.models.roleModels;
import com.CanTinGo.dev.models.userModels;

@Repository
public class userDaos {
	private DataSource dataSource;

	public userDaos() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Autowired
	public userDaos(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}
	
	// function check username -> findByUsername
	public boolean findByUsername(String username) {
		String sql = "SELECT COUNT(*) FROME users WHERE username=?";
		try (Connection conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)){
			ps.setString(1, username);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) return rs.getInt(1) > 0;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	// function check email -> findByEmail
	public boolean findByEmail(String email) {
		String sql = "SELECT COUNT(*) FROM users WHERE email=?";
		try(Connection conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            return rs.getInt(1) > 0;
	        }
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// function check account -> login
	public userModels checkUser(String username, String password) {
	    String sql = "SELECT u.id, u.first_name, u.last_name, u.username, u.password, u.email, u.phone_number, u.role_id, r.role_name "
	            + "FROM users u "
	            + "JOIN role r ON u.role_id = r.id "
	            + "WHERE u.username=? AND u.password=?";
		
		try (Connection conn = dataSource.getConnection(); 
			 PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, username);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
                roleModels role = new roleModels();
                role.setId(rs.getInt("role_id"));
                role.setRole_name(rs.getString("role_name"));

                userModels user = new userModels();
                user.setId(rs.getInt("id"));
                user.setFirst_name(rs.getString("first_name"));
                user.setLast_name(rs.getString("last_name"));
                user.setPassword(rs.getString("password"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPhone_number(rs.getString("phone_number"));
                user.setRole(role);
                return user;
            }


		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	// function create new user -> register
	public boolean register(userModels newUser) {
		String sql = "INSERT INTO users (first_name, last_name, username, password, email, phone_number, role_id)" +
					"VALUES (?, ?, ?, ?, ?, ?, ?)";
		try(Connection conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, newUser.getFirst_name());
	        ps.setString(2, newUser.getLast_name());
	        ps.setString(3, newUser.getUsername());
	        ps.setString(4, newUser.getPassword()); 
	        ps.setString(5, newUser.getEmail());
	        ps.setString(6, newUser.getPhone_number());
	        ps.setLong(7, 2L); // TỰ ĐỘNG GÁN ROLE USER (id = 2)

	        int rows = ps.executeUpdate();
	        return rows > 0;
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	// function get all data user => getAllUser
	public List<userModels> getAllUser() {
		String sql = """
				SELECT id, first_name, last_name, username ,email, phone_number FROM users WHERE role_id = 2
			    """;
		List<userModels> userList = new ArrayList<>();
		try(Connection conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				userModels user = new userModels(
		                rs.getInt("id"),
		                rs.getString("first_name"),
		                rs.getString("last_name"),
		                rs.getString("email"),
		                rs.getString("phone_number"),
		                rs.getString("username")
		            );
				userList.add(user);
			}
		} 
		catch(SQLException e) {
			e.printStackTrace();
		}
		return userList;
	}
	
	// function delete user by id -> deleteUserById
	public boolean deleteUserById(int id) {
	    String sql = "DELETE FROM users WHERE id = ? AND role_id = 2";

	    try (Connection conn = dataSource.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setInt(1, id);
	        int rowsAffected = ps.executeUpdate();
	        return rowsAffected > 0;

	    } catch (SQLException e) {
	        throw new RuntimeException("Lỗi khi xóa người dùng ID = " + id, e);
	    }
	}
}
