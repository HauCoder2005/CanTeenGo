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
    
    @DeleteMapping("/delete-food/{id}")
    public String deleteFood(@PathVariable int id, RedirectAttributes ra) {
        boolean success = foodMenuDaos.deleteFoodByIdAndCategory(id);
        if(success) {
            ra.addFlashAttribute("message", "Xoá món thành công!");
        } else {
            ra.addFlashAttribute("error", "Xoá món thất bại!");
        }
        return "redirect:/food-menu/food";
    }
    
    // hide food 
    @PatchMapping("/hide/{id}")
    public String hideFood(@PathVariable int id) {
        foodMenuDaos.updateAvailable(id, false); 
        return "redirect:/food-menu/food";
    }

    // show food
    @PatchMapping("/show/{id}")
    public String showFood(@PathVariable int id) {
        foodMenuDaos.updateAvailable(id, true);
        return "redirect:/food-menu/food";
    }
    
    
   // Edit food By Id 
    @PostMapping("/edit-food/{id}")
    public String updateFood(
            @PathVariable int id,
            @RequestParam("food_name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("available_quantity") int quantity,
            @RequestParam("category_id") int categoryId,
            @RequestParam(value = "isAvailable", required = false) boolean isAvailable,
            RedirectAttributes ra
    ) {
        foodModels food = new foodModels();
        food.setId(id);
        food.setFood_name(name);
        food.setDescription(description);
        food.setPrice(price);
        food.setAvailable_quantity(quantity);
        food.setIsAvailable(isAvailable);

        foodCategoryModels cate = categoryDaos.getCateById(categoryId);
        food.setFoodCategory(cate);

        foodMenuDaos.updateFoodById(food);

        ra.addFlashAttribute("message", "Cập nhật món thành công!");

        return "redirect:/food-menu/food";
    }
    @GetMapping("/edit-food/{id}")
    public String editFood(@PathVariable int id, Model model) {

        foodModels food = foodMenuDaos.getFoodById(id);

        model.addAttribute("foodEdit", food);   
        model.addAttribute("listFood", foodMenuDaos.getAllFoodWithCategory());
        model.addAttribute("categories", categoryDaos.getAllFoodCate());

        return "authen/admin/menu-food";

    }

}
