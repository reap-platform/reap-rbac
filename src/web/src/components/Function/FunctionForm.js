import React from 'react'
import { Form, Input, Modal, Button, Radio } from 'antd'

const RadioGroup = Radio.Group
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
        onClick={() => (dispatch({ type: 'REAPRB0004/setState', showCreateModal: true }))}
      >新增</Button>
      <Modal title="新建功能码"
        visible={showCreateModal}
        onOk={() => {
          form.validateFieldsAndScroll((err, values) => {
            if (!err) {
              dispatch({ type: 'REAPRB0004/create', func: values, form })
            }
          })
        }}
        onCancel={() => {
          dispatch({ type: 'REAPRB0004/setState', showCreateModal: false })
          form.resetFields()
        }}
      >
        <Form>
          <FormItem
            {...formItemLayout}
            label="功能码"
          >
            {getFieldDecorator('code', {
            rules: [{
              required: true, message: '请输入功能码',
            }],
          })(<Input />)}
          </FormItem>
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
          <FormItem
            {...formItemLayout}
            label="归属系统"
          >
            {getFieldDecorator('serviceId', {
            rules: [{
              required: true, message: '请输入归属系统',
            }],
          })(<Input />)}
          </FormItem>
          <FormItem
            {...formItemLayout}
            label="类型"
          >
            {getFieldDecorator('type', {
            rules: [{
              required: true, message: '请选择类型',
            }],
          })(<RadioGroup>
            <Radio value="M">菜单</Radio>
            <Radio value="O">操作</Radio>
          </RadioGroup>)}
          </FormItem>
          <FormItem
            {...formItemLayout}
            label="动作"
          >
            {getFieldDecorator('action')(<Input />)}
          </FormItem>
          <FormItem {...formItemLayout} label="备注">
            {getFieldDecorator('remark')(<Input />)}
          </FormItem>
        </Form>
      </Modal>
    </div>
  )
}
export default Form.create()(Component)
