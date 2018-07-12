import React from 'react'
import { Row, Col, Table, Input, Form, Button, Divider, Popconfirm } from 'antd'
import moment from 'moment'
import EditableCell from '../EditableCell'
import UserForm from './UserForm'
import RoleTransfer from './RoleTransfer'
import OrgSelect from './OrgSelect'

const formItemLayout = {
  labelCol: {
    span: 4,
  },
  wrapperCol: {
    span: 16,
  },
}

const Component = ({
  page, dispatch, showCreateModal, selected, loading, orgs, search, user, selectedKeys, showTransferModal, roles,
}) => {
  const { Item } = Form
  const onCellChange = (key, dataIndex) => {
    return (value) => {
      const update = page.content.find(o => o.id === key)
      update[dataIndex] = value
      dispatch({ type: 'User/update', user: update })
    }
  }
  const columns = [
    {
      title: '用户名',
      dataIndex: 'username',
      width: '15%',
      key: 'username',
      render: (text, record) => (record.username),
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
          <Popconfirm title="确认删除?" onConfirm={() => dispatch({ type: 'User/delete', id: record.id })}>
            <a href="#">删除</a>
          </Popconfirm>
          <Divider type="vertical" />
          <a href="#"
            onClick={(e) => {
              e.preventDefault()
              dispatch({ type: 'User/showTransferModal', user: record })
          }}
          >分配角色</a>
        </span>),
    },
  ]

  return (
    <Col >
      <Row>
        <Form>
          <Row gutter={{ md: 8, lg: 24, xl: 48 }}>
            <Col md={8} sm={24}>
              <Item label="机构" {...formItemLayout}>
                <OrgSelect
                  style={{ width: '100%' }}
                  orgs={orgs}
                  multiple
                  value={search.orgIds}
                  onFocus={() => dispatch({ type: 'User/orgTree' })}
                  onChange={(value) => {
                dispatch({
                  type: 'User/setState',
                  search: {
                    orgIds: value || null,
                  },
                })
            }}
                />
              </Item>
            </Col>

            <Col md={8} sm={24}>
              <Item label="用户名" {...formItemLayout}>
                <Input
                  onChange={(e) => {
                const { value } = e.target
                dispatch({
                  type: 'User/setState',
                  search: {
                    username: value || null,
                  },
                })
              }}
                />
              </Item>
            </Col>
            <Col md={8} sm={24} >
              <Item label="姓名" {...formItemLayout}>
                <Input
                  onChange={(e) => {
              const { value } = e.target
              dispatch({
                type: 'User/setState',
                search: {
                  name: value || null,
                },
              })
            }}
                />
              </Item>
            </Col>
          </Row>
          <Row gutter={{ md: 8, lg: 24, xl: 48 }}>
            <Col md={8} sm={24} >
              <Item label="邮箱" {...formItemLayout}>
                <Input
                  onChange={(e) => {
              const { value } = e.target
              dispatch({
                type: 'User/setState',
                search: {
                  email: value || null,
                },
              })
            }}
                />
              </Item>
            </Col>
            <Col md={8} sm={24}>
              <Item label="电话" {...formItemLayout}>
                <Input
                  onChange={(e) => {
              const { value } = e.target
              dispatch({
                type: 'User/setState',
                search: {
                  phoneNo: value || null,
                },
              })
            }}
                />
              </Item>
            </Col>
            <Col md={8} sm={24}>
              <Item {...formItemLayout}>
                <Button type="primary" htmlType="button" icon="search" onClick={() => dispatch({ type: 'User/query', orgId: selected && selected.key })}>
                  查询
                </Button>
              </Item>
            </Col>
          </Row>
        </Form>
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
              type: 'User/query', page: number - 1, size, parentOrgId: selected && selected.key,
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
