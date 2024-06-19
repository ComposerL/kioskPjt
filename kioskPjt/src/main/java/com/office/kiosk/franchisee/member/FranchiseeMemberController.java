package com.office.kiosk.franchisee.member;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.office.kiosk.franchisee.FranchiseeStoreDto;

import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/franchisee/member")
public class FranchiseeMemberController {

	@Autowired
	FranchiseeMemberService franchiseeMemberService;

	/*
	 * 회원가입
	 */
	@GetMapping("/createFranchiseeAccountForm")
	public String createFranchiseeAccountForm() {
		log.info("createFranchiseeAccountForm()");

		String nextPage = "franchisee/member/create_franchisee_account_form";

		return nextPage;

	}

	/*
	 * 회원가입 확인
	 */
	@PostMapping("/createFranchiseeAccountConfirm")
	public String createFranchiseeAccountConfirm(FranchiseeMemberDto memberDto, Model model) {
		log.info("createFranchiseeAccountConfirm()");

		String nextPage = "franchisee/member/create_franchisee_account_ok";

		int result = franchiseeMemberService.createFranchiseeAccountConfirm(memberDto);

		if (result <= 0)
			nextPage = "franchisee/member/create_franchisee_account_ng";

		return nextPage;

	}

	/*
	 * 로그인 폼
	 */
	@GetMapping("/franchiseeLoginForm")
	public String franchiseeLoginForm() {
		log.info("FranchiseeLoginForm()");

		String nextPage = "franchisee/member/franchisee_login_form";

		return nextPage;

	}

//	/*
//	 * 	로그인 확인
//	 */
//	@PostMapping("/FranchiseeLoginConfirm")
//	public String franchiseeLoginConfirm(FranchiseeMemberDto franchiseeMemberDto, HttpSession session) {
//		log.info("FranchiseeLoginConfirm()");
//		
//		String nextPage = "/franchisee/member/franchisee_login_ok";
//		
//		FranchiseeMemberDto loginedFranchiseeMemberDto = 
//				franchiseeMemberService.FranchiseeLoginConfirm(franchiseeMemberDto);
//		
//		if (loginedFranchiseeMemberDto != null) {
//			session.setAttribute("loginedFranchiseeMemberDto", loginedFranchiseeMemberDto);
//			session.setMaxInactiveInterval(60 * 30);
//			
//		} else {
//			nextPage = "/franchisee/member/franchisee_login_ng";
//			
//		}
//	
//		return nextPage;
//		
//	}

	/*
	 * 로그인 성공
	 */
	@GetMapping("/franchiseeLoginSuccess")
	public String franchiseeLoginSuccess(HttpSession session, Model model) {
		log.info("franchiseeLoginSuccess()");

		String nextPage = "franchisee/member/franchisee_login_ok";

		FranchiseeMemberDto loginedFranchiseeMemberDto = (FranchiseeMemberDto) session
				.getAttribute("loginedFranchiseeMemberDto");

		List<FranchiseeStoreDto> result = franchiseeMemberService.getStoreListByDto(loginedFranchiseeMemberDto);

		model.addAttribute("result", result);

		return nextPage;

	}

	/*
	 * 로그인 실패
	 */
	@GetMapping("/franchiseeLoginFail")
	public String franchiseeLoginFail() {
		log.info("franchiseeLoginFail()");

		String nextPage = "franchisee/member/franchisee_login_ng";

		return nextPage;

	}
	
	/*
	 * 	로그인 실패(관리자 승인 안됨)
	 */
	@GetMapping("/franchiseeLoginApproval")
	public String franchiseeLoginApproval() {
		log.info("franchiseeLoginApproval()");

		String nextPage = "franchisee/member/franchisee_login_approval";

		return nextPage;

	}

//	/*
//	 * 	로그아웃 확인
//	 */
//	@GetMapping("/franchiseeLogoutConfirm")
//	public String franchiseeLogoutConfirm(HttpSession session) {
//		log.info("FranchiseeLogoutConfirm()");
//		
//		String nextPage = "/franchisee/franchisee_home";
//		
//		session.removeAttribute("loginedFranchiseeMemberDto");
//	
//		return nextPage;
//	}
//	

	/*
	 * 정보수정 폼
	 */
	@GetMapping("/franchiseeModifyForm")
	public String franchiseeModifyForm(HttpSession session) {
		log.info("FranchiseeModifyForm()");

		String nextPage = "franchisee/member/franchisee_modify_form";

		return nextPage;

	}

	/*
	 * 정보수정 확인
	 */
	@PostMapping("/franchiseeModifyConfirm")
	public String franchiseeModifyConfirm(FranchiseeMemberDto franchiseeMemberDto, HttpSession session) {
		log.info("franchiseeModifyConfirm()");

		String nextPage = "franchisee/member/franchisee_modify_ok";

		FranchiseeMemberDto modifieDto = franchiseeMemberService.franchiseeModifyConfirm(franchiseeMemberDto);

		if (modifieDto == null) {
			nextPage = "franchisee/member/franchisee_modify_ng";

		} else {
			session.setAttribute("loginedFranchiseeMemberDto", modifieDto);
			session.setMaxInactiveInterval(60 * 30);

		}

		return nextPage;
	}

