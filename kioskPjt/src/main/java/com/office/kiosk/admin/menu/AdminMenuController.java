package com.office.kiosk.admin.menu;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.office.kiosk.paging.kioskPageDto;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/admin/menu")
public class AdminMenuController {

	@Autowired
	AdminMenuService adminMenuService;

	// 메뉴 등록 화면 페이지 이동(뷰 변경)

	@GetMapping("/createMenuForm")
	public String createMenuForm(Model model, AdminMenuCategoryDto adminMenuCategoryDto) {
		log.info("createMenuForm()");

		String nextPage = "admin/menu/create_menu_account_form";

		model.addAttribute("adminMenuCategoryDto", adminMenuCategoryDto);

		return nextPage;

	}

	// 메뉴 등록

	@PostMapping("/createMenuAccountConfirm")
	public String createMenuAccountConfirm(AdminMenuDto adminMenuDto, @RequestParam("file") MultipartFile file) {
		log.info("createMenuAccountConfirm()");

		String nextPage = "redirect:/admin/menu/menuList";

		log.info(file);

		ResponseEntity<String> saveFileName = adminMenuService.uploadFile(file);

		if (saveFileName != null) {
			adminMenuDto.setFc_menu_img_name(saveFileName.getBody());

			int result = adminMenuService.createMenuAccountConfirm(adminMenuDto);

			if (result <= 0)
				nextPage = "admin/menu/create_menu_account_ng";

		} else {
			nextPage = "admin/menu/create_menu_account_ng";

		}

		return nextPage;
	}

	// 모든 카테고리 가져오기 (기존)

	/*
	@PostMapping("/getCategory")
	@ResponseBody
	public Object getCategory() {
		log.info("getCategory()");

		Map<String, Object> cateDtos = adminMenuService.getCategory();

		return cateDtos;

	}
	*/
	
	// 모든 카테고리 가져오기 (수정)

	@PostMapping("/getCategory")
	@ResponseBody
	public Object getCategory() {
		log.info("getCategory()");

		Map<String, Object> cateDtos = adminMenuService.getCategory();

		return cateDtos;

	}

	// 메뉴 카테고리 등록 확인 컨펌

	@PostMapping("/createMenuCategoryAccountConfirm")
	public String createMenuCategoryAccountConfirm(Model model, AdminMenuCategoryDto adminMenuCategoryDto) {
		log.info("createMenuCategoryAccountConfirm");

		String nextPage = "redirect:/admin/menu/createMenuForm";

		int result = -1;

		result = adminMenuService.createMenuCategoryAccountConfirm(adminMenuCategoryDto);

		if (result <= 0) {
			nextPage = "admin/menu/create_menu_account_ng";

		}

		return nextPage;

	}

	// 메뉴리스트 화면 페이지 이동(뷰 변경)

	@GetMapping("/menuList")
	public String menuList() {
		log.info("menuList()");

		String nextPage = "admin/menu/admin_menu_list";

		return nextPage;

	}

	// 메뉴리스트 화면 기존 메뉴 리스트 불러오기 메서드

	@GetMapping("/getMenus")
	@ResponseBody
	public Object getMenus(@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("getMenus()");
		log.info("page:" + page);

		Map<String, Object> pagingAdminMenuDtos = adminMenuService.pagingAdminMenuList(page);
		
		kioskPageDto adminMenuListPageDto = adminMenuService.getAllAdminMenuListPageNum(page);

		pagingAdminMenuDtos.put("adminMenuListPageDto", adminMenuListPageDto);
		
		return pagingAdminMenuDtos;

	}

	// 카테고리에 따른 메뉴 불러오기(기존)
	
	/*

	@PostMapping("/getMenusByCategory")
	@ResponseBody
	public Object getMenusByCategory(Model model, @RequestParam("fcmc_no") String fcmc_no) {
		log.info("getMenusByCategory()");

		Map<String, Object> adminMenuDtos = adminMenuService.getMenusByCategory(fcmc_no);
		

		
		return adminMenuDtos;

	}
	
	*/


	// 카테고리에 따른 메뉴 불러오기(페이지네이션추가)

	@GetMapping("/getMenusByCategory")
	@ResponseBody
	public Object getMenusByCategory(Model model, @RequestParam("fcmc_no") String fcmc_no, @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("getMenusByCategory()");

		Map<String, Object> pagingAdminMenuDtos = adminMenuService.pagingAdminMenuListByCate(page, fcmc_no);
		
		kioskPageDto adminMenuListPageDto = adminMenuService.getAllAdminMenuListPageNumByCate(page, fcmc_no);

		pagingAdminMenuDtos.put("adminMenuListPageDto", adminMenuListPageDto);
		
		return pagingAdminMenuDtos;

	}
	
	
	// 모달창으로 선택한 메뉴의 정보 가져오기

	@PostMapping("/getSelectMenuInfo")
	@ResponseBody
	public AdminMenuDto getSelectMenuInfo(Model model, @RequestParam("fc_menu_no") String fc_menu_no) {
		log.info("getSelectMenuInfo()");

		AdminMenuDto dto = adminMenuService.getSelectMenuInfo(fc_menu_no);

		return dto;
	}

	// 메뉴 정보 수정 버튼 컨펌
	@PostMapping("/modifyMenuAccountConfirm")
	public String modifyMenuAccountConfirm(AdminMenuDto adminMenuDto, @RequestParam(value = "file", required = false) MultipartFile file, @RequestParam("fc_menu_no") String fc_menu_no) {
		log.info("modifyMenuAccountConfirm()");
		log.info(adminMenuDto);

		String nextPage = "redirect:/admin/menu/menuList";
		/*
		 * if (file.getOriginalFilename().equals("")) { log.info("file is null!!"); }
		 * else { log.info("file is not null!!"); }
		 */

		log.info("=====> {}", file);

		if (!file.getOriginalFilename().equals("")) {
			log.info("-----------> not null");
			ResponseEntity<String> saveFileName = adminMenuService.uploadFile(file);
			adminMenuDto.setFc_menu_img_name(saveFileName.getBody());

//			int result = adminMenuService.modifyMenuAccountConfirm(adminMenuDto);
			
//			log.info("---------------------------------->" + result);

			

		} else {													// 기존의 img name 불러오기 추가돼야함!!!
			log.info("-----------> null");
			
			AdminMenuDto currentMenuDto = adminMenuService.getSelectMenuInfo(fc_menu_no);
			
			adminMenuDto.setFc_menu_img_name(currentMenuDto.getFc_menu_img_name());
			log.info("-----------> ",adminMenuDto.getFc_menu_img_name());
			
		}
		
		int result = adminMenuService.modifyMenuAccountConfirm(adminMenuDto);

		log.info("---------------------------------->" + result);
		
		
		 if (result <= 0) {
		        nextPage = "admin/menu/modify_menu_account_ng";
		    }
		
		return nextPage;
	}

	// 메뉴 삭제 확인 컨펌

	@GetMapping({ "/deleteMenuConfirm", "/admin/menu/deleteMenuConfirm" })
	public String deleteMenuConfirm(@RequestParam("fc_menu_no") String fc_menu_no) {
		log.info("deleteMenuConfirm");

		String nextPage = "redirect:/admin/menu/menuList";

		int result = -1;

		result = adminMenuService.deleteMenuConfirm(fc_menu_no);

		if (result <= 0) {

			nextPage = "admin/menu/delete_menu_account_ng";
		}

		return nextPage;

	}

}
