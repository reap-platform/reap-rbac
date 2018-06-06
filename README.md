*reap-rbac* 提供了平台基础数据（用户、机构、角色、功能码）的维护及并将这些数据暴露给平台其他应用使用，对外提供的接口包含两类：

 - API：提供给前端使用的用于维护 rbac 基础数据的接口。 

 - SPI：提供给其他应用的做为服务支撑的服务，比如用户鉴权、功能码查询。

实际应用场景下企业往往有遗留的用户管理系统，这种情况仅需要实现 *SPI* 接口同已有的用户系统集成是一个推荐的用法，这种情况下可能不需要在 *REAP* 中维护用户、机构的信息也不需要关注  *API* 接口。

## 环境准备

### 初始化数据库

执行 *./database/ddl.sql* 及 *./database/init.sql* 进行数据库的初始化工作，默认 *reap-rbac* 监听的端口为 `8070`，可以通过修改 *init.sql* 中的 `server.port` 来调整监听端口。

### 设置环境变量

应用同 *reap-facility* 运行在不同的服务器时需要显式的设定 `REAP_EUREKA_CLIENT_SERVICE_URL_DEFAULT_ZONE` 环境变量：
```bash
export REAP_EUREKA_CLIENT_SERVICE_URL_DEFAULT_ZONE=http://your.reap.facility.ip:8761/eureka/
```

## 应用构建

```bash
# 构建
mvn package

# 运行应用，注意应用启动时需要连接 reap-facility 获取应用参数，请确保 reap-facility 正常启动
java -jar target/reap-rbac-*.jar
```

## SPI 接口

### 用户鉴权
 
根据用户名称及密码进行用户鉴权，成功鉴权返回用户及机构信息。 

* **URL**

    /authentication

* **Method:**
  
    POST 
  
*  **URL Params**

    None

* **Data Params**

```javascript
{
  username: '7upcat',
  password: 'password'
}
```

* **Success Response:**
 
 认证成功后返回用户及其归属机构的信息。 
 
```javascript
  {
    status: 0, // 
    success: true,
    responseCode: 'SC0000',
    payload: {
      id: '用户ID',
      username: '用户名',
      name: '姓名',
      email: '邮箱',
      phoneNo: '电话号码',
      // 姓别：女|男
      gender: 'F|M',
      remark: '备注',
      createTime: '2018-05-24T03:25:28.000+0000', // 用户创新时间
      // 机构信息
      org: {
        id: '机构ID',
        name: '机构名称',
        remark: '备注'
      },
      // 用户所拥有的角色列表
      roles: [
        {
          id: '角色ID',
          name: '角色名称',
          remark: '备注',
          // 角色所拥有的功能列表
          functions: [
            {
              id: '功能ID',
              serviceId: 'reap-rbac',
              code: '功能码', // 规则为 4位系统码+2位模块代码+4位数字 例如:  REAPRB0001
              name: '功能名称',
              type: 'M|O', // 功能码类型 'M' 为菜单、'O' 为操作
              action: '操作', // 对于菜单类型的功能为页面全限定 url
              desc: '功能码描述' 
            }
          ]
        }
      ]
    }
  }
```
 
* **Error Response:**

```javascript
  {
    status: 1,
    success: false,
    responseCode: '错误码',
    responseMessage: '具体错误消息',
  }
```

### 功能查询-所有
 
查询所有的功能，此接口提供给 *reap-portal* 使用，用于将菜单同功能关联。 

* **URL**

    /functions/all

* **Method:**
  
    GET 
  
*  **URL Params**

    None

* **Data Params**
 
    None

* **Success Response:**
 
```javascript
  {
    status: 0, 
    success: true,
    responseCode: 'SC0000',
    payload: [
      {
        id: '功能ID', // 比如 reap-rbac
        serviceId: 'reap-rbac',
        code: '功能码', // 规则为 4位系统码+2位模块代码+4位数字 例如:  REAPRB0001
        name: '功能名称',
        type: 'M|O', // 功能码类型 'M' 为菜单、'O' 为操作
        action: '操作', // 对于菜单类型的功能为页面全限定 url
        desc: '功能码描述' 
      }
    ]
  }
```
 