	/* CustomerController로 이동함.
	 * customer 로그인 화면 가맹점리스트 불러오기
	 * 
	 * @PostMapping("/getStoreList")
	 * 
	 * @ResponseBody public Object getStoreList(@RequestParam("fcm_id") String
	 * fcm_id) { log.info("getStoreList()");
	 * 
	 * Map<String, Object> resultMap = franchiseeMemberService.getStoreList(fcm_id);
	 * 
	 * return resultMap; }
	 * 
	 * 
	 * customer 로그인 화면(가맹회원 로그인)
	 * 
	 * @PostMapping("/franchiseeStoreLogin") public Object
	 * franchiseeStoreLogin(FranchiseeStoreDto franchiseeStoreDto, HttpSession
	 * session) { log.info("franchiseeStoreLogin()");
	 * 
	 * String nextPage = "/customer/franchisee_store_login_result";
	 * 
	 * FranchiseeStoreDto loginedFranchiseeStoreDto =
	 * franchiseeMemberService.franchiseeLoginConfirmForCustomer(franchiseeStoreDto)
	 * ; loginedFranchiseeStoreDto.setFcs_no(franchiseeStoreDto.getFcs_no());
	 * 
	 * 
	 * session.setAttribute("loginedFranchiseeStoreDto", loginedFranchiseeStoreDto);
	 * session.setMaxInactiveInterval(60 * 720);
	 * 
	 * return nextPage;
	 * 
	 * }
	 * 
	 * 
	 * 매장 or 포장 유무 선택 후 오더 뷰 이동
	 * 
	 * @GetMapping("/customerOrderView") public String
	 * customerOrderView(@RequestParam("fco_packaging") int fco_packaging, Model
	 * model) { log.info("customerOrderView()");
	 * 
	 * String nextPage = "/customer/customer_order_view";
	 * 
	 * model.addAttribute("fco_packaging", fco_packaging); // 매장 or 포장 유무
	 * 
	 * return nextPage;
	 * 
	 * }
	 * 
	 * 
	 * customer login 후 포장유무 선택화면
	 * 
	 * @GetMapping("/sotreLoginResultView") public String
	 * sotreLoginResultView(HttpSession session) {
	 * log.info("sotreLoginResultView()");
	 * 
	 * String nextPage = "/customer/franchisee_store_login_result";
	 * 
	 * return nextPage;
	 * 
	 * }
	 * CustomerController로 이동함.
	 */

	/*
	 * 비밀번호 찾기 폼
	 */
	@GetMapping("/franchiseeFindPassword")
	public String franchiseeFindPassword() {
		log.info("franchiseeFindPassword()");

		String nextPage = "franchisee/member/franchisee_find_password_form";

		return nextPage;

	}

	/*
	 * 비밀번호 찾기 회원정보 확인
	 */
	@PostMapping("/franchiseeFindPasswordForm")
	public String franchiseeFindPasswordForm(FranchiseeMemberDto franchiseeMemberDto, Model model) {
		log.info("franchiseeFindPasswordForm()");

		String nextPage = "franchisee/member/franchisee_find_password_result";

		FranchiseeMemberDto resultDto = franchiseeMemberService.franchiseeFindPasswordForm(franchiseeMemberDto);

		model.addAttribute("resultDto", resultDto);

		return nextPage;

	}

	/*
	 * 비밀번호 찾기 확인
	 */
	@PostMapping("/franchiseeFindPasswordConfirm")
	public String franchiseeFindPasswordConfirm(FranchiseeMemberDto franchiseeMemberDto, Model model) {
		log.info("franchiseeFindPasswordConfirm()");

		String nextPage = "franchisee/member/franchisee_find_password_confirm_result";

		int findPasswordResult = franchiseeMemberService.franchiseeFindPasswordConfirm(franchiseeMemberDto);

		model.addAttribute("findPasswordResult", findPasswordResult);

		return nextPage;
	}

	/*
	 * 비밀번호 변경
	 */
	@GetMapping("/franchiseeModifyPassword")
	public String franchiseeModifyPassword() {
		log.info("franchiseeModifyPassword()");

		String nextPage = "franchisee/member/franchisee_modify_password_form";

		return nextPage;

	}

	/*
	 * 비밀번호 변경 확인
	 */
	@PostMapping("/franchiseeModifyPasswordConfirm")
	public String franchiseeModifyPasswordConfirm(FranchiseeMemberDto franchiseeMemberDto, HttpSession session,
			Model model) {
		log.info("franchiseeModifyPasswordConfirm()");

		String nextPage = "franchisee/member/franchisee_modify_password_result";

		FranchiseeMemberDto loginedFranchiseeMemberDto = (FranchiseeMemberDto) session
				.getAttribute("loginedFranchiseeMemberDto");

		franchiseeMemberDto.setFcm_no(loginedFranchiseeMemberDto.getFcm_no());

		FranchiseeMemberDto modifiedDto = franchiseeMemberService.franchiseeModifyPasswordConfirm(franchiseeMemberDto);
		if (modifiedDto != null) {
			session.setAttribute("loginedFranchiseeMemberDto", modifiedDto);
			session.setMaxInactiveInterval(60 * 30);
		}

		return nextPage;

	}

	/*
	 * 로그인 후 매장 선택
	 */
	@GetMapping("/sltStoreHome")
	public String sltStoreHome(@RequestParam("fcs_no") int fcs_no,
								@RequestParam("fcs_name") String fcs_name, HttpSession session) {
		log.info("sltStoreHome()");

		String nextPage = "franchisee/franchisee_home";

		FranchiseeMemberDto loginedFranchiseeMemberDto = (FranchiseeMemberDto) session
				.getAttribute("loginedFranchiseeMemberDto");

		loginedFranchiseeMemberDto.setFcs_no(fcs_no);
		loginedFranchiseeMemberDto.setFcs_name(fcs_name);

		session.setAttribute("loginedFranchiseeMemberDto", loginedFranchiseeMemberDto);
		session.setMaxInactiveInterval(60 * 30);

		return nextPage;

	}

}
