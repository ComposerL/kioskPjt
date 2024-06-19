package com.office.kiosk.admin.sales;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.office.kiosk.franchisee.dto.SearchSalesDto;
import com.office.kiosk.franchisee.sales.FranchiseeSalesDto;
import com.office.kiosk.paging.kioskPageDto;

import lombok.extern.log4j.Log4j2;



@Log4j2
@Controller
@RequestMapping("/admin/sales")
public class AdminSalesController {

	@Autowired
	AdminSalesService adminSalesService;
	
	/*
	 * 	전체 매출 리스트
	 */
	@GetMapping("/salesList")
	public String salesList() {
		log.info("salesList()");
		
		String nextPage = "admin/sales/admin_sales_list";
		
		return nextPage;
		
	}
	
	
	/*
	 * 	get all sales info
	 */
	@PostMapping("/getAllSalesInfo")
	@ResponseBody
	public Object getAllSalesInfo(@RequestParam("page") int page) {
		log.info("getAllSalesInfo()");
		
		Map<String, Object> resultMap = adminSalesService.pagingAllSalesInfo(page);
		
		kioskPageDto allSalesListPageDto = adminSalesService.allSalesListPageNum(page);
		
		FranchiseeSalesDto allMemberTotalSales = adminSalesService.getAllMemberTotalSales();
		
		resultMap.put("allSalesListPageDto", allSalesListPageDto);
		resultMap.put("allMemberTotalSales", allMemberTotalSales);
		
		return resultMap;
	}
	
	/*
	 * 	get search sales list
	 */
	@PostMapping("/getSearchSales")
	@ResponseBody
	public Object getSearchSales(SearchSalesDto searchSalesDto) {
		log.info("getSearchSales()");
		
		Map<String, Object> resultMap = adminSalesService.pagingSearchSalesInfo(searchSalesDto);
		
		kioskPageDto searchSalesListPageDto = adminSalesService.SearchSalesListPageNum(searchSalesDto);
		
		FranchiseeSalesDto searchTotalSales = adminSalesService.getSearchTotalSales(searchSalesDto);
		
		resultMap.put("searchSalesListPageDto", searchSalesListPageDto);
		resultMap.put("searchTotalSales", searchTotalSales);
		
		return resultMap;
		
	}
	
	/*
	 * 	선택 fco_ori_no detail info modal
	 */
	@PostMapping("/getSalesDetailInfo")
	@ResponseBody
	public Object getSalesDetailInfo(@RequestParam("fco_ori_no") int fco_ori_no) {
		log.info("getSalesDetailInfo()");
		
		Map<String, Object> resultMap = 
				adminSalesService.getSalesDetailInfo(fco_ori_no);
		
		return resultMap;
	}
	
	
	
	
	/*
	 * 	가맹점별 매출리스트 뷰
	 */
	@GetMapping("/storeSalesList")
	public String storeSalesList() {
		log.info("storeSalesList()");
		
		String nextPage = "admin/sales/admin_store_sales_list";
		
		return nextPage;
		
	}
	
	
	/*
	 * 	전체 가맹점 매출 불러오기
	 */
	@PostMapping("/getStoreAllSalesInfo")
	@ResponseBody
	public Object getStoreAllSalesInfo(@RequestParam("page") int page) {
		log.info("getStoreAllSalesInfo()");
		
		Map<String, Object> resultMap = 
				adminSalesService.pagingStoreAllSalesInfo(page);
		
		kioskPageDto allStoreSalesListPageDto = adminSalesService.allStoreSalesListPageNum(page);
		
		resultMap.put("allStoreSalesListPageDto", allStoreSalesListPageDto);
		
		return resultMap;
	}
	
	
//	/*
//	 * 	선택 날짜 가맹점 매출 리스트 불러오기
//	 */
//	@PostMapping("/getSelectDateSalesInfo")
//	@ResponseBody
//	public Object getSelectDateSalesInfo(@RequestBody Map<String, String> currentDate) {
//		log.info("getSelectDateSalesInfo()");
//		
//		Map<String, Object> resultMap = 
//				adminSalesService.getSelectDateSalesInfo(currentDate);
//		
//		return resultMap;
//	}
	
	/*
	 * 	선택 날짜 가맹점 매출 리스트 불러오기
	 */
	@PostMapping("/getSelectDateSalesInfo")
	@ResponseBody
	public Object getSelectDateSalesInfo(@RequestBody Map<String, String> currentDate) {
		log.info("getSelectDateSalesInfo()");
		
		Map<String, Object> resultMap = 
				adminSalesService.pagingSelectDateSalesInfo(currentDate);
		
		kioskPageDto StoreSalesListPageDtoBySelectDate = 
				adminSalesService.StoreSalesListPageNumBySelectDate(currentDate);
		
		resultMap.put("StoreSalesListPageDtoBySelectDate", StoreSalesListPageDtoBySelectDate);
		
		return resultMap;
	}
	
