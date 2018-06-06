import config from '../config'
import { EVENT_TYPE_LIFECYCLE_READY, EVENT_TYPE_UI_RESET_VIEW, EVENT_TYPE_LIFECYCLE_START } from '../constants'
/**
 * 应用启动的初始化函数，用于同 GUIP 窗口间传递应用初始化所需要使用的信息（如用户、token 等）
 */
export function init (cb) {
  // 对于
  if (window.parent !== window) {
    window.addEventListener('message', (event) => {
      if (event.data.type === EVENT_TYPE_LIFECYCLE_START) {
        cb(event.data.session)
      }
    })
    window.document.body.onclick = () => {
      window.parent.postMessage({ type: EVENT_TYPE_UI_RESET_VIEW }, '*')
    }
    window.parent.postMessage({ type: EVENT_TYPE_LIFECYCLE_READY }, '*')
  } else {
    cb(config.devSession)
  }
}
