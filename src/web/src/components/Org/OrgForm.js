import React from 'react'
import { Form, Input, Modal, Button } from 'antd'

const FormItem = Form.Item

const formItemLayout = {
  labelCol: {
    xs: { span: 6 },
  },
  wrapperCol: {
    xs: { span: 12 },
  },
}

const Component = ({
  form, selected, dispatch, showCreateModal,
}) => {
  const { getFieldDecorator } = form


  return (
    <div>
      <Button type="primary"
        icon="plus"
        onClick={() => (dispatch({ type: 'Org/setState', showCreateModal: true }))}
      >新增</Button>
      <Modal title="新增机构"
        visible={showCreateModal}
        onOk={() => {
          form.validateFieldsAndScroll((err, values) => {
            if (!err) {
              dispatch({ type: 'Org/create', org: values })
              form.resetFields()
            }
          })
        }}
        onCancel={() => {
          dispatch({ type: 'Org/setState', showCreateModal: false })
          form.resetFields()
        }}
      >
        <Form>
          {
        selected ? (
          <FormItem
            {...formItemLayout}
            label="父级机构"
          >
            <Input value={selected.title} readOnly />
          </FormItem>
        ) : null
      }
          <FormItem
            {...formItemLayout}
            label="机构代码"
          >
            {getFieldDecorator('code', {
            rules: [{
              required: true, message: '请输入机构代码',
            }],
          })(<Input />)}
          </FormItem>
          <FormItem
            {...formItemLayout}
            label="机构名称"
          >
            {getFieldDecorator('name', {
            rules: [{
              required: true, message: '请输入机构名称',
            }],
          })(<Input />)}
          </FormItem>
          <FormItem {...formItemLayout} label="备注">
            {getFieldDecorator('remark', { rules: [] })(<Input />)}
          </FormItem>
        </Form>
      </Modal>
    </div>
  )
}
export default Form.create()(Component)
