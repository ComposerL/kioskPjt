package com.office.kiosk.customer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.office.kiosk.franchisee.order.FranchiseeOrderDto;
import com.office.kiosk.franchisee.order.IFranchiseeOrderDao;
import com.office.kiosk.franchisee.sales.FranchiseeSalesDto;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class CustomerService {
	
	final static int INSERT_ORDER_FAIL = -1;
	
	@Autowired
	IFranchiseeOrderDao iFranchiseeOrderDao;
	
	@Transactional
	public int insertCustomerOrder(Map<String, Object> dataMsg, int fcs_no) {
		log.info("insertCustomerOrder()");
		
		List<Map<String, Object>> dtos = (List<Map<String, Object>>) dataMsg.get("menuOrders");
		
		FranchiseeOrderDto oriNoDto = iFranchiseeOrderDao.getOriNo();
		int fco_ori_no;
		if(oriNoDto == null) {
			log.info("oriNoDto is null default value fco_ori_no=1 changed");
			fco_ori_no = 1;
		}else {
			log.info("oriNoDto is not null !!");
			fco_ori_no = oriNoDto.getFco_no() + 1;
		}
		
		for(int i = 0; i < dtos.size(); i++) {
			
			LinkedHashMap<String, Object> dataDto = (LinkedHashMap<String, Object>) dtos.get(i);
			
			FranchiseeOrderDto franchiseeOrderDto = new FranchiseeOrderDto();
			
			franchiseeOrderDto.setFco_packaging((int)dataDto.get("fco_packaging"));
			franchiseeOrderDto.setPm_type((String)dataDto.get("pm_type"));
			franchiseeOrderDto.setFcmc_no((int)dataDto.get("fcmc_no"));
			franchiseeOrderDto.setFc_menu_no((int)dataDto.get("fc_menu_no"));
		    franchiseeOrderDto.setFco_menu_cnt((int)dataDto.get("fco_menu_cnt"));
		    franchiseeOrderDto.setFco_menu_option((String)dataDto.get("fco_menu_option"));
		    franchiseeOrderDto.setFco_menu_option_price((int)dataDto.get("fco_menu_option_price"));
		    franchiseeOrderDto.setFco_total_price((int)dataDto.get("fco_total_price"));
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
			result = INSERT_ORDER_FAIL;
		}else {
			result = fco_ori_no;
		}
		
		return result;
	}

}
