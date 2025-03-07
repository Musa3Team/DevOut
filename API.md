# API 명세서

## 목차
- [회원 API](#회원-api)
- [가게 API](#가게-api)
- [메뉴 API](#메뉴-api)
- [주문 API](#주문-api)
- [리뷰 API](#리뷰-api)

---

## 회원 API

### Auth 관련 API

| API 이름   | HTTP Method | URL     | 요청 예시 (요약)                                                                                   | 응답 예시 (요약)                                                    | 상태 코드         |
|------------|-------------|---------|-----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------|-------------------|
| 회원 가입  | POST        | /auth/signup | `{ "name", "email", "password", "address", "phone_number", "role" }`                                  | `{ "id", "name", "email", "address", "phone_number", "role" }`         | 201 (정상 등록), 400 |
| 로그인     | POST        | /auth/login  | `{ "email", "password" }`                                                                           | `{ "id", "name", "email", "address", "phone_number", "role", "token" }`  | 200 (OK), 401     |

---

### Member 관련 API

| API 이름           | HTTP Method | URL                     | 요청 예시 (요약)                                            | 응답 예시 (요약)                                                  | 상태 코드           |
|--------------------|-------------|-------------------------|------------------------------------------------------------|--------------------------------------------------------------------|---------------------|
| 회원 정보 수정     | PATCH       | /member/{id}            | `{ "name", "address", "phone_number" }`                     | `{ "id", "name", "email", "address", "phone_number", "role" }`       | 200 (OK), 401, 404  |
| 비밀번호 변경      | PATCH       | /member/{id}/password   | `{ "old_password", "new_password" }`                        | *Empty body*                                                       | 200 (OK)            |
| 회원 탈퇴          | DELETE      | /member/{id}            | `{ "password" }` *(DeleteMemberRequest)*                    | *Empty body*                                                       | 200 (OK)            |


---

## 가게 API

| API 이름       | HTTP Method | URL                              | 요청 예시 (요약)                                           | 응답 예시 (요약)                                      | 상태 코드             |
|----------------|-------------|----------------------------------|------------------------------------------------------|------------------------------------------------------|-----------------------|
| 가게 생성      | POST        | /stores                          | `{ "telephone_number", "address", "contents", ... }` | `{ "id", "telephone_number", "address", "contents", ... }` | 201, 400              |
| 가게 수정      | PATCH       | /stores/{id}                     | `{ "telephone_number", "address", "contents", ... }` | `{ "id", "telephone_number", "address", "contents", ... }` | 200, 401, 404         |
| 가게 다건 조회 | GET         | /stores?page=1&pagesize=20&...    | Query: page, pagesize, name, category                | `{ "result": [ { "id", "name", "contents" }, ... ], "page", "size", "totalElement" }` | 200, 400              |
| 가게 단건 조회 | GET         | /stores/{id}                     | -                                                    | `{ "id", "name", "contents", "menu": [ ... ] }`        | 200, 404              |
| 가게 폐업      | DELETE      | /stores/{id}                     | `{ "password" }`                                     | -                                                    | 200, 401, 404         |
|가게 상태 수정 | PATCH   | /status/{id}         | `{ "id": 1, "name": "가게 이름", "status": "OPEN" }`     | 200 (OK), 401       |
---

## 메뉴 API

| API 이름       | HTTP Method | URL          | 요청 예시 (요약)                             | 응답 예시 (요약)                              | 상태 코드             |
|----------------|-------------|--------------|---------------------------------------------|----------------------------------------------|-----------------------|
| 메뉴 생성      | POST        | /menus       | `{ "name", "price", "contents", "category" }`| `{ "id", "name", "price", "contents", ... }`   | 201, 400, 401         |
| 메뉴 수정      | PATCH       | /menus/{id}  | `{ "name", "price", "contents", "category" }`| `{ "id", "name", "price", "contents", ... }`   | 200, 401, 404         |
| 메뉴 삭제      | DELETE      | /menus/{id}  | -                                           | -                                            | 200, 401, 404         |

---

## 주문 API

| API 이름         | HTTP Method | URL                                | 요청 예시 (요약)                                                                                             | 응답 예시 (요약)                                                                                                             | 상태 코드   |
|------------------|-------------|------------------------------------|-------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------|-------------|
| 주문 생성        | POST        | /orders                            | `{ "store_id": 1, "orderItem": [ { "menu_id": 1, "count": 2, "price": 20000 }, ... ] }`                     | `{ "id": 1, "store_id": 1, "total_count": 3, "total_price": 24000, "status": "PENDING", "orderItem": [ ... ] }`             | 200 (OK)    |
| 주문 상태 변경   | PATCH       | /orders/{id}                       | `{ "orderStatus": "ACCEPT" }`                                                                                | *Empty body*                                                                                                                | 200 (OK)    |
| 고객 - 단건조회  | GET         | /orders/{id}                       | *No body*                                                                                                   | `{ "id": 1, "store_id": 1, "total_count": 3, "total_price": 24000, "status": "DELIVERED", ... }`                             | 200 (OK)    |
| 고객 - 다건조회  | GET         | /orders?page=1&size=10               | Query parameter: `page`, `size`                                                                             | `Page<OrderResponse>` (예: `[ { 주문 데이터1 }, { 주문 데이터2 }, ... ]`)                                                   | 200 (OK)    |
| 사장 - 단건조회  | GET         | /{storeId}/orders/{id}               | *No body*                                                                                                   | `{ "id": 1, "store_id": 1, "total_count": 3, "total_price": 24000, "status": "DELIVERED", ... }`                             | 200 (OK)    |
| 사장 - 다건조회  | GET         | /{storeId}/orders?page=1&size=10       | Query parameter: `page`, `size`                                                                             | `Page<OrderResponse>` (예: `[ { 주문 데이터1 }, { 주문 데이터2 }, ... ]`)                                                   | 200 (OK)    |
---

## 리뷰 API

| API 이름        | HTTP Method | URL         | 요청 예시 (요약)                          | 응답 예시 (요약)                                                  | 상태 코드           |
|-----------------|-------------|-------------|------------------------------------------|--------------------------------------------------------------------|---------------------|
| 리뷰 생성       | POST        | /reviews    | `{ "order_id", "contents", "rating" }`     | -                                                                  | 201, 400            |
| 리뷰 다건 조회  | GET         | /reviews    | Query: storeId, memberId                   | `{ "store_id", "review": [ { "order_id", "buy_member_id", ... } ] }`  | 200, 404            |
