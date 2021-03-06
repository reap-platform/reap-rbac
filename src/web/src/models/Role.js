import feedback from '../utils/feedback'
import { create, update, query, remove, findFunctions } from '../apis/Role'
import { queryAll, allocateFunctions } from '../apis/Function'
import { DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE } from '../constants'

const { notification: { error } } = feedback

function roleSpec (state) {
  return {
    size: state.page && state.page.size,
    page: state.page && state.page.number,
  }
}

export default {

  state: {
    search: {
      name: null,
    },
    showCreateModal: false,
    showTransferModal: false,
    role: {},
    functions: [],
    selectedKeys: [],
    page: {
      size: DEFAULT_PAGE_SIZE,
      number: DEFAULT_PAGE_NUMBER,
    },
  },
  effects: {
    * query ({ page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE }, { call, put, select }) {
      const state = yield select(({ Role }) => (Role))
      const params = {
        size, page, ...state.search,
      }
      const result = yield call(query, params)
      if (result.success) {
        yield put({ type: 'setState', page: result.payload })
      }
    },
    * update ({ role }, { call, put, select }) {
      const state = yield select(({ Role }) => (Role))
      const result = yield call(update, role)
      if (result.success) {
        yield put({
          type: 'query', ...roleSpec(state),
        })
      } else {
        error(result)
      }
    },
    * delete ({ id }, { call, put, select }) {
      const result = yield call(remove, id)
      if (result.success) {
        const state = yield select(({ Role }) => (Role))
        yield put({ type: 'query', ...roleSpec(state) })
      } else {
        error(result)
      }
    },
    * create ({ role, form }, { call, select, put }) {
      const state = yield select(({ Role }) => (Role))
      const result = yield call(create, role)
      if (result.success) {
        form.resetFields()
        yield put({ type: 'setState', showCreateModal: false })
        yield put({ type: 'query', ...roleSpec(state) })
      } else {
        error(result)
      }
    },
    * showTransferModal ({ role }, { call, put }) {
      const result = yield call(queryAll)
      if (result.success) {
        const functionsResult = yield call(findFunctions, role.id)
        if (functionsResult.success) {
          role.functions = functionsResult.payload
          yield put({
            type: 'setState', functions: result.payload, showTransferModal: true, role,
          })
        } else {
          error(result)
        }
      } else {
        error(result)
      }
    },
    * allocateFunction (action, { call, select, put }) {
      const state = yield select(({ Role }) => (Role))
      const result = yield call(allocateFunctions, state.role.id, state.role.functions && state.role.functions.map(r => r.id))
      if (result.success) {
        yield put({ type: 'query', ...roleSpec(state) })
        yield put({ type: 'resetFunctionState' })
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
    resetFunctionState (state) {
      return {
        ...state,
        showTransferModal: false,
        functions: [],
        role: {},
        selectedKeys: [],
      }
    },
  },
  subscriptions: {
    setup ({ context, dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === `/${context.code}`) {
          dispatch({ type: 'query', page: DEFAULT_PAGE_NUMBER, size: DEFAULT_PAGE_SIZE })
        }
      })
    },
  },
}
