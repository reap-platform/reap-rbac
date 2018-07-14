import { notification as andNotification } from 'antd'
import React from 'react'
import Ellipsis from '../components/Ellipsis'

export default {
  notification: {
    error: (result) => {
      andNotification.error({
        message: `交易失败：错误码 ${result.responseCode}`,
        description: <Ellipsis length={100} tooltip >{`详细原因：${result.responseMessage}`}</Ellipsis>,
      })
    },
  },
}
