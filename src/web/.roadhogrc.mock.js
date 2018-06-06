import mockjs from 'mockjs';
import { delay } from 'roadhog-api-doc';

// 响应码: 成功
const STATUS_SUCCESS = 0;

// 响应码: 失败
const STATUS_FAIL = 1;

// 错误码: 系统错误
const ERROR_CODE_SYSTEM_ERROR = 'ERCO0000';

const noProxy = process.env.NO_PROXY === 'true';

function buildProxy() {
  const cases = {};
  require('fs')
    .readdirSync(require('path').join(`${__dirname}/mock`))
    .forEach(file => {
      try {
        Object.assign(cases, require(`./mock/${file}`));
      } catch (error) {
        console.log(error);
      }
    });
  const proxy = {};
  Object.keys(cases).forEach(key => {
    proxy[key] = (req, res) => {
      const succ = (data = {}) => {
        res.json({ status: STATUS_SUCCESS, success: true, responseCode: 'SC0000', payload: data });
      };

      const fail = (responseCode = ERROR_CODE_SYSTEM_ERROR, responseMessage, data = {}) => {
        res.json({
          status: STATUS_FAIL,
          success: false,
          responseCode,
          responseMessage,
          payload: data,
        });
      };
      // 包含了原生的请求、响应、请求的数据（bdoy、params） 以及
      cases[key]({ req, res, succ, fail, body: req.body, params: req.params, query: req.query });
    };
  });
  return proxy;
}

export default (noProxy ? {} : delay(buildProxy(), 1000));
