import { stringify } from 'qs'
import request from '../utils/request'

export function allocateRoles (userId, roleIds) {
  return request(`/apis/reap-rbac/role/user/${userId}`, { method: 'POST', body: roleIds })
}

export function queryAll () {
  return request('/apis/reap-rbac/roles/all', { method: 'GET' })
}

export function query (specification) {
  return request(`/apis/reap-rbac/roles?${stringify(specification)}`, { method: 'GET' })
}

export function update (role) {
  return request('/apis/reap-rbac/role', { method: 'PUT', body: role })
}

export function remove (id) {
  return request(`/apis/reap-rbac/role/${id}`, { method: 'DELETE' })
}

export function create (role) {
  return request('/apis/reap-rbac/role', { method: 'POST', body: role })
}

