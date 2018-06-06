-- 参数
INSERT INTO CONFIG (ID, APPLICATION, PROFILE, LABEL, NAME, VALUE) VALUES ('reap-rbac.prd.default.server.port','reap-rbac','prd','default','server.port','8070');
INSERT INTO CONFIG (ID, APPLICATION, PROFILE, LABEL, NAME, VALUE) VALUES ('reap-rbac.prd.default.password.md5.salt','reap-rbac','prd','default','password.md5.salt', 'reap');
-- 路由 
INSERT INTO ROUTE VALUES('reap-rbac','reap-rbac','/apis/reap-rbac/**','reap-rbac',null,null);
INSERT INTO ROUTE VALUES('reap-rbac-ui','reap-rbac-ui','/ui/reap-rbac/**','reap-rbac',null,null);
-- 功能码
INSERT INTO FUNCTION (ID,SERVICE_ID,CODE,NAME,TYPE,ACTION) VALUES ('REAPRB0001','reap-rbac','REAPRB0001','机构管理','M','');
INSERT INTO FUNCTION (ID,SERVICE_ID,CODE,NAME,TYPE,ACTION) VALUES ('REAPRB0002','reap-rbac','REAPRB0002','用户管理','M','');
INSERT INTO FUNCTION (ID,SERVICE_ID,CODE,NAME,TYPE,ACTION) VALUES ('REAPRB0003','reap-rbac','REAPRB0003','岗位管理','M','');
INSERT INTO FUNCTION (ID,SERVICE_ID,CODE,NAME,TYPE,ACTION) VALUES ('REAPRB0004','reap-rbac','REAPRB0004','功能码管理','M','');


