-- DB 생성
CREATE DATABASE DB_KIOSK;
USE DB_KIOSK;

-- ADMIN MEMBER 인가 테이블
CREATE TABLE TBL_ADMIN_AUTHORITY(
   AM_AUTHORITY_NO          INT NOT NULL AUTO_INCREMENT,
    AM_AUTHORITY_ROLE_NAME       VARCHAR(20) NOT NULL,
    PRIMARY KEY(AM_AUTHORITY_NO)
);

SELECT * FROM TBL_ADMIN_AUTHORITY;

INSERT INTO TBL_ADMIN_AUTHORITY(AM_AUTHORITY_ROLE_NAME) 
VALUES("SUPER_ADMIN");
INSERT INTO TBL_ADMIN_AUTHORITY(AM_AUTHORITY_ROLE_NAME) 
VALUES("SUB_ADMIN");
INSERT INTO TBL_ADMIN_AUTHORITY(AM_AUTHORITY_ROLE_NAME) 
VALUES("ADMIN");


CREATE TABLE TBL_ADMIN_MEMBER(
   AM_NO                  INT NOT NULL AUTO_INCREMENT, 
   AM_NAME                  VARCHAR(20) NOT NULL,
   AM_ID                  VARCHAR(20) NOT NULL UNIQUE,
   AM_PW                  VARCHAR(100) NOT NULL,
   AM_PHONE               VARCHAR(20) NOT NULL,
   AM_MAIL                  VARCHAR(20) NOT NULL,
   AM_AUTHORITY_NO          INT NOT NULL DEFAULT 3,         -- 기본값 "ADMIN"
    AM_ISACCOUNTNONEXPIRED      TINYINT NOT NULL DEFAULT 1,      -- 만료되지 않은 계정. 0: 만료 1: 만료X 
    AM_ISACCOUNTNONLOCKED      TINYINT NOT NULL DEFAULT 1,      -- 계정이 잠기지 않음. 0: 잠김 1: 안잠김
    AM_ISCREDENTIALSNONEXPIRED   TINYINT NOT NULL DEFAULT 1,    -- 만료되지 않은 자격 증명
    AM_ISENABLED            TINYINT NOT NULL DEFAULT 1,    -- 활성화. 휴면 OR 활성
   AM_REG_DATE               DATETIME DEFAULT NOW(),
   AM_MOD_DATE               DATETIME DEFAULT NOW(),
    PRIMARY KEY(AM_NO)
);
-- ADMIN MEMBER TABLE (관리자 기존버전)
CREATE TABLE TBL_ADMIN_MEMBER(
   AM_NO                  INT NOT NULL AUTO_INCREMENT, 
   AM_NAME                  VARCHAR(20) NOT NULL,
   AM_ID                  VARCHAR(20) NOT NULL UNIQUE,
   AM_PW                  VARCHAR(100) NOT NULL,
   AM_PHONE               VARCHAR(20) NOT NULL,
   AM_MAIL                  VARCHAR(20) NOT NULL,
   AM_APPROVAL         		TINYINT NOT NULL DEFAULT 0,         -- 기본값 "0"
   AM_REG_DATE               DATETIME DEFAULT NOW(),
   AM_MOD_DATE               DATETIME DEFAULT NOW(),
    PRIMARY KEY(AM_NO)
);

SELECT * FROM TBL_ADMIN_MEMBER;
DROP TABLE TBL_ADMIN_MEMBER;

-- FRANCHISEE 인가 테이블
CREATE TABLE TBL_FC_MEMBER_AUTHORITY(
   FCM_AUTHORITY_NO             INT NOT NULL AUTO_INCREMENT,
    FCM_AUTHORITY_ROLE_NAME       VARCHAR(20) NOT NULL,
    PRIMARY KEY(FCM_AUTHORITY_NO)
);

SELECT * FROM TBL_FC_MEMBER_AUTHORITY;
DROP TABLE TBL_FC_MEMBER_AUTHORITY;

INSERT INTO TBL_FC_MEMBER_AUTHORITY(FCM_AUTHORITY_ROLE_NAME) 
VALUES("FRANCHISEE");
INSERT INTO TBL_FC_MEMBER_AUTHORITY(FCM_AUTHORITY_ROLE_NAME) 
VALUES("USER");


