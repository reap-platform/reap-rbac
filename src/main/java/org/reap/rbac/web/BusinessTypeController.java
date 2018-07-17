
package org.reap.rbac.web;

import java.util.List;

import org.reap.rbac.common.Constants;
import org.reap.rbac.common.ErrorCodes;
import org.reap.rbac.domain.BusinessType;
import org.reap.rbac.domain.BusinessTypeFunctionRepository;
import org.reap.rbac.domain.BusinessTypeRepository;
import org.reap.rbac.domain.Function;
import org.reap.rbac.domain.FunctionRepository;
import org.reap.support.Result;
import org.reap.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BusinessTypeController {

	@Autowired
	private BusinessTypeRepository businessTypeRepository;
	
	@Autowired
	private BusinessTypeFunctionRepository businessTypeFunctionRepository;
	
	@Autowired
	private FunctionRepository functionRepository;
	

	@RequestMapping(path = "/businessType", method = RequestMethod.POST)
	public Result<BusinessType> create(@RequestBody BusinessType entity) {
		Assert.isTrue(!businessTypeRepository.existsByName(entity.getName()),
				ErrorCodes.DUPLICATED_BUSINESS_TYPE);
		return Result.newResult(businessTypeRepository.save(entity));
	}

	@RequestMapping(path = "/businessType", method = RequestMethod.PUT)
	public Result<BusinessType> update(@RequestBody BusinessType function) {
		return Result.newResult(businessTypeRepository.updateIgnoreNull(function));
	}

	@RequestMapping(path = "/businessTypes", method = RequestMethod.GET)
	public Result<Page<BusinessType>> search(@RequestParam(defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size, BusinessType spec) {
		Example<BusinessType> example = Example.of(spec,
				ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING));
		return Result.newResult(businessTypeRepository.findAll(example, PageRequest.of(page, size)));
	}

	@RequestMapping(path = "/businessTypes/all", method = RequestMethod.GET)
	public Result<List<BusinessType>> findAll() {
		return Result.newResult(businessTypeRepository.findAll());
	}

	@RequestMapping(path = "/businessType/{id}", method = RequestMethod.GET)
	public Result<BusinessType> get(@PathVariable String id) {
		return Result.newResult(businessTypeRepository.findById(id).get());
	}

	@RequestMapping(path = "/businessType/{id}", method = RequestMethod.DELETE)
	@Transactional
	public Result<?> delete(@PathVariable String id) {
		businessTypeRepository.deleteById(id);
		businessTypeFunctionRepository.deleteById_BusinessTypeId(id);
		return Result.newResult();
	}
	
	@RequestMapping(path = "/businessType/{id}/functions", method = RequestMethod.GET)
	public Result<List<Function>> findFunctions(@PathVariable String id) {
		return Result.newResult(functionRepository.findByBusinessTypeId(id));
	}
}
