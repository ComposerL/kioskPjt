package com.office.kiosk.customer;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.office.kiosk.franchisee.FranchiseeStoreDto;
import com.office.kiosk.franchisee.member.FranchiseeMemberService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/customer/order")
public class CustomerController {
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	FranchiseeMemberService franchiseeMemberService;
	
	@PostMapping("/customerOrderAccountConfirm")
	@ResponseBody
	public Object customerOrderAccountConfirm(@RequestBody Map<String, Object> dataMsg, HttpSession session) {
		log.info("customerOrderAccountConfirm()");
		
		log.info("dataMsg--- " + dataMsg);
		
		FranchiseeStoreDto loginedFranchiseeStoreDto = 
				(FranchiseeStoreDto) session.getAttribute("loginedFranchiseeStoreDto");
		 		 
		log.info("loginedFranchiseeStoreDto--- " + loginedFranchiseeStoreDto);
		int fcs_no = loginedFranchiseeStoreDto.getFcs_no();
		int orderResult = customerService.insertCustomerOrder(dataMsg, fcs_no);
		
		return orderResult;
	}
	
	/*
	 * 	customer 로그인 화면 가맹점리스트 불러오기
	 */
	@PostMapping("/getStoreList")
	@ResponseBody
	public Object getStoreList(@RequestParam("fcm_id") String fcm_id) {
		log.info("getStoreList()");
		
		Map<String, Object> resultMap = franchiseeMemberService.getStoreList(fcm_id);
		
		return resultMap;
	}
	
	/*
	 * 	customer 로그인 화면(가맹회원 로그인)
	 */
	@PostMapping("/franchiseeStoreLogin")
	public Object franchiseeStoreLogin(FranchiseeStoreDto franchiseeStoreDto,
									HttpSession session) {
		log.info("franchiseeStoreLogin()");
		
		String nextPage = "customer/franchisee_store_login_result";
		
		FranchiseeStoreDto loginedFranchiseeStoreDto = franchiseeMemberService.franchiseeLoginConfirmForCustomer(franchiseeStoreDto);
		loginedFranchiseeStoreDto.setFcs_no(franchiseeStoreDto.getFcs_no());
		
		
		session.setAttribute("loginedFranchiseeStoreDto", loginedFranchiseeStoreDto);
		session.setMaxInactiveInterval(60 * 720);
		
		return nextPage;
		
	}
	
	/*
	 * 	매장 or 포장 유무 선택 후 오더 뷰 이동
	 */
	@GetMapping("/customerOrderView")
	public String customerOrderView(@RequestParam("fco_packaging") int fco_packaging,
									Model model) {
		log.info("customerOrderView()");
		
		String nextPage = "customer/customer_order_view";
		
		model.addAttribute("fco_packaging", fco_packaging);	// 매장 or 포장 유무
		
		return nextPage;
		
	}
	
	/*
	 * 	customer login 후 포장유무 선택화면
	 */
	@GetMapping("/sotreLoginResultView")
	public String sotreLoginResultView(HttpSession session) {
		log.info("sotreLoginResultView()");
		
		String nextPage = "customer/franchisee_store_login_result";
		
		return nextPage;
	
	}
	
	
}