* **Error Response:**

```javascript
  {
    status: 1,
    success: false,
    responseCode: '错误码',
    responseMessage: '具体错误消息',
  }
```

## API 接口

### 机构

#### 创建机构

创建顶层机构，机构代码、机构名称不可重复，默认创建的机构均为叶子节点。

* **URL**

    /org

* **Method:**
  
    POST 
  
*  **URL Params**

    None

* **Data Params**

```javascript
{
  code: '机构代码',
  name: '机构名称',
  remark: '备注'
}
```

* **Success Response:**
 
 创建成功后，返回新创建的机构实体。 
 
```javascript
  {
    status: 0,
    success: true,
    responseCode: 'SC0000',
    payload: {
      id: '机构ID',
      code: '机构代码',
      name: '机构名称',
      // 创建时间
      createTime: '2018-05-13T12:57:36.285+0000',
      remark: '备注',
      // 是否叶子结点，顶层机构默认创建固定是叶子节点
      leaf:  true,
    }
  }
```
 
* **Error Response:**

```javascript
  {
    status: 1,
    success: false,
    responseCode: '错误码',
    responseMessage: '具体错误消息',
  }
```

#### 创建下级机构

在指定的机构下创建子机构，会修改指定的机构变为非叶子节点。

* **URL**

    /org/:id

* **Method:**
  
    POST 
  
*  **URL Params**

    - id  父机构id  String Required
 
* **Data Params**

```javascript
{
  code: '机构代码',
  name: '机构名称',
  remark: '备注'
}
```

* **Success Response:**
  
 
```javascript
  {
    status: 0,
    success: true,
    responseCode: 'SC0000',
    payload: {
      id: '机构ID',
      code: '机构代码',
      name: '机构名称',
      // 创建时间
      createTime: '2018-05-13T12:57:36.285+0000',
      remark: '备注',
      // 默认创建的机构均为叶子节点
      leaf:  true,
    }
  }
```
 
* **Error Response:**

```javascript
  {
    status: 1,
    success: false,
    responseCode: '错误码',
    responseMessage: '具体错误消息',
  }
```

#### 删除指定机构

删除指定的机构协同会一并删除子机构下的所有的子机构。

* **URL**

    /org/:id

* **Method:**
  
    DELETE 
  
*  **URL Params**

    - id  被删除的机构id  String Required
 
* **Data Params**

    None

* **Success Response:**
  
```javascript
  {
    status: 0,
    success: true,
    responseCode: 'SC0000'
  }
```
 
* **Error Response:**

```javascript
  {
    status: 1,
    success: false,
    responseCode: '错误码',
    responseMessage: '具体错误消息',
  }
```


#### 更新机构信息

* **URL**

    /org/:id

* **Method:**
  
    PUT 
  
*  **URL Params**

    - id  被更新的机构id  String Required
 
* **Data Params**

```javascript
  {
    name: '机构名称',
    code: '机构代码',
    remark: '备注'
  }
```

* **Success Response:**
  
```javascript
  {
    status: 0,
    success: true,
    responseCode: 'SC0000',
    payload: {
      id: '机构ID',
      code: '机构代码',
      name: '机构名称',
      // 创建时间
      createTime: '2018-05-13T12:57:36.285+0000',
      remark: '备注',
      // 是否叶子结点
      leaf:  true/false,
    }
  }
```
 
* **Error Response:**

```javascript
  {
    status: 1,
    success: false,
    responseCode: '错误码',
    responseMessage: '具体错误消息',
  }
```

#### 查找机构列表

* **URL**

    /orgs?page=:page&size=:size&name=:name&code=:code

* **Method:**
  
    GET
  
*  **URL Params**

    - page 页码  Integer  Optional 默认为 0(代表第1页)
    - size 每页条数   Integer Optional 默认为 10 
    - parentOrgId 父机构ID Optional 默认查找所有机构，指定了父机构ID会查询指定父机构下的机构列表
    - name 机构名称  String Optional 模糊匹配
    - code 机构代码 String Optional 模糊匹配
 
* **Data Params**

    None

* **Success Response:**
  
