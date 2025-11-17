package com.CanTinGo.dev.models;

public class foodModels {
	private int id = 0;
	private String food_name = "";
	private String description = "";
    private Double price = 0.0;
    private Boolean isAvailable = true;
    private foodCategoryModels category;
	public foodModels() {
		super();
		// TODO Auto-generated constructor stub
	}
	public foodModels(int id, String food_name, String description, Double price, Boolean isAvailable,
			foodCategoryModels category) {
		super();
		this.id = id;
		this.food_name = food_name;
		this.description = description;
		this.price = price;
		this.isAvailable = isAvailable;
		this.category = category;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFood_name() {
		return food_name;
	}
	public void setFood_name(String food_name) {
		this.food_name = food_name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Boolean getIsAvailable() {
		return isAvailable;
	}
	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	public foodCategoryModels getCategory() {
		return category;
	}
	public void setCategory(foodCategoryModels category) {
		this.category = category;
	}
}
