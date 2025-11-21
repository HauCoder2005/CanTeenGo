package com.CanTinGo.dev.controllers.client;

import com.CanTinGo.dev.config.CustomUserDetails;
import com.CanTinGo.dev.daos.foodMenuDaos;
import com.CanTinGo.dev.models.foodModels;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/client")
public class clientControllers {

    private final foodMenuDaos foodService;

    public clientControllers(foodMenuDaos foodService) {
        this.foodService = foodService;
    }

    @GetMapping("/home")
    public String clientHome(Model model, Authentication authentication) {
        List<foodModels> listFood = foodService.getAllFoodByActive();
        model.addAttribute("listFood", listFood);
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            model.addAttribute("firstName", userDetails.getFirstName());
            model.addAttribute("lastName", userDetails.getLastName());
        }

        return "authen/client/client"; 
    }

}  
