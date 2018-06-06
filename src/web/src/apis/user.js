import { stringify } from 'qs'
import request from '../utils/request'

export function query (specification) {
  return request(`/apis/reap-rbac/users?${stringify(specification)}`, { method: 'GET' })
}

export function update (user) {
  return request('/apis/reap-rbac/user', { method: 'PUT', body: user })
}

export function remove (id) {
  return request(`/apis/reap-rbac/user/${id}`, { method: 'DELETE' })
}

export function create (user) {
  return request(`/apis/reap-rbac/user/org/${user.orgId}`, { method: 'POST', body: user })
}

