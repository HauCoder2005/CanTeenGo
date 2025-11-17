package com.CanTinGo.dev.controllers.athen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.CanTinGo.dev.daos.userDaos;
import com.CanTinGo.dev.models.userModels;

@Controller
public class loginControllers {
	@Autowired private userDaos userDao;

	@GetMapping("/login")
	public String loginHome() {
		return "authen/login";
	}
    @PostMapping("/login")
    public String loginPost(@RequestParam String username,
                            @RequestParam String password) {
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        return "authen/login";
    }
    @GetMapping("/logout")
    public String logout() {
    	return "authen/login";
    }
    // get form register first
    @GetMapping("/register")
    public String getRegister(Model model) {
    	model.addAttribute("user", new userModels());
        return "authen/register";
    }
    
    // post form register last
    @PostMapping("/register")
    public String postRegister(
            @ModelAttribute("user") userModels user,
            @RequestParam String confirmPassword,
            Model model) {

        // 1 check password
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("popupError", "Mật khẩu không khớp!");
            return "authen/register";
        }

        // 2 check username
        if (userDao.findByUsername(user.getUsername())) {
            model.addAttribute("popupError", "Tên đăng nhập đã tồn tại!");
            return "authen/register";
        }

        // 3 check email
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            if (userDao.findByEmail(user.getEmail())) {
                model.addAttribute("popupError", "Email đã được sử dụng!");
                return "authen/register";
            }
        }

        // 4 register
        if (userDao.register(user)) {
            model.addAttribute("registeredUser", user.getUsername());
            return "authen/login";
        } else {
            model.addAttribute("popupError", "Lỗi hệ thống. Vui lòng thử lại!");
            return "authen/register";
        }
    }
    
    
}
