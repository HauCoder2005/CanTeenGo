package com.CanTinGo.dev.models;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class orderModels {
	private int id = 0;
    private LocalDateTime orderDate = LocalDateTime.now();
	private BigDecimal total_price;
	private String payment_method = "";
	private String status = "";
    private List<orderItemModels> items;
	private userModels users;
	public orderModels() {
		super();
		// TODO Auto-generated constructor stub
	}
	public List<orderItemModels> getItems() {
		return items;
	}
	public void setItems(List<orderItemModels> items) {
		this.items = items;
	}
	public orderModels(int id, LocalDateTime orderDate, BigDecimal total_price, String payment_method, String status,
			userModels users) {
		super();
		this.id = id;
		this.orderDate = orderDate;
		this.total_price = total_price;
		this.payment_method = payment_method;
		this.status = status;
		this.users = users;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LocalDateTime getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}
	public BigDecimal getTotal_price() {
		return total_price;
	}
	public void setTotal_price(BigDecimal total_price) {
		this.total_price = total_price;
	}
	public String getPayment_method() {
		return payment_method;
	}
	public void setPayment_method(String payment_method) {
		this.payment_method = payment_method;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public userModels getUsers() {
		return users;
	}
	public void setUsers(userModels users) {
		this.users = users;
	}
	
}
