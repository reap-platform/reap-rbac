/*
 * Copyright (c) 2018-present the original author or authors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.reap.rbac.web;

import java.util.Optional;

import org.reap.rbac.common.ErrorCodes;
import org.reap.rbac.domain.User;
import org.reap.rbac.domain.UserRepository;
import org.reap.rbac.util.MD5Utils;
import org.reap.support.DefaultResult;
import org.reap.support.Result;
import org.reap.util.FunctionalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用于用户认证的 Controller 密码使用盐值进行 MD5 加密.
 * 
 * @author 7cat
 * @since 1.0
 */
@RestController
public class AuthenticationController {

	@Autowired
	private UserRepository userRepository;

	@Value("${password.md5.salt}")
	private String salt = "";

	/** @apiDefine Security 安全 */

	/**
	 * @api {post} /authentication 用户认证
	 * @apiName authentication
	 * @apiGroup Security
	 * @apiParam (Body) {String} username 用户名
	 * @apiParam (Body) {String} password 密码
	 * @apiSuccess (Success) {Boolean} success 业务成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiSuccess (Success) {Object} payload 用户信息
	 * @apiSuccess (Success) {String} payload.username 用户名
	 * @apiSuccess (Success) {String} payload.name 姓名
	 * @apiSuccess (Success) {String} payload.email 邮箱
	 * @apiSuccess (Success) {String} payload.phoneNo 手机
	 * @apiSuccess (Success) {String} payload.createTime 创建时间
	 * @apiSuccess (Success) {Object} payload.org 归属机构
	 * @apiSuccess (Success) {String} payload.org.id 机构 id
	 * @apiSuccess (Success) {String} payload.org.name 机构名称
	 * @apiSuccess (Success) {String} payload.org.code 机构代码
	 * @apiSuccess (Success) {String} payload.org.createTime 创建时间
	 * @apiSuccess (Success) {Object[]} payload.roles 岗位
	 * @apiSuccess (Success) {String} payload.roles.id 岗位 id
	 * @apiSuccess (Success) {String} payload.roles.name 岗位名称
	 * @apiSuccess (Success) {String} payload.roles.remark 备注
	 * @apiSuccess (Success) {Object[]} payload.roles.functions 岗位功能
	 * @apiSuccess (Success) {String} payload.roles.functions.id 功能 id
	 * @apiSuccess (Success) {String} payload.roles.functions.serviceId 归属系统
	 * @apiSuccess (Success) {String} payload.roles.functions.code 功能码
	 * @apiSuccess (Success) {String} payload.roles.functions.name 功能名称
	 * @apiSuccess (Success) {String} payload.roles.functions.type 功能类型 'M' 菜单 'O' 操作
	 * @apiSuccess (Success) {String} payload.roles.functions.action 动作
	 * @apiSuccess (Success) {String} payload.roles.functions.remark 备注
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/authentication", method = RequestMethod.POST)
	public Result<User> authentication(@RequestParam String username, @RequestParam String password) {
		Optional<User> user = userRepository.findOneByUsernameAndPassword(username, MD5Utils.encode(password, salt));
		return DefaultResult.newResult(FunctionalUtils.orElseThrow(user, ErrorCodes.USERNAME_OR_PASSWORD_IS_INCORRECT));
	}
}
