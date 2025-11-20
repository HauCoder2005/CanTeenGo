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
    
    @GetMapping("")
    public String getTemplate() {
    	return "redirect:/food-menu/food";
    }
    
    @GetMapping("/food")
    public String getFoodById(Model model) {
    	List<foodModels> food = foodMenuDaos.getAllFoodWithCategory();
    	System.out.printf("Danh sách món ăn: ", food);
    	model.addAttribute("listFood", food);
    	return "authen/admin/menu-food";
    }
}
