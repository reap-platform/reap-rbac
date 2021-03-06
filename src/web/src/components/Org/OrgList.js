import React from 'react'
import { Row, Col, Table, Popconfirm, Input, Form, Button, Card } from 'antd'
import moment from 'moment'
import EditableCell from '../EditableCell'
import OrgForm from './OrgForm'

const formItemLayout = {
  labelCol: {
    span: 6,
  },
  wrapperCol: {
    span: 16,
  },
}

const Component = ({
  businessTypes, page, dispatch, showCreateModal, selected, loading,
}) => {
  const { Item } = Form
  const onCellChange = (key, dataIndex) => {
    return (value) => {
      const updateOrg = page.content.find(o => o.id === key)
      updateOrg[dataIndex] = value
      dispatch({ type: 'Org/update', org: updateOrg })
    }
  }
  const columns = [
    {
      title: '机构代码',
      dataIndex: 'code',
      width: '10%',
      key: 'code',
      render: (text, record) => (
        <EditableCell
          value={text}
          onChange={onCellChange(record.id, 'code')}
        />
      ),
    },
    {
      title: '归属行业',
      dataIndex: 'businessTypeId',
      width: '10%',
      key: 'businessTypeId',
      render: (text) => {
        const businessType = businessTypes.find(bizTyp => bizTyp.id === text)
        if (businessType) {
          return businessType.name
        }
        return text
      },
    },
    {
      title: '机构名称',
      dataIndex: 'name',
      width: '20%',
      key: 'name',
      render: (text, record) => (
        <EditableCell
          value={text}
          onChange={onCellChange(record.id, 'name')}
        />
      ),
    },
    {
      title: '备注',
      width: '20%',
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
      title: '创建时间',
      width: '20%',
      dataIndex: 'createTime',
      key: 'createTime',
      render: text => (text ? moment(text).format('YYYY-MM-DD HH:mm:ss') : null),
    },
    {
      title: '操作',
      dataIndex: 'operation',
      render: (text, record) => {
        return (
          page && page.content && page.content.length > 0 ?
            (
              <Popconfirm title="确认删除?" onConfirm={() => dispatch({ type: 'Org/delete', id: record.id })}>
                <a href="#">删除</a>
              </Popconfirm>
            ) : null
        )
      },
    },
  ]

  return (
    <Card bordered={false}>
      <Row gutter={24}>
        <Form>
          <Row gutter={{ md: 8, lg: 24, xl: 48 }}>
            <Col md={8} sm={24}>
              <Item label="机构代码" {...formItemLayout}>
                <Input
                  onChange={(e) => {
                const { value } = e.target
                dispatch({
                  type: 'Org/setState',
                  search: {
                    code: value,
                  },
                })
              }}
                />
              </Item>
            </Col>
            <Col md={8} sm={24}>
              <Item label="机构名称" {...formItemLayout}>
                <Input
                  onChange={(e) => {
              const { value } = e.target
              dispatch({
                type: 'Org/setState',
                search: {
                  name: value,
                },
              })
            }}
                />
              </Item>
            </Col>
            <Col md={8} sm={24}>
              <Item {...formItemLayout}>
                <Button
                  type="primary"
                  htmlType="button"
                  icon="search"
                  onClick={
                () => dispatch({ type: 'Org/query', parentOrgId: selected && selected.key })
              }
                >
            查询
                </Button>
              </Item>
            </Col>
          </Row>
        </Form>
      </Row>

      <Col span={22}>
        <OrgForm businessTypes={businessTypes} showCreateModal={showCreateModal} selected={selected} dispatch={dispatch} />
      </Col>
      <Col span={22}>
        <Table dataSource={page && page.content}
          pagination={{
            total: page && page.totalElements,
            showTotal: total => `总记录数 ${total}`,
            onChange: (number, size) => (dispatch({
              type: 'Org/query', page: number - 1, size, parentOrgId: selected && selected.key,
             })),
        }}
          columns={columns}
          rowKey="id"
          loading={loading}
          bordered
        />
      </Col>
    </Card>
  )
}
export default Component
