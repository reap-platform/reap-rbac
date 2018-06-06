const config = function () {
  // 应用配置
  const configurations = {
    // 配置功能码
    functions: [
      { code: 'REAPRB0001', description: '机构信息维护' },
      { code: 'REAPRB0002', description: '用户信息维护' },
      { code: 'REAPRB0003', description: '角色维护' },
      { code: 'REAPRB0004', description: '功能码维护' },
    ],
  }

  if (process.env.NODE_ENV === 'development') {
    Object.assign(configurations, require('../mock/devConfig'))
  }

  return configurations
}

export default config()
