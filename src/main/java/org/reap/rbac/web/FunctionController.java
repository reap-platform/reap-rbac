
package org.reap.rbac.web;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.reap.rbac.common.Constants;
import org.reap.rbac.common.ErrorCodes;
import org.reap.rbac.common.Fields;
import org.reap.rbac.domain.BusinessTypeFunction;
import org.reap.rbac.domain.BusinessTypeFunctionRepository;
import org.reap.rbac.domain.Function;
import org.reap.rbac.domain.FunctionRepository;
import org.reap.rbac.domain.RoleFunction;
import org.reap.rbac.domain.RoleFunctionRepository;
import org.reap.support.Result;
import org.reap.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
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
	private RoleFunctionRepository roleFunctionRepository;
	
	@Autowired
	private BusinessTypeFunctionRepository businessTypeFunctionRepository;
	

	/** @apiDefine Function 功能维护 */

	/**
	 * @api {post} /function/role/{id} 分配功能
	 * @apiDescription 为指定的岗位分配功能
	 * @apiName allocateFunctions
	 * @apiGroup Function
	 * @apiParam (PathVariable) {String} id 角色 id
	 * @apiParam (Body) {String[]} functionIds 被分配的功能 id 列表
	 * @apiSuccess (Success) {Boolean} success 业务成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/function/role/{id}", method = RequestMethod.POST)
	@Transactional
	public Result<?> allocateFunctions(@PathVariable String id, @RequestBody String[] functionIds) {
		roleFunctionRepository.deleteById_RoleId(id);
		roleFunctionRepository.insertAll(
				Arrays.asList(functionIds).stream().map(functionId -> RoleFunction.of(id, functionId)).collect(
						Collectors.toList()));
		return Result.newResult();
	}

	
	@RequestMapping(path = "/function/businessType/{id}", method = RequestMethod.POST)
	@Transactional
	public Result<?> allocateBusinessTypeFunctions(@PathVariable String id, @RequestBody String[] functionIds) {
		businessTypeFunctionRepository.deleteById_BusinessTypeId(id);
		businessTypeFunctionRepository.insertAll(
				Arrays.asList(functionIds).stream().map(functionId -> BusinessTypeFunction.of(id, functionId)).collect(
						Collectors.toList()));
		return Result.newResult();
	}

	
	/**
	 * @api {get} /functions/all 所有功能
	 * @apiName functionsAll
	 * @apiGroup Function
	 * @apiSuccess (Success) {Boolean} success 业务成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiSuccess (Success) {Object[]} payload 功能列表
	 * @apiSuccess (Success) {String} payload.id 功能 id
	 * @apiSuccess (Success) {String} payload.serviceId 归属系统
	 * @apiSuccess (Success) {String} payload.code 功能码
	 * @apiSuccess (Success) {String} payload.name 功能名称
	 * @apiSuccess (Success) {String} payload.type 类型 'M' 菜单 'O' 操作
	 * @apiSuccess (Success) {String} payload.action 动作
	 * @apiSuccess (Success) {String} payload.remark 备注
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/functions/all", method = RequestMethod.GET)
	public Result<List<Function>> findAll() {
		return Result.newResult(functionRepository.findAll());
	}

	/**
	 * @api {post} /function 创建功能
	 * @apiName createFunction
	 * @apiGroup Function
	 * @apiParam (Body) {String} serviceId 归属系统
	 * @apiParam (Body) {String} code 功能码
	 * @apiParam (Body) {String} name 功能名称
	 * @apiParam (Body) {String} type 类型 'M' 菜单 'O' 操作
	 * @apiParam (Body) {String} action 动作
	 * @apiParam (Body) {String} remark 备注
	 * @apiSuccess (Success) {Boolean} success 业务成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiSuccess (Success) {Object} payload 创建的功能
	 * @apiSuccess (Success) {String} payload.id 功能 id
	 * @apiSuccess (Success) {String} payload.serviceId 归属系统
	 * @apiSuccess (Success) {String} payload.code 功能码
	 * @apiSuccess (Success) {String} payload.name 功能名称
	 * @apiSuccess (Success) {String} payload.type 类型 'M' 菜单 'O' 操作
	 * @apiSuccess (Success) {String} payload.action 动作
	 * @apiSuccess (Success) {String} payload.remark 备注
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/function", method = RequestMethod.POST)
	public Result<Function> create(@RequestBody Function function) {
		validate(function);
		return Result.newResult(functionRepository.save(function));
	}

	/**
	 * @api {delete} /route/{id} 删除功能
	 * @apiName deleteFunction
	 * @apiGroup Function
	 * @apiParam (PathVariable) {String} id 参数 id
	 * @apiSuccess (Success) {Boolean} success 业务成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/function/{id}", method = RequestMethod.DELETE)
	@Transactional
	public Result<?> delete(@PathVariable String id) {
		roleFunctionRepository.deleteById_FunctionId(id);
		functionRepository.deleteById(id);
		return Result.newResult();
	}

	/**
	 * @api {put} /function 更新功能
	 * @apiName updateFunction
	 * @apiGroup Function
	 * @apiParam (Body) {String} id 功能 id
	 * @apiParam (Body) {String} serviceId 归属系统
	 * @apiParam (Body) {String} code 功能码
	 * @apiParam (Body) {String} name 功能名称
	 * @apiParam (Body) {String} type 功能类型
	 * @apiParam (Body) {String} action 动作
	 * @apiParam (Body) {String} remark 备注
	 * @apiSuccess (Success) {Boolean} success 业务成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiSuccess (Success) {Object} payload 功能
	 * @apiSuccess (Success) {String} payload.id 功能 id
	 * @apiSuccess (Success) {String} payload.serviceId 归属系统
	 * @apiSuccess (Success) {String} payload.code 功能码
	 * @apiSuccess (Success) {String} payload.name 功能名称
	 * @apiSuccess (Success) {String} payload.type 类型 'M' 菜单 'O' 操作
	 * @apiSuccess (Success) {String} payload.action 动作
	 * @apiSuccess (Success) {String} payload.remark 备注
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/function", method = RequestMethod.PUT)
	public Result<Function> update(@RequestBody Function function) {
		return Result.newResult(functionRepository.updateIgnoreNull(function));
	}

	/**
	 * @api {get} /functions 查询功能
	 * @apiName queryFunction
	 * @apiGroup Function
	 * @apiParam (QueryString) {Number} [page=0] 页码
	 * @apiParam (QueryString) {Number} [size=10] 每页记录数
	 * @apiParam (QueryString) {String} [serviceId] 归属系统
	 * @apiParam (QueryString) {String} [code] 功能码
	 * @apiParam (QueryString) {String} [name] 功能名称
	 * @apiParam (QueryString) {String} [action] 动作
	 * @apiParam (QueryString) {String} [remark] 备注
	 * @apiSuccess (Success) {Boolean} success 成功标识 <code>true</code>
	 * @apiSuccess (Success) {String} responseCode 响应码 'SC0000'
	 * @apiSuccess (Success) {Object} payload 响应数据
	 * @apiSuccess (Success) {Number} payload.totalPages 总页数
	 * @apiSuccess (Success) {Number} payload.totalElements 总记录数
	 * @apiSuccess (Success) {Number} payload.numberOfElements 当前记录数
	 * @apiSuccess (Success) {Object[]} payload.content 路由列表
	 * @apiSuccess (Success) {String} payload.content.id 路由 id
	 * @apiSuccess (Success) {String} payload.content.serviceId 归属系统
	 * @apiSuccess (Success) {String} payload.content.code 功能码
	 * @apiSuccess (Success) {String} payload.content.name 功能名称
	 * @apiSuccess (Success) {String} payload.content.action 动作
	 * @apiSuccess (Success) {String} payload.content.remark 备注
	 * @apiError (Error) {Boolean} success 业务成功标识 <code>false</code>
	 * @apiError (Error) {String} responseCode 错误码
	 * @apiError (Error) {String} responseMessage 错误消息
	 */
	@RequestMapping(path = "/functions", method = RequestMethod.GET)
	public Result<Page<Function>> find(@RequestParam(defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size, Function spec) {
		Example<Function> example = Example.of(spec,
				ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING));
		return Result.newResult(
				functionRepository.findAll(example, PageRequest.of(page, size, Sort.by(Fields.CODE))));
	}

	private void validate(Function func) {
		Assert.isTrue(!functionRepository.existsByCode(func.getCode()), ErrorCodes.DUPLICATED_FUNC_CODE);
		Assert.isTrue(!functionRepository.existsByName(func.getName()), ErrorCodes.DUPLICATED_FUNC_NAME);
	}
}
