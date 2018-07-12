import { stringify } from 'qs'
import request from '../utils/request'

export function orgsTree () {
  return request('/apis/reap-rbac/orgs/tree', { method: 'GET' })
}

export function query (specification) {
  return request(`/apis/reap-rbac/orgs?${stringify(specification, { skipNulls: true })}`, { method: 'GET' })
}

export function update (org) {
  return request('/apis/reap-rbac/org', { method: 'PUT', body: org })
}

export function remove (id) {
  return request(`/apis/reap-rbac/org/${id}`, { method: 'DELETE' })
}

export function create (parentOrgId, org) {
  const url = parentOrgId ? `/apis/reap-rbac/org/${parentOrgId}` : '/apis/reap-rbac/org'
  return request(url, { method: 'POST', body: org })
}