-- FRANCHISEE TABLE (가맹회원) 
CREATE TABLE TBL_FC_MEMBER (
   FCM_NO                   INT NOT NULL AUTO_INCREMENT,
   FCM_ID                   VARCHAR(20) NOT NULL UNIQUE,
   FCM_PW                   VARCHAR(100) NOT NULL,
   FCM_NAME                VARCHAR(30) NOT NULL,
   FCM_PHONE                VARCHAR(30) NOT NULL,
   FCM_MAIL                VARCHAR(50) NOT NULL,
   FCM_AUTHORITY_NO          TINYINT NOT NULL DEFAULT 2,      -- 기본값 "USER"
   FCM_ISACCOUNTNONEXPIRED      TINYINT NOT NULL DEFAULT 1,      -- 만료되지 않은 계정. 0: 만료 1: 만료X 
   FCM_ISACCOUNTNONLOCKED      TINYINT NOT NULL DEFAULT 1,      -- 계정이 잠기지 않음. 0: 잠김 1: 안잠김
   FCM_ISCREDENTIALSNONEXPIRED   TINYINT NOT NULL DEFAULT 1,    -- 만료되지 않은 자격 증명
   FCM_ISENABLED            TINYINT NOT NULL DEFAULT 1,    -- 활성화. 휴면 OR 활성
   FCM_REG_DATE             DATETIME DEFAULT NOW(),
   FCM_MOD_DATE             DATETIME DEFAULT NOW(),
   PRIMARY KEY (FCM_NO)
);
-- FRANCHISEE TABLE (가맹회원 기존테이블) 
CREATE TABLE TBL_FC_MEMBER (
   FCM_NO                   INT NOT NULL AUTO_INCREMENT,
   FCM_ID                   VARCHAR(20) NOT NULL UNIQUE,
   FCM_PW                   VARCHAR(100) NOT NULL,
   FCM_NAME                VARCHAR(30) NOT NULL,
   FCM_PHONE                VARCHAR(30) NOT NULL,
   FCM_MAIL                VARCHAR(50) NOT NULL,
   FCM_APPROVAL				TINYINT NOT NULL DEFAULT 0,
   FCM_REG_DATE             DATETIME DEFAULT NOW(),
   FCM_MOD_DATE             DATETIME DEFAULT NOW(),
   PRIMARY KEY (FCM_NO)
);

SELECT * FROM TBL_FC_MEMBER;
DROP TABLE TBL_FC_MEMBER;

-- FRANCHISEE STORE TABLE (가맹점)
CREATE TABLE TBL_FC_STORE(
   FCS_NO             INT AUTO_INCREMENT,            -- 가맹점 NO
    FCS_NAME         VARCHAR(30) NOT NULL UNIQUE,   -- 가맹점명
    FCS_LOCATION       VARCHAR(100) NOT NULL,         -- 가맹점 위치
   FCS_PHONE         VARCHAR(30) NOT NULL,         -- 가맹점 연락처
   FCM_NO            INT NOT NULL,               -- 가맹회원 NO (TBL_FC_MEMBER) 
    FCS_DELETED         TINYINT DEFAULT 0,             -- 폐점 여부 (0: 기본값 1: 폐점)
    FCS_DELETED_DATE   DATETIME,                  -- 폐점 일자
   FCS_REG_DATE      DATETIME DEFAULT NOW(),         -- 가맹점 등록일
   FCS_MOD_DATE      DATETIME DEFAULT NOW(),         -- 가맹점 수정일
   PRIMARY KEY(FCS_NO)
);

SELECT * FROM TBL_FC_STORE;
DROP TABLE TBL_FC_STORE;

-- FRANCHISEE SALES TABLE (가맹점 매출)
CREATE TABLE TBL_FC_SALES(
   FCSA_NO         INT AUTO_INCREMENT,         -- 매출 NO
   FCO_ORI_NO      INT NOT NULL,            -- 오더 NO (TBL_FC_ORDER)
   FCSA_PRICE      INT NOT NULL,            -- 메뉴 NO (TBL_FC_MENU)
   PM_TYPE         VARCHAR(20) NOT NULL,      -- 현금 or 카드
   FCS_NO         INT NOT NULL,            -- 가맹점 NO (TBL_FC_STORE)
    FCSA_COMPLETE   TINYINT DEFAULT 0,         -- 주문 완료 여부. 출고 여부?
   FCSA_REG_DATE   DATETIME DEFAULT NOW(),      -- 등록일
   FCSA_MOD_DATE   DATETIME DEFAULT NOW(),    -- 수정일
   PRIMARY KEY(FCSA_NO)
);

