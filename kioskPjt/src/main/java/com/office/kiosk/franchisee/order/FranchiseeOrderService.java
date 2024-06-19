package com.office.kiosk.franchisee.order;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.office.kiosk.franchisee.sales.FranchiseeSalesDto;
import com.office.kiosk.paging.kioskPageDto;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class FranchiseeOrderService {
	
	private int pageLimit = 5;
	private int blockLimit = 3;
	
	@Autowired
	IFranchiseeOrderDao iFranchiseeOrderDao;
	


//	public Map<String, Object> getOrdersNo(int loginNo) {
//		log.info("getOrdersByFcsNo()");
//		
//		Map<String, Object> orderDtos = new HashMap<>();
//		log.info("loginNo=====> " + loginNo);
//		
//	    List<FranchiseeOrderDto> ordersDtos = iFranchiseeOrderDao.getOrdersByNo(loginNo);
//	    
//	    
//	    orderDtos.put("ordersDtos", ordersDtos);
//
//	    return orderDtos;
//	}


	public Map<String, Object> pagingOrderList(int page, int loginNo) {
		log.info("pagingOrderList()");
		
		int pageingStart = (page - 1) * pageLimit;
		
		Map<String,Object> pagingList = new HashMap<>();
		
		Map<String, Integer> pagingParams = new HashMap<>();
		pagingParams.put("start", pageingStart);
		pagingParams.put("limit", pageLimit);
		
		List<FranchiseeOrderDto> franchiseeOrderDtos = iFranchiseeOrderDao.selectOrderPagingList(pagingParams, loginNo);
		
		pagingList.put("franchiseeOrderDtos", franchiseeOrderDtos);
		
		return pagingList;
	}


	public kioskPageDto getAllOrderListPageNum(int page, int loginNo) {
		log.info("getAllOrderListPageNum()");
		
		//전체 admin 갯수 조회
		int orderListCnt = iFranchiseeOrderDao.selectAllOrderListCnt(loginNo);
		log.info("orderListCnt------->>"+orderListCnt);
		//전체 페이지 갯수 계산
		int maxPage = (int) (Math.ceil((double) orderListCnt / pageLimit));
		
		//시작 페이지 값 계산 (페이지 번호를 3개씩 보여줄 경우 = (1,4,7,10,~~~~))
		int startPage = (((int)(Math.ceil((double) page / blockLimit))) - 1 ) * blockLimit + 1;
		
		//마지막 페이지 값 계산 (페이지 번호를 3개씩 보여줄 경우 = (3,6,9,12,~~~~~))
		int endPage = startPage + blockLimit - 1;
		if(endPage > maxPage) {
			endPage = maxPage;
		}
		kioskPageDto orderListPageDto = new kioskPageDto();
		orderListPageDto.setPage(page);
		orderListPageDto.setStartPage(startPage);
		orderListPageDto.setMaxPage(maxPage);
		orderListPageDto.setEndPage(endPage);
				
		return orderListPageDto;
	}


	
	public Map<String, Object> getCategory() {
		
			log.info("getCategoryser()");
			
			Map<String, Object> cateDtos = new HashMap<>();

			List<FranchiseeOrderDto> categoryDtos = (List<FranchiseeOrderDto>) iFranchiseeOrderDao.selectAllCategory();

			cateDtos.put("categoryDtos", categoryDtos);

			return cateDtos;
		}


	public Map<String, Object> getMenus(int fcmc_no) {
		
			log.info("getMenus()");
	
			Map<String, Object> MenuDtos = new HashMap<>();
	
			List<FranchiseeOrderDto> franchiseeMenusDtos = (List<FranchiseeOrderDto>) iFranchiseeOrderDao.selectAllMenu(fcmc_no);
			
			log.info("franchiseeMenusDtos: "+franchiseeMenusDtos);
			
			MenuDtos.put("franchiseeMenusDtos", franchiseeMenusDtos);
	
			return MenuDtos;
	}


	public Map<String, Object> getPrice(int fc_menu_no) {
		
		log.info("getPrice()");
		
		Map<String, Object> PriceDtos = new HashMap<>();
		
		List<FranchiseeOrderDto> franchiseePriceDtos = iFranchiseeOrderDao.selectAllPrice(fc_menu_no);
		
		PriceDtos.put("franchiseePriceDtos", franchiseePriceDtos);
		
		return PriceDtos;
	}
	
	@Transactional
	public int getAllOrder(Map<String, Object> dataMsg, int fcs_no) {
		log.info("getAllOrder()");
		
		List<Map<String, Object>> dtos = (List<Map<String, Object>>) dataMsg.get("menuOrders");

		FranchiseeOrderDto oriNoDto = iFranchiseeOrderDao.getOriNo();
		int fco_ori_no;
		if(oriNoDto == null) {
			log.info("oriNoDto is null default value fco_ori_no=1 changed");
			fco_ori_no = 1;
		} else {
			log.info("oriNoDto is not null !!");
			fco_ori_no = oriNoDto.getFco_no() + 1;
		}
		
		for(int i = 0; i < dtos.size(); i++) {
			
			LinkedHashMap<String, Object> dataDto = (LinkedHashMap<String, Object>) dtos.get(i);
			
			FranchiseeOrderDto franchiseeOrderDto = new FranchiseeOrderDto();
			franchiseeOrderDto.setFco_packaging(Integer.parseInt((String)dataDto.get("fco_packaging")));
			franchiseeOrderDto.setPm_type((String)dataDto.get("pm_type"));
			franchiseeOrderDto.setFcmc_no(Integer.parseInt((String)dataDto.get("fcmc_no")));
			franchiseeOrderDto.setFc_menu_no(Integer.parseInt((String)dataDto.get("fc_menu_no")));
		    franchiseeOrderDto.setFco_menu_cnt(Integer.parseInt((String)dataDto.get("fco_menu_cnt")));
		    franchiseeOrderDto.setFco_menu_option((String)dataDto.get("fco_menu_option"));
		    franchiseeOrderDto.setFco_menu_option_price(Integer.parseInt((String)dataDto.get("fco_menu_option_price")));
		    franchiseeOrderDto.setFco_total_price(Integer.parseInt((String)dataDto.get("fco_total_price")));
		    franchiseeOrderDto.setFco_ori_no(fco_ori_no);
		    franchiseeOrderDto.setFcs_no(fcs_no);
		    
	        int result = iFranchiseeOrderDao.insertAllOrder(franchiseeOrderDto);
	        
	        if (result <= 0) {
	        	break;
	        }

		}
		
		FranchiseeSalesDto salesDto = iFranchiseeOrderDao.selectOrderTotalPriceByOriNo(fco_ori_no);
		salesDto.setFco_ori_no(fco_ori_no);
		LinkedHashMap<String, Object> dataDto = (LinkedHashMap<String, Object>)dtos.get(0);
		salesDto.setPm_type((String)dataDto.get("pm_type"));
		salesDto.setFcs_no(fcs_no);
		
		int result = iFranchiseeOrderDao.insertSalesByOrder(salesDto);
		
		if(result <= 0) {
			result = -1;
		}else {
			result = fco_ori_no;
		}
		
		return result;
	}

	@Transactional
	public int orderListCompleteConfirm(int fco_ori_no) {
		log.info("orderListCompleteConfirm()");
		
		int result = iFranchiseeOrderDao.updateOrderCompleteByOriNo(fco_ori_no);
		
		if(result > 0) {
			result = iFranchiseeOrderDao.updateSalesCompleteByOriNo(fco_ori_no);
		}
		
		return result;
	}

	

	/*
	 * FranchiseeOrderDto franchiseeOrderOriNoDto = new FranchiseeOrderDto();
	 * franchiseeOrderOriNoDto.setFco_ori_no(getOriNo + 1);
	 */
	
	 /* franchiseeOrderDto.setFco_packaging(Integer.parseInt((int)dataMsgObj.get(
	 * "fco_packaging")));
	 * franchiseeOrderDto.setPm_type((String)dataMsgObj.get("pm_type"));
	 * franchiseeOrderDto.setFcmc_no(Integer.parseInt((String)dataMsgObj.get(
	 * "fcmc_no")));
	 * franchiseeOrderDto.setFc_menu_no(Integer.parseInt((String)dataMsgObj.get(
	 * "fc_menu_no")));
	 * franchiseeOrderDto.setFco_menu_cnt(Integer.parseInt((String)dataMsgObj.get(
	 * "fco_menu_cnt")));
	 */

	


}
