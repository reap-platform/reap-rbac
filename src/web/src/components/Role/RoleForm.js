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
  form, dispatch, showCreateModal,
}) => {
  const { getFieldDecorator } = form


  return (
    <div>
      <Button type="primary"
        icon="plus"
        onClick={() => (dispatch({ type: 'Role/setState', showCreateModal: true }))}
      >新增</Button>
      <Modal title="新增角色"
        visible={showCreateModal}
        onOk={() => {
          form.validateFieldsAndScroll((err, values) => {
            if (!err) {
              dispatch({ type: 'Role/create', role: values, form })
            }
          })
        }}
        onCancel={() => {
          dispatch({ type: 'Role/setState', showCreateModal: false })
          form.resetFields()
        }}
      >
        <Form>
          <FormItem
            {...formItemLayout}
            label="名称"
          >
            {getFieldDecorator('name', {
            rules: [{
              required: true, message: '请输入名称',
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
