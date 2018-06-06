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

import javax.transaction.Transactional;

import org.junit.Test;
import org.reap.BaseTest;
import org.reap.rbac.domain.Role;
import org.reap.rbac.domain.UserRepository;
import org.reap.rbac.vo.QueryRoleSpec;
import org.reap.rbac.web.RoleController;
import org.reap.support.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import static org.junit.Assert.*;

/**
 * 
 * @author 7cat
 * @since 1.0
 */
public class RoleControllerTest extends BaseTest {

	@Autowired
	private RoleController roleController;

	@Autowired
	private UserRepository userRepository;

	@Test
	@Transactional
	public void testAllocateRoles() {
		Result<?> result = roleController.allocateRoles("0000000001", new String[] { "0001", "0002", "0003" });
		assertTrue(result.isSuccess());
		assertEquals(3, userRepository.findById("0000000001").get().getRoles().size());
	}
	
	@Test
	public void testFind() {
		QueryRoleSpec spec= new QueryRoleSpec();
		spec.setName("岗位1");
		Result<Page<Role>> result = roleController.find(0, 10, spec);
		assertTrue(result.isSuccess());
		assertEquals(1, result.getPayload().getContent().size());
	}
}