```javascript
  {
    status: 0,
    success: true,
    responseCode: 'SC0000',
    payload: {
      // 机构记录
      content: [
        {
          id: '机构ID',
          name: '机构名称',
          code: '机构代码',
          createTime: '2018-05-13T12:57:36.285+0000',
          remark: '备注',
          leaf: true/false
        }
      ],
    // 总页数
    totalPages: 1,
    // 总记录数
    totalElements: 1,
    // 当前页码
    number: 0,
    // 每页条数
    size: 10,
    // 当前查询记录数
    numberOfElements: 1
  },
  }
```
 
* **Error Response:**

```javascript
  {
    status: 1,
    success: false,
    responseCode: '错误码',
    responseMessage: '具体错误消息',
  }
```

#### 查询机构树

查询所有机构并按照树型结构返回。

* **URL**

    /orgs/tree

* **Method:**
  
    GET
  
*  **URL Params**

    None
 
* **Data Params**

    None

* **Success Response:**
  
```javascript
{
  status: 0,
  responseCode: "SC0000",
  success: true
  payload: [
    {
      // 顶层机构
      id: '机构ID',
      name: '机构名称',
      code: '机构代码',
      createTime: 'yyyy-MM-ddTHH:mm:ss.SSSXXX',
      remark: '备注',
      // 拥有子机构所有非叶子节点
      leaf: false,
      // 子机构
      childs: [
        {
          id: '子机构ID',
          name: '子机构名称',
          code: '子机构代码',
          createTime: 'yyyy-MM-ddTHH:mm:ss.SSSXXX',
          remark: '备注',
          // 子机构数据为空为叶子节点
          childs: [],
          leaf: true
        }
      ]
    }
  ]
}
```
 
* **Error Response:**

```javascript
  {
    status: 1,
    success: false,
    responseCode: '错误码',
    responseMessage: '具体错误消息',
  }
```

### 用户

#### 创建用户

创建归属于指定机构的用户。

* **URL**

    /user/org/:id

* **Method:**
  
    POST
  
*  **URL Params**

    - id  机构id  String Required
 
* **Data Params**

```javascript
{
   username: '用户名',
   name: '用户姓名',
   password: '登录密码',
   email: '邮箱',
   phoneNo: '手机号码',
   // 姓别：男|女
   gender: 'M|F',
   remark: '备注信息'
}
```

* **Success Response:**
  
```javascript
{
  status: 0,
  responseCode: 'SC0000',
  success: true
  payload: {
    id: '用户ID',
    username: '用户名',
    name: '用户姓名',
    email: '邮箱',
    phoneNo: '手机号码',
    // 姓别：男|女
    gender: 'M|F',
    createTime: 'yyyy-MM-ddTHH:mm:ss.SSSXXX',
    remark: '备注信息',
    // 用户归属的机构信息
    org: {
      id: '归属机构ID',
      name: '归属机构名称',
      code: '归属机构代码',
      createTime: 'yyyy-MM-ddTHH:mm:ss.SSSXXX',
      remark: '备注',
      childs: [],
      leaf: true
    },
    // 默认创建的用户的角色为空
    roles: []
  }
}
```
 
* **Error Response:**

```javascript
  {
    status: 1,
    success: false,
    responseCode: '错误码',
    responseMessage: '具体错误消息',
  }
```

#### 更新用户

更新用户信息。

* **URL**

    /user

* **Method:**
  
    PUT
  
*  **URL Params**

    None
 
* **Data Params**

```javascript
{
   id: '用户id',
   username: '用户名',
   name: '用户姓名',
   email: '邮箱',
   phoneNo: '手机号码',
   // 姓别：男|女
   gender: 'M|F',
   remark: '备注信息'
}
```

* **Success Response:**
  
