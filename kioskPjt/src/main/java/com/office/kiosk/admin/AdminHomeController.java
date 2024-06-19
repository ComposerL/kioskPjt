package com.office.kiosk.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/admin")
public class AdminHomeController {
	
	@GetMapping({"/home", "/", ""})
	public String home() {
		log.info("home()");
		
		String nextPage = "admin/admin_home";
		
		return nextPage;
		
	}
	

	
	

}
