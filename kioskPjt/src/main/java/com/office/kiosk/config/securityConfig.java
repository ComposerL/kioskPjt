package com.office.kiosk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.office.kiosk.franchisee.member.FranchiseeMemberDto;
import com.office.kiosk.franchisee.member.IFranchiseeMemberDao;

import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
@EnableWebSecurity
public class securityConfig {
	
	@Autowired
	IFranchiseeMemberDao iFranchiseeMemberDao;

	@Bean
	public PasswordEncoder passwordEncoder() {
		log.info("passwordEncoder()");

		return new BCryptPasswordEncoder();

	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		log.info("filterChain()");

		http
			.cors((cors) -> cors.disable())
			.csrf((csrf) -> csrf.disable());
		
		http
			.authorizeHttpRequests((request) -> request
					.requestMatchers("/franchisee/order/**",
									"/franchisee/menu/**",
									"/franchisee/sales/**",
									"/franchisee/member/franchiseeModifyForm",
									"/franchisee/member/franchiseeModifyConfirm",
									"/franchisee/member/franchiseeModifyPassword",
									"/franchisee/member/franchiseeModifyPasswordConfirm",
									"/franchisee/member/sltStoreHome",
									"/franchisee/member/franchiseeLoginSuccess"
									).authenticated()
					.requestMatchers("/",
									"/favicon.ico",
									"/home2",
									"/admin/**",
									"/css/**",
									"/js/**",
									"/error/**",
									"/img/**",
									"/include/**",
									"/franchisee/home",
									"/franchisee/",
									"/franchisee",
									"/franchisee/member/franchiseeLoginForm",
									"/franchisee/member/createFranchiseeAccountForm",
									"/franchisee/member/createFranchiseeAccountConfirm",
									"/franchisee/member/franchiseeFindPassword",
									"/franchisee/member/franchiseeFindPasswordForm",
									"/franchisee/member/franchiseeFindPasswordConfirm",
									"/franchisee/member/franchiseeLoginApproval",
									"/franchisee/member/franchiseeLoginFail",
									"/customer/order/customerOrderView",
									"/customer/order/customerOrderAccountConfirm",
									"/customer/order/getStoreList",
									"/customer/order/franchiseeStoreLogin",
									"/customer/order/sotreLoginResultView"
									).permitAll());

		http.formLogin(login -> login
				.loginPage("/franchisee/member/franchiseeLoginForm")
				.loginProcessingUrl("/franchisee/member/franchiseeLoginConfirm")
				.usernameParameter("fcm_id")
				.passwordParameter("fcm_pw")
				.successHandler((request, response, authentication) -> {
					log.info("success handler");

					FranchiseeMemberDto franchiseeMemberDto = new FranchiseeMemberDto();
					franchiseeMemberDto.setFcm_id(authentication.getName());

					FranchiseeMemberDto loginedFranchiseeMemberDto = iFranchiseeMemberDao
							.selectFranchiseeForLogin(franchiseeMemberDto);
					
					HttpSession session = request.getSession();
					
					int fcmApproval = loginedFranchiseeMemberDto.getFcm_approval(); 
					
					if(fcmApproval == 1) {
						session.setAttribute("loginedFranchiseeMemberDto", loginedFranchiseeMemberDto);
						session.setMaxInactiveInterval(60 * 480);
						
						response.sendRedirect("/franchisee/member/franchiseeLoginSuccess");

					} else {
						response.sendRedirect("/franchisee/member/franchiseeLoginApproval");
						
					}


				}).failureHandler((request, response, exception) -> {
					log.info("fail handler");
					
					response.sendRedirect("/franchisee/member/franchiseeLoginFail");

				}).permitAll());

		http.logout(logout -> logout
				.logoutUrl("/franchisee/member/franchiseeLogoutConfirm")
				.logoutSuccessHandler((request, response, authentication) -> {
					log.info("logout handler");

					HttpSession session = request.getSession();
					session.removeAttribute("loginedFranchiseeMemberDto");

					response.sendRedirect("/franchisee");

				}));

		return http.build();

	}
	
	
}
