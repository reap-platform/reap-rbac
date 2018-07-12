
package org.reap.rbac.domain;

import java.util.List;

import org.springframework.data.mybatis.annotations.Statement;
import org.springframework.data.mybatis.repository.support.MybatisRepository;
import org.springframework.data.repository.query.Param;

public interface FunctionRepository extends MybatisRepository<Function, String> {

	boolean existsByCode(String code);

	boolean existsByName(String name);

	@Statement
	List<Function> findByRoleId(@Param("roleId") String roleId);
}
