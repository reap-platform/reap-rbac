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
import org.reap.rbac.domain.User;
import org.reap.rbac.domain.UserRepository;
import org.reap.rbac.util.MD5Utils;
import org.reap.rbac.vo.QueryUserSpec;
import org.reap.support.DefaultResult;
import org.reap.support.Result;
import org.reap.util.Assert;
import org.reap.util.FunctionalUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrgRepository orgRepository;

	@Value ("${password.md5.salt}")
	private String salt;
	
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

	@RequestMapping(path = "/user", method = RequestMethod.PUT)
	public Result<User> update(@RequestBody User user) {
		User u = userRepository.findById(user.getId()).get();
		BeanUtils.copyProperties(user, u, Fields.PASSWORD, Fields.CREATE_TIME);
		return DefaultResult.newResult(userRepository.save(u));
	}

	@RequestMapping(path = "/user/{id}", method = RequestMethod.DELETE)
	public Result<?> delete(@PathVariable String id) {
		userRepository.deleteById(id);
		return DefaultResult.newResult();
	}

	@RequestMapping(path = "/users", method = RequestMethod.GET)
	public Result<Page<User>> find(@RequestParam(defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size, QueryUserSpec spec) {
		return DefaultResult.newResult(userRepository.findAll(spec.toSpecification(), PageRequest.of(page, size)));
	}
}
