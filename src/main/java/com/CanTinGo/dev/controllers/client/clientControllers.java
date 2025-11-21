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
        List<foodModels> listFood = foodService.getAllFoodWithCategory();
        model.addAttribute("listFood", listFood);
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String firstName = userDetails.getFirstName();
            String lastName = userDetails.getLastName();
            model.addAttribute("firstName", firstName);
            model.addAttribute("lastName", lastName);
        }

        return "authen/client/client"; 
    }
}
