import React from 'react'
import { Row, Col, Table, Input, Form, Button, Divider, Popconfirm } from 'antd'
import moment from 'moment'
import EditableCell from '../EditableCell'
import UserForm from './UserForm'
import RoleTransfer from './RoleTransfer'
import OrgSelect from './OrgSelect'
import styles from './UserList.less'

const Component = ({
  page, dispatch, showCreateModal, selected, loading, orgs, search, user, selectedKeys, showTransferModal, roles,
}) => {
  const { Item } = Form
  const onCellChange = (key, dataIndex) => {
    return (value) => {
      const update = page.content.find(o => o.id === key)
      update[dataIndex] = value
      dispatch({ type: 'REAPRB0002/update', user: update })
    }
  }
  const columns = [
    {
      title: '用户名',
      dataIndex: 'username',
      width: '15%',
      key: 'username',
      render: (text, record) => (
        <EditableCell
          value={text}
          onChange={onCellChange(record.id, 'username')}
        />
      ),
    },
    {
      title: '姓名',
      dataIndex: 'name',
      key: 'name',
      width: '10%',
      render: (text, record) => (
        <EditableCell
          value={text}
          onChange={onCellChange(record.id, 'name')}
        />
      ),
    },
    {
      title: '姓别',
      dataIndex: 'gender',
      key: 'gender',
      width: '5%',
      render: text => ({ M: '男', F: '女' }[text]),
    },
    {
      title: '邮箱',
      dataIndex: 'email',
      key: 'email',
      width: '15%',
      render: (text, record) => (
        <EditableCell
          value={text}
          onChange={onCellChange(record.id, 'email')}
        />
      ),
    },
    {
      title: '电话',
      width: '10%',
      dataIndex: 'phoneNo',
      key: 'phoneNo',
      render: (text, record) => (
        <EditableCell
          value={text}
          onChange={onCellChange(record.id, 'phoneNo')}
        />
      ),
    },
    {
      title: '归属机构',
      key: 'orgId',
      width: '10%',
      render: (text, record) => (record.org && record.org.name),
    },
    {
      title: '创建时间',
      width: '10%',
      dataIndex: 'createTime',
      key: 'createTime',
      render: text => (text ? moment(text).format('YYYY-MM-DD HH:mm:ss') : null),
    },
    {
      title: '备注',
      dataIndex: 'remark',
      key: 'remark',
      render: (text, record) => (
        <EditableCell
          value={text}
          onChange={onCellChange(record.id, 'remark')}
        />
      ),
    },
    {
      title: '操作',
      width: '10%',
      dataIndex: 'operation',
      render: (text, record) =>
        (<span>
          <Popconfirm title="确认删除?" onConfirm={() => dispatch({ type: 'REAPRB0002/delete', id: record.id })}>
            <a href="#">删除</a>
          </Popconfirm>
          <Divider type="vertical" />
          <a href="#"
            onClick={(e) => {
              e.preventDefault()
              dispatch({ type: 'REAPRB0002/showTransferModal', user: record })
          }}
          >分配角色</a>
        </span>),
    },
  ]

  return (
    <Col >
      <Row gutter={24} className={styles.searchForm}>
        <Row>
          <Col span={16}>
            <Item label="归属机构">
              <OrgSelect
                style={{ width: '40%' }}
                orgs={orgs}
                multiple
                value={search.orgIds}
                onFocus={() => dispatch({ type: 'REAPRB0002/orgTree' })}
                onChange={(value) => {
                dispatch({
                  type: 'REAPRB0002/setState',
                  search: {
                    orgIds: value,
                  },
                })
            }}
              />
            </Item>
          </Col>
        </Row>
        <Col span={4}>
          <Item label="用户名">
            <Input
              onChange={(e) => {
                const { value } = e.target
                dispatch({
                  type: 'REAPRB0002/setState',
                  search: {
                    username: value,
                  },
                })
              }}
            />
          </Item>
        </Col>
        <Col span={4} >
          <Item label="姓名">
            <Input
              onChange={(e) => {
              const { value } = e.target
              dispatch({
                type: 'REAPRB0002/setState',
                search: {
                  name: value,
                },
              })
            }}
            />
          </Item>
        </Col>
        <Col span={4} >
          <Item label="邮箱">
            <Input
              onChange={(e) => {
              const { value } = e.target
              dispatch({
                type: 'REAPRB0002/setState',
                search: {
                  email: value,
                },
              })
            }}
            />
          </Item>
        </Col>
        <Col span={4} >
          <Item label="电话">
            <Input
              onChange={(e) => {
              const { value } = e.target
              dispatch({
                type: 'REAPRB0002/setState',
                search: {
                  phoneNo: value,
                },
              })
            }}
            />
          </Item>
        </Col>
        <Col span={4} >
          <Item>
            <Button
              type="primary"
              htmlType="button"
              icon="search"
              onClick={
                () => dispatch({ type: 'REAPRB0002/query', orgId: selected && selected.key })
              }
            >
            查询
            </Button>
          </Item>
        </Col>
      </Row>
      <Col>
        <UserForm orgs={orgs} showCreateModal={showCreateModal} selected={selected} dispatch={dispatch} />
      </Col>
      <Col>
        <RoleTransfer user={user} showTransferModal={showTransferModal} roles={roles} selectedKeys={selectedKeys} dispatch={dispatch} />
      </Col>
      <Col>
        <Table dataSource={page && page.content}
          pagination={{
            total: page && page.totalElements,
            showTotal: total => `总记录数 ${total} `,
            onChange: (number, size) => (dispatch({
              type: 'REAPRB0002/query', page: number - 1, size, parentOrgId: selected && selected.key,
             })),
        }}
          columns={columns}
          rowKey="id"
          loading={loading}
          bordered
        />
      </Col>
    </Col>
  )
}
export default Component
