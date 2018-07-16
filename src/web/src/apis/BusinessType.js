import { stringify } from 'qs'
import request from '../utils/request'

export function create (data) {
  return request('/apis/reap-rbac/businessType', { method: 'POST', body: data })
}

export function update (data) {
  return request('/apis/reap-rbac/businessType', { method: 'PUT', body: data })
}

export function findFunctions (businessTypeId) {
  return request(`/apis/reap-rbac/businessType/${businessTypeId}/functions`, { method: 'GET' })
}

export function remove (id) {
  return request(`/apis/reap-rbac/businessType/${id}`, { method: 'DELETE' })
}

export function search (specification) {
  return request(`/apis/reap-rbac/businessTypes?${stringify(specification, { skipNulls: true })}`, { method: 'GET' })
}

export function get (id) {
  return request(`/apis/reap-rbac/businessType/${id}`, { method: 'DELETE' })
}

export function getAll () {
  return request('/apis/reap-rbac/businessTypes/all', { method: 'GET' })
}
