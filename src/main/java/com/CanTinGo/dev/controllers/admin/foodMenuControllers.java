package com.CanTinGo.dev.controllers.admin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.CanTinGo.dev.daos.foodCategoryDaos;
import com.CanTinGo.dev.daos.foodMenuDaos;
import com.CanTinGo.dev.models.foodModels;

import jakarta.annotation.PostConstruct;


import jakarta.annotation.PostConstruct;

import com.CanTinGo.dev.models.foodCategoryModels;

@Controller
@RequestMapping("/food-menu")
public class foodMenuControllers {

    @Autowired
    private foodMenuDaos foodMenuDaos;

    @Autowired 
    private foodCategoryDaos categoryDaos;

    private final String uploadDir = "C:/CanTinGo/uploads/images/";

    @PostConstruct
    public void init() {
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) uploadFolder.mkdirs();
    }

    @GetMapping("")
    public String getTemplate() {
        return "redirect:/food-menu/food";
    }

    @GetMapping("/food")
    public String getFoodById(Model model) {
        List<foodModels> food = foodMenuDaos.getAllFoodWithCategory();
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
            @RequestParam(value = "isAvailable", required = false) Boolean isAvailable,
            @RequestParam("imageFile") MultipartFile imageFile,
            RedirectAttributes ra
    ) {

        foodModels food = new foodModels();
        food.setFood_name(foodName);
        food.setDescription(description);
        food.setPrice(price);
        food.setAvailable_quantity(availableQuantity);
        food.setIsAvailable(isAvailable != null ? isAvailable : false);

        foodCategoryModels category = categoryDaos.getCateById(categoryId);
        food.setFoodCategory(category);

        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
            food.setImage(fileName);
            try {
                Path filePath = Paths.get(uploadDir).resolve(fileName);
                imageFile.transferTo(filePath.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        boolean success = foodMenuDaos.createDataFood(food);
        if(success) ra.addFlashAttribute("message", "Food added successfully!");
        else ra.addFlashAttribute("error", "Failed to add food.");

        return "redirect:/food-menu/food";
    }

    @DeleteMapping("/delete-food/{id}")
    public String deleteFood(@PathVariable int id, RedirectAttributes ra) {
        boolean success = foodMenuDaos.deleteFoodByIdAndCategory(id);
        if(success) ra.addFlashAttribute("message", "Deleted food successfully!");
        else ra.addFlashAttribute("error", "Failed to delete food!");
        return "redirect:/food-menu/food";
    }

    @PatchMapping("/hide/{id}")
    public String hideFood(@PathVariable int id) {
        foodMenuDaos.updateAvailable(id, false); 
        return "redirect:/food-menu/food";
    }

    @PatchMapping("/show/{id}")
    public String showFood(@PathVariable int id) {
        foodMenuDaos.updateAvailable(id, true);
        return "redirect:/food-menu/food";
    }

    @PostMapping("/edit-food/{id}")
    public String updateFood(
            @PathVariable int id,
            @RequestParam("food_name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("available_quantity") int quantity,
            @RequestParam("category_id") int categoryId,
            @RequestParam(value = "isAvailable", required = false) Boolean isAvailable,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            RedirectAttributes ra
    ) {
        foodModels food = new foodModels();
        food.setId(id);
        food.setFood_name(name);
        food.setDescription(description);
        food.setPrice(price);
        food.setAvailable_quantity(quantity);
        food.setIsAvailable(isAvailable != null ? isAvailable : false);

        foodCategoryModels cate = categoryDaos.getCateById(categoryId);
        food.setFoodCategory(cate);

        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
            food.setImage(fileName);
            try {
                Path filePath = Paths.get(uploadDir).resolve(fileName);
                imageFile.transferTo(filePath.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            foodModels existingFood = foodMenuDaos.getFoodById(id);
            if(existingFood != null) food.setImage(existingFood.getImage());
        }

        foodMenuDaos.updateFoodById(food);
        ra.addFlashAttribute("message", "Food updated successfully!");
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
