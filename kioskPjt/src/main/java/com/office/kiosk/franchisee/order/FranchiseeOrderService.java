package com.office.kiosk.franchisee.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.DocFlavor.BYTE_ARRAY;

import org.apache.ibatis.ognl.ASTBitNegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.office.kiosk.admin.menu.AdminMenuCategoryDto;
import com.office.kiosk.admin.menu.AdminMenuDto;
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
		
		log.info("page: "+page);
		log.info("maxPage: "+maxPage);
		log.info("startPage: "+startPage);
		log.info("endPage: "+endPage);
				
		return orderListPageDto;
	}


	public int deleteOrderListConfirm(int fco_no) {
		log.info("deleteOrderListConfirm()");
		
		int result = iFranchiseeOrderDao.deleteSelectOrder(fco_no);
		
		return result;
	}

	
	public Map<String, Object> getCategory() {
		
			log.info("getCategoryser()");
			

			Map<String, Object> cateDtos = new HashMap<>();

			List<FranchiseeOrderDto> categoryDtos = (List<FranchiseeOrderDto>) iFranchiseeOrderDao.selectAllCategory();

			cateDtos.put("categoryDtos", categoryDtos);

			return cateDtos;
		}


	public Map<String, Object> getMenus() {
		
		log.info("getMenus()");

		Map<String, Object> franchiseeMenuDtos = new HashMap<>();

		List<FranchiseeOrderDto> franchiseeMenusDtos = (List<FranchiseeOrderDto>) iFranchiseeOrderDao.selectAllMenu();

		franchiseeMenuDtos.put("franchiseeMenusDtos", franchiseeMenusDtos);

		return franchiseeMenuDtos;
	}
	




}
