# 배달 서비스

## 개요
- `배달의 민족`, `쿠팡이츠`와 같은 음식 `배달` 서비스 입니다.
- 사용자는 앱을 통해 `음식점 검색`,`주문`,`배달 현황 확인`, `리뷰` 등의 기능을 이용할 수 있습니다.
- 점주는 `가게관리`, `메뉴관리`, `주문 관리`를 할 수 있습니다.

## 프로젝트 목표
1. 사용자 `회원가입`, `로그인`, `회원정보수정`, `회원탈퇴`, `비밀번호 변경` 기능 구현
2. `로그인` 시 `이메일`과 `비밀번호`를 이용하여 인증
3. 주문한 음식에 대한 `리뷰 작성 및 관리`
4. 점주 `가게 관리`, `메뉴 관리`, `주문 상태 변경` 기능 구현

## 주요 기능
### 사용자 관리
-  `닉네임`, `이메일`, `비밀번호`, `핸드폰번호`, `주소`를 입력하여 회원가입
- 회원가입시 `고객`과 `사장`을 구분하며 입력한 비밀번호를 암호화 하여 저장
- `로그인` 시 `이메일`과 `비밀번호`를 이용하여 인증

### 가게 관리
- `사장` 권한을 가진 유저만 가게 생성 가능
- `사장` 계정 당 최대 `3개`의 가게만 생성 가능
- 고객은 `가게명`을 기준으로 검색 가능
- 가게를 `폐업` 상태로 변경된 경우 사장은 가게를 추가 생성 가능

### 메뉴 관리
- 해당 가게 사장 유저만 `메뉴 생성`, `메뉴 수정`, `메뉴 삭제` 가능
- 메뉴는 단독 조회 불가능
- `삭제된 메뉴`의 경우 조회가 되지 않지만 기존 주문 정보조회 가능

### 주문 기능
- 고객 유저만 주문 가능하며 주문 이후 취소 불가
- 사장 유저는 `주문 수락` 또는 `주문 취소` 가능
- 주문의 상태 변화
  - `대기 중` -> `조리 중` -> `배달 중` -> `배달 완료`
  - `주문 취소`는 사장 유저만 예외적으로 가능 
- 가게의 `최소 주문 금액`이상인 경우 주문 가능
- 가게의 `운영 시간` 외에는 주문 불가능

### 리뷰 기능
- `배달 완료`상태인 주문만 리뷰 작성 가능
- 리뷰에는 별점을 부여(1~5점)
- 리뷰는 단건 조회 불가능
- 가게를 기준으로 다건 조회 가능하며, 최신순으로 정렬
- 별점 범위(예: 3~5점)에 따라 조회 가능

## 프로젝트 요구사항
### **사용자 관리**
- [x] **회원가입**
  - 사용자 아이디는 `이메일` 형식
  - 비밀번호는 `Bcrypt`로 인코딩하며 
  - 비밀번호는 8글자 이상, 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1개 포함
  - `고객`, `사장`으로 구분하여 회원 관리

- [x] **회원탈퇴**
  - 탈퇴 처리 시 `비밀번호`확인
  - 사용자의 아이디는 `재사용 및 복구 불가`

- [x] **로그인**
  - 가입한 `이메일`, `비밀번호`가 일치할 경우 로그인 허용

### **가게 관리**
- [x] **가게 생성/수정**
  - `사장` 유저만 가게 `생성`,`수정`,`삭제` 가능
  - `사장` 유저는 최대 `3개`의 가게 생성 가능
  - 가게에는 `운영 시간(오픈/마감)` 시간 및 `최소 주문 금액` 존재

- [x] **가게 조회**
  - `가게명`을 기준으로 조회 가능
  - 다건 조회 시 `메뉴` 확인 불가능

- [x] **가게 폐업**
  - 가게 상태만 `폐업`으로 변경
  - 폐업 상태의 가게는 `조회 불가능`
  - 사장은 새로운 가게를 `추가 등록` 가능

