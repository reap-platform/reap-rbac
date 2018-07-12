import React from 'react'
import { Row, Col, Table, Popconfirm, Input, Form, Button } from 'antd'
import EditableCell from '../EditableCell'
import FunctionForm from './FunctionForm'

const formItemLayout = {
  labelCol: {
    span: 6,
  },
  wrapperCol: {
    span: 16,
  },
}

const Component = ({
  page, dispatch, showCreateModal, loading,
}) => {
  const { Item } = Form
  const onCellChange = (key, dataIndex) => {
    return (value) => {
      const update = page.content.find(o => o.id === key)
      update[dataIndex] = value
      dispatch({ type: 'REAPRB0004/update', func: update })
    }
  }
  const columns = [
    {
      title: '功能码',
      dataIndex: 'code',
      width: '15%',
      key: 'code',
      render: (text, record) => (
        <EditableCell
          value={text}
          onChange={onCellChange(record.id, 'code')}
        />
      ),
    },
    {
      title: '归属系统',
      dataIndex: 'serviceId',
      width: '15%',
      key: 'serviceId',
      render: (text, record) => (
        <EditableCell
          value={text}
          onChange={onCellChange(record.id, 'serviceId')}
        />
      ),
    },
    {
      title: '功能名称',
      dataIndex: 'name',
      width: '15%',
      key: 'name',
      render: (text, record) => (
        <EditableCell
          value={text}
          onChange={onCellChange(record.id, 'name')}
        />
      ),
    },
    {
      title: '类型',
      width: '5%',
      dataIndex: 'type',
      key: 'type',
      render: text => ({ M: '菜单', O: '操作' }[text]),
    },
    {
      title: '动作',
      dataIndex: 'action',
      key: 'action',
      render: (text, record) => (
        <EditableCell
          value={text}
          onChange={onCellChange(record.id, 'action')}
        />
      ),
    },
    {
      title: '备注',
      width: '15%',
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
      render: (text, record) => {
        return (
          page && page.content && page.content.length > 0 ?
            (
              <Popconfirm title="确认删除?" onConfirm={() => dispatch({ type: 'REAPRB0004/delete', id: record.id })}>
                <a href="#">删除</a>
              </Popconfirm>
            ) : null
        )
      },
    },
  ]

  return (
    <Col>
      <Row gutter={{ md: 8, lg: 24, xl: 48 }}>
        <Col span={6}>
          <Item label="归属系统" {...formItemLayout}>
            <Input
              onChange={(e) => {
                const { value } = e.target
                dispatch({
                  type: 'REAPRB0004/setState',
                  search: {
                    serviceId: value || null,
                  },
                })
              }}
            />
          </Item>
        </Col>
        <Col span={6}>
          <Item label="名称" {...formItemLayout}>
            <Input
              onChange={(e) => {
                const { value } = e.target
                dispatch({
                  type: 'REAPRB0004/setState',
                  search: {
                    name: value || null,
                  },
                })
              }}
            />
          </Item>
        </Col>
        <Col span={6}>
          <Item label="功能码" {...formItemLayout}>
            <Input
              onChange={(e) => {
                const { value } = e.target
                dispatch({
                  type: 'REAPRB0004/setState',
                  search: {
                    code: value || null,
                  },
                })
              }}
            />
          </Item>
        </Col>
        <Col span={6}>
          <Item {...formItemLayout}>
            <Button
              type="primary"
              htmlType="button"
              icon="search"
              onClick={
                () => dispatch({ type: 'REAPRB0004/query' })
              }
            >
            查询
            </Button>
          </Item>
        </Col>
      </Row>
      <Row>
        <FunctionForm showCreateModal={showCreateModal} dispatch={dispatch} />
      </Row>
      <Row>
        <Table dataSource={page && page.content}
          pagination={{
            total: page && page.totalElements,
            showTotal: total => `总记录数 ${total} `,
            onChange: (number, size) => (dispatch({ type: 'REAPRB0004/query', page: number - 1, size })),
        }}
          rowKey="id"
          columns={columns}
          loading={loading}
          bordered
        />
      </Row>
    </Col>
  )
}
export default Component
