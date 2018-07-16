
package org.reap.rbac.domain;

import java.util.List;

import org.springframework.data.mybatis.annotations.Statement;
import org.springframework.data.mybatis.repository.support.MybatisRepository;
import org.springframework.data.repository.query.Param;

public interface BusinessTypeRepository extends MybatisRepository<BusinessType, String> {

	boolean existsByName(String name);
	
	@Statement
	List<Role> findByUserId(@Param("userId") String userId);
	
}