```javascript
{
  status: 0,
  responseCode: 'SC0000',
  success: true
  payload: {
    id: '用户ID',
    username: '用户名',
    name: '用户姓名',
    email: '邮箱',
    phoneNo: '手机号码',
    // 姓别：男|女
    gender: 'M|F',
    createTime: 'yyyy-MM-ddTHH:mm:ss.SSSXXX',
    remark: '备注',
    // 用户归属的机构信息
    org: {
      id: '机构ID',
      name: '机构名称',
      code: '机构代码',
      createTime: 'yyyy-MM-ddTHH:mm:ss.SSSXXX',
      remark: '备注',
      childs: [],
      leaf: true
    },
    roles: [
      {
        id: '角色ID',
        createTime: 'yyyy-MM-ddTHH:mm:ss.SSSXXX',
        name: '角色名称',
        remark: '角色备注'
        // 角色的功能列表
        functions: [
          {
            id: '功能ID',
            serviceId: '功能归属系统',
            code: '功能码',
            name: '功能名称',
            type: 'M/O', //功能类型
            action: '动作',
            remark: '备注'
          }
        ]
      }
    ]
  }
}
```
 
* **Error Response:**

```javascript
  {
    status: 1,
    success: false,
    responseCode: '错误码',
    responseMessage: '具体错误消息',
  }
```

#### 删除用户

删除指定 id 的用户。

* **URL**

    /user/:id

* **Method:**
  
    DELETE
  
*  **URL Params**

    - id 用户id  String  Required 
 
* **Data Params**

    None

* **Success Response:**
  
```javascript
{
  status: 0,
  responseCode: 'SC0000',
  success: true
}
```
 
* **Error Response:**

```javascript
  {
    status: 1,
    success: false,
    responseCode: '错误码',
    responseMessage: '具体错误消息',
  }
```

#### 查询用户

根据指定的条件分页查询用户列表。

* **URL**

    /users

* **Method:**

    GET
  
*  **URL Params**

    - page 页数  Integer  Optional 默认为 0，查询第 1 页
    - size 分页条数  Integer  Optional 默认为 10
    - username 用户名  String  Optional 模糊匹配
    - name 用户姓名  String  Optional  模糊匹配
    - email 邮箱 String  Optional  模糊匹配
    - phoneNo 手机号码 String  Optional  模糊匹配
    - orgIds 机构列表  String[]  Optional 指定查询归属在指定机构下的用户
 
* **Data Params**

    None

* **Success Response:**
  
```javascript
{
  status: 0,
  responseCode: 'SC0000',
  success: true,
  payload: {
    // 总记录数 
    totalElements: 1,
    // 总页数
    totalPages: 1,
    // 当前查询返回记录数
    numberOfElements: 1
    // 页码
    number: 0,
    // 分页记录数
    size: 10
    content: [
      {
        id: '用户ID',
        username: '用户名',
        name: '姓名',
        email: '邮箱',
        phoneNo: '电话',
        createTime: 'yyyy-MM-ddTHH:mm:ss.SSSXXX',
        gender: '姓别', // M|F
        remark: '备注',
        // 用户归属的机构信息
        org: {
          id: '机构ID',
          name: '机构名称',
          code: '机构代码',
          createTime: 'yyyy-MM-ddTHH:mm:ss.SSSXXX',
          remark: '备注',
          childs: [],
          leaf: true
        },
        roles: [
          {
            id: '角色ID',
            createTime: 'yyyy-MM-ddTHH:mm:ss.SSSXXX',
            name: '角色名称',
            remark: '角色备注'
            // 角色的功能列表
            functions: [
              {
                id: '功能ID',
                serviceId: '功能归属系统',
                code: '功能码',
                name: '功能名称',
                type: 'M/O', //功能类型
                action: '动作',
                remark: '备注'
              }
            ]
          }
        ]
      }
    ]
  }
}
```
 
* **Error Response:**

```javascript
  {
    status: 1,
    success: false,
    responseCode: '错误码',
    responseMessage: '具体错误消息',
  }
```
 
#### 分配角色

为指定用户分配角色

* **URL**

    /role/user/:id

* **Method:**

    POST
  
*  **URL Params**
	 - id 用户id  String  Required
 
* **Data Params**

```javascript
//分配给对应用户的角色列表
['角色id1', '角色ID2',……]
``` 

* **Success Response:**
  
```javascript
{
  status: 0,
  responseCode: 'SC0000',
  success: true
}
```
 
* **Error Response:**

```javascript
  {
    status: 1,
    success: false,
    responseCode: '错误码',
    responseMessage: '具体错误消息',
  }
```

