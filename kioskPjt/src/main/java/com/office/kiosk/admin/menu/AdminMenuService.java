package com.office.kiosk.admin.menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.office.kiosk.paging.kioskPageDto;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class AdminMenuService {

	// 메뉴 카테고리 추가 메서드용 상수
	final static public int ADMIN_MENU_CATEGORY_ALREADT_EXIST = -2;
	final static public int ADMIN_MENU_CATEGORY_DATABASE_TROUBLE = -1;
	final static public int ADMIN_MENU_CATEGORY_INSERT_FAIL = 0;
	final static public int ADMIN_MENU_CATEGORY_INSERT_SUCCESS = 1;

	// 메뉴 추가 메서드용 상수
	final static public int ADMIN_MENU_ALREADT_EXIST = -2;
	final static public int ADMIN_MENU_DATABASE_TROUBLE = -1;
	final static public int ADMIN_MENU_INSERT_FAIL = 0;
	final static public int ADMIN_MENU_INSERT_SUCCESS = 1;
	
	private int pageLimit = 12; // 한 페이지당 보여줄 menu정보 갯수
	private int blockLimit = 3; // 하단에 보여줄 페이지 번호 갯수
	

	@Autowired
	IAdminMenuDao iAdminMenuDao;

	@Autowired
	RestTemplate restTemplate;

	// 모든 카테고리 가져오기

	public Map<String, Object> getCategory() {
		log.info("getCategory()");

		Map<String, Object> cateDtos = new HashMap<>();

		List<AdminMenuCategoryDto> categoryDtos = (List<AdminMenuCategoryDto>) iAdminMenuDao.selectAllCategory();

		cateDtos.put("categoryDtos", categoryDtos);

		return cateDtos;
	}

	// 카테고리 생성

	public int createMenuCategoryAccountConfirm(AdminMenuCategoryDto adminMenuCategoryDto) {
		log.info("createMenuCategoryAccountConfirm()");

		boolean isMenuCategory = iAdminMenuDao.isMenuCategory(adminMenuCategoryDto.getFcmc_name());

		if (!isMenuCategory) {

			int result = iAdminMenuDao.insertMenuCategory(adminMenuCategoryDto);

			switch (result) {
			case ADMIN_MENU_CATEGORY_DATABASE_TROUBLE:
				log.info("DATABASE COMMUNICATION TROUBLE");

				break;

			case ADMIN_MENU_CATEGORY_INSERT_FAIL:
				log.info("INSERT MENU CATEGORY FAIL");

				break;

			case ADMIN_MENU_CATEGORY_INSERT_SUCCESS:
				log.info("INSERT MENU CATEGORY SUCCESS");

				break;

			}

			return result;

		} else {

			return ADMIN_MENU_CATEGORY_ALREADT_EXIST;
		}

	}

	// 모든 메뉴 가져오기 (페이지네이션때문에안씀)

	/*
	
	public Map<String, Object> getMenus() {
		log.info("getCategory()");

		log.info("getMenus()");

		Map<String, Object> adminMenuDtos = new HashMap<>();

		List<AdminMenuDto> adminMenusDtos = (List<AdminMenuDto>) iAdminMenuDao.selectAllMenus();

		adminMenuDtos.put("adminMenusDtos", adminMenusDtos);

		return adminMenuDtos;
	}
	
	*/

	// 메뉴 등록

	public int createMenuAccountConfirm(AdminMenuDto adminMenuDto) {
		log.info("createMenuAccountConfirm()");

		boolean isMenu = iAdminMenuDao.isMenu(adminMenuDto.getFc_menu_name());

		if (!isMenu) {

			int result = iAdminMenuDao.insertMenu(adminMenuDto);

			switch (result) {
			case ADMIN_MENU_DATABASE_TROUBLE:
				log.info("DATABASE COMMUNICATION TROUBLE");

				break;

			case ADMIN_MENU_INSERT_FAIL:
				log.info("INSERT MENU FAIL");

				break;

			case ADMIN_MENU_INSERT_SUCCESS:
				log.info("INSERT MENU SUCCESS");

				break;

			}

			return result;

		} else {
			return ADMIN_MENU_ALREADT_EXIST;
		}

	}

	// 파일(img)업로드

	public ResponseEntity<String> uploadFile(MultipartFile file) {

		log.info("uploadFile()");
		log.info("file: " + file);

		// RestTemplate 객체생성 //상단에 @Autowired 생성함
		// RestTemplate restTemplate = new RestTemplate();

		// Request Header 설정
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		// Request body 설정
		MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
		requestBody.add("file", file.getResource());

		// Request Entity
		HttpEntity<MultiValueMap<String, Object>> responseEntity = new HttpEntity<>(requestBody, headers);

		// API 호출
//		String severURL = "http://14.42.124.93:8091/upload_file";
		String severURL = "http://localhost:8091/upload_file"; //local
		ResponseEntity<String> response = restTemplate.postForEntity(severURL, responseEntity, String.class);

		return response;
	}

	// 카테고리에 따른 메뉴 불러오기

	public Map<String, Object> getMenusByCategory(String fcmc_no) {
		log.info("getMenusByCategory()");

		Map<String, Object> adminMenuDtos = new HashMap<>();

		List<AdminMenuDto> adminMenusDtos = (List<AdminMenuDto>) iAdminMenuDao.selectMenusByCategory(fcmc_no);

		adminMenuDtos.put("adminMenusDtos", adminMenusDtos);

		return adminMenuDtos;
	}

	// 모달창으로 선택한 메뉴의 정보 가져오기

	public AdminMenuDto getSelectMenuInfo(String fc_menu_no) {

		log.info("getSelectMenuInfo()");

		AdminMenuDto dto = iAdminMenuDao.selectMenuInfo(fc_menu_no);

		return dto;
	}

	// 메뉴 수정 컨펌

	public int modifyMenuAccountConfirm(AdminMenuDto adminMenuDto) {
		log.info("modifyMenuAccountConfirm()");
		log.info(adminMenuDto);

		return iAdminMenuDao.updateSelectMenu(adminMenuDto);

	}

	public int deleteMenuConfirm(String fc_menu_no) {
		log.info("deleteMenuConfirm");
		
		int result = -1;
		
		result = iAdminMenuDao.deleteSelectMenu(fc_menu_no);
		
		return result;
	}

	// 한페이지의 메뉴 리스트 불러오기
	
	
	
	public Map<String, Object> pagingAdminMenuList(int page) {
		log.info("pagingAdminMenuList");
		
		/*		 
		 1페이지에 보여지는 리스트 갯수 12개
		 1page => 0 (시작 인덱스)
		 2page => 12 (시작 인덱스)
		 3page => 24 (시작 인덱스)		 
		 */
		
		int pageingStart = (page - 1) * pageLimit;
		
		Map<String, Object> pagingList = new HashMap<>();
		
		Map<String, Integer> pagingParams = new HashMap<>();
		pagingParams.put("start", pageingStart);
		pagingParams.put("limit", pageLimit);
		
		List<AdminMenuDto> adminMenuDtos = iAdminMenuDao.selectAdminMenuPagingList(pagingParams);
		
		pagingList.put("adminMenuDtos", adminMenuDtos);
		
		return pagingList;
	}



	// 모든 페이지 number 불러오기
	
	public kioskPageDto getAllAdminMenuListPageNum(int page) {
		log.info("getAllAdminMenuListPageNum");
		
		// 전체 menu 갯수 조회
		int menuListcnt = iAdminMenuDao.selcetAllAdminMenuListCnt();
		
		// 전체 페이지 갯수 계산
		int maxPage = (int) (Math.ceil((double) menuListcnt / pageLimit));
		
		//시작 페이지 값 계산 (페이지 번호를 3개씩 보여줄 경우 = (1,4,7,10,~~~~))
		int startPage = (((int)(Math.ceil((double) page / blockLimit))) - 1 ) * blockLimit + 1;
		
		// 마지막 페이지 값 계산
		int endPage = startPage + blockLimit - 1;
		if (endPage > maxPage) {
			endPage = maxPage;
		}
		kioskPageDto adminMenuListPageDto = new kioskPageDto();
		adminMenuListPageDto.setPage(page);
		adminMenuListPageDto.setMaxPage(maxPage);
		adminMenuListPageDto.setStartPage(startPage);
		adminMenuListPageDto.setEndPage(endPage);
		
		log.info("page :" + page);
		log.info("maxPage :" + maxPage);
		log.info("startPage :" + startPage);
		log.info("endPage :" + endPage);
		
		return adminMenuListPageDto;
	}

	public Map<String, Object> pagingAdminMenuListByCate(int page, String fcmc_no) {
		log.info("pagingAdminMenuList");
		
		 /*
		 1페이지에 보여지는 리스트 갯수 12개
		 1page => 0 (시작 인덱스)
		 2page => 12 (시작 인덱스)
		 3page => 24 (시작 인덱스)		 
		 */
		
		int pageingStart = (page - 1) * pageLimit;
		
		Map<String, Object> pagingList = new HashMap<>();
		
		Map<String, Integer> pagingParams = new HashMap<>();
		pagingParams.put("start", pageingStart);
		pagingParams.put("limit", pageLimit);
		pagingParams.put("fcmc_no", Integer.parseInt(fcmc_no));
		
		List<AdminMenuDto> adminMenuDtos = iAdminMenuDao.selectAdminMenuPagingListByCate(pagingParams);
		
		pagingList.put("adminMenuDtos", adminMenuDtos);
		
		return pagingList;
	}

	public kioskPageDto getAllAdminMenuListPageNumByCate(int page, String fcmc_no) {
		log.info("getAllAdminMenuListPageNumByCate");
		
		// 전체 menu 갯수 조회
		int menuListcnt = iAdminMenuDao.selcetAllAdminMenuListCntByCate(fcmc_no);
		
		// 전체 페이지 갯수 계산
		int maxPage = (int) (Math.ceil((double) menuListcnt / pageLimit));
		
		//시작 페이지 값 계산 (페이지 번호를 3개씩 보여줄 경우 = (1,4,7,10,~~~~))
		int startPage = (((int)(Math.ceil((double) page / blockLimit))) - 1 ) * blockLimit + 1;
		
		// 마지막 페이지 값 계산
		int endPage = startPage + blockLimit - 1;
		if (endPage > maxPage) {
			endPage = maxPage;
		}
		kioskPageDto adminMenuListPageDto = new kioskPageDto();
		adminMenuListPageDto.setPage(page);
		adminMenuListPageDto.setMaxPage(maxPage);
		adminMenuListPageDto.setStartPage(startPage);
		adminMenuListPageDto.setEndPage(endPage);
		
		log.info("page :" + page);
		log.info("maxPage :" + maxPage);
		log.info("startPage :" + startPage);
		log.info("endPage :" + endPage);
		
		return adminMenuListPageDto;
	}
	}

	// 모달창에서 기존에 선택된 메뉴 카테고리 불러오기


