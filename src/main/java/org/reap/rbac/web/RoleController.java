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
import java.util.List;
import java.util.stream.Collectors;

import org.reap.rbac.common.Constants;
import org.reap.rbac.common.Fields;
import org.reap.rbac.domain.Function;
import org.reap.rbac.domain.FunctionRepository;
import org.reap.rbac.domain.Role;
import org.reap.rbac.domain.RoleRepository;
import org.reap.rbac.domain.UserRole;
import org.reap.rbac.domain.UserRoleRepository;
import org.reap.support.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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
public class RoleController {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;
	
	@Autowired
	private FunctionRepository functionRepository;
	

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
	@Transactional
	public Result<?> allocateRoles(@PathVariable String id, @RequestBody String[] roleIds) {
		userRoleRepository.deleteById_userId(id);
		userRoleRepository.insertAll(
				Arrays.asList(roleIds).stream().map(roleId -> UserRole.of(id, roleId)).collect(Collectors.toList()));
		return Result.newResult();
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
		return Result.newResult(roleRepository.findAll());
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
		roleRepository.save(role);
		return Result.newResult(role);
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
		roleRepository.updateIgnoreNull(role);
		return Result.newResult(role);
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
		userRoleRepository.deleteById_RoleId(id);
		roleRepository.deleteById(id);
		return Result.newResult();
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
			@RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size, Role role) {
		Example<Role> example = Example.of(role, ExampleMatcher.matching());
		return Result.newResult(
				roleRepository.findAll(example, PageRequest.of(page, size, Sort.by(Fields.NAME))));
	}
	
	
	/**
	 * @api {get} /role/{id}/functions 查询角色
	 * @apiName queryRole
	 * @apiGroup Role
	 * @apiParam (PathVariable) {String} [roleId] 角色 id
	 * @apiSuccess (Success) {Boolean} success 成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiSuccess (Success) {Object[]} payload 响应数据
	 * @apiSuccess (Success) {String} payload.id 功能 id
	 * @apiSuccess (Success) {String} payload.serviceId 归属系统
	 * @apiSuccess (Success) {String} payload.code 功能码
	 * @apiSuccess (Success) {String} payload.name 功能名称
	 * @apiSuccess (Success) {String} payload.type 类型 'M' 菜单 'O' 操作
	 * @apiSuccess (Success) {String} payload.action 动作
	 * @apiSuccess (Success) {String} payload.remark 备注
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/role/{id}/functions", method = RequestMethod.GET)
	public Result<List<Function>> findFunctions(@PathVariable String id) {
		return Result.newResult(functionRepository.findByRoleId(id));
	}
}