### 角色

#### 创建角色

创建角色

* **URL**

    /role

* **Method:**

    POST
  
*  **URL Params**

    none
 
* **Data Params**

    ```javascript
    {
      name:'角色',
      remark:'备注',
    }
    ```

* **Success Response:**
  
```javascript
{
  status: 0,
  responseCode: "SC0000",
  success: true,
  payload: {
    id: "角色ID",
    name: '角色名称',
    createTime: "yyyy-MM-ddTHH:mm:ss.SSSXXX",
    remark:'备注',
}
```
 
* **Error Response:**

```javascript
  {
    status: 1,
    success: false,
    responseCode: '错误码',
    responseMessage: '具体错误消息',
  }
```
#### 删除角色

指定角色ID删除角色

* **URL**

    /role:id

* **Method:**

    DELETE
  
*  **URL Params**

    - id 角色id  String  Required
 
* **Data Params**

    none

* **Success Response:**
  
```javascript
{
  status: 0,
  responseCode: 'SC0000',
  success: true
}
```
 
* **Error Response:**

```javascript
  {
    status: 1,
    success: false,
    responseCode: '错误码',
    responseMessage: '具体错误消息',
  }
```
#### 更新角色

通过输入信息更新对应角色

* **URL**

    /role

* **Method:**

    PUT
  
*  **URL Params**

    none
 
* **Data Params**

    ```javascript
    {
      id:'角色ID', // Required
      name:'更新角色', // Optional
      remark:'更新备注', //Optional
    }
    ```

* **Success Response:**
  
```javascript
{
  status: 0,
  responseCode: 'SC0000',
  success: true,
  payload: {
    id: '角色ID',
    name: '角色名称',
    createTime: 'yyyy-MM-ddTHH:mm:ss.SSSXXX',
    remark:'备注',
}
```

* **Error Response:**

```javascript
  {
    status: 1,
    success: false,
    responseCode: '错误码',
    responseMessage: '具体错误消息',
  }
```
#### 角色查询-所有

查询所有的角色信息，用于为用户分配角色时使用

* **URL**

    /roles/all

* **Method:**

    GET
  
*  **URL Params**

    none
 
* **Data Params**

    none

* **Success Response:**
  
```javascript
{
  status: 0,
  responseCode: 'SC0000',
  success: true,
  payload: [
    {
      id: '角色ID',
      name: '角色名称',
      remark: '备注',
      createTime: 'yyyy-MM-ddTHH:mm:ss.SSSXXX',
      functions: [
        {
          id: '功能ID',
          serviceId: '功能归属系统',
          code: '功能码',
          name: '功能名称',
          type: 'M/O', //功能类型
          action: '动作',
          remark: '备注'
        }
      ],
        
    }
  ]
}
```

* **Error Response:**

```javascript
  {
    status: 1,
    success: false,
    responseCode: '错误码',
    responseMessage: '具体错误消息',
  }
```
#### 角色查询-分页

输入分页信息查询角色

* **URL**

    /roles

* **Method:**

    GET
  
*  **URL Params**

    - page 页数  Integer Optional 默认为0
    - size 分页条数  Integer Optional 默认为10
    - name 角色名字 String Optional 模糊查询

    
* **Data Params**

    none

* **Success Response:**
  
```javascript
{
  status: 0,
  responseCode: 'SC0000',
  success: true
  payload: {
    // 总记录数 
    totalElements: 1,
    // 总页数
    totalPages: 1,
    // 当前查询返回记录数
    numberOfElements: 1
    // 页码
    number: 0,
    // 分页记录数
    size: 10  
    content: [
      {
        id: '角色ID',
        name: '角色名称',
        remark: '备注',
        createTime: 'yyyy-MM-ddTHH:mm:ss.SSSXXX',
        functions: [
          {
            id: '功能ID',
            serviceId: '功能归属系统',
            code: '功能码',
            name: '功能名称',
            type: 'M/O', //功能类型
            action: '动作',
            remark: '备注'
        }
      ],
    ]
}
```

* **Error Response:**

