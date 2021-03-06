/*UPDATE mysql.user SET Password = PASSWORD('password') WHERE User = 'root';
FLUSH PRIVILEGES;*/

DROP TABLE IF EXISTS ITEM;
DROP TABLE IF EXISTS CATEGORY;
DROP TABLE IF EXISTS USER_ACCOUNT_VERIFICATION_TOKEN;
DROP TABLE IF EXISTS PASSWORD_RESET_TOKEN;
DROP TABLE IF EXISTS USER_ACCOUNT_USER_ROLE;
DROP TABLE IF EXISTS USER_ROLE;
DROP TABLE IF EXISTS USER_ACCOUNT;
DROP TABLE IF EXISTS PERSISTENT_LOGIN;
DROP TABLE IF EXISTS USERCONNECTION;

CREATE TABLE USER_ACCOUNT (
   USER_ID BIGINT NOT NULL AUTO_INCREMENT,
   /*USER_SSO_ID VARCHAR(30) NOT NULL,*/
   USER_LOGIN VARCHAR(20) NOT NULL,
   USER_PASSWORD VARCHAR(60) NOT NULL,
   USER_FIRST_NAME VARCHAR(50) NOT NULL,
   USER_LAST_NAME VARCHAR(50) NOT NULL,
   USER_EMAIL VARCHAR(254) NOT NULL,
   USER_IS_EMAIL_CONFIRMED TINYINT(1) DEFAULT 0,
   USER_DATE_REGISTRATION DATETIME DEFAULT CURRENT_TIMESTAMP,
   USER_DATE_BIRTH DATE,
   USER_CITY VARCHAR(50),
   USER_ZIP_CODE INT,
   USER_ADDRESS_PRINCIPAL VARCHAR(60),
   USER_ADDRESS_DETAIL VARCHAR(80),
   USER_COUNTRY	VARCHAR(30),
   USER_LAST_ACTION DATETIME,
   USER_IS_DELETED TINYINT(1) DEFAULT 0,
   USER_SIGN_IN_PROVIDER VARCHAR(255),
   USER_SIGN_IN_PROVIDER_USER_ID VARCHAR(255),
   VERSION BIGINT NOT NULL,
   PRIMARY KEY (USER_ID),
   UNIQUE (USER_LOGIN),
   UNIQUE (USER_EMAIL)
);
   
/* USER_ROLE table contains all possible roles */ 
CREATE TABLE USER_ROLE (
   USER_ROLE_ID BIGINT NOT NULL AUTO_INCREMENT,
   USER_ROLE_TYPE VARCHAR(30) NOT NULL,
   PRIMARY KEY (USER_ROLE_ID),
   UNIQUE (USER_ROLE_ID)
);
   
/* JOIN TABLE for MANY-TO-MANY relationship*/  
CREATE TABLE USER_ACCOUNT_USER_ROLE (
    USER_ID BIGINT NOT NULL,
    USER_ROLE_ID BIGINT NOT NULL,
    PRIMARY KEY (USER_ID, USER_ROLE_ID),
    CONSTRAINT FK_USER_USER_ROLE_USER FOREIGN KEY (USER_ID) REFERENCES USER_ACCOUNT (USER_ID),
    CONSTRAINT FK_USER_USER_ROLE_USER_ROLE FOREIGN KEY (USER_ROLE_ID) REFERENCES USER_ROLE (USER_ROLE_ID)
);
  
/* Populate USER_ROLE Table */
INSERT INTO USER_ROLE(USER_ROLE_TYPE)
VALUES ('USER');
  
INSERT INTO USER_ROLE(USER_ROLE_TYPE)
VALUES ('ADMIN');
  
/* Populate one Admin User which will further create other users for the application using GUI (abcd1234) */
INSERT INTO USER_ACCOUNT(USER_LOGIN, USER_PASSWORD, USER_FIRST_NAME, USER_LAST_NAME, USER_DATE_BIRTH, USER_EMAIL, USER_IS_EMAIL_CONFIRMED, VERSION)
VALUES ('manu', '$2a$10$27VaG6hjRrj2PMvwpSErbumO39uDHUGl.6Q98STrzKYzUbbpE1EMW', 'Manu', 'Manu', '1981-08-14', 'manu@xyz.com', 1, 0);
  
