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

package org.reap.rbac.vo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.reap.rbac.common.Fields;
import org.reap.rbac.domain.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * 用户模糊查询值对象.
 * 
 * @author 7cat
 * @since 1.0
 */
public class QueryUserSpec {

	private String[] orgIds;

	private String username;

	private String name;

	private String email;

	private String phoneNo;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public Specification<User> toSpecification() {
		return new Specification<User>() {

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicate = new ArrayList<>();
				if (query.getResultType().equals(User.class)) {
					root.fetch(Fields.ORG, JoinType.LEFT);
					root.fetch(Fields.ROLES, JoinType.LEFT);
				}

				if (getOrgIds() != null) {
					predicate.add(cb.in(root.get(Fields.ORG).get(Fields.ID)).value(Arrays.asList(getOrgIds())));
				}

				if (StringUtils.hasText(getUsername())) {
					predicate.add(cb.like(root.get(Fields.USERNAME), "%" + getUsername() + "%"));
				}
				if (StringUtils.hasText(getName())) {
					predicate.add(cb.like(root.get(Fields.NAME), "%" + getName() + "%"));
				}
				if (StringUtils.hasText(getEmail())) {
					predicate.add(cb.like(root.get(Fields.EMAIL), "%" + getEmail() + "%"));
				}
				if (StringUtils.hasText(getPhoneNo())) {
					predicate.add(cb.like(root.get(Fields.PHONE_NO), "%" + getPhoneNo() + "%"));
				}
				query.where(predicate.toArray(new Predicate[predicate.size()]));
				return query.getRestriction();

			}
		};
	}

	public String[] getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(String[] orgIds) {
		this.orgIds = orgIds;
	}
}
