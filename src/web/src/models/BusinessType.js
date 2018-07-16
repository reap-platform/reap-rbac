import feedback from '../utils/feedback'
import { create, findFunctions, update, search, remove } from '../apis/BusinessType'
import { queryAll, allocateBusinessTypeFunctions } from '../apis/Function'
import { DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE } from '../constants'

const { notification: { error } } = feedback


function pageRequest (state) {
  return {
    size: state.page && state.page.size,
    page: state.page && state.page.number,
  }
}

export default {
  state: {
    search: {
      code: null,
      name: null,
      remark: null,

    },
    showModal: false,
    showTransferModal: false,
    businessType: {},
    selectedKeys: [],
    functions: [],
    page: {
      size: DEFAULT_PAGE_SIZE,
      number: DEFAULT_PAGE_NUMBER,
    },
  },
  effects: {
    * search ({ page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE }, { call, put, select }) {
      const state = yield select(rootState => rootState.BusinessType)
      const result = yield call(search, { size, page, ...state.search })
      if (result.success) {
        yield put({ type: 'setState', page: result.payload })
      } else {
        error(result)
      }
    },
    * showTransferModal ({ businessType }, { call, put }) {
      const result = yield call(queryAll)
      if (result.success) {
        const functionsResult = yield call(findFunctions, businessType.id)
        if (functionsResult.success) {
          businessType.functions = functionsResult.payload
          yield put({
            type: 'setState', functions: result.payload, showTransferModal: true, businessType,
          })
        } else {
          error(result)
        }
      } else {
        error(result)
      }
    },
    * update ({ data }, { call, put, select }) {
      const state = yield select(rootState => rootState.BusinessType)
      const result = yield call(update, data)
      if (result.success) {
        yield put({
          type: 'search', ...pageRequest(state),
        })
      } else {
        error(result)
      }
    },
    * remove ({ id }, { call, put, select }) {
      const result = yield call(remove, id)
      if (result.success) {
        const state = yield select(rootState => rootState.BusinessType)
        yield put({ type: 'search', ...pageRequest(state) })
      } else {
        error(result)
      }
    },
    * create ({ data, form }, { call, select, put }) {
      const state = yield select(rootState => rootState.BusinessType)
      const result = yield call(create, data)
      if (result.success) {
        form.resetFields()
        yield put({ type: 'setState', showModal: false })
        yield put({ type: 'search', ...pageRequest(state) })
      } else {
        error(result)
      }
    },
    * allocateFunction (action, { call, select, put }) {
      const state = yield select(({ BusinessType }) => (BusinessType))
      const result = yield call(allocateBusinessTypeFunctions, state.businessType.id, state.businessType.functions && state.businessType.functions.map(r => r.id))
      if (result.success) {
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
        search: {
          ...state.search,
          ...newState.search,
        },
      }
    },
    resetFunctionState (state) {
      return {
        ...state,
        showTransferModal: false,
        functions: [],
        businessType: {},
        selectedKeys: [],
      }
    },
  },
  subscriptions: {
    setup ({ context, dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === `/${context.code}`) {
          dispatch({
            type: 'search', context, page: DEFAULT_PAGE_NUMBER, size: DEFAULT_PAGE_SIZE,
          })
        }
      })
    },
  },
}
