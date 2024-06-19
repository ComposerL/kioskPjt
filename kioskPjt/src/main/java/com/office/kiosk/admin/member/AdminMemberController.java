package com.office.kiosk.admin.member;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.office.kiosk.franchisee.sales.FranchiseeSalesDto;
import com.office.kiosk.paging.kioskPageDto;

import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;


@Log4j2
@Controller
@RequestMapping("/admin/member")
public class AdminMemberController {

	@Autowired
	AdminMemberService adminMemberService;
		
	/*
	 * 	admin 회원가입
	 */
	@GetMapping("/adminMemberAccountForm")
	public String adminMemberAccountForm() {
		log.info("adminMemberAccountForm()");
		
		String nextPage = "admin/member/create_admin_account_form";
	
		return nextPage;
		
	}
	
	/*
	 * 	admin 회원가입 확인
	 */
	@PostMapping("/createadminAccountConfirm")
	public String createadminAccountConfirm(AdminMemberDto adminMemberDto, Model model) {
		log.info("createadminAccountConfirm()");
		
		String nextPage = "admin/member/create_admin_account_ok";
		
		int result = adminMemberService.createAdminAccountConfirm(adminMemberDto);
		
		if (result <= 0) 
			nextPage = "admin/member/create_admin_account_ng";
		
		return nextPage;
		
	}
	
	/*
	 * 	admin 로그인
	 */
	@GetMapping("/adminLoginForm")
	public String adminLoginForm() {
		log.info("adminLoginForm()");
		
		String nextPage = "admin/member/admin_login_form";
	
		return nextPage;
		
	}
	
	/*
	 *  admin 로그인 확인
	 */
	@PostMapping("/adminLoginConfirm")
	public String adminLoginConfirm(AdminMemberDto adminMemberDto, HttpSession session) {
		log.info("adminLoginConfirm()");
		
		String nextPage = "admin/member/admin_login_ok";
		
		AdminMemberDto loginedAdminMemberDto = 
				adminMemberService.adminLoginConfirm(adminMemberDto);
		
		if (loginedAdminMemberDto != null) {
			session.setAttribute("loginedAdminMemberDto", loginedAdminMemberDto);
			session.setMaxInactiveInterval(60 * 30);
			
		} else {
			nextPage = "admin/member/admin_login_ng";
			
		}
	
		return nextPage;
		
	}
	
	/*
	 * 	admin 로그아웃 확인
	 */
	@GetMapping("/adminLogoutConfirm")
	public String adminLogoutConfirm(HttpSession session) {
		log.info("adminLogoutConfirm()");
		
		String nextPage = "admin/admin_home";
		
		session.removeAttribute("loginedAdminMemberDto");
	
		return nextPage;
	}
	
	/*
	 *  amdin 정보수정
	 */
	@GetMapping("/adminMemberModifyForm")
	public String adminMemberModifyForm(HttpSession session) {
		log.info("adminMemberModifyForm()");
		
		String nextPage = "admin/member/admin_modify_form";
	
		return nextPage;
	
	}
	
	/*
	 * 	admin 정보수정 확인
	 */
	@PostMapping("/adminModifyConfirm")
	public String adminModifyConfirm(AdminMemberDto adminMemberDto, HttpSession session) {
		log.info("adminModifyConfirm()");
		
		String nextPage = "admin/member/admin_modify_ok";
		
		AdminMemberDto modifiedDTo = 
				adminMemberService.adminModifyConfirm(adminMemberDto);
		
		if (modifiedDTo == null) {
			nextPage = "admin/member/admin_modify_ng";
			
		} else {
			session.setAttribute("loginedAdminMemberDto", modifiedDTo);
			session.setMaxInactiveInterval(60 * 30);
			
		}
		
		return nextPage;
	}
	
