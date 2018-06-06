import { notification as andNotification } from 'antd'

export default {
  notification: {
    error: (result) => {
      andNotification.error({
        message: `交易失败：错误码 ${result.responseCode}`,
        description: `详细原因：${result.responseMessage}`,
      })
    },
  },
}
