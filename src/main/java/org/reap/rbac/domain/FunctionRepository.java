package org.reap.rbac.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FunctionRepository extends JpaRepository<Function, String>, JpaSpecificationExecutor<Function> {
	boolean existsByCode(String code);

	boolean existsByName(String name);
}
