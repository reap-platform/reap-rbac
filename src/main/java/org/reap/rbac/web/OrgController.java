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

	/** @apiDefine Org 机构维护 */
	
	/**
	 * @api {post} /org 创建机构
	 * @apiDescription 创建归属在指定父机构的子机构
	 * @apiName createOrg 创建机构
	 * @apiGroup Org
	 * @apiParam (PathVariable) {String} id 父机构 id
	 * @apiParam (Body) {String} name 机构名
	 * @apiParam (Body) {String} code 机构代码
	 * @apiSuccess (Success) {Boolean} success 业务成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiSuccess (Success) {Object} payload 新建的机构
	 * @apiSuccess (Success) {String} payload.id 机构 id
	 * @apiSuccess (Success) {String} payload.name 机构名
	 * @apiSuccess (Success) {String} payload.code 功能代码
	 * @apiSuccess (Success) {String} payload.createTime 创建时间
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/org/{id}", method = RequestMethod.POST)
	public Result<Org> create(@RequestBody Org org, @PathVariable String id) {
		validate(org);
		Org parent = FunctionalUtils.orElseThrow(orgRepository.findById(id), ErrorCodes.ORG_NOT_EXIST);
		org.setParent(parent);
		parent.setLeaf(Constants.LEAF_FLAG_N);
		orgRepository.save(parent);
		org.setCreateTime(new Date());
		return DefaultResult.newResult(orgRepository.save(org));
	}

	/**
	 * @api {post} /org 创建机构
	 * @apiName createRootOrg 创建机构
	 * @apiGroup Org
	 * @apiParam (Body) {String} name 机构名
	 * @apiParam (Body) {String} code 机构代码
	 * @apiSuccess (Success) {Boolean} success 业务成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiSuccess (Success) {Object} payload 新建的机构
	 * @apiSuccess (Success) {String} payload.id 机构 id
	 * @apiSuccess (Success) {String} payload.name 机构名
	 * @apiSuccess (Success) {String} payload.code 功能代码
	 * @apiSuccess (Success) {String} payload.createTime 创建时间
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/org", method = RequestMethod.POST)
	public Result<Org> createRootOrg(@RequestBody Org org) {
		validate(org);
		org.setCreateTime(new Date());
		return DefaultResult.newResult(orgRepository.save(org));
	}

	/**
	 * @api {delete} /org/{id} 删除功能
	 * @apiName deleteOrg
	 * @apiGroup Org
	 * @apiParam (PathVariable) {String} id 机构 id
	 * @apiSuccess (Success) {Boolean} success 业务成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/org/{id}", method = RequestMethod.DELETE)
	@Transactional
	public Result<?> create(@PathVariable String id) {
		Org org = FunctionalUtils.orElseThrow(orgRepository.findById(id), ErrorCodes.ORG_NOT_EXIST);
		if (Constants.LEAF_FLAG_N.equalsIgnoreCase(org.getLeaf())) {
			List<Org> childs = orgRepository.findByParent(org).stream().map((o) -> {
				o.setParent(org.getParent() != null ? org.getParent() : null);
				return o;
			}).collect(Collectors.toList());
			orgRepository.saveAll(childs);
		}
		orgRepository.delete(org);
		return DefaultResult.newResult();
	}

	/**
	 * @api {put} /org 更新机构
	 * @apiName updateOrg
	 * @apiGroup Org
	 * @apiParam (Body) {String} id 功能 id
	 * @apiParam (Body) {String} name 机构名称
	 * @apiParam (Body) {String} code 机构代码
	 * @apiSuccess (Success) {Boolean} success 业务成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiSuccess (Success) {Object} payload 机构
	 * @apiSuccess (Success) {String} payload.id 机构 id
	 * @apiSuccess (Success) {String} payload.name 机构名称
	 * @apiSuccess (Success) {String} payload.code 机构代码
	 * @apiSuccess (Success) {String} payload.createTime 创建时间
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/org", method = RequestMethod.PUT)
	public Result<Org> update(@RequestBody Org org) {
		Org persisted = FunctionalUtils.orElseThrow(orgRepository.findById(org.getId()), ErrorCodes.ORG_NOT_EXIST);
		persisted.setName(org.getName());
		persisted.setRemark(org.getRemark());
		return DefaultResult.newResult(orgRepository.save(org));
	}

	/**
	 * @api {get} /org/{id}/user 查询指定机构的用户
	 * @apiName findUserByOrgId
	 * @apiGroup Org
	 * @apiParam (PathVariable) {String} id 机构 id
	 * @apiParam (QueryString) {Number} [page=0] 页码
	 * @apiParam (QueryString) {Number} [size=10] 每页记录数
	 * @apiSuccess (Success) {Boolean} success 业务成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiSuccess (Success) {Object} payload 响应数据
	 * @apiSuccess (Success) {Number} payload.totalPages 总页数
	 * @apiSuccess (Success) {Number} payload.totalElements 总记录数
	 * @apiSuccess (Success) {Number} payload.numberOfElements 当前记录数
	 * @apiSuccess (Success) {Object[]} payload.content 路由列表
	 * @apiSuccess (Success) {String} payload.content.username 用户名
	 * @apiSuccess (Success) {String} payload.content.name 姓名
	 * @apiSuccess (Success) {String} payload.content.email 邮箱
	 * @apiSuccess (Success) {String} payload.content.phoneNo 手机
	 * @apiSuccess (Success) {String} payload.content.createTime 创建时间
	 * @apiSuccess (Success) {Object} payload.content.org 归属机构
	 * @apiSuccess (Success) {String} payload.content.org.id 机构 id
	 * @apiSuccess (Success) {String} payload.content.org.name 机构名称
	 * @apiSuccess (Success) {String} payload.content.org.code 机构代码
	 * @apiSuccess (Success) {String} payload.content.org.createTime 创建时间
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */	
	@RequestMapping(path = "/org/{id}/user", method = RequestMethod.GET)
	public Result<Page<User>> findUserByOrgId(@PathVariable String id,
			@RequestParam(defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size) {
		Org org = FunctionalUtils.orElseThrow(orgRepository.findById(id), ErrorCodes.ORG_NOT_EXIST);
		return DefaultResult.newResult(userRepository.findByOrg(org, PageRequest.of(page, size)));
	}

	/**
	 * @api {get} /orgs 查询机构
	 * @apiName queryOrg
	 * @apiGroup Org
	 * @apiParam (QueryString) {Number} [page=0] 页码
	 * @apiParam (QueryString) {Number} [size=10] 每页记录数
	 * @apiParam (QueryString) {String} [code] 机构代码
	 * @apiParam (QueryString) {String} [name] 机构名称
	 * @apiSuccess (Success) {Boolean} success 成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiSuccess (Success) {Object} payload 响应数据
	 * @apiSuccess (Success) {Number} payload.totalPages 总页数
	 * @apiSuccess (Success) {Number} payload.totalElements 总记录数
	 * @apiSuccess (Success) {Number} payload.numberOfElements 当前记录数
	 * @apiSuccess (Success) {Object[]} payload.content 机构列表
	 * @apiSuccess (Success) {String} payload.content.id 机构 id
	 * @apiSuccess (Success) {String} payload.content.code 机构代码
	 * @apiSuccess (Success) {String} payload.content.name 机构名称
	 * @apiSuccess (Success) {String} payload.content.createTime 创建时间
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/orgs", method = RequestMethod.GET)
	public Result<Page<Org>> find(@RequestParam(defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size, QueryOrgSpec spec) {
		return DefaultResult.newResult(orgRepository.findAll(spec.toSpecification(), PageRequest.of(page, size)));
	}

	/**
	 * @api {get} /orgs/tree 查询机构树
	 * 
	 * @apiName orgTree
	 * @apiGroup Org
	 * @apiSuccess (Success) {Boolean} success 成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiSuccess (Success) {Object} payload 机构列表
	 * @apiSuccess (Success) {Number} payload.totalPages 总页数
	 * @apiSuccess (Success) {Number} payload.totalElements 总记录数
	 * @apiSuccess (Success) {Number} payload.numberOfElements 当前记录数
	 * @apiSuccess (Success) {Object[]} payload.content 机构列表
	 * @apiSuccess (Success) {String} payload.content.id 机构 id
	 * @apiSuccess (Success) {String} payload.content.code 机构代码
	 * @apiSuccess (Success) {String} payload.content.name 机构名称
	 * @apiSuccess (Success) {String} payload.content.createTime 创建时间
	 * @apiSuccess (Success) {Object[]} payload.content.childs 子机构
	 * @apiSuccess (Success) {String} payload.content.childs.id 子机构 id
	 * @apiSuccess (Success) {String} payload.content.childs.code 子机构代码
	 * @apiSuccess (Success) {String} payload.content.childs.name 子机构名称
	 * @apiSuccess (Success) {String} payload.content.childs.createTime 子机构创建时间
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
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

	/**
	 * @api {get} org/{id} 查询 Org
	 * @apiName getOrgById
	 * @apiGroup Org
	 * @apiSuccess (Success) {Boolean} success 成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiSuccess (Success) {Object} payload 机构
	 * @apiSuccess (Success) {String} payload.id 机构 id
	 * @apiSuccess (Success) {String} payload.code 机构代码
	 * @apiSuccess (Success) {String} payload.name 机构名称
	 * @apiSuccess (Success) {String} payload.createTime 创建时间
	 * @apiSuccess (Success) {Object[]} payload.content.parent 父机构
	 * @apiSuccess (Success) {String} payload.content.parent.id 父机构 id
	 * @apiSuccess (Success) {String} payload.content.parent.code 父机构代码
	 * @apiSuccess (Success) {String} payload.content.parent.name 父机构名称
	 * @apiSuccess (Success) {String} payload.content.parent.createTime 父机构创建时间
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
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
