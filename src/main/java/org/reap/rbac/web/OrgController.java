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

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.reap.rbac.common.Constants;
import org.reap.rbac.common.ErrorCodes;
import org.reap.rbac.domain.Org;
import org.reap.rbac.domain.OrgRepository;
import org.reap.rbac.domain.User;
import org.reap.rbac.domain.UserRepository;
import org.reap.rbac.vo.QueryOrgSpec;
import org.reap.support.DefaultResult;
import org.reap.support.Result;
import org.reap.util.Assert;
import org.reap.util.FunctionalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 机构维护相关的服务.
 * 
 * @author 7cat
 * @since 1.0
 */
@RestController
public class OrgController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrgRepository orgRepository;

	@RequestMapping(path = "/org/{id}", method = RequestMethod.POST)
	public Result<Org> create(@RequestBody Org org, @PathVariable String id) {
		validate(org);
		Org parent = FunctionalUtils.orElseThrow(orgRepository.findById(id), ErrorCodes.ORG_NOT_EXIST);
		org.setParent(parent);
		parent.setLeaf("N");
		orgRepository.save(parent);
		org.setCreateTime(new Date());
		return DefaultResult.newResult(orgRepository.save(org));
	}

	@RequestMapping(path = "/org", method = RequestMethod.POST)
	public Result<Org> createRootOrg(@RequestBody Org org) {
		validate(org);
		org.setCreateTime(new Date());
		return DefaultResult.newResult(orgRepository.save(org));
	}

	@RequestMapping(path = "/org/{id}", method = RequestMethod.DELETE)
	@Transactional
	public Result<?> create(@PathVariable String id) {
		Org org = FunctionalUtils.orElseThrow(orgRepository.findById(id), ErrorCodes.ORG_NOT_EXIST);
		if ("N".equalsIgnoreCase(org.getLeaf())) {
			List<Org> childs = orgRepository.findByParent(org).stream().map((o)->{
				o.setParent(org.getParent()!=null? org.getParent(): null);
				return o;
			}).collect(Collectors.toList());
			orgRepository.saveAll(childs);
		}
		orgRepository.delete(org);
		return DefaultResult.newResult();
	}

	@RequestMapping(path = "/org", method = RequestMethod.PUT)
	public Result<Org> update(@RequestBody Org org) {
		Org persisted = FunctionalUtils.orElseThrow(orgRepository.findById(org.getId()), ErrorCodes.ORG_NOT_EXIST);
		persisted.setName(org.getName());
		persisted.setRemark(org.getRemark());
		return DefaultResult.newResult(orgRepository.save(org));
	}

	@RequestMapping(path = "/org/{id}/user", method = RequestMethod.GET)
	public Result<Page<User>> findUserByOrgId(@PathVariable String id,
			@RequestParam(defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size) {
		Org org = FunctionalUtils.orElseThrow(orgRepository.findById(id), ErrorCodes.ORG_NOT_EXIST);
		return DefaultResult.newResult(userRepository.findByOrg(org, PageRequest.of(page, size)));
	}

	@RequestMapping(path = "/orgs", method = RequestMethod.GET)
	public Result<Page<Org>> find(@RequestParam(defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size, QueryOrgSpec spec) {
		return DefaultResult.newResult(orgRepository.findAll(spec.toSpecification(), PageRequest.of(page, size)));
	}

	@RequestMapping(path = "/orgs/tree", method = RequestMethod.GET)
	public Result<List<Org>> orgsTree() {
		List<Org> orgs = orgRepository.findAll();
		Map<String, Org> orgMapping = orgs.stream().collect(Collectors.toMap(Org::getId, o -> o));
		for (Org o : orgs) {
			if (o.getParent() != null) {
				orgMapping.get(o.getParent().getId()).addChild(o);
			}
		}
		return DefaultResult.newResult(
				orgs.stream().filter((o) -> o.getParent() == null).sorted(Comparator.comparing(Org::getCode)).collect(
						Collectors.toList()));
	}

	@RequestMapping(path = "/org/{id}", method = RequestMethod.GET)
	public Result<Org> findOne(@PathVariable String id) {
		return DefaultResult.newResult(
				FunctionalUtils.orElseThrow(orgRepository.findById(id), ErrorCodes.ORG_NOT_EXIST));
	}

	private void validate(Org org) {
		Assert.isTrue(!orgRepository.existsByCode(org.getCode()), ErrorCodes.DUPLICATED_ORG_CODE);
		Assert.isTrue(!orgRepository.existsByName(org.getName()), ErrorCodes.DUPLICATED_ORG_NAME);
	}
}
