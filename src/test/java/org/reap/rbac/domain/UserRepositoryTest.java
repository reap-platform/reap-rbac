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

package org.reap.rbac.domain;

import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;
import org.reap.BaseTest;
import org.reap.rbac.domain.User;
import org.reap.rbac.domain.UserRepository;
import org.reap.rbac.vo.QueryUserSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.Assert.*;

/**
 * 
 * @author 7cat
 * @since 1.0
 */
public class UserRepositoryTest extends BaseTest {

	@Autowired
	private UserRepository userRepository;
	
	/**
	 * Test method for {@link org.springframework.data.repository.CrudRepository#findById(java.lang.Object)}.
	 */
	@Test
	public void testFindAll() {
		QueryUserSpec spec = new QueryUserSpec();
		Page<User> page = userRepository.findBySpecification(spec, PageRequest.of(0, 10));
		assertEquals(3, page.getContent().size());
		Map<String, User> userMapping = page.getContent().stream().collect(Collectors.toMap(User::getId, user -> user));
		assertEquals(2, userMapping.get("0000000001").getRoles().size());
		assertEquals(1, userMapping.get("0000000002").getRoles().size());
		assertEquals(1, userMapping.get("0000000003").getRoles().size());
		userMapping.get("0000000001").getRoles().clear();
		userRepository.saveAll(page.getContent());
		page = userRepository.findBySpecification(spec, PageRequest.of(0, 10));
		userMapping = page.getContent().stream().collect(Collectors.toMap(User::getId, user -> user));
		assertEquals(0, userMapping.get("0000000001").getRoles().size());
		assertEquals(1, userMapping.get("0000000002").getRoles().size());
		assertEquals(1, userMapping.get("0000000003").getRoles().size());
	}

}
