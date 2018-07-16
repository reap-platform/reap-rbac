
package org.reap.rbac.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.reap.rbac.common.Constants;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(schema = Constants.RBAC_SCHEMA)
public class BusinessType {

	@Id
	@GeneratedValue
	private String id;

	private String name;

	private String remark;

	@CreatedDate
	private Date createdTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
