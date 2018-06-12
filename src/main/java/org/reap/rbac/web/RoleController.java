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

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.reap.rbac.common.Constants;
import org.reap.rbac.common.ErrorCodes;
import org.reap.rbac.domain.Role;
import org.reap.rbac.domain.RoleRepository;
import org.reap.rbac.domain.User;
import org.reap.rbac.domain.UserRepository;
import org.reap.rbac.vo.QueryRoleSpec;
import org.reap.support.DefaultResult;
import org.reap.support.Result;
import org.reap.util.FunctionalUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
public class RoleController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;
	@PersistenceContext
	private EntityManager entityManager;

	/** @apiDefine Role 角色维护 */

	/**
	 * @api {post} /role/user/{id} 分配角色
	 * @apiDescription 为指定的用户分配角色
	 * @apiName allocateRoles 分配角色
	 * @apiGroup Role
	 * @apiParam (PathVariable) {String} id 用户 id
	 * @apiParam (Body) {String[]} roleIds 角色 id 列表
	 * @apiSuccess (Success) {Boolean} success 业务成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/role/user/{id}", method = RequestMethod.POST)
	public Result<?> allocateRoles(@PathVariable String id, @RequestBody String[] roleIds) {
		User user = FunctionalUtils.orElseThrow(userRepository.findById(id), ErrorCodes.USER_NOT_EXIST);
		List<Role> roles = roleRepository.findAllById(Arrays.asList(roleIds));
		user.setRoles(new HashSet<>(roles));
		userRepository.save(user);
		return DefaultResult.newResult();
	}

	/**
	 * @api {get} /roles/all 所有角色
	 * @apiName allRoles 所有角色
	 * @apiGroup Role
	 * @apiSuccess (Success) {Boolean} success 业务成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiSuccess (Success) {Object[]} payload 角色列表
	 * @apiSuccess (Success) {String} payload.id 角色 id
	 * @apiSuccess (Success) {String} payload.name 角色名称
	 * @apiSuccess (Success) {String} payload.createTime 创建时间
	 * @apiSuccess (Success) {String} payload.remark 备注
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/roles/all", method = RequestMethod.GET)
	public Result<List<Role>> findAll() {
		return DefaultResult.newResult(roleRepository.findAll());
	}

	/**
	 * @api {post} /role 创建角色
	 * @apiName createRole 创建角色
	 * @apiGroup Role
	 * @apiParam (Body) {String} name 角色名称
	 * @apiParam (Body) {String} remark 备注
	 * @apiSuccess (Success) {Boolean} success 业务成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiSuccess (Success) {Object} payload 角色
	 * @apiSuccess (Success) {String} payload.id 角色 id
	 * @apiSuccess (Success) {String} payload.name 角色名称
	 * @apiSuccess (Success) {String} payload.createTime 创建时间
	 * @apiSuccess (Success) {String} payload.remark 备注
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/role", method = RequestMethod.POST)
	public Result<Role> create(@RequestBody Role role) {
		role.setCreateTime(new Date());
		roleRepository.save(role);
		return DefaultResult.newResult(role);
	}

	/**
	 * @api {put} /role 更新角色
	 * @apiName updateRole 更新角色
	 * @apiGroup Role
	 * @apiParam (Body) {String} id 角色 id
	 * @apiParam (Body) {String} name 角色名称
	 * @apiParam (Body) {String} remark 备注
	 * @apiSuccess (Success) {Boolean} success 业务成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiSuccess (Success) {Object} payload 角色
	 * @apiSuccess (Success) {String} payload.id 角色 id
	 * @apiSuccess (Success) {String} payload.name 角色名称
	 * @apiSuccess (Success) {String} payload.createTime 创建时间
	 * @apiSuccess (Success) {String} payload.remark 备注
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/role", method = RequestMethod.PUT)
	public Result<Role> update(@RequestBody Role role) {
		Role entity = FunctionalUtils.orElseThrow(roleRepository.findById(role.getId()), ErrorCodes.ROLE_NOT_EXIST);
		BeanUtils.copyProperties(role, entity, "users");
		roleRepository.save(entity);
		return DefaultResult.newResult(role);
	}

	/**
	 * @api {delete} /role 删除角色
	 * @apiName deleteRole 删除角色
	 * @apiGroup Role
	 * @apiParam (PathVariable) {String} id 角色 id
	 * @apiSuccess (Success) {Boolean} success 业务成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/role/{id}", method = RequestMethod.DELETE)
	@Transactional
	public Result<?> delete(@PathVariable String id) {
		// 关联删除用户表映射记录
		Role role = roleRepository.findById(id).get();
		List<User> users = role.getUsers();
		users.forEach(user -> {
			user.getRoles().remove(role);
		});
		roleRepository.deleteById(id);
		return DefaultResult.newResult();
	}

	/**
	 * @api {get} /roles 查询角色
	 * @apiName queryRole
	 * @apiGroup Role
	 * @apiParam (QueryString) {Number} [page=0] 页码
	 * @apiParam (QueryString) {Number} [size=10] 每页记录数
	 * @apiParam (QueryString) {String} [name] 角色名称
	 * @apiSuccess (Success) {Boolean} success 成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiSuccess (Success) {Object} payload 响应数据
	 * @apiSuccess (Success) {Number} payload.totalPages 总页数
	 * @apiSuccess (Success) {Number} payload.totalElements 总记录数
	 * @apiSuccess (Success) {Number} payload.numberOfElements 当前记录数
	 * @apiSuccess (Success) {Object[]} payload.content 角色列表
	 * @apiSuccess (Success) {String} payload.content.id 机构 id
	 * @apiSuccess (Success) {String} payload.content.name 角色名称
	 * @apiSuccess (Success) {String} payload.content.createTime 创建时间
	 * @apiSuccess (Success) {String} payload.content.remark 备注
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/roles", method = RequestMethod.GET)
	public Result<Page<Role>> find(@RequestParam(defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size, QueryRoleSpec spec) {
		return DefaultResult.newResult(roleRepository.findAll(spec.toSpecification(), PageRequest.of(page, size)));
	}
}
