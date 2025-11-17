package com.CanTinGo.dev.controllers.admin;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.CanTinGo.dev.daos.userDaos;
import com.CanTinGo.dev.models.userModels;


@Controller
@RequestMapping("/admin")
public class adminControllers {
	@Autowired private userDaos userDaos;
	@GetMapping("/")
    public String adminHome() {
        return "authen/admin/dashboard"; 
    }

    @GetMapping("/users")
    public String showAllUsers(Model model) {
    	List<userModels> users = userDaos.getAllUser();
    	model.addAttribute("users", users);
        return "authen/admin/main-users";
    }
    
    @GetMapping("/menu-food")
    public String menuFood() {
    	return "authen/admin/menu-food";
    }
    @GetMapping("/main")
    public String main() {
        return "authen/admin/main";
    }

    @GetMapping("/fragments/content-{page}")
    public String getContent(@PathVariable String page) {
        return "authen/admin/fragments/content-" + page;
    }
    
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable int id, RedirectAttributes ra) {
    	boolean deleted = userDaos.deleteUserById(id);
    	if (deleted) {
            ra.addFlashAttribute("message", "Xóa người dùng thành công!");
        } else {
            ra.addFlashAttribute("error", "Không thể xóa (có thể là Admin hoặc không tồn tại)!");
        } 
    	return "redirect:/admin/users";
    }
}
