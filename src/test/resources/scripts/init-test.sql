
DELETE FROM ORG;
INSERT INTO ORG(ID,NAME,CODE,PARENT_ID,REMARK,LEAF)  VALUES('0000000001','测试机构1','0001', NULL ,'备注信息', 'N');
INSERT INTO ORG(ID,NAME,CODE,PARENT_ID,REMARK,LEAF)  VALUES('0000000011','测试机构11','0011','0000000001' ,'备注信息', 'Y');
INSERT INTO ORG(ID,NAME,CODE,PARENT_ID,REMARK,LEAF)  VALUES('0000000012','测试机构12','0012','0000000001' ,'备注信息', 'N');
INSERT INTO ORG(ID,NAME,CODE,PARENT_ID,REMARK,LEAF)  VALUES('0000000121','测试机构121','0121','0000000012' ,'备注信息', 'Y');
INSERT INTO ORG(ID,NAME,CODE,PARENT_ID,REMARK,LEAF)  VALUES('0000000002','测试机构2','0002',NULL ,'备注信息', 'Y');
INSERT INTO ORG(ID,NAME,CODE,PARENT_ID,REMARK,LEAF)  VALUES('0000000003','测试机构3', '0003',NULL ,'备注信息', 'Y');
INSERT INTO ORG(ID,NAME,CODE,PARENT_ID,REMARK,LEAF)  VALUES('0000000004','测试机构4','0004',NULL ,'备注信息', 'Y');

DELETE FROM USER;
INSERT INTO USER(ID,USERNAME,NAME,PASSWORD,EMAIL,PHONE_NO,GENDER,ORG_ID,REMARK) VALUES ('0000000001', 'user0000000001', '用户1', 'a47f7482937221bbb5c5b153cc71cb25', 'eamil01@test.com','18902000111','M','0000000121',NULL);
INSERT INTO USER(ID,USERNAME,NAME,PASSWORD,EMAIL,PHONE_NO,GENDER,ORG_ID,REMARK) VALUES ('0000000002', 'user0000000002', '用户2', 'fc5b3bb6e5bb06e96f9b08beea732871', 'eamil02@test.com','18902000112','M','0000000002',NULL);
INSERT INTO USER(ID,USERNAME,NAME,PASSWORD,EMAIL,PHONE_NO,GENDER,ORG_ID,REMARK) VALUES ('0000000003', 'user0000000003', '用户3', '6322641a20eeb6cc509f43c8bb7df79c', 'eamil03@test.com','18902000113','M','0000000003',NULL);

DELETE FROM ROLE;
INSERT INTO ROLE(ID, NAME) VALUES ('0001','岗位1');
INSERT INTO ROLE(ID, NAME) VALUES ('0002','岗位2');
INSERT INTO ROLE(ID, NAME) VALUES ('0003','岗位3');

DELETE FROM USER_ROLE;
INSERT INTO USER_ROLE (USER_ID, ROLE_ID) VALUES ('0000000001', '0001');
INSERT INTO USER_ROLE (USER_ID, ROLE_ID) VALUES ('0000000001', '0002');
INSERT INTO USER_ROLE (USER_ID, ROLE_ID) VALUES ('0000000002', '0001');
INSERT INTO USER_ROLE (USER_ID, ROLE_ID) VALUES ('0000000003', '0002');

DELETE FROM FUNCTION;
INSERT INTO FUNCTION(ID, SERVICE_ID, CODE, NAME, TYPE, REMARK) VALUES ('0001' , 'reap-rbac', 'REAPRB0001', '机构信息维护' ,'M',NULL );
INSERT INTO FUNCTION(ID, SERVICE_ID, CODE, NAME, TYPE, REMARK) VALUES ('0002' , 'reap-rbac', 'REAPRB0002', '用户信息维护' ,'M',NULL );
INSERT INTO FUNCTION(ID, SERVICE_ID, CODE, NAME, TYPE, REMARK) VALUES ('0003' , 'reap-rbac', 'REAPRB0003', '角色维护' ,'M',NULL );
INSERT INTO FUNCTION(ID, SERVICE_ID, CODE, NAME, TYPE, REMARK) VALUES ('0004' , 'reap-rbac', 'REAPRB0004', '功能码维护' ,'M',NULL );
