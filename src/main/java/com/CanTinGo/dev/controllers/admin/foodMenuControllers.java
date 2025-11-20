package com.CanTinGo.dev.controllers.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.CanTinGo.dev.daos.foodCategoryDaos;
import com.CanTinGo.dev.daos.foodMenuDaos;
import com.CanTinGo.dev.models.foodModels;
import com.CanTinGo.dev.models.foodCategoryModels;

@Controller
@RequestMapping("/food-menu")
public class foodMenuControllers {
    
    @Autowired
    private foodMenuDaos foodMenuDaos;
    
    @Autowired 
    private foodCategoryDaos categoryDaos;
    @GetMapping("")
    public String getTemplate() {
    	return "redirect:/food-menu/food";
    }
    
    @GetMapping("/food")
    public String getFoodById(Model model) {
    	List<foodModels> food = foodMenuDaos.getAllFoodWithCategory();
    	System.out.printf("Danh sách món ăn: ", food);
    	model.addAttribute("listFood", food);
        model.addAttribute("food", new foodModels()); 
    	model.addAttribute("categories", categoryDaos.getAllFoodCate());
    	return "authen/admin/menu-food";
    }
    
    @PostMapping("/add-food")
    public String createFoodMenu(
            @RequestParam("food_name") String foodName,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("available_quantity") Integer availableQuantity,
            @RequestParam("category_id") Integer categoryId,
            @RequestParam(value = "isAvailable", required = false) boolean isAvailable
    ) {
        foodModels food = new foodModels();
        food.setFood_name(foodName);
        food.setDescription(description);
        food.setPrice(price);
        food.setAvailable_quantity(availableQuantity);
        food.setIsAvailable(isAvailable);

        foodCategoryModels category = categoryDaos.getCateById(categoryId);
        food.setFoodCategory(category);

        foodMenuDaos.createDataFood(food);
        return "redirect:/food-menu/food";
    }
    
    @DeleteMapping("/delete-food/{id}/{category_id}")
    public String deleteFood(@PathVariable int id, @PathVariable int categoryId, RedirectAttributes ra) {
        boolean success = foodMenuDaos.deleteFoodByIdAndCategory(id, categoryId);
        if(success) {
            ra.addFlashAttribute("message", "Xoá món thành công!");
        } else {
            ra.addFlashAttribute("error", "Xoá món thất bại!");
        }
        return "redirect:/food-menu/food";
    }
}
