DROP DATABASE IF EXISTS moonstone_mine;

CREATE DATABASE IF NOT EXISTS moonstone_mine;

USE moonstone_mine;

DROP TABLE IF EXISTS login_history;
CREATE TABLE IF NOT EXISTS login_history(
                                            l_id VARCHAR(10) PRIMARY KEY,
                                            login_date DATE,
                                            logout_date DATE,
                                            login_time TIME,
                                            logout_time TIME
);

DROP TABLE IF EXISTS user;
CREATE TABLE IF NOT EXISTS user(
                                   email    VARCHAR(35) PRIMARY KEY,
                                   name     VARCHAR(155) NOT NULL,
                                   password VARCHAR(30)         NOT NULL
                                  );


DROP TABLE IF EXISTS employee;
CREATE TABLE IF NOT EXISTS employee(
                                       e_id VARCHAR(10) PRIMARY KEY,
                                       name VARCHAR(25) NOT NULL,
                                        address TEXT,
                                       phone INT(10),
                                       email VARCHAR(30)
);

DROP TABLE IF EXISTS report;
CREATE TABLE IF NOT EXISTS report(
                                     r_code VARCHAR(10) PRIMARY KEY,
                                     date DATE,
                                     type VARCHAR(30),
                                     income DECIMAL(10,2) NOT NULL
);

DROP TABLE IF EXISTS customer;
CREATE TABLE IF NOT EXISTS customer(
                                       id VARCHAR(10) PRIMARY KEY,
                                       name VARCHAR(30) NOT NULL,
                                       address  VARCHAR(30),
                                       phone VARCHAR(15),
                                       email VARCHAR(30)

);

DROP TABLE IF EXISTS ticket;
CREATE TABLE IF NOT EXISTS ticket(
                                     code VARCHAR(10) PRIMARY KEY,
                                     type text not null ,
                                     price double not null,
                                     qty_on_hand int not null
);

DROP TABLE IF EXISTS booking;
CREATE TABLE IF NOT EXISTS booking(
                                     booking_id VARCHAR(10) PRIMARY KEY,
                                     cus_id VARCHAR(10),
                                     date DATE NOT NULL,
                                     FOREIGN KEY (cus_id) REFERENCES customer(id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS booking_detail;
CREATE TABLE IF NOT EXISTS booking_detail(
                                           booking_id VARCHAR(10),
                                           code VARCHAR(10),
                                           qty INT NOT NULL,
                                           price DOUBLE NOT NULL,
                                           FOREIGN KEY (booking_id) REFERENCES booking (booking_id) ON DELETE CASCADE ON UPDATE CASCADE,
                                           FOREIGN KEY (code) REFERENCES ticket(code) ON DELETE CASCADE ON UPDATE CASCADE
);


DROP TABLE IF EXISTS orders;
CREATE TABLE IF NOT EXISTS orders(
                                     order_id VARCHAR(10) PRIMARY KEY,
                                     cus_id VARCHAR(10),
                                     date DATE NOT NULL,
                                     FOREIGN KEY (cus_id) REFERENCES customer(id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS item;
CREATE TABLE IF NOT EXISTS item(
                                   code VARCHAR(10) PRIMARY KEY,
                                   description text not null ,
                                   unit_price double not null,
                                   qty_on_hand int not null
);

DROP TABLE IF EXISTS order_detail;
CREATE TABLE IF NOT EXISTS order_detail(
                                           order_id VARCHAR(10),
                                           code VARCHAR(10),
                                           qty INT NOT NULL,
                                           unit_price DOUBLE NOT NULL,
                                           FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE ON UPDATE CASCADE,
                                           FOREIGN KEY (code) REFERENCES item(code) ON DELETE CASCADE ON UPDATE CASCADE
);

