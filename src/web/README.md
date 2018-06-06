# REAP 前端脚手架

## 工程结构

### 替换

新建项目替换以下位置的系统码配置信息：

- `package.json` 中的 `name` 
- `src/config.js` 中的 `systemCode` 

### 目录

```
- mock 后台Mock案例
- public 全局静态资源
- src 源码
  - assets/images  图片
  - models 模型（数据/操作）
    - 功能码.js （每一个功能码对应一个模型）
  - routes 页面（展现的视图）
    - 功能码.js （每一个功能码对应一个页面）
  - config.js  应用配置
  - index.ejs  渲染首页（应用开发不需要改动）
  - index.js 应用入口
  - index.less 应用全局样式
```

## 脚本

### 本地开发

```bash
$ npm start         # 访问 http://localhost:8000
```

### 应用构建

```bash
$ npm run build   # 构建产出物在 ./dist 目录下
```

## 交易开发

以功能码 `YIBBIP0001` 为例,开发按以下流程，完成开发后，可以通过以下 URL 访问这个功能码下的功能 `http://localhost:8000/#/YIBBIP0001`。

### 配置功能码

在 `config.js` `functions` 节点下增加功能码的配置，示例如下：

```javascript
 functions: [
    // component:'' , model: ''  均为可选，如果不设置 {component: 'routers/功能码.js',model: 'models/功能码.js'}，如果设置则 override 掉默认的
   {code:'YIBBIP0001', description: '这个功能码的描述信息'}
  ],
```

### 新建模型

在 `models` 目录下创建 `YIBBIP0001.js` 每个功能码对应一个模型用于存放功能码页面相关的状态信息：

```javascript
export default {
  namespace: 'YIBBIP0001',
  // 状态
  state: {},
  // 操作（副作用操作，比如：后台交互）
  effects: {},
  // 操作（纯函数操作）
  reducers: {},
};

```

### 新建页面

在 `routes` 目录下创建 `YIBBIP0001.js`：

```javascript
import React from 'react';

export default () => (
    <div>
        YIBBIP0001 页面
    </div>
  );
```

## 编程风格

### 组件定义风格

页面的组件风格按以下顺序选择，无特殊情况下均应该采用 *Stateless Functional Component* 的方式定义组件：

- `Stateless Functional Component`  无本地状态、无生命周期回调均采用此风格定义组件，更简洁、更高效
```javascript
export default ()=>( <div> A Stateless Functional Component </div>)
```
- `React.PureComponent`  无本地状态、有生命周期回调采用此风格定义组件

```javascript
import React from 'react'

class APureComponent extends React.PureComponent {

 render () {
   return (
     <div>
      A Pure Component
     </div>
   )
 }
}
export default APureComponent
```
 
- `React.Component` 有本地状态采用此风格定义组件

```javascript
import React from 'react'

class ALocalStateComponent extends React.PureComponent {

 constructor (props) {
   super(props)
   this.state = {someKey: 'someValue'}
 }

 render () {
   return (
     <div>
      A Local State Component With State ： {this.state.someKey}.
     </div>
   )
 }
}
export default ALocalStateComponent
```