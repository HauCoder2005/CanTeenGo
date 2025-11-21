package com.CanTinGo.dev.models;

import java.math.BigDecimal;

public class foodModels {
	private Integer id = 0;
	private String food_name = "";
	private String description = "";
    private BigDecimal price ;
    private Boolean isAvailable = true;
    private String image = "";
    private Integer available_quantity = 0;
    public foodModels(Integer id, String food_name, String description, BigDecimal price, Boolean isAvailable, String image,
			Integer available_quantity, foodCategoryModels foodCate) {
		super();
		this.id = id;
		this.food_name = food_name;
		this.description = description;
		this.price = price;
		this.isAvailable = isAvailable;
		this.image = image;
		this.available_quantity = available_quantity;
		this.foodCategory = foodCate;
	}
	public Integer getAvailable_quantity() {
		return available_quantity;
	}
	public void setAvailable_quantity(Integer available_quantity) {
		this.available_quantity = available_quantity;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public foodModels(int id, String food_name, String description, BigDecimal price, Boolean isAvailable, String image,
			foodCategoryModels foodCate) {
		super();
		this.id = id;
		this.food_name = food_name;
		this.description = description;
		this.price = price;
		this.isAvailable = isAvailable;
		this.image = image;
		this.foodCategory = foodCate;
	}
	private foodCategoryModels foodCategory;
	public foodModels() {
		super();
		// TODO Auto-generated constructor stub
	}
	public foodModels(int id, String food_name, String description, BigDecimal price, Boolean isAvailable,
			foodCategoryModels foodCate) {
		super();
		this.id = id;
		this.food_name = food_name;
		this.description = description;
		this.price = price;
		this.isAvailable = isAvailable;
		this.foodCategory = foodCate;
	}

	public foodCategoryModels getFoodCategory() {
		return foodCategory;
	}
	public void setFoodCategory(foodCategoryModels foodCategory) {
		this.foodCategory = foodCategory;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
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
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Boolean getIsAvailable() {
		return isAvailable;
	}
	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

}
