
package org.reap.rbac.web;

import java.util.Arrays;
import java.util.List;

import org.reap.rbac.common.Constants;
import org.reap.rbac.common.ErrorCodes;
import org.reap.rbac.domain.Function;
import org.reap.rbac.domain.FunctionRepository;
import org.reap.rbac.domain.Role;
import org.reap.rbac.domain.RoleRepository;
import org.reap.rbac.vo.QueryFuncSpec;
import org.reap.support.DefaultResult;
import org.reap.support.Result;
import org.reap.util.Assert;
import org.reap.util.FunctionalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FunctionController {

	@Autowired
	private FunctionRepository functionRepository;

	@Autowired
	private RoleRepository roleRepository;
	
	@RequestMapping(path = "/function/role/{id}", method = RequestMethod.POST)
	public Result<?> allocateFunctions(@PathVariable String id, @RequestBody String[] functionIds) {
		Role role = FunctionalUtils.orElseThrow(roleRepository.findById(id), ErrorCodes.ROLE_NOT_EXIST);
		List<Function> functions = functionRepository.findAllById(Arrays.asList(functionIds));
		role.setFunctions(functions);
		roleRepository.save(role);
		return DefaultResult.newResult();
	}
	
	
	@RequestMapping(path = "/functions/all", method = RequestMethod.GET)
	public Result<List<Function>> findAll() {
		return DefaultResult.newResult(functionRepository.findAll());
	}

	@RequestMapping(path = "/function", method = RequestMethod.POST)
	public Result<Function> create(@RequestBody Function org) {
		validate(org);
		return DefaultResult.newResult(functionRepository.save(org));
	}

	@RequestMapping(path = "/function/{id}", method = RequestMethod.DELETE)
	public Result<?> delete(@PathVariable String id) {
		functionRepository.deleteById(id);
		return DefaultResult.newResult();
	}

	@RequestMapping(path = "/function", method = RequestMethod.PUT)
	public Result<Function> update(@RequestBody Function function) {
		return DefaultResult.newResult(functionRepository.save(function));
	}

	@RequestMapping(path = "/functions", method = RequestMethod.GET)
	public Result<Page<Function>> find(@RequestParam(defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size, QueryFuncSpec spec) {
		return DefaultResult.newResult(functionRepository.findAll(spec.toSpecification(), PageRequest.of(page, size)));
	}

	private void validate(Function func) {
		Assert.isTrue(!functionRepository.existsByCode(func.getCode()), ErrorCodes.DUPLICATED_FUNC_CODE);
		Assert.isTrue(!functionRepository.existsByName(func.getName()), ErrorCodes.DUPLICATED_FUNC_NAME);
	}

}
