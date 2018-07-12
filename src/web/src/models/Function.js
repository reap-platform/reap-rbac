import feedback from '../utils/feedback'
import { create, update, query, remove } from '../apis/Function'
import { DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE } from '../constants'

const { notification: { error } } = feedback

function functionSpec (state) {
  return {
    size: state.page && state.page.size,
    page: state.page && state.page.number,
  }
}

export default {

  state: {
    search: {
      name: null,
      serviceId: null,
      code: null,
    },
    showCreateModal: false,
    page: {
      size: DEFAULT_PAGE_SIZE,
      number: DEFAULT_PAGE_NUMBER,
    },
  },
  effects: {
    * query ({ page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE }, { call, put, select }) {
      const state = yield select(({ REAPRB0004 }) => (REAPRB0004))
      const params = {
        size, page, ...state.search,
      }
      const result = yield call(query, params)
      if (result.success) {
        yield put({ type: 'setState', page: result.payload })
      }
    },
    * update ({ func }, { call, put, select }) {
      const state = yield select(({ REAPRB0004 }) => (REAPRB0004))
      const result = yield call(update, func)
      if (result.success) {
        yield put({
          type: 'query', ...functionSpec(state),
        })
      } else {
        error(result)
      }
    },
    * delete ({ id }, { call, put, select }) {
      const result = yield call(remove, id)
      if (result.success) {
        const state = yield select(({ REAPRB0004 }) => (REAPRB0004))
        yield put({ type: 'query', ...functionSpec(state) })
      } else {
        error(result)
      }
    },
    * create ({ func, form }, { call, select, put }) {
      const state = yield select(({ REAPRB0004 }) => (REAPRB0004))
      const result = yield call(create, func)
      if (result.success) {
        form.resetFields()
        yield put({ type: 'setState', showCreateModal: false })
        yield put({ type: 'query', ...functionSpec(state) })
      } else {
        error(result)
      }
    },
  },
  reducers: {
    setState (state, newState) {
      return {
        ...state,
        ...newState,
      }
    },
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/REAPRB0004') {
          dispatch({ type: 'query', page: DEFAULT_PAGE_NUMBER, size: DEFAULT_PAGE_SIZE })
        }
      })
    },
  },
}
