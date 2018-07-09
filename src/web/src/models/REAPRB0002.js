import feedback from '../utils/feedback'
import { create, update, query, get, remove } from '../apis/user'
import { queryAll, allocateRoles } from '../apis/role'
import { orgsTree } from '../apis/org'
import { DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE } from '../constants'

const { notification: { error } } = feedback

function usersSpec (state) {
  return {
    orgId: state.selected && state.selected.key,
    size: state.page && state.page.size,
    page: state.page && state.page.number,
  }
}

export default {

  state: {
    search: {
      name: null,
      username: null,
      email: null,
      phoneNo: null,
    },
    showCreateModal: false,
    selected: null,
    orgs: [],
    showTransferModal: false,
    // 可选的Role
    roles: [],
    // 当前设置用户
    user: {},
    // 选中的key
    selectedKeys: [],
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
      } else {
        error(result)
      }
    },
    * query ({ page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE, orgId }, { call, put, select }) {
      const state = yield select(({ REAPRB0002 }) => (REAPRB0002))
      const params = {
        size, page, orgId, ...state.search,
      }
      const result = yield call(query, params)
      if (result.success) {
        yield put({ type: 'setState', page: result.payload })
      }
    },
    * update ({ user }, { call, put, select }) {
      const state = yield select(({ REAPRB0002 }) => (REAPRB0002))
      const result = yield call(update, user)
      if (result.success) {
        yield put({
          type: 'query', ...usersSpec(state),
        })
      } else {
        error(result)
      }
    },
    * select ({ selected }, { put }) {
      yield put({
        type: 'query', orgId: selected && selected.key, page: DEFAULT_PAGE_NUMBER, size: DEFAULT_PAGE_SIZE,
      })
      yield put({ type: 'setState', selected })
    },
    * delete ({ id }, { call, put, select }) {
      const result = yield call(remove, id)
      if (result.success) {
        const state = yield select(({ REAPRB0002 }) => (REAPRB0002))
        yield put({ type: 'query', ...usersSpec(state) })
      } else {
        error(result)
      }
    },
    * create ({ user, form }, { call, select, put }) {
      const state = yield select(({ REAPRB0002 }) => (REAPRB0002))
      const result = yield call(create, user)
      if (result.success) {
        form.resetFields()
        yield put({ type: 'setState', showCreateModal: false })
        yield put({ type: 'query', ...usersSpec(state) })
      } else {
        error(result)
      }
    },
    * showTransferModal ({ user }, { call, put }) {
      const result = yield call(queryAll)
      if (result.success) {
        const getUserResult = yield call(get, user.id)
        if (getUserResult.success) {
          yield put({
            type: 'setState', roles: result.payload, showTransferModal: true, user: getUserResult.payload,
          })
        } else {
          error(getUserResult)
        }
      } else {
        error(result)
      }
    },
    * allocateRole (action, { call, select, put }) {
      const state = yield select(({ REAPRB0002 }) => (REAPRB0002))
      const result = yield call(allocateRoles, state.user.id, state.user.roles && state.user.roles.map(r => r.id))
      if (result.success) {
        yield put({ type: 'query', ...usersSpec(state) })
        yield put({ type: 'resetRoleState' })
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
    resetRoleState (state) {
      return {
        ...state,
        showTransferModal: false,
        roles: [],
        user: {},
        selectedKeys: [],
      }
    },
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/REAPRB0002') {
          dispatch({ type: 'orgTree' })
          dispatch({ type: 'query', page: DEFAULT_PAGE_NUMBER, size: DEFAULT_PAGE_SIZE })
        }
      })
    },
  },
}
