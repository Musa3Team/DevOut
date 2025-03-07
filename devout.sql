
-- 회원 테이블 생성
CREATE TABLE member (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        created_at DATETIME(6),
                        modified_at DATETIME(6),
                        deleted_at DATETIME(6),
                        address VARCHAR(100),
                        email VARCHAR(50) NOT NULL,
                        name VARCHAR(20) NOT NULL,
                        password VARCHAR(100) NOT NULL,
                        phone_number VARCHAR(13) NOT NULL,
                        member_role ENUM('CUSTOMER', 'OWNER'),
                        PRIMARY KEY (id),
                        UNIQUE KEY UK_email_role (email, member_role)
) ENGINE=InnoDB;

-- 매장 테이블 생성
CREATE TABLE store (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       created_at DATETIME(6),
                       modified_at DATETIME(6),
                       close_time TIME(6),
                       open_time TIME(6),
                       minimum_price INTEGER NOT NULL,
                       member_id BIGINT,
                       address VARCHAR(100),
                       contents VARCHAR(100),
                       name VARCHAR(30),
                       telephone_number VARCHAR(10),
                       category ENUM('CHICKEN'),
                       status ENUM('CLOSE','OPEN','SHUTDOWN','UNPREPARED'),
                       PRIMARY KEY (id),
                       CONSTRAINT FK_store_member FOREIGN KEY (member_id) REFERENCES member(id)
) ENGINE=InnoDB;

-- 메뉴 테이블 생성
CREATE TABLE menu (
                      id BIGINT NOT NULL AUTO_INCREMENT,
                      created_at DATETIME(6),
                      modified_at DATETIME(6),
                      price INTEGER NOT NULL,
                      store_id BIGINT NOT NULL,
                      contents VARCHAR(100),
                      name VARCHAR(20),
                      category ENUM('APPETIZERS','DESSERTS','DRINKS','MAIN_DISHES'),
                      status ENUM('ACTIVE','DISABLED'),
                      PRIMARY KEY (id),
                      CONSTRAINT FK_menu_store FOREIGN KEY (store_id) REFERENCES store(id)
) ENGINE=InnoDB;

-- 주문 테이블 생성
CREATE TABLE orders (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        created_at DATETIME(6),
                        modified_at DATETIME(6),
                        buy_member_id BIGINT,
                        store_id BIGINT,
                        status ENUM('CANCELED','DELIVERED','DELIVERING','PENDING','PREPARING'),
                        PRIMARY KEY (id),
                        CONSTRAINT FK_orders_member FOREIGN KEY (buy_member_id) REFERENCES member(id)
) ENGINE=InnoDB;

-- 주문 항목 테이블 생성
CREATE TABLE order_item (
                            id BIGINT NOT NULL AUTO_INCREMENT,
                            created_at DATETIME(6),
                            modified_at DATETIME(6),
                            count INTEGER NOT NULL,
                            price INTEGER NOT NULL,
                            order_id BIGINT NOT NULL,
                            menu_name VARCHAR(20) NOT NULL,
                            PRIMARY KEY (id),
                            CONSTRAINT FK_order_item_orders FOREIGN KEY (order_id) REFERENCES orders(id)
) ENGINE=InnoDB;

-- 리프레시 토큰 테이블 생성
CREATE TABLE refresh_token (
                               id BIGINT NOT NULL AUTO_INCREMENT,
                               email VARCHAR(50) NOT NULL,
                               refresh_token VARCHAR(100) NOT NULL,
                               PRIMARY KEY (id)
) ENGINE=InnoDB;

-- 리뷰 테이블 생성
CREATE TABLE review (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        created_at DATETIME(6),
                        modified_at DATETIME(6),
                        rating INTEGER NOT NULL,
                        order_id BIGINT,
                        contents VARCHAR(100),
                        PRIMARY KEY (id)
) ENGINE=InnoDB;