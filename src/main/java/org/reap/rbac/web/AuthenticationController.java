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

import java.util.Optional;

import org.reap.rbac.common.ErrorCodes;
import org.reap.rbac.domain.User;
import org.reap.rbac.domain.UserRepository;
import org.reap.rbac.util.MD5Utils;
import org.reap.support.DefaultResult;
import org.reap.support.Result;
import org.reap.util.FunctionalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用于用户认证的 Controller 密码使用盐值进行 MD5 加密.
 * 
 * @author 7cat
 * @since 1.0
 */
@RestController
public class AuthenticationController {

	@Autowired
	private UserRepository userRepository;

	@Value("${password.md5.salt}")
	private String salt = "";

	@RequestMapping(path = "/authentication", method = RequestMethod.POST)
	public Result<User> authentication(@RequestParam String username, @RequestParam String password) {
		Optional<User> user = userRepository.findOneByUsernameAndPassword(username, MD5Utils.encode(password, salt));
		return DefaultResult.newResult(FunctionalUtils.orElseThrow(user, ErrorCodes.USERNAME_OR_PASSWORD_IS_INCORRECT));
	}
}
