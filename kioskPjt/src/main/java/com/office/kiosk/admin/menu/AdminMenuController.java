package com.office.kiosk.admin.menu;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.office.kiosk.franchisee.menu.FranchiseeMenuCategoryDto;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/admin/menu")
public class AdminMenuController {

	@Autowired
	AdminMenuService adminMenuService;
	
	
	// 메뉴 등록 화면 페이지네이션
	
	@GetMapping("/createMenuForm")
	public String createMenuForm(Model model, FranchiseeMenuCategoryDto franchiseeMenuCategoryDto) {
		log.info("createMenuForm()");
		
		String nextPage = "/admin/menu/create_menu_account_form";
		
		model.addAttribute("franchiseeMenuCategoryDto", franchiseeMenuCategoryDto);
		
		return nextPage;
		
	}
	
	// 메뉴 등록 화면 기존 카테고리들 select창에 불러오기 메서드
	
	@PostMapping("/getCategory")
	@ResponseBody
	public Object getCategory()  {
		log.info("getCategory()");
		
		Map<String, Object> cateDtos = adminMenuService.getCategory();
				
		return cateDtos;
		
	}
	
	// 메뉴 카테고리 등록 확인 컨펌(넥스트페이지 안넘어가게 바뀌어야함)
	
	@PostMapping("/createMenuCategoryAccountConfirm")
	public String createMenuCategoryAccountConfirm(Model model, FranchiseeMenuCategoryDto franchiseeMenuCategoryDto) {
		log.info("createMenuCategoryAccountConfirm");
		
		String nextPage = "/admin/menu/create_menu_account_ok";
		
		int result = -1;
		
		result = adminMenuService.createMenuCategoryAccountConfirm(franchiseeMenuCategoryDto);
		
		if (result <= 0) {
			nextPage = "/admin/menu/create_menu_account_ng";
			
		}
		
		return nextPage;
		
	}
	
	// 메뉴리스트 화면 페이지네이션
	
	@GetMapping("/menuList")
	public String menuList() {
		log.info("menuList()");
		
		String nextPage = "/admin/menu/admin_menu_list";
		
		return nextPage;
		
	}
	
	
	
	
	// 메뉴리스트 화면 기존 메뉴 리스트 불러오기 메서드
	
	@PostMapping("/getMenus")
	@ResponseBody
	public Object getMenus()  {
		log.info("getMenus()");
		
		Map<String, Object> menuDtos = adminMenuService.getMenus();
				
		return menuDtos;
		
	}
	
	
	
	
	
}