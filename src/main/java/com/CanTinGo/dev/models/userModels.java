package com.CanTinGo.dev.models;

public class userModels {
	private int id = 0;
	private String first_name = "";
	private String last_name = "";
	private String username = "";
	private String password = "";
	private String email = "";
	private String phone_number = "";
	private boolean active = false;
	private roleModels role;
	
	public userModels(int id, String first_name, String last_name, String username, String password, String email,
			String phone_number, boolean active, roleModels role) {
		super();
		this.id = id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.username = username;
		this.password = password;
		this.email = email;
		this.phone_number = phone_number;
		this.active = active;
		this.role = role;
	}
	public userModels(int id, String first_name, String last_name, String username, String password, String email,
			String phone_number, roleModels role) {
		super();
		this.id = id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.username = username;
		this.password = password;
		this.email = email;
		this.phone_number = phone_number;
		this.role = role;
	}
	public userModels(int id, String first_name, String last_name, String email, String phone_number, String username, boolean active) {
		super();
		this.id = id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.phone_number = phone_number;
		this.username = username;
		this.active = active;
	}
	public userModels() {
		super();
		// TODO Auto-generated constructor stub
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public roleModels getRole() {
		return role;
	}
	public void setRole(roleModels role) {
		this.role = role;
	}
	
	
}
