import React from 'react'

import { Form, Input, Modal, Button } from 'antd'

const FormItem = Form.Item

const formItemLayout = {
  labelCol: {
    xs: { span: 6 },
  },
  wrapperCol: {
    xs: { span: 14 },
  },
}

const Component = ({
  form, dispatch, showModal,
}) => {
  const { getFieldDecorator } = form

  return (
    <div>
      <Button type="primary" icon="plus" style={{ marginBottom: 16 }} onClick={() => (dispatch({ type: 'BusinessType/setState', showModal: true }))} >新增</Button>
      <Modal
        title="创建机构类型"
        visible={showModal}
        onOk={() => {
          form.validateFieldsAndScroll((err, values) => {
            if (!err) {
              dispatch({ type: 'BusinessType/create', data: values, form })
            }
          })
        }}
        onCancel={() => {
          dispatch({ type: 'BusinessType/setState', showModal: false })
          form.resetFields()
        }}
      >
        <Form>
          <FormItem {...formItemLayout} label="业务类型名称" >
            {getFieldDecorator('name', { rules: [{ required: true, message: '请输入类型' }] })(<Input />)}
          </FormItem>
          <FormItem {...formItemLayout} label="备注" >
            {getFieldDecorator('remark', { rules: [{ message: '请输入类型' }] })(<Input />)}
          </FormItem>
        </Form>
      </Modal>
    </div>
  )
}
export default Form.create()(Component)
