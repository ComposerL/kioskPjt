package com.office.kiosk.franchisee;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
public class FranchiseeHomeController {

	@GetMapping({"/franchisee/home","/franchisee", "/franchisee/"})
	public String franchiseeHome() {
		log.info("franchiseeHome()");
	
		String nextPage = "franchisee/franchisee_home";
		
		return nextPage;
		
	}
}
