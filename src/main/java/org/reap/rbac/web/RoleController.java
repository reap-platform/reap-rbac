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
 * 
 * @author 7cat
 * @since 1.0
 */
@RestController
public class RoleController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@RequestMapping(path = "/role/user/{id}", method = RequestMethod.POST)
	public Result<?> allocateRoles(@PathVariable String id, @RequestBody String[] roleIds) {
		User user = FunctionalUtils.orElseThrow(userRepository.findById(id), ErrorCodes.USER_NOT_EXIST);
		List<Role> roles = roleRepository.findAllById(Arrays.asList(roleIds));
		user.setRoles(new HashSet<>(roles));
		userRepository.save(user);
		return DefaultResult.newResult();
	}
	
	
	@RequestMapping(path = "/roles/all", method = RequestMethod.GET)
	public Result<List<Role>> findAll() {
		return DefaultResult.newResult(roleRepository.findAll());
	}

	@RequestMapping(path = "/role", method = RequestMethod.POST)
	public Result<Role> create(@RequestBody Role role) {
		role.setCreateTime(new Date());
		roleRepository.save(role);
		return DefaultResult.newResult(role);
	}

	@RequestMapping(path = "/role", method = RequestMethod.PUT)
	public Result<Role> update(@RequestBody Role role) {
		Role entity = FunctionalUtils.orElseThrow(roleRepository.findById(role.getId()), ErrorCodes.ROLE_NOT_EXIST);
		BeanUtils.copyProperties(role, entity, "users");
		roleRepository.save(entity);
		return DefaultResult.newResult(role);
	}

	
	@RequestMapping(path = "/role/{id}", method = RequestMethod.DELETE)
	public Result<?> delete(@PathVariable String id) {
		roleRepository.deleteById(id);
		return DefaultResult.newResult();
	}
	
	@RequestMapping(path = "/roles", method = RequestMethod.GET)
	public Result<Page<Role>> find(@RequestParam(defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size, QueryRoleSpec spec) {
		return DefaultResult.newResult(roleRepository.findAll(spec.toSpecification(), PageRequest.of(page, size)));
	}
}
