import React from 'react'
import { Router, Route, Switch } from 'dva/router'
import { LocaleProvider } from 'antd'
import { connect } from 'dva'
import zhCN from 'antd/lib/locale-provider/zh_CN'
import BasicLayout from './layout/BasicLayout'
import { functions } from './config'

function getRouterData (app) {
  const routerData = {}
  // 首先注册全量的 models 避免 connect 无法找到
  functions.forEach((aFunction) => {
    // eslint-disable-next-line
    const model = require(`./models/${aFunction.name ? aFunction.name : aFunction.code}.js`)
    if (model === null) {
      throw new Error(`src/models/${aFunction.name ? aFunction.name : aFunction.code}.js`)
    }
    // 初始化 model 中的信息使它可以被 dva 识别
    if (!model.state) {
      model.state = {}
    }
    if (!model.namespace) {
      model.namespace = aFunction.code
    }
    if (!model.effects) {
      model.effects = {}
    }
    if (!model.reducers) {
      model.reducers = {}
    }
    if (model.subscriptions && model.subscriptions.setup) {
      const { setup } = model.subscriptions
      model.subscriptions.setup = function (data) {
        setup({ ...data, context: aFunction })
      }
    }

    app.model(model)
  })
  functions.forEach((aFunction) => {
    routerData[aFunction.code] = {
      // 在框架中自动 connect ，如果在功能码中有配置 connect 函数使用 功能码配置中的，否则使用默认的，默认将会根据匹配功能码下的状态以及当前功能码下的 loading 状态
      // eslint-disable-next-line
      component: connect(aFunction.connect ? aFunction.connect : state => ({ ...state[aFunction.code], context: aFunction ,loading: state.loading.models[aFunction.code],effects: state.loading.effects }))(require(`./routes/${aFunction.name ? aFunction.name : aFunction.code}.js`)),
    }
  })
  return routerData
}

function createRouterConfig () {
  // eslint-disable-next-line
return function RouterConfig ({ history, app }) {
    const routerData = getRouterData(app)
    return (
      <LocaleProvider locale={zhCN}>
        <Router history={history}>
          <Switch>
            <Route path="/" render={props => <BasicLayout {...props} routerData={routerData} />} />
          </Switch>
        </Router>
      </LocaleProvider>
    )
  }
}
export default createRouterConfig
