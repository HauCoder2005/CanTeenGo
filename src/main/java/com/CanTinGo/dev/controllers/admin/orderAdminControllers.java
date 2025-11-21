package com.CanTinGo.dev.controllers.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.CanTinGo.dev.daos.orderDaos;
import com.CanTinGo.dev.models.orderModels;

@Controller
@RequestMapping("/order/admin")
public class orderAdminControllers {

    @Autowired
    private orderDaos orderDaos;

    @GetMapping("/data")
    public String getDataOrder(Model model) {
        // Lấy toàn bộ order
        List<orderModels> orders = orderDaos.getAllOrders();
        model.addAttribute("orders", orders);
        return "authen/admin/order-data";
    }
    
    @PostMapping("/update-status")
    public String updateOrderStatus(@RequestParam int orderId, @RequestParam String status, RedirectAttributes ra) {
        boolean success = orderDaos.updateOrderStatus(orderId, status);
        if(success) {
            ra.addFlashAttribute("message", "Cập nhật trạng thái thành công!");
        } else {
            ra.addFlashAttribute("error", "Cập nhật thất bại!");
        }
        return "redirect:/order/admin/data";
    }

}

