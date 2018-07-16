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

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.reap.rbac.common.Constants;

/**
 * 
 * @author 7cat
 * @since 1.0
 */
@Entity
@Table(schema = Constants.RBAC_SCHEMA)
public class BusinessTypeFunction {

	@EmbeddedId
	private ID id;

	public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}

	public static final BusinessTypeFunction of(String businessTypeId, String functionId) {
		BusinessTypeFunction roleFunction = new BusinessTypeFunction();
		ID id = new ID();
		id.setBusinessTypeId(businessTypeId);
		id.setFunctionId(functionId);
		roleFunction.setId(id);
		return roleFunction;
	}

	public static class ID implements Serializable {

		private String businessTypeId;

		private String functionId;

		public String getBusinessTypeId() {
			return businessTypeId;
		}

		public void setBusinessTypeId(String businessTypeId) {
			this.businessTypeId = businessTypeId;
		}

		public String getFunctionId() {
			return functionId;
		}

		public void setFunctionId(String functionId) {
			this.functionId = functionId;
		}
	}
}