### **메뉴 관리**
- [x] **메뉴 생성/수정**
    - `사장` 유저만 본인 가게에 메뉴 `생성`, `수정` 가능
    - 같은 가게에서 동일한 메뉴명 중복 불가

- [x] **메뉴 삭제**
    - `사장` 유저만 본인 가게의 메뉴 삭제 가능
    - 삭제된 메뉴는 가게 메뉴 목록에서 조회되지 않음
    - 기존 주문 내역에서는 삭제된 메뉴 정보 확인 가능 

- [x] **메뉴 조회**
    - `단독 조회 불가능` (가게 조회 시 함께 조회됨)
    - 고객이 `가게 조회` 시 등록된 메뉴 리스트 함께 표시

### **주문 관리**
- [x] **주문 기능**
    - 고객만 주문 가능하며, 주문 후 취소 불가
    - 사장은 `주문 수락` 또는 `주문 취소` 가능
    - 주문 상태는 `대기 중 → 조리 중 → 배달 중 → 배달 완료` 순서로 진행
    - `최소 주문 금액` 이상 주문 가능
    - `운영 시간` 외 주문 불가능

### **리뷰 관리**
- [x] **리뷰 기능**
    - `배달 완료` 상태인 주문만 리뷰 작성 가능
    - 리뷰 작성 시 별점(1~5점) 부여 가능
    - 리뷰 단건 조회 불가, 가게 기준으로 최신순 정렬
    - 별점 범위(예: 3~5점)로 필터링 가능

### API 명세서
- [API명세서](API.md)

### SQL
- [SQL](devout.sql)

### ERD

```mermaid
erDiagram
  MEMBER ||--o{ STORE : "owns"
  STORE ||--o{ MENU : "offers"
  MEMBER ||--o{ ORDERS : "places"
  STORE ||--o{ ORDERS : "receives"
  ORDERS ||--o{ ORDER_ITEM : "contains"
  ORDERS ||--o{ REVIEW : "has"
  
  MEMBER {
      BIGINT id PK "auto_increment"
      VARCHAR(20) name "not null"
      VARCHAR(50) email "not null"
      VARCHAR(100) password "not null"
      VARCHAR(13) phone_number "not null"
      VARCHAR(100) address
      VARCHAR(20) member_role
      DATETIME deleted_at
      DATETIME created_at
      DATETIME modified_at
    }
    
  STORE {
      BIGINT id PK "auto_increment"
      BIGINT member_id
      VARCHAR(30) name
      VARCHAR(100) address
      VARCHAR(100) contents
      VARCHAR(10) telephone_number
      TIME close_time
      TIME open_time
      INTEGER minimum_price "not null"
      VARCHAR(20) category
      VARCHAR(20) status
      DATETIME created_at
      DATETIME modified_at
      }
      
  MENU {
      BIGINT id PK "auto_increment"
      INTEGER price "not null"
      BIGINT store_id "not null"
      VARCHAR(100) contents
      VARCHAR(20) name
      VARCHAR(20) category
      VARCHAR(20) status
      DATETIME created_at
      DATETIME modified_at
  }

  ORDERS {
      BIGINT id PK "auto_increment"
      BIGINT buy_member_id
      BIGINT store_id
      VARCHAR(20) status
      DATETIME created_at
      DATETIME modified_at
  }

  ORDER_ITEM {
      BIGINT id PK "auto_increment"
      VARCHAR(255) menu_name "not null"
      INTEGER count "not null"
      INTEGER price "not null"
      BIGINT order_id "not null"
      DATETIME created_at
      DATETIME modified_at
  }

  REFRESH_TOKEN {
      BIGINT id PK "auto_increment"
      VARCHAR(255) email "not null"
      VARCHAR(255) refresh_token "not null"
  }

  REVIEW {
      BIGINT id PK "auto_increment"
      BIGINT order_id
      VARCHAR(255) contents
      INTEGER rating "not null"
      DATETIME created_at
      DATETIME modified_at
  }

```

