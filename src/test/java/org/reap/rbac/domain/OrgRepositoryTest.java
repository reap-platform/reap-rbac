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

import org.junit.Test;
import org.reap.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.Assert.*;


/**
 * 
 * @author 7cat
 * @since 1.0
 */
public class OrgRepositoryTest extends BaseTest{

	@Autowired
	private OrgRepository orgRepository;
	
	/**
	 * Test method for {@link org.reap.rbac.domain.OrgRepository#findByParentAndNameLike(org.reap.rbac.domain.Org, java.lang.String, org.springframework.data.domain.Pageable)}.
	 */
	@Test
	public void testFindByParentAndNameLike() {
		Org spec = new Org();
		spec.setName("测试");
		spec.setParentId("0000000001");
		Page<Org> page = orgRepository.findAll(Example.of(spec), PageRequest.of(0, 10));
		assertEquals(3, page.getContent().size());
		Page<Org> page2 = orgRepository.findAll(Example.of(spec), PageRequest.of(0, 10));
		assertEquals(7, page2.getContent().size());
	}

}
