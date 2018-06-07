import React from 'react'
import { Row, Col, Table, Popconfirm, Input, Form, Button, Divider } from 'antd'
import moment from 'moment'
import EditableCell from '../EditableCell'
import FunctionTransfer from './FunctionTransfer'
import RoleForm from './RoleForm'

const formItemLayout = {
  labelCol: {
    span: 4,
  },
  wrapperCol: {
    span: 16,
  },
}

const Component = ({
  page, dispatch, showCreateModal, loading, role, showTransferModal, functions, selectedKeys,
}) => {
  const { Item } = Form
  const onCellChange = (key, dataIndex) => {
    return (value) => {
      const update = page.content.find(o => o.id === key)
      update[dataIndex] = value
      dispatch({ type: 'REAPRB0003/update', role: update })
    }
  }
  const columns = [
    {
      title: '名称',
      dataIndex: 'name',
      key: 'name',
      width: '30%',
      render: (text, record) => (
        <EditableCell
          value={text}
          onChange={onCellChange(record.id, 'name')}
        />
      ),
    },
    {
      title: '备注',
      dataIndex: 'remark',
      key: 'remark',
      width: '30%',
      render: (text, record) => (
        <EditableCell
          value={text}
          onChange={onCellChange(record.id, 'remark')}
        />
      ),
    },
    {
      title: '创建时间',
      width: '10%',
      dataIndex: 'createTime',
      key: 'createTime',
      render: text => (text ? moment(text).format('YYYY-MM-DD HH:mm:ss') : null),
    },
    {
      title: '操作',
      width: '15%',
      dataIndex: 'operation',
      render: (text, record) => {
        return (

          <span>
            <Popconfirm title="确认删除?" onConfirm={() => dispatch({ type: 'REAPRB0003/delete', id: record.id })}>
              <a href="#">删除</a>
            </Popconfirm>
            <Divider type="vertical" />
            <a href="#"
              onClick={(e) => {
              e.preventDefault()
              dispatch({ type: 'REAPRB0003/showTransferModal', role: record })
          }}
            >分配功能</a>
          </span>
        )
      },
    },
  ]

  return (
    <Col >
      <Row>
        <Row gutter={{ md: 8, lg: 24, xl: 48 }}>
          <Col span={8}>
            <Item label="名称" {...formItemLayout}>
              <Input
                onChange={(e) => {
                const { value } = e.target
                dispatch({
                  type: 'REAPRB0003/setState',
                  search: {
                    name: value,
                  },
                })
              }}
              />
            </Item>
          </Col>
          <Col span={8}>
            <Item {...formItemLayout}>
              <Button
                type="primary"
                htmlType="button"
                icon="search"
                onClick={
                () => dispatch({ type: 'REAPRB0003/query' })
              }
              >
            查询
              </Button>
            </Item>
          </Col>
        </Row>

      </Row>

      <Col>
        <RoleForm showCreateModal={showCreateModal} dispatch={dispatch} />
      </Col>
      <Col>
        <FunctionTransfer
          role={role}
          showTransferModal={showTransferModal}
          functions={functions}
          selectedKeys={selectedKeys}
          dispatch={dispatch}
        />
      </Col>
      <Col>
        <Table dataSource={page && page.content}
          pagination={{
            total: page && page.totalElements,
            showTotal: total => `总记录数 ${total} `,
            onChange: (number, size) => (dispatch({ type: 'REAPRB0003/query', page: number - 1, size })),
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
