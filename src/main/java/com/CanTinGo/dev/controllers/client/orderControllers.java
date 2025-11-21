package com.CanTinGo.dev.controllers.client;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.CanTinGo.dev.config.CustomUserDetails;
import com.CanTinGo.dev.daos.foodMenuDaos;
import com.CanTinGo.dev.daos.orderDaos;
import com.CanTinGo.dev.models.foodModels;
import com.CanTinGo.dev.models.orderItemModels;
import com.CanTinGo.dev.models.orderModels;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/order")
public class orderControllers {

    @Autowired
    private orderDaos orderDaos;
   
    @Autowired
    private foodMenuDaos foodMenuDaos;
    
    @PostMapping("/cart/add")
    @ResponseBody
    public String addToCart(@RequestParam int foodId, @RequestParam int quantity, HttpSession session) {
        List<orderItemModels> cart = (List<orderItemModels>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        foodModels food = foodMenuDaos.getFoodById(foodId);
        boolean found = false;

        for (orderItemModels item : cart) {
            if (item.getFoods().getId() == foodId) {
                item.setQuantity(item.getQuantity() + quantity);
                item.setPrice(food.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                found = true;
                break;
            }
        }

        if (!found) {
            orderItemModels item = new orderItemModels();
            item.setFoods(food);
            item.setQuantity(quantity);
            item.setPrice(food.getPrice().multiply(BigDecimal.valueOf(quantity)));
            cart.add(item);
        }

        session.setAttribute("cart", cart);
        return String.valueOf(cart.size());
    }

    @GetMapping("/cart/count")
    @ResponseBody
    public String getCartCount(HttpSession session) {
        List<orderItemModels> cart = (List<orderItemModels>) session.getAttribute("cart");
        if (cart == null) return "0";
        return String.valueOf(cart.size());
    }


    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        List<orderItemModels> cart = (List<orderItemModels>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();
        model.addAttribute("cart", cart);
        return "authen/client/cart";
    }

    @PostMapping("/checkout")
    public String checkout(@RequestParam String paymentMethod, Authentication authentication, HttpSession session) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return "redirect:/login";
        }

        List<orderItemModels> cart = (List<orderItemModels>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) return "redirect:/client/home";

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        orderModels order = new orderModels();
        order.setUsers(userDetails.getUser());
        order.setPayment_method(paymentMethod);

        BigDecimal total = BigDecimal.ZERO;
        for (orderItemModels item : cart) {
            BigDecimal price = item.getFoods().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            item.setPrice(price);
            total = total.add(price);
        }
        order.setTotal_price(total);

        int orderId = orderDaos.createOrder(order);
        orderDaos.createOrderItems(orderId, cart);

        session.removeAttribute("cart");
        return "redirect:/order/success/" + orderId;
    }

    @GetMapping("/success/{orderId}")
    public String orderSuccess(@PathVariable int orderId, Model model) {
        orderModels order = orderDaos.getOrderById(orderId);
        model.addAttribute("order", order);
        return "authen/client/order-success";
    }
}
