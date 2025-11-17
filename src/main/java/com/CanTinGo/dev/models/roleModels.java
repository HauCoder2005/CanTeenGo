package com.CanTinGo.dev.models;

public class roleModels {
	private int id = 0;
	private String role_name = "";
	public roleModels(int id, String role_name) {
		super();
		this.id = id;
		this.role_name = role_name;
	}
	public roleModels() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRole_name() {
		return role_name;
	}
	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}
	
}
