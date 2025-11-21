package com.CanTinGo.dev.models;

import java.math.BigDecimal;

public class orderItemModels {
	private int id = 0;
	private int quantity = 0;
	private BigDecimal price ;
	private orderModels orders;
	private foodModels foods;
	public orderItemModels() {
		super();
		// TODO Auto-generated constructor stub
	}
	public orderItemModels(int id, int quantity, BigDecimal price, orderModels orders, foodModels foods) {
		super();
		this.id = id;
		this.quantity = quantity;
		this.price = price;
		this.orders = orders;
		this.foods = foods;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public orderModels getOrders() {
		return orders;
	}
	public void setOrders(orderModels orders) {
		this.orders = orders;
	}
	public foodModels getFoods() {
		return foods;
	}
	public void setFoods(foodModels foods) {
		this.foods = foods;
	}
	
}
