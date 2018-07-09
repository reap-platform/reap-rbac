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
import org.reap.rbac.domain.Org;
import org.reap.rbac.domain.User;
import org.reap.support.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import static org.junit.Assert.*;

/**
 * 
 * @author 7cat
 * @since 1.0
 */
public class OrgControllerTest extends BaseTest {

	@Autowired
	private OrgController orgController;

	/**
	 * Test method for
	 * {@link org.reap.rbac.web.OrgController#findUserByOrgId(java.lang.String, int, int)}.
	 */
	@Test
	public void findUserByOrgId() {
		Result<Page<User>> result = orgController.findUserByOrgId("0000000121", 0, 2);
		assertTrue(result.isSuccess());
		assertEquals(1, result.getPayload().getContent().size());
	}

	/**
	 * Test method for {@link org.reap.rbac.web.OrgController#find()
	 */
	@Test
	public void find() {
		Org spec = new Org();
		spec.setParentId("0000000001");
		Result<Page<Org>> result = orgController.find(0,20,spec);
		assertEquals(3, result.getPayload().getContent().size());
	}
	
}
