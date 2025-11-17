package com.CanTinGo.dev.models;

import java.time.LocalDate;

public class dailyMenuModels {
	private int id = 0;
	private int quantity_limit = 0;
	private int quantity_remaining = 0;
	private LocalDate date;
	private foodModels food;
	public dailyMenuModels() {
		super();
		// TODO Auto-generated constructor stub
	}
	public dailyMenuModels(int id, int quantity_limit, int quantity_remaining, LocalDate date, foodModels food) {
		super();
		this.id = id;
		this.quantity_limit = quantity_limit;
		this.quantity_remaining = quantity_remaining;
		this.date = date;
		this.food = food;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getQuantity_limit() {
		return quantity_limit;
	}
	public void setQuantity_limit(int quantity_limit) {
		this.quantity_limit = quantity_limit;
	}
	public int getQuantity_remaining() {
		return quantity_remaining;
	}
	public void setQuantity_remaining(int quantity_remaining) {
		this.quantity_remaining = quantity_remaining;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public foodModels getFood() {
		return food;
	}
	public void setFood(foodModels food) {
		this.food = food;
	}
	
}
