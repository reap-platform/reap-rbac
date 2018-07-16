DROP SCHEMA IF EXISTS REAP_RBAC;

CREATE SCHEMA REAP_RBAC;

DROP TABLE IF EXISTS REAP_RBAC.USER;

CREATE TABLE REAP_RBAC.USER
(
   ID VARCHAR(50) NOT NULL PRIMARY KEY,
   USERNAME VARCHAR(100) NOT NULL,
   NAME VARCHAR(100) NOT NULL,
   PASSWORD VARCHAR(100) NOT NULL,
   EMAIL VARCHAR(200) NOT NULL,
   PHONE_NO VARCHAR(20) NOT NULL,
   CREATE_TIME TIMESTAMP,
   GENDER CHAR(1),
   ORG_ID VARCHAR(50),
   REMARK VARCHAR(500)
);

DROP TABLE IF EXISTS REAP_RBAC.ORG;

CREATE TABLE REAP_RBAC.ORG
(
   ID VARCHAR(50) NOT NULL PRIMARY KEY,
   NAME VARCHAR(100) NOT NULL,
   CODE VARCHAR(50) NOT NULL,
   BUSINESS_TYPE_ID VARCHAR(50),
   CREATE_TIME TIMESTAMP,
   PARENT_ID VARCHAR(50),
   REMARK VARCHAR(500),
   LEAF CHAR(1)
);

DROP TABLE IF EXISTS REAP_RBAC.ROLE;
CREATE TABLE REAP_RBAC.ROLE
(
   ID VARCHAR(50) NOT NULL PRIMARY KEY,
   CREATE_TIME TIMESTAMP,
   NAME VARCHAR(100) NOT NULL,
   REMARK VARCHAR(500)
);
DROP TABLE IF EXISTS REAP_RBAC.USER_ROLE;
CREATE TABLE REAP_RBAC.USER_ROLE
(
   USER_ID VARCHAR(50) NOT NULL,
   ROLE_ID VARCHAR(50) NOT NULL,
   PRIMARY KEY (USER_ID,ROLE_ID)
);
DROP TABLE IF EXISTS REAP_RBAC.FUNCTION;
CREATE TABLE REAP_RBAC.FUNCTION
(
   ID VARCHAR(50) NOT NULL PRIMARY KEY,
   SERVICE_ID VARCHAR(200) NOT NULL,
   CODE VARCHAR(50) NOT NULL,
   NAME VARCHAR(100) NOT NULL,
   TYPE CHAR(1) NOT NULL,
   ACTION VARCHAR(500) DEFAULT NULL,
   REMARK VARCHAR(500) DEFAULT NULL
);

DROP TABLE IF EXISTS REAP_RBAC.ROLE_FUNCTION;

CREATE TABLE REAP_RBAC.ROLE_FUNCTION (
 ROLE_ID VARCHAR(50),
 FUNCTION_ID VARCHAR(50)
);

DROP TABLE IF EXISTS REAP_RBAC.BUSINESS_TYPE;

CREATE TABLE REAP_RBAC.BUSINESS_TYPE
(
  ID VARCHAR(100) NOT NULL PRIMARY KEY,
  NAME VARCHAR(50) NOT NULL UNIQUE,
  CREATED_TIME TIMESTAMP,
  REMARK VARCHAR(100)
);

DROP TABLE IF EXISTS REAP_RBAC.BUSINESS_TYPE_FUNCTION;

CREATE TABLE REAP_RBAC.BUSINESS_TYPE_FUNCTION (
  BUSINESS_TYPE_ID VARCHAR(50),
  FUNCTION_ID VARCHAR(50)
)
