import { orgsTree, remove, query, create, update } from '../apis/Org'
import feedback from '../utils/feedback'
import { DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE } from '../constants'

const { notification: { error } } = feedback

function orgsSpec (state) {
  return {
    parentOrgId: state.selected && state.selected.key,
    size: state.page && state.page.size,
    page: state.page && state.page.number,
  }
}

export default {

  state: {
    search: {
      code: null,
      name: null,
    },
    showCreateModal: false,
    selected: null,
    orgs: [],
    page: {
      size: DEFAULT_PAGE_SIZE,
      number: DEFAULT_PAGE_NUMBER,
    },
  },
  effects: {
    * orgTree (action, { call, put }) {
      const result = yield call(orgsTree)
      if (result.success) {
        yield put({ type: 'setState', orgs: result.payload })
      }
    },
    * query ({ page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE, parentOrgId }, { call, put, select }) {
      const state = yield select(({ Org }) => (Org))
      const params = {
        size, page, parentId: parentOrgId, ...state.search,
      }
      const result = yield call(query, params)
      if (result.success) {
        yield put({ type: 'setState', page: result.payload })
      }
    },
    * update ({ org }, { call, put, select }) {
      const state = yield select(({ Org }) => (Org))
      const result = yield call(update, org)
      if (result.success) {
        yield put({
          type: 'query', ...orgsSpec(state),
        })
        yield put({ type: 'orgTree' })
      } else {
        error(result)
      }
    },
    * select ({ selected }, { put }) {
      yield put({
        type: 'query', parentOrgId: selected && selected.key, page: DEFAULT_PAGE_NUMBER, size: DEFAULT_PAGE_SIZE,
      })
      yield put({ type: 'setState', selected })
    },
    * delete ({ id }, { call, put, select }) {
      const result = yield call(remove, id)
      if (result.success) {
        const state = yield select(({ Org }) => (Org))
        if (state.selected && state.selected.key === id) {
          yield put({ type: 'setState', selected: null })
          yield put({ type: 'query', ...orgsSpec(state), parentOrgId: null })
        } else {
          yield put({ type: 'query', ...orgsSpec(state) })
        }
        yield put({ type: 'orgTree' })
      } else {
        error(result)
      }
    },
    * create ({ org }, { call, select, put }) {
      const state = yield select(({ Org }) => (Org))
      const result = yield call(create, state.selected && state.selected.key, org)
      yield put({ type: 'setState', showCreateModal: false })
      if (result.success) {
        yield put({ type: 'query', ...orgsSpec(state) })
        yield put({ type: 'orgTree' })
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
    setup ({ context, dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === `/${context.code}`) {
          dispatch({ type: 'orgTree' })
          dispatch({ type: 'query', page: DEFAULT_PAGE_NUMBER, size: DEFAULT_PAGE_SIZE })
        }
      })
    },
  },
}
