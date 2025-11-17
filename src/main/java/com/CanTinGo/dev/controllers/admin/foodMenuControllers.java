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
    private foodCategoryDaos foodCateGory;

    @Autowired
    private foodMenuDaos foodMenuDao;

    // Load all categories for every view
    @ModelAttribute("allCategories")
    public List<foodCategoryModels> populateCategories() {
        List<foodCategoryModels> categories = foodCateGory.getAllFoodCate();
        return categories != null ? categories : new ArrayList<>();
    }

    // Main page: food list + add form
    @GetMapping("")
    public String getMenuFood(Model model) {
        model.addAttribute("food", new foodModels());
        model.addAttribute("listFood", foodMenuDao.getAllFood());
        model.addAttribute("isEdit", false);
        return "authen/admin/menu-food";
    }

    // Filter by category
    @GetMapping("/category/{id}")
    public String filterByCategory(@PathVariable("id") int categoryId, Model model) {
        model.addAttribute("listFood", foodMenuDao.getAllFoodByIdCate(categoryId));
        model.addAttribute("food", new foodModels());
        model.addAttribute("isEdit", false);
        model.addAttribute("selectedCate", categoryId);
        return "authen/admin/menu-food";
    }

    // Edit food form
    @GetMapping("/edit/{id}")
    public String editFood(@PathVariable("id") int id, Model model) {
        foodModels food = foodMenuDao.getFoodById(id);
        if (food == null) {
            model.addAttribute("error", "Food item not found!");
            food = new foodModels();
        }
        model.addAttribute("food", food);
        model.addAttribute("listFood", foodMenuDao.getAllFood());
        model.addAttribute("isEdit", true);
        return "authen/admin/menu-food";
    }

    // Add new food
    @PostMapping("/add")
    public String addFood(@ModelAttribute foodModels food, RedirectAttributes ra) {
        if (food.getFood_name() == null || food.getFood_name().trim().isEmpty()) {
            ra.addFlashAttribute("error", "Food name cannot be empty!");
            return "redirect:/food-menu";
        }
        boolean ok = foodMenuDao.addFood(food);
        ra.addFlashAttribute("message", ok ? "Food added successfully!" : "Failed to add food!");
        return "redirect:/food-menu";
    }

    // Update food
    @PostMapping("/update")
    public String updateFood(@ModelAttribute foodModels food, RedirectAttributes ra) {
        boolean ok = foodMenuDao.updateFood(food);
        ra.addFlashAttribute("message", ok ? "Update successful!" : "Update failed!");
        return "redirect:/food-menu";
    }

    // Delete food
    @GetMapping("/delete/{id}")
    public String deleteFood(@PathVariable("id") int id, RedirectAttributes ra) {
        boolean ok = foodMenuDao.deleteFood(id);
        ra.addFlashAttribute("message", ok ? "Deleted successfully!" : "Delete failed!");
        return "redirect:/food-menu";
    }
}
