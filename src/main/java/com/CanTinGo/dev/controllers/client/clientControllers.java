package com.CanTinGo.dev.controllers.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client")
public class clientControllers {
	@GetMapping("/home")
	public String clientHome() {
		return "authen/client/client";
	}
}
