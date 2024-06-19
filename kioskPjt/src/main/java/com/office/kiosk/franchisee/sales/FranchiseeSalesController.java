package com.office.kiosk.franchisee.sales;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.office.kiosk.paging.kioskPageDto;

import lombok.extern.log4j.Log4j2;



@Log4j2
@Controller
@RequestMapping("/franchisee/sales")
public class FranchiseeSalesController {

	@Autowired
	FranchiseeSalesService franchiseeSalesService;
	
	// 뷰변경
	
	@GetMapping("/getSalesInfo")
	public String getSalesInfo() {
		log.info("getSalesInfo()");
		
		String nextPage = "franchisee/sales/franchisee_sales_list";
		
		return nextPage;
	}
	
	
	// 우리가게의 모든 매출 정보 가져오기
	
	@GetMapping("/getMyStoreAllSalesInfo")
	@ResponseBody
	public Object getMyStoreAllSalesInfo(@RequestParam("fcs_no") String fcs_no,@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("getMyStoreAllSalesInfo()");
		
		Map<String, Object> pagingMyStoreAllSalesDtos = 
				franchiseeSalesService.pagingMyStoreAllSalesInfo(page, fcs_no);
		
		kioskPageDto myStoreAllSalesPageDtos = franchiseeSalesService.getMyStoreSalesInfoPageNum(page, fcs_no);
		
		FranchiseeSalesDto totalsalesDto = franchiseeSalesService.getMyStoreTotalSales(fcs_no);
		
		pagingMyStoreAllSalesDtos.put("myStoreAllSalesPageDtos", myStoreAllSalesPageDtos);
		pagingMyStoreAllSalesDtos.put("totalsalesDto", totalsalesDto);
		
		return pagingMyStoreAllSalesDtos;
		
	}
	
	// 우리가게의 하루 매출 가져오기(년도, 월, 일 셀렉트 체인지 시)
	
	@PostMapping("/getCurrentDateMyStoreSalesInfo")
	@ResponseBody
	public Object getCurrentDateMyStoreSalesInfo(@RequestBody Map<String, String> currentDate) {
		log.info("getCurrentDateMyStoreSalesInfo()");
		
		
		 Map<String, Object> pagingMyStoreSalesInfoBySelectDate =
		 franchiseeSalesService.pagingMyStoreSalesInfoBySelectDate(currentDate);
		 
		 FranchiseeSalesDto currentDateTotalsalesDto = franchiseeSalesService.getCurrentDateMyStoreTotalSales(currentDate);
		 
		 kioskPageDto myStoreSalesInfoBySelectDatePageNum = franchiseeSalesService.getMyStoreSalesInfoBySelectDatePageNum(currentDate);
		
		 pagingMyStoreSalesInfoBySelectDate.put("myStoreSalesInfoBySelectDatePageNum", myStoreSalesInfoBySelectDatePageNum);
		 pagingMyStoreSalesInfoBySelectDate.put("currentDateTotalsalesDto", currentDateTotalsalesDto);
		
		 return pagingMyStoreSalesInfoBySelectDate;
		
	}
	
	// 우리가게의 기간별 매출 가져오기(달력선택으로 기간 선택 시)
	
	@PostMapping("/getMyStoreSalesInfoByInputPeriod")
	@ResponseBody
	public Object getMyStoreSalesInfoByInputPeriod(@RequestBody Map<String, String> period) {
		log.info("getCurrentDateMyStoreSalesInfo()");
		
		 Map<String, Object> pagingMyStoreSalesInfoByInputPeriod =
		 franchiseeSalesService.pagingMyStoreSalesInfoByInputPeriod(period);
		 
		 kioskPageDto myStoreSalesInfoByInputPeriodPageNum = franchiseeSalesService.getMyStoreSalesInfoByInputPeriodPageNum(period);
		 
		 FranchiseeSalesDto periodDateTotalSalesDto = franchiseeSalesService.getMyStoreTotalSalesByInputPeriod(period);
		
		 pagingMyStoreSalesInfoByInputPeriod.put("myStoreSalesInfoByInputPeriodPageNum", myStoreSalesInfoByInputPeriodPageNum);
		 pagingMyStoreSalesInfoByInputPeriod.put("periodDateTotalSalesDto", periodDateTotalSalesDto);
		 
		 return pagingMyStoreSalesInfoByInputPeriod;
		
	}
	
	
	
}
