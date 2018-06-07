import React from 'react'
import { Layout } from 'antd'
import { Route, Switch } from 'dva/router'

const { Content } = Layout

class BasicLayout extends React.PureComponent {
  render () {
    const { routerData } = this.props
    const routers = []
    // const width = window.innerWidth
    Object.keys(routerData).forEach((key) => {
      routers.push({
        key: `/${key}`,
        path: `/${key}`,
        component: routerData[key].component,
        exact: true,
      })
    })
    return (
      <Layout>
        <Layout>
          <Content style={{ margin: '24px 24px 0', height: '100%' }}>
            <div style={{
                minHeight: 'calc(100vh - 30px)',
                margin: '0 auto 0 auto',
              }}
            >
              <Switch>
                {
                  routers.map(item => (
                    <Route
                      key={item.key}
                      path={item.path}
                      component={item.component}
                      exact={item.exact}
                    />
                  ))
                }
              </Switch>
            </div>
          </Content>
        </Layout>
      </Layout>
    )
  }
}

export default BasicLayout
