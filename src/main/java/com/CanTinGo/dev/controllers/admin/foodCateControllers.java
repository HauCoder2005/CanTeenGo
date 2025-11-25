package com.CanTinGo.dev.controllers.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.CanTinGo.dev.daos.foodCategoryDaos;
import com.CanTinGo.dev.models.foodCategoryModels;

@Controller
@RequestMapping("/food-category")
public class foodCateControllers {
	@Autowired
	private foodCategoryDaos foodCateDaos;
	
	@GetMapping("")
	public String getTemplateFoodCate() {
	    return "redirect:/food-category/category";
	}
	
	@GetMapping("/category")
	public String getAllCate(Model model) {
		List<foodCategoryModels> cate = foodCateDaos.getAllFoodCate();
		System.out.print(cate);
		model.addAttribute("listCate", cate);
 		return "authen/admin/food-category";
	}
	
	
    @PostMapping("/add-category")
    public String addCateFood(@RequestParam String food_name, RedirectAttributes ra) {
        boolean created = foodCateDaos.createFoodCate(food_name);
        if (created) {
            ra.addFlashAttribute("message", "Thêm loại món ăn thành công!");
        } else {
            ra.addFlashAttribute("error", "Loại món ăn đã tồn tại hoặc lỗi hệ thống!");
        }
        return "redirect:/food-category";
    }
    
    @GetMapping("/show-edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        foodCategoryModels cate = foodCateDaos.getCateById(id);
        model.addAttribute("category", cate);
        return "admin/edit-form"; 
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model, RedirectAttributes ra) {
        foodCategoryModels cate = foodCateDaos.getCateById(id); 
        if (cate == null) {
            ra.addFlashAttribute("error", "Không tìm thấy category!");
            return "redirect:/food-category";
        }
        model.addAttribute("category", cate); 
        model.addAttribute("listCate", foodCateDaos.getAllFoodCate()); 
        return "authen/admin/food-category"; 
    }
}
