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

import org.junit.Test;
import org.reap.BaseTest;
import org.reap.rbac.domain.User;
import org.reap.rbac.vo.QueryUserSpec;
import org.reap.rbac.web.UserController;
import org.reap.support.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import static org.junit.Assert.*;

/**
 * @author 7cat
 * @since 1.0
 */
public class UserControllerTest extends BaseTest {

	@Autowired
	private UserController userController;

	/**
	 * Test method for {@link org.reap.rbac.web.UserController#find(int, int, QueryUserSpec)}.
	 */
	@Test
	public void find() {
		QueryUserSpec spec = new QueryUserSpec();
		spec.setUsername("user000000000");
		spec.setEmail("test.com");
		spec.setPhoneNo("189");
		spec.setName("用户");
		Result<Page<User>> result = userController.find(0, 2, spec);
		Page<User> page = result.getPayload();
		assertEquals(0, page.getNumber());
		assertEquals(2, page.getSize());
		assertEquals(3, page.getTotalElements());
		assertEquals(2, page.getTotalPages());
		assertEquals(2, page.getContent().size());
	}
}