SELECT * FROM TBL_FC_SALES;
DROP TABLE TBL_FC_SALES;

-- FRANCHISEE ORDER TABLE (오더 테이블)
CREATE TABLE TBL_FC_ORDER(
   FCO_NO               INT NOT NULL AUTO_INCREMENT,   -- 오더 NO
    FCO_ORI_NO            INT NOT NULL,               -- 오더 ORI NO. 여러가지 동시 주문 시 묶어주는 NO
    FCO_PACKAGING         TINYINT NOT NULL,             -- 포장 여부. 0: 매장 1: 포장   
    FCMC_NO               INT NOT NULL,                -- 메뉴 카테고리 NO (TBL_FC_MENU_CATEGORY)
    FC_MENU_NO            INT NOT NULL,               -- 메뉴 NO (TBL_FC_MENU)
    FCO_MENU_CNT         INT NOT NULL,                -- 메뉴 주문 수량
    FCO_MENU_OPTION         VARCHAR(30),               -- 추가 옵션. 샷 OR 펄 
    FCO_MENU_OPTION_PRICE      INT,                      -- 추가 옵션 가격
    FCO_TOTAL_PRICE         INT NOT NULL,               -- 총 가격
    FCS_NO               INT NOT NULL,               -- 가맹점 NO (TBL_FC_STORE)
    FCSA_COMPLETE			TINYINT DEFAULT 0,
    PM_TYPE         	VARCHAR(20) NOT NULL,      -- 현금 or 카드
    FCO_REG_DATE         DATETIME DEFAULT NOW(),         -- 등록일
    FCO_MOD_DATE         DATETIME DEFAULT NOW(),         -- 수정일
    PRIMARY KEY(FCO_NO)
);

SELECT * FROM TBL_FC_ORDER;
DROP TABLE TBL_FC_ORDER;

-- MENU CATEGORY TABLE 
CREATE TABLE TBL_FC_MENU_CATEGORY(
   FCMC_NO         INT AUTO_INCREMENT,         -- 메뉴 대분류
   FCMC_P_NO      INT DEFAULT 0,            -- 부모여부(0:대분류, ELSE 소분류)
   FCMC_NAME      VARCHAR(30) NOT NULL,      -- 메뉴분류 이름
   FCMC_REG_DATE   DATETIME DEFAULT NOW(),      -- 메뉴분류 등록일
   FCMC_MOD_DATE   DATETIME DEFAULT NOW(),      -- 메뉴분류 수정일
   PRIMARY KEY(FCMC_NO)
);

SELECT * FROM TBL_FC_MENU_CATEGORY;


-- MENU TABLE
CREATE TABLE TBL_FC_MENU(
   FC_MENU_NO         INT AUTO_INCREMENT,      -- 메뉴 NO
   FC_MENU_NAME      VARCHAR(30) NOT NULL,   -- 메뉴 이름
   FCMC_NO            INT NOT NULL,         -- 메뉴 대분류(TBL_FC_MENU_CATEGORY)
   FC_MENU_TEXT      VARCHAR(200) NOT NULL,   -- 메뉴 설명
   FC_MENU_PRICE      INT NOT NULL,         -- 메뉴 가격
    FC_MENU_IMG_NAME   VARCHAR(300),         -- 메뉴 이미지파일이름
    FC_MENU_QUANTITY   INT,               -- 메뉴 수량
   FC_MENU_REG_DATE   DATETIME DEFAULT NOW(),   -- 메뉴 등록일
   FC_MENU_MOD_DATE   DATETIME DEFAULT NOW(),   -- 메뉴 수정일
   PRIMARY KEY(FC_MENU_NO)
);

SELECT * FROM TBL_FC_MENU;




      INSERT INTO 
         TBL_FC_STORE(
            FCS_NAME,
              FCS_LOCATION,
              FCS_PHONE,
              FCM_NO) 
      VALUES ("지행점", 
            "경기도 동두천시 지행동",
            "031-865-4545",
            (
               SELECT 
                  FCM_NO 
               FROM 
                  TBL_FC_MEMBER 
               WHERE 
                  FCM_ID = "rkdtmdgh"
            ));