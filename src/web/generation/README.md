
生成交易 Metadata的示例，文件名称为 `${functionName}.json`，格式如下：

```javascript
{
  "fields": [
    { "key": "id", "name": "ID", "type": "String", "primaryKey": true}, 
    { "key": "name", "name": "姓名", "type": "String", "editable": true, "searchable": true, "required": true },
    { "key": "age", "name": "年龄", "type": "Date", "editable": true, "searchable": true, "required": true }
  ]
}
```

对于每个数据域 （`field`） 的定义属性包含以下
  - `key`  数据域 id ，命名小写开头驼峰式，必输
  - `name` 数据域中文名称，前端的表单、表格使用，必输
  - `type` 数据类型，支持以下 String、Integer、Long、Date、Timestamp、BigDecimal（区分大小写），默认为 String
  - `primaryKey` 主键标识，如果未指定自动设置为 id，类型支持 Long、String
  - `required` 是否必输，默认 false
  - `editable` 是否可修改，默认 flase
  - `searchable` 是否做为查询条件 ，默认 false 