	/*
	 *  franchisee list 불러오기
	 */
//	@GetMapping("/franchiseeList")
//	public String franchiseeList(Model model) {
//		log.info("franchiseeList()");
//		
//		String nextPage = "/admin/member/franchisee_list";
//		
//		List<FranchiseeMemberDto> franchiseeMemberDtos = adminMemberService.franchiseeList();
//		
//		model.addAttribute("franchiseeMemberDtos", franchiseeMemberDtos);
//		
//		return nextPage;
//		
//	}
	
	/*
	 * 	가맹회원 목록
	 */
	@GetMapping("/franchiseeList")
	public String franchiseeList() {
		log.info("franchiseeList()");
		
		String nextPage = "admin/member/franchisee_list";
		
		return nextPage;
		
	}
	
	/*
	 * 	가맹회원 리스트 불러오기
	 */
	@GetMapping("/getFranchiseeList")
	@ResponseBody
	public Object franchiseeList(@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("franchiseeList()");
		log.info("page: "+page);
		
		Map<String, Object> pagingFranchiseeMemberDtos = adminMemberService.pagingFranchiseeList(page);
		
		kioskPageDto franchiseeMemberListPageDto = adminMemberService.getAllFranchiseeListPageNum(page);
		
		pagingFranchiseeMemberDtos.put("franchiseeMemberListPageDto", franchiseeMemberListPageDto);
				
		return pagingFranchiseeMemberDtos;
		
	}
	
	/*
	 * 	franchisee 승인하기
	 */
	@GetMapping("/franchiseeApprove")
	public String franchiseeApprove(@RequestParam("fcm_no") int fcm_no) {
		log.info("franchiseeApprove()");
		
		String nextPage = "redirect:/admin/member/franchiseeList";
		
		adminMemberService.franchiseeApprove(fcm_no);
		
		return nextPage;
		
	}
	
	/*
	 *  admin list 불러오기
	 */
//	@GetMapping("/adminList")
//	public String adminList(Model model, HttpSession session) {
//		log.info("adminList()");
//		
//		String nextPage = "/admin/member/admin_list";
//		
//		AdminMemberDto loginedAdminMemberDto = 
//				(AdminMemberDto) session.getAttribute("loginedAdminMemberDto");
//		
//		if (loginedAdminMemberDto.getAm_id().equals("super admin")) {
//			
//			List<AdminMemberDto> adminMemberDtos = adminMemberService.adminList();
//			
//			model.addAttribute("adminMemberDtos", adminMemberDtos);
//		} else {
//			nextPage = "/admin/member/admin_list_fail";
//			
//		}
//		
//		return nextPage;
//		
//	}
	
	/*
	 *  admin 목록 불러오기
	 */
	@GetMapping("/adminList")
	public String adminList(Model model, HttpSession session) {
		log.info("adminList()");
		
		String nextPage = "admin/member/admin_list";
		
		AdminMemberDto loginedAdminMemberDto = 
				(AdminMemberDto) session.getAttribute("loginedAdminMemberDto");
		
		if (loginedAdminMemberDto == null || !loginedAdminMemberDto.getAm_id().equals("super admin")) {
			nextPage = "admin/member/admin_list_fail";
			
		} else if (loginedAdminMemberDto.getAm_id().equals("super admin")) {
			nextPage = "admin/member/admin_list";
			
		} 
		
		return nextPage;
	}
	
//	@GetMapping("/getAdminList")
//	@ResponseBody
//	public Object adminList( HttpSession session) {
//		log.info("adminList()");
//				
//		AdminMemberDto loginedAdminMemberDto = 
//				(AdminMemberDto) session.getAttribute("loginedAdminMemberDto");
//		
//		if (loginedAdminMemberDto.getAm_id().equals("super admin")) {
//			
//			Map<String, Object> adminMemberDtos = adminMemberService.adminList();
//			
//			return adminMemberDtos;
//		} else {
//			
//			return null;
//			
//		}
//				
//	}
	