	/*
	 * 	선택 기간 가맹점별 매출 리스트 
	 */
	@PostMapping("/getStoreTotalSalesByInputPeriod")
	@ResponseBody
	public Object getStoreTotalSalesByInputPeriod(@RequestBody Map<String, String> period) {
		log.info("getStoreTotalSalesByInputPeriod()");
		
		Map<String, Object> resultMap = 
				adminSalesService.pagingStoreSalesListByInputPeriod(period);
		
		log.info("resultMap-----------" + resultMap);
		
		kioskPageDto storeSalesListPageDtoByInputPeriod = 
				adminSalesService.storeSalesListPageNumByInputPeriod(period);
		
		resultMap.put("storeSalesListPageDtoByInputPeriod", storeSalesListPageDtoByInputPeriod);
		
		return resultMap;
	}
	
	
	/*
	 * 	가맹회원별 매출 리스트 뷰
	 */
	@GetMapping("/franchiseeSalesList")
	public String franchiseeSalesList() {
		log.info("franchiseeSalesList()");
		
		String nextPage = "admin/sales/admin_franchisee_sales_list";
		
		return nextPage;
		
	}
	
//	/*
//	 * 	가맹 회원별 매출 불러오기
//	 */
//	@PostMapping("/getFranchiseeAllSalesInfo")
//	@ResponseBody
//	public Object getFranchiseeAllSalesInfo() {
//		log.info("getFranchiseeAllSalesInfo()");
//		
//		Map<String, Object> resultMap = 
//				adminSalesService.getFranchiseeAllSalesInfo();
//		
//		return resultMap;
//	}
	
	/*
	 * 	가맹 회원별 매출 불러오기
	 */
	@PostMapping("/getFranchiseeAllSalesInfo")
	@ResponseBody
	public Object getFranchiseeAllSalesInfo(@RequestParam("page") int page) {
		log.info("getFranchiseeAllSalesInfo()");
		
		Map<String, Object> resultMap = 
				adminSalesService.pagingFranchiseeAllSalesInfo(page);
		
		kioskPageDto allFranchiseeSalesListPageDto = 
				adminSalesService.allFranchiseeSalesListPageNum(page);
		
		resultMap.put("allFranchiseeSalesListPageDto", allFranchiseeSalesListPageDto);
		
		return resultMap;
	}
	
	
//	/*
//	 * 	선택 날짜 회원별 매출 리스트 불러오기
//	 */
//	@PostMapping("/getSelectDateFranchiseeSalesInfo")
//	@ResponseBody
//	public Object getSelectDateFranchiseeSalesInfo(@RequestBody Map<String, String> currentDate) {
//		log.info("getSelectDateFranchiseeSalesInfo()");
//		
//		Map<String, Object> resultMap = 
//				adminSalesService.getSelectDateFranchiseeSalesInfo(currentDate);
//		
//		return resultMap;
//	}
	
	/*
	 * 	선택 날짜 회원별 매출 리스트 불러오기
	 */
	@PostMapping("/getSelectDateFranchiseeSalesInfo")
	@ResponseBody
	public Object getSelectDateFranchiseeSalesInfo(@RequestBody Map<String, String> currentDate) {
		log.info("getSelectDateFranchiseeSalesInfo()");
		
		Map<String, Object> resultMap = 
				adminSalesService.pagingFranchiseeSalesInfoBySelectDate(currentDate);
		
		kioskPageDto FranchiseeSalesListPageDtoBySelectDate = 
				adminSalesService.franchiseeSalesListPageNumBySelectDate(currentDate);
		
		resultMap.put("FranchiseeSalesListPageDtoBySelectDate", FranchiseeSalesListPageDtoBySelectDate);
		
		return resultMap;
	}
	
//	/*
//	 * 	선택 기간 회원별 매출 리스트 불러오기
//	 */
//	@PostMapping("/getFranchiseeTotalSalesByInputPeriod")
//	@ResponseBody
//	public Object getFranchiseeTotalSalesByInputPeriod(@RequestBody Map<String, String> period) {
//		log.info("getFranchiseeTotalSalesByInputPeriod()");
//		
//		Map<String, Object> resultMap = 
//				adminSalesService.getFranchiseeTotalSalesByInputPeriod(period);
//		
//		return resultMap;
//		
//	}
	
	/*
	 * 	선택 기간 회원별 매출 리스트 불러오기
	 */
	@PostMapping("/getFranchiseeTotalSalesByInputPeriod")
	@ResponseBody
	public Object getFranchiseeTotalSalesByInputPeriod(@RequestBody Map<String, String> period) {
		log.info("getFranchiseeTotalSalesByInputPeriod()");
		
		Map<String, Object> resultMap = 
				adminSalesService.pagingFranchiseeTotalSalesByInputPeriod(period);
		
		kioskPageDto franchiseeSalesListPageDtoByInputPeriod = 
				adminSalesService.franchiseeSalesListPageNumByInputPeriod(period);
		
		resultMap.put("franchiseeSalesListPageDtoByInputPeriod", franchiseeSalesListPageDtoByInputPeriod);
		
		return resultMap;
		
	}
	
	/*
	 * 	선택 가맹정 월별 매출 정보 가져오기 ajax
	 */
	@PostMapping("/getStoreMonthlySales")
	@ResponseBody
	public Object getStoreMonthlySales(@RequestParam("fcs_no") int fcs_no) {
		log.info("getStoreMonthlySales()");
		
		Map<String, Object> resultMap = adminSalesService.getStoreMonthlySales(fcs_no);
		
		return resultMap;
	}
	
	
	
	
}
