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

package org.reap.rbac.service.impl;

import java.util.List;
import java.util.Optional;

import org.reap.rbac.common.ErrorCodes;
import org.reap.rbac.domain.FunctionRepository;
import org.reap.rbac.domain.Org;
import org.reap.rbac.domain.OrgRepository;
import org.reap.rbac.domain.Role;
import org.reap.rbac.domain.RoleRepository;
import org.reap.rbac.domain.User;
import org.reap.rbac.domain.UserRepository;
import org.reap.rbac.service.UserService;
import org.reap.util.FunctionalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * @author 7cat
 * @since 1.0
 */
@Component
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrgRepository orgRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private FunctionRepository functionRepository;

	@Override
	public User logon(String username, String password) {
		Optional<User> userOptional = userRepository.findOneByUsernameAndPassword(username, password);
		User user = FunctionalUtils.orElseThrow(userOptional, ErrorCodes.USERNAME_OR_PASSWORD_IS_INCORRECT);
		Optional<Org> orgOptional = orgRepository.findById(user.getOrgId());
		Org org = FunctionalUtils.orElseThrow(orgOptional, ErrorCodes.ORG_NOT_EXIST);
		user.setOrg(org);
		List<Role> roles = roleRepository.findByUserId(user.getId());
		roles.stream().forEach(r -> r.setFunctions(functionRepository.findByRoleId(r.getId())));
		user.setRoles(roles);
		return user;
	}
}
