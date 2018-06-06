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
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.reap.rbac.common.Fields;
import org.reap.rbac.domain.Function;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * 功能模糊查询值对象.
 * 
 * @author 7cat
 * @since 1.0
 */
public class QueryFuncSpec {

	private String serviceId;

	private String code;

	private String name;

	private String action;

	private String remark;

	public Specification<Function> toSpecification() {
		return new Specification<Function>() {

			@Override
			public Predicate toPredicate(Root<Function> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

				List<Predicate> predicate = new ArrayList<>();
				if (StringUtils.hasText(getServiceId())) {
					predicate.add(cb.like(root.get(Fields.SERVICE_ID), "%" + getServiceId() + "%"));
				}
				if (StringUtils.hasText(getName())) {
					predicate.add(cb.like(root.get(Fields.NAME), "%" + getName() + "%"));
				}
				if (StringUtils.hasText(getCode())) {
					predicate.add(cb.like(root.get(Fields.CODE), "%" + getCode() + "%"));
				}
				if (StringUtils.hasText(getAction())) {
					predicate.add(cb.like(root.get(Fields.ACTION), "%" + getAction() + "%"));
				}
				if (StringUtils.hasText(getRemark())) {
					predicate.add(cb.like(root.get(Fields.REMARK), "%" + getRemark() + "%"));
				}
				query.orderBy(cb.asc(root.get(Fields.CODE)));
				query.where(predicate.toArray(new Predicate[predicate.size()]));
				return query.getRestriction();
			}

		};
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