```javascript
  {
    status: 1,
    success: false,
    responseCode: '错误码',
    responseMessage: '具体错误消息',
  }
```

#### 功能分配

为对应角色分配功能

* **URL**

    /function/role/:id

* **Method:**

    POST
  
*  **URL Params**
	 - id 角色id  String  Required
 
* **Data Params**

```javascript
['功能码id1', '功能码ID2',……]
``` 


* **Success Response:**
  
```javascript
{
  status: 0,
  responseCode: 'SC0000',
  success: true
}
```
 
* **Error Response:**

```javascript
  {
    status: 1,
    success: false,
    responseCode: '错误码',
    responseMessage: '具体错误消息',
  }
```

### 功能

#### 创建功能

* **URL**

    /function

* **Method:**

    POST
  
*  **URL Params**

    none
 
* **Data Params**

    ```javascript
    {
      serviceId: '系统ID',
      code:'功能码',
      name:'功能码名称',
      //M-菜单 O-操作
      type:'M', 
      action:'动作', 
      remark: '备注',
    }
    ```

* **Success Response:**
  
```javascript
{
  status: 0,
  responseCode: 'SC0000',
  success: true,
  payload: {
    id: '功能码ID',
    serviceId: '归属系统ID',
    code: '功能码',
    name: '功能名称',
    type: '功能类型', // M 菜单|O 操作
    action: '功能码地址',
    remark: '备注'
  }
}
```
 
* **Error Response:**

```javascript
  {
    status: 1,
    success: false,
    responseCode: '错误码',
    responseMessage: '具体错误消息',
  }
```
#### 删除功能

根据 ID 删除对应功能。

* **URL**

    /function:id

* **Method:**

    DELETE
  
*  **URL Params**

    - id 功能ID  String  Required
 
* **Data Params**

    none

* **Success Response:**
  
```javascript
{
  status: 0,
  responseCode: 'SC0000',
  success: true
}
```
 
* **Error Response:**

```javascript
  {
    status: 1,
    success: false,
    responseCode: '错误码',
    responseMessage: '具体错误消息',
  }
```
#### 更新功能

* **URL**

    /function

* **Method:**

    PUT
  
*  **URL Params**

    none
 
* **Data Params**

    
    ```javascript
    {
      id:'功能ID', // Required
      serviceId: '归属系统',
      code: '功能码',
      name: '功能名称',
      type: '功能类型', // M-菜单 | O-操作
      action:'动作',
      remark: '备注',
    }
    ```

* **Success Response:**
  
```javascript
{
  status: 0,
  responseCode: 'SC0000',
  success: true,
  payload: {
    id: '功能ID',
    serviceId: '归属系统',
    code: '功能码',
    name: '功能名称',
    type: '功能类型',
    action: '动作',
    remark: '备注'
  }
}
```

* **Error Response:**

```javascript
  {
    status: 1,
    success: false,
    responseCode: '错误码',
    responseMessage: '具体错误消息',
  }
```
#### 分页查询功能

* **URL**

    /functions

* **Method:**

    GET
  
*  **URL Params**

    - page 页数  Integer Optional 默认为0
    - size 分页条数  Integer Optional  默认为10
    - serviceId 系统标识 String Optional 模糊查询
    - code 功能码 String Optional 模糊查询
    - name 功能码名称 String Optional 模糊查询
    - action 动作 String Optional 模糊查询
    - remark 备注 String Optional 模糊查询

    
* **Data Params**

    none

* **Success Response:**
  
```javascript
{
  status: 0,
  responseCode: 'SC0000',
  success: true
  payload: {
    // 总记录数 
    totalElements: 1,
    // 总页数
    totalPages: 1,
    // 当前查询返回记录数
    numberOfElements: 1
    // 页码
    number: 0,
    // 分页记录数
    size: 10  
    content: [
      {
        id: '功能ID',
        serviceId: '归属系统',
        code: '功能码',
        name: '功能名称',
        type: '功能类型',
        action: '动作',
        remark: '备注'
      }
    ]
  }
}
```

* **Error Response:**

```javascript
  {
    status: 1,
    success: false,
    responseCode: '错误码',
    responseMessage: '具体错误消息',
  }
```