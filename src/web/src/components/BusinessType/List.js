import React from 'react'
import { Row, Col, Table, Popconfirm, Input, Form, Button, Card, Divider } from 'antd'
import moment from 'moment'
import EditableCell from '../EditableCell'

import CreateForm from './Form'
import FunctionTransfer from './FunctionTransfer'

const formItemLayout = { labelCol: { span: 8 }, wrapperCol: { span: 16 } }

const idKey = 'id'

const Component = ({
  page, dispatch, showModal, loading, businessType, showTransferModal, functions, selectedKeys,
}) => {
  const { Item } = Form
  const onCellChange = (key, dataIndex) => {
    return (value) => {
      const data = {}
      data[idKey] = key
      data[dataIndex] = value
      dispatch({ type: 'BusinessType/update', data })
    }
  }

  const columns = [
    {
      title: '机构类型名称',
      dataIndex: 'name',
      width: '30%',
      key: 'name',
      render: (text, record) => (<EditableCell value={text} onChange={onCellChange(record[idKey], 'name')} />),
    },
    {
      title: '创建时间',
      dataIndex: 'createdTime',
      key: 'createdTime',
      width: '30%',
      render: text => (text ? moment(text).format('YYYY-MM-DD HH:mm:ss') : null),
    },
    {
      title: '备注',
      dataIndex: 'remark',
      width: '20%',
      key: 'remark',
      render: (text, record) => (<EditableCell value={text} onChange={onCellChange(record[idKey], 'remark')} />),
    },
    {
      title: '操作',
      dataIndex: 'operation',
      width: '20%',
      render: (text, record) => {
        return (
          <span>
            <Popconfirm title="确认删除?" onConfirm={() => dispatch({ type: 'BusinessType/remove', id: record[idKey] })}>
              <a href="#">删除</a>
            </Popconfirm>
            <Divider type="vertical" />
            <a href="#"
              onClick={(e) => {
            e.preventDefault()
            dispatch({ type: 'BusinessType/showTransferModal', businessType: record })
        }}
            >分配功能</a>
          </span>

        )
      },
    },
  ]

  return (
    <Card title="机构类型维护" bordered={false}>
      <Row>
        <Col md={6}>
          <Item label="业务类型名称" {...formItemLayout}>
            <Input onChange={e => dispatch({ type: 'BusinessType/setState', search: { name: e.target.value } })} />
          </Item>
        </Col>
        <Col md={6}>
          <Item label="备注" {...formItemLayout}>
            <Input onChange={e => dispatch({ type: 'BusinessType/setState', search: { remark: e.target.value } })} />
          </Item>
        </Col>
        <Col md={6}>
          <Item {...{ wrapperCol: { span: 16, push: 2 } }}>
            <Button type="primary" htmlType="button" icon="search" onClick={() => dispatch({ type: 'BusinessType/search' })} >查询 </Button>
          </Item>
        </Col>
      </Row>
      <Row>
        <CreateForm showModal={showModal} dispatch={dispatch} />
      </Row>
      <Row>
        <FunctionTransfer
          businessType={businessType}
          showTransferModal={showTransferModal}
          functions={functions}
          selectedKeys={selectedKeys}
          dispatch={dispatch}
        />
      </Row>
      <Row>
        <Table
          dataSource={page && page.content}
          pagination={{
 total: page && page.totalElements,
        showTotal: total => `总记录数 ${total} `,
        showSizeChanger: true,
        showQuickJumper: true,
        onShowSizeChange: (number, size) => (dispatch({ type: 'BusinessType/search', page: number - 1, size })),
        onChange: (number, size) => (dispatch({ type: 'BusinessType/search', page: number - 1, size })),
}}
          rowKey={idKey}
          columns={columns}
          loading={loading}
          bordered
        />
      </Row>
    </Card>
  )
}
export default Component
