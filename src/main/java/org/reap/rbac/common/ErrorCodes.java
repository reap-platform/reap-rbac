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

package org.reap.rbac.common;

/**
 * 集中定义错误码格式如下: 'REAP【RB】【2位子模块】【2位错误码】'
 * 
 * <pre>
 * REAPRB0001
 * </pre>
 * 
 * @author 7cat
 * @since 1.0
 */
public final class ErrorCodes {

	/** 用户或密码错误. */
	public static final String USERNAME_OR_PASSWORD_IS_INCORRECT = "REAPRB0101";

	/** 机构不存在. */
	public static final String ORG_NOT_EXIST = "REAPRB0102";

	/** 用户不存在. */
	public static final String USER_NOT_EXIST = "REAPRB0103";

	/** 机构名重复. */
	public static final String DUPLICATED_ORG_NAME = "REAPRB0104";

	/** 机构代码重复. */
	public static final String DUPLICATED_ORG_CODE = "REAPRB0105";

	/** 用户名重复. */
	public static final String DUPLICATED_USERNAME = "REAPRB0106";

	/** 邮箱重复. */
	public static final String DUPLICATED_EMAIL = "REAPRB0107";

	/** 角色不存在. */
	public static final String ROLE_NOT_EXIST = "REAPRB0108";
	
	/** 功能码不存在. */
	public static final String FUNC_NOT_EXIST = "REAPRB0109";
	
	/** 功能码重复. */
	public static final String DUPLICATED_FUNC_CODE = "REAPRB0110";

	/** 功能名重复. */
	public static final String DUPLICATED_FUNC_NAME = "REAPRB0111";
	

}
