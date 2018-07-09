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

import java.util.Date;
import java.util.Optional;

import org.reap.rbac.common.Constants;
import org.reap.rbac.common.ErrorCodes;
import org.reap.rbac.common.Fields;
import org.reap.rbac.domain.Org;
import org.reap.rbac.domain.OrgRepository;
import org.reap.rbac.domain.RoleRepository;
import org.reap.rbac.domain.User;
import org.reap.rbac.domain.UserRepository;
import org.reap.rbac.domain.UserRoleRepository;
import org.reap.rbac.util.MD5Utils;
import org.reap.rbac.vo.QueryUserSpec;
import org.reap.support.DefaultResult;
import org.reap.support.Result;
import org.reap.util.Assert;
import org.reap.util.FunctionalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 7cat
 * @since 1.0
 */
@RestController
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrgRepository orgRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;
	
	@Autowired
	private RoleRepository roleRepository;

	@Value("${password.md5.salt}")
	private String salt;

	/** @apiDefine User 用户维护 */

	/**
	 * @api {post} /user/org/{orgId} 创建用户
	 * @apiDescription 创建归属在指定机构下的用户
	 * @apiName createUser
	 * @apiGroup User
	 * @apiParam (PathVariable) {String} orgId 机构 id
	 * @apiParam (Body) {String} username 用户名
	 * @apiParam (Body) {String} name 姓名
	 * @apiParam (Body) {String} password 密码
	 * @apiParam (Body) {String} email 邮箱
	 * @apiParam (Body) {String} phoneNo 电话号码
	 * @apiParam (Body) {String} gender 姓别 'M' 男 'F' 女
	 * @apiParam (Body) {String} remark 备注
	 * @apiSuccess (Success) {Boolean} success 业务成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/user/org/{orgId}", method = RequestMethod.POST)
	public Result<User> create(@RequestBody User user, @PathVariable String orgId) {
		validate(user);
		user.setCreateTime(new Date());
		Optional<Org> org = orgRepository.findById(orgId);
		user.setOrg(FunctionalUtils.orElseThrow(org, ErrorCodes.ORG_NOT_EXIST));
		user.setPassword(MD5Utils.encode(user.getPassword(), salt));
		return DefaultResult.newResult(userRepository.save(user));
	}

	private void validate(User user) {
		Assert.isTrue(!userRepository.existsByUsername(user.getUsername()), ErrorCodes.DUPLICATED_USERNAME);
		Assert.isTrue(!userRepository.existsByEmail(user.getEmail()), ErrorCodes.DUPLICATED_EMAIL);
	}

	/**
	 * @api {put} /user 更新用户
	 * @apiName createUser
	 * @apiGroup User
	 * @apiParam (PathVariable) {String} orgId 机构 id
	 * @apiParam (Body) {String} username 用户名
	 * @apiParam (Body) {String} name 姓名
	 * @apiParam (Body) {String} password 密码
	 * @apiParam (Body) {String} email 邮箱
	 * @apiParam (Body) {String} phoneNo 电话号码
	 * @apiParam (Body) {String} gender 姓别 'M' 男 'F' 女
	 * @apiParam (Body) {String} remark 备注
	 * @apiSuccess (Success) {Boolean} success 业务成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/user", method = RequestMethod.PUT)
	public Result<?> update(@RequestBody User user) {
		userRepository.updateIgnoreNull(user);
		return DefaultResult.newResult();
	}

	/**
	 * @api {delete} /user 删除用户
	 * @apiName deleteUser
	 * @apiGroup User
	 * @apiParam (PathVariable) {String} id 用户 id
	 * @apiSuccess (Success) {Boolean} success 业务成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@Transactional
	@RequestMapping(path = "/user/{id}", method = RequestMethod.DELETE)
	public Result<?> delete(@PathVariable String id) {
		userRepository.deleteById(id);
		userRoleRepository.deleteById_userId(id);
		return DefaultResult.newResult();
	}

	/**
	 * @api {get} /users 查询用户
	 * @apiName queryUser
	 * @apiGroup User
	 * @apiParam (QueryString) {Number} [page=0] 页码
	 * @apiParam (QueryString) {Number} [size=10] 每页记录数
	 * @apiParam (QueryString) {String} [username] 用户名
	 * @apiParam (QueryString) {String} [name] 姓名
	 * @apiParam (QueryString) {String} [email] 邮箱
	 * @apiParam (QueryString) {String} [phoneNo] 电话号码
	 * @apiSuccess (Success) {Boolean} success 成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiSuccess (Success) {Object} payload 响应数据
	 * @apiSuccess (Success) {Number} payload.totalPages 总页数
	 * @apiSuccess (Success) {Number} payload.totalElements 总记录数
	 * @apiSuccess (Success) {Number} payload.numberOfElements 当前记录数
	 * @apiSuccess (Success) {Object[]} payload.content 机构列表
	 * @apiSuccess (Success) {String} payload.content.id 用户 id
	 * @apiSuccess (Success) {String} payload.content.username 用户名
	 * @apiSuccess (Success) {String} payload.content.name 用户姓名
	 * @apiSuccess (Success) {String} payload.content.email 邮箱
	 * @apiSuccess (Success) {String} payload.content.phoneNo 电话号码
	 * @apiSuccess (Success) {String} payload.content.gender 姓别 'M' 男 'F' 女
	 * @apiSuccess (Success) {String} payload.content.remark 备注
	 * @apiSuccess (Success) {String} payload.content.createTime 创建时间
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/users", method = RequestMethod.GET)
	public Result<Page<User>> find(@RequestParam(defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size, QueryUserSpec spec) {
		return DefaultResult.newResult(
				userRepository.findBySpecification(spec, PageRequest.of(page, size, Sort.by(Fields.CREATE_TIME))));
	}
	
	
	/**
	 * @api {get} /user/{id} 查询用户
	 * @apiName getUser
	 * @apiGroup User
	 * @apiParam (PathVariable) {String} orgId 机构 id
	 * @apiSuccess (Success) {Boolean} success 成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiSuccess (Success) {String} payload.id 用户 id
	 * @apiSuccess (Success) {String} payload.username 用户名
	 * @apiSuccess (Success) {String} payload.name 用户姓名
	 * @apiSuccess (Success) {String} payload.email 邮箱
	 * @apiSuccess (Success) {String} payload.phoneNo 电话号码
	 * @apiSuccess (Success) {String} payload.gender 姓别 'M' 男 'F' 女
	 * @apiSuccess (Success) {String} payload.remark 备注
	 * @apiSuccess (Success) {String} payload.createTime 创建时间
	 * @apiSuccess (Success) {Object[]} payload.roles 岗位
	 * @apiSuccess (Success) {String} payload.roles.id 岗位 id
	 * @apiSuccess (Success) {String} payload.roles.name 岗位名称
 	 * @apiSuccess (Success) {String} payload.roles.remark 备注
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/user/{id}", method = RequestMethod.GET)
	public Result<User> get(@PathVariable String id) {
		User user = FunctionalUtils.orElseThrow(userRepository.findById(id), ErrorCodes.USER_NOT_EXIST);
		user.setRoles(roleRepository.findByUserId(id));
		return DefaultResult.newResult(user);
	}
}
