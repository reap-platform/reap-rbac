import 'babel-polyfill'
import dva from 'dva'
import createLoading from 'dva-loading'
import 'moment/locale/zh-cn'
import FastClick from 'fastclick'
import './index.less'

import createRouterConfig from './router'
import { init } from './app'

// 1. Initialize
const app = dva({
  onError: () => {},
})

// 2. Plugins
app.use(createLoading())

// 3. Register global model
// app.model(require('./models/global'))


// 在初始化完成后，回调继续进行 dva 的路由初始化及启动应用
init(() => {
  // 4. Router
  app.router(createRouterConfig())

  // 5. Start
  app.start('#root')

  FastClick.attach(document.body)
})