/* Populate JOIN Table */
INSERT INTO USER_ACCOUNT_USER_ROLE (USER_ID, USER_ROLE_ID)
  SELECT u.USER_ID, r.USER_ROLE_ID FROM USER_ACCOUNT u, USER_ROLE r
  WHERE u.USER_LOGIN = 'manu' AND r.USER_ROLE_TYPE = 'ADMIN';
 
/* Create persistent_login Table used to store rememberme related stuff*/
CREATE TABLE PERSISTENT_LOGIN (
	SERIES VARCHAR(64) NOT NULL,
    USERNAME VARCHAR(64) NOT NULL,
    TOKEN VARCHAR(64) NOT NULL,
    LAST_USED TIMESTAMP NOT NULL,
    PRIMARY KEY (SERIES),
    UNIQUE (USERNAME)
);

/* Social */
CREATE TABLE USERCONNECTION (
	USERID VARCHAR(255) NOT NULL,
    PROVIDERID VARCHAR(255) NOT NULL,
    PROVIDERUSERID VARCHAR(255),
    RANK INT NOT NULL,
    DISPLAYNAME VARCHAR(255),
    PROFILEURL VARCHAR(512),
    IMAGEURL VARCHAR(512),
    ACCESSTOKEN VARCHAR(512) NOT NULL,
    SECRET VARCHAR(512),
    REFRESHTOKEN VARCHAR(512),
    EXPIRETIME BIGINT,
    PRIMARY KEY (USERID, PROVIDERID, PROVIDERUSERID)
);
CREATE UNIQUE INDEX USERCONNECTIONRANK ON USERCONNECTION(USERID, PROVIDERID, RANK);

CREATE TABLE USER_ACCOUNT_VERIFICATION_TOKEN
(
	TOKEN_ID BIGINT NOT NULL AUTO_INCREMENT,
   	TOKEN VARCHAR(36) NOT NULL,
   	TOKEN_EXPIRY_DATE TIMESTAMP NOT NULL,
   	TOKEN_USER_ID BIGINT NOT NULL,
   	PRIMARY KEY (TOKEN_ID),
   	CONSTRAINT FK_VERIFICATION_TOKEN_USER FOREIGN KEY (TOKEN_USER_ID) REFERENCES USER_ACCOUNT (USER_ID)
);

CREATE TABLE PASSWORD_RESET_TOKEN
(
	TOKEN_ID BIGINT NOT NULL AUTO_INCREMENT,
   	TOKEN VARCHAR(36) NOT NULL,
   	TOKEN_EXPIRY_DATE TIMESTAMP NOT NULL,
   	TOKEN_USER_ID BIGINT NOT NULL,
   	PRIMARY KEY (TOKEN_ID),
   	CONSTRAINT FK_PASSWORD_RESET_TOKEN_USER_ACCOUNT FOREIGN KEY (TOKEN_USER_ID) REFERENCES USER_ACCOUNT (USER_ID)
);

CREATE TABLE CATEGORY
(
   CATEGORY_ID INT NOT NULL AUTO_INCREMENT,
   CATEGORY_NAME VARCHAR(50) NOT NULL,
   CATEGORY_IS_DELETED TINYINT(1) DEFAULT 0,
   CATEGORY_IMAGE_LINK VARCHAR(255),
   PRIMARY KEY (CATEGORY_ID)
);

CREATE TABLE ITEM
(
   ITEM_ID BIGINT NOT NULL AUTO_INCREMENT,
   ITEM_USER_ID BIGINT NOT NULL,
   ITEM_CATEGORY_ID INT NOT NULL,
   ITEM_TITLE VARCHAR(50) NOT NULL,
   ITEM_TEXT	TEXT NOT NULL,
   ITEM_DATE_CREATION DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   ITEM_DATE_LAST_UPDATE DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, -- ON UPDATE CURRENT_TIMESTAMP
   ITEM_IS_DELETED TINYINT(1) DEFAULT 0,
   PRIMARY KEY (ITEM_ID),
   CONSTRAINT FK_ITEM_USER FOREIGN KEY (ITEM_USER_ID) REFERENCES USER_ACCOUNT (USER_ID),
   CONSTRAINT FK_ITEM_CATEGORY FOREIGN KEY (ITEM_CATEGORY_ID) REFERENCES CATEGORY (CATEGORY_ID)
);