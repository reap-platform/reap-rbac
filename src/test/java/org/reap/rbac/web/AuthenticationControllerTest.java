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
import org.reap.CoreException;
import org.reap.rbac.common.ErrorCodes;
import org.reap.rbac.domain.User;
import org.reap.rbac.web.AuthenticationController;
import org.reap.support.Result;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * 
 * @author 7cat
 * @since 1.0
 */
public class AuthenticationControllerTest extends BaseTest {

	@Autowired
	private AuthenticationController authenticationController;

	@Test
	public void testAuthentication() {
		Result<User> success = authenticationController.authentication("user0000000001", "user0000000001");
		assertTrue(success.isSuccess());
		assertNotNull(success.getPayload().getOrg());
		assertNotNull(success.getPayload().getRoles());
		try {
			authenticationController.authentication("user0000000001", "user0000000002");
			fail();
		}
		catch (CoreException e) {
			assertEquals(ErrorCodes.USERNAME_OR_PASSWORD_IS_INCORRECT, e.getCode());
		}
	}

}