	/*
	 * 	어드민 목록 불러오기
	 */
	@GetMapping("/getAdminList")
	@ResponseBody
	public Object adminList(@RequestParam(value = "page", required = false, defaultValue = "1") int page, HttpSession session) {
		log.info("adminList()");
		log.info("page: "+page);
		
		AdminMemberDto loginedAdminMemberDto = 
				(AdminMemberDto) session.getAttribute("loginedAdminMemberDto");
		
		if (loginedAdminMemberDto.getAm_id().equals("super admin")) {
			
			Map<String, Object> pagingAdminMemberDtos = adminMemberService.pagingAdminList(page);
			
			kioskPageDto adminMemberListPageDto = adminMemberService.getAllAdminListPageNum(page);
			
			pagingAdminMemberDtos.put("adminMemberListPageDto", adminMemberListPageDto);
			
			return pagingAdminMemberDtos;
		} else {
			
			return null;
			
		}
				
	}
		
	/*
	 * 	admin 승인하기
	 */
	@GetMapping("/adminApprove")
	public String adminApprove(@RequestParam("am_no") int am_no) {
		log.info("adminApprove()");
		
		String nextPage = "redirect:/admin/member/adminList";
		
		adminMemberService.adminApprove(am_no);
		
		return nextPage;
		
	}
	
	/*
	 * 	store list 불러오기
	 */
	@GetMapping("/storeList")
	public String storeList(Model model) {
		log.info("storeList()");
		
		String nextPage = "admin/member/franchisee_store_list";
				
		return nextPage;
		
	}
	@GetMapping("/getAllFranchiseeStoreInfo")
	@ResponseBody
	public Object storeList(@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("storeList()");
				
		Map<String, Object> franchiseeSotreDtos = adminMemberService.storeList(page);
		
		kioskPageDto storeListPageDto = adminMemberService.getAllStoreListPageNum(page);
		
		franchiseeSotreDtos.put("storeListPageDto", storeListPageDto);
				
		return franchiseeSotreDtos;
		
	}
	
	/*
	 * 	신규 가맹점 등록 폼
	 */
	@GetMapping("/createFranchiseeStore")
	public String createFranchiseeStore() {
		log.info("createFranchiseeStore()");
		
		String nextPage = "admin/member/create_franchisee_store_form";
		
		return nextPage;
		
	}
	
	/*
	 * 	all franchisee info 불러오기
	 */
	@GetMapping("/franchiseeInfoList")
	@ResponseBody
	public Object franchiseeInfoList() {
		log.info("franchiseeInfoList()");
		
		Map<String, Object> resultMap = adminMemberService.franchiseeInfoList();
		
		
		return resultMap;
		
	}
	
	/*
	 * 	select franchisee store info 불러오기
	 */
	@PostMapping("/getFranchiseeStoreListBySelect")
	@ResponseBody
	public Object getFranchiseeStoreListBySelect(@RequestParam("fcm_id") String fcm_id) {
		log.info("getFranchiseeStoreListBySelect()");
		
		Map<String, Object> resultMap = adminMemberService.getFranchiseeStoreListBySelect(fcm_id);
		
		return resultMap;
		
	}
	
	/*
	 * 	create franchisee store confirm 
	 */
	@PostMapping("/createFranchiseeStoreConfirm")
	@ResponseBody
	public Object createFranchiseeStoreConfirm(FranchiseeSalesDto franchiseeSalesDto) {
		log.info("createFranchiseeStoreConfirm()");
		
		Map<String, Object> resultMap = adminMemberService.createFranchiseeStoreConfirm(franchiseeSalesDto);

		return resultMap;
	}
	
	/*
	 * 	가맹점 폐점
	 */
	@PostMapping("/franchiseeStoreDelete")
	@ResponseBody
	public String franchiseeStoreDelete(@RequestParam("fcs_no") int fcs_no) {
		log.info("franchiseeStoreDelete()");
		
		adminMemberService.franchiseeStoreDelete(fcs_no);
		
		return null;
	
	}
	
	

}
