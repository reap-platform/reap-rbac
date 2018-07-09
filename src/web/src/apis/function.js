import { stringify } from 'qs'
import request from '../utils/request'

export function allocateFunctions (roleId, functionIds) {
  return request(`/apis/reap-rbac/function/role/${roleId}`, { method: 'POST', body: functionIds })
}

export function queryAll () {
  return request('/apis/reap-rbac/functions/all', { method: 'GET' })
}

export function query (specification) {
  return request(`/apis/reap-rbac/functions?${stringify(specification, { skipNulls: true })}`, { method: 'GET' })
}

export function update (func) {
  return request('/apis/reap-rbac/function', { method: 'PUT', body: func })
}

export function remove (id) {
  return request(`/apis/reap-rbac/function/${id}`, { method: 'DELETE' })
}

export function create (func) {
  return request('/apis/reap-rbac/function', { method: 'POST', body: func })
}

