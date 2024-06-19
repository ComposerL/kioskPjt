package com.office.kiosk.franchisee.menu;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.office.kiosk.admin.menu.AdminMenuDto;
import com.office.kiosk.franchisee.member.FranchiseeMemberDto;
import com.office.kiosk.paging.kioskPageDto;

import ch.qos.logback.core.model.Model;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/franchisee/menu")
public class FranchiseeMenuController {

	@Autowired
	FranchiseeMenuService franchiseeMenuService;

	
	// 프랜차이즈 메뉴 홈
	
//	@GetMapping("/franchiseeMenuHome")	
//	public String franchiseeMenuHome() {
//		log.info("franchiseeMenuHome()");
//		
//		String nextPage = "/franchisee/menu/franchisee_menu_home";
//		
//		return nextPage;
//		
//	}
//	
	// 프랜차이즈 메뉴 추가 신청 화면
	
//	@GetMapping("/createMenuForm")	
//	public String createMenuForm() {
//		log.info("createMenuForm()");
//		
//		String nextPage = "/franchisee/menu/create_menu_account_form";
//		
//		return nextPage;
//		
//	}
//	

	// 프랜차이즈 메뉴 추가 컨펌
	
//	@PostMapping("/createMenuAccountConfirm")
//	public String createMenuAccountConfirm(FranchiseeAddMenuDto franchiseeAddMenuDto, Model model) {
//		log.info("createMenuAccountConfirm()");
//		
//		String nextPage = "/franchisee/menu/create_menu_account_ok";
//		
//		int result = -1;
//		
//		result = franchiseeMenuService.createMenuAccountConfirm(franchiseeAddMenuDto);
//		
//		if (result <= 0) {
//			
//			nextPage = "/franchisee/menu/create_menu_account_ng";
//			
//		}		
//		
//		return nextPage;
//		
//	}	
//	
	
	// 프랜차이즈 메뉴 리스트(전체) 화면
	
	@GetMapping("/franchiseeMenuHome")
//	public Object getAllMenus(HttpSession session, Map<String, Object> menuMap) {
		public Object getAllMenus() {
		log.info("getAllMenus()");
				
		String nextPage = "franchisee/menu/franchisee_menu_list";
		
		
//		FranchiseeMemberDto loginedFranchiseeMemberDto = 
//				(FranchiseeMemberDto) session.getAttribute("loginedFranchiseeMemberDto");
//		
//		menuMap.put("m_id", loginedFranchiseeMemberDto.getF_id());
//		Map<String, Object> resultMap = franchiseeMenuService.getAllMenus(menuMap);
//		
//		return resultMap;
		
		return nextPage;
		
	}
	
	//프렌차이즈 모든 카테고리 가져오기
	@PostMapping("/getCategory")
	@ResponseBody
	public Object getCategory() {
		log.info("getCategory()");
		
		Map<String, Object> cateDtos = franchiseeMenuService.getCategory();
		
		return cateDtos;
	}
	
	//프렌차이즈 메뉴 가져오기
	@GetMapping("/getMenus")
	@ResponseBody
	public Object getMenus(@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("getMenus()");
		log.info("page:" + page);

		Map<String, Object> pagingFranchiseeMenuDtos = franchiseeMenuService.pagingFranchiseeMenuList(page);
		
		kioskPageDto franchiseeMenuListPageDto = franchiseeMenuService.getAllFranchiseeMenuListPageNum(page);

		pagingFranchiseeMenuDtos.put("franchiseeMenuListPageDto", franchiseeMenuListPageDto);
		
		return pagingFranchiseeMenuDtos;

	}
	
	// 카테고리에 따른 메뉴 불러오기(페이지네이션추가)

	@GetMapping("/getMenusByCategory")
	@ResponseBody
	public Object getMenusByCategory(Model model, @RequestParam("fcmc_no") String fcmc_no, @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("getMenusByCategory()");

		Map<String, Object> pagingFranchiseeMenuDtos = franchiseeMenuService.pagingFranchiseeMenuListByCate(page, fcmc_no);
			
		kioskPageDto FranchiseeMenuListPageDto = franchiseeMenuService.getAllFranchiseeMenuListPageNumByCate(page, fcmc_no);

		pagingFranchiseeMenuDtos.put("FranchiseeMenuListPageDto", FranchiseeMenuListPageDto);
			
		return pagingFranchiseeMenuDtos;

	}
	
	// 프랜차이즈 메뉴 수정 화면
	
//	@GetMapping("/modifySelectMenu")
//	public String modifySelectMenu(Model model) {
//		log.info("modifySelectMenu()");
//		
//		String nextPage = "/franchisee/menu/modify_menu_account_form";
//		
//		return nextPage;
//		
//	}
//	
//	@PostMapping("/getSelectMenuInfo")
//	@ResponseBody
//	public FranchiseeMenuDto getSelectMenuInfo(Model model, @RequestParam("fc_menu_no") String fc_menu_no) {
//		log.info("getSelectMenuInfo()");
//
//		FranchiseeMenuDto dto = franchiseeMenuService.getSelectMenuInfo(fc_menu_no);
//
//		return dto;
//	}
//	
	
	// 프랜차이즈 메뉴 수정 컨펌
	
//	@PostMapping("/modifyMenuAccountConfirm")
//	public String modifyMenuAccountConfirm(FranchiseeMenuDto franchiseeMenuDto, Model model) {
//		log.info("createMenuAccountConfirm()");
//		
//		String nextPage = "/franchisee/menu/modify_menu_account_ok";
//		
//		int result = -1;
//		
//		result = franchiseeMenuService.createMenuAccountConfirm(franchiseeMenuDto);
//		
//		if (result <= 0) {
//			
//			nextPage = "/franchisee/menu/modify_menu_account_ng";
//			
//		}		
//		
//		return nextPage;
//		
//	}	
	
	
	// 프랜차이즈 메뉴 삭제 컨펌
	
	

	
}
