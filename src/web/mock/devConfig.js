//  本地开发阶段的应用配置
export default {

  // 本地开发阶段的通讯配置
  devComm: {
    // proxy 为 true 时会使用配置的 urlPrefix 而非向本地 mock server 发送请求，用于本地开发的页面同后台联调
    proxy: true,
    urlPrefix: 'http://localhost:8080',
  },
  devSession: {

  },
}
