import React from 'react'
import { Form, Input, Modal, Button, Radio } from 'antd'
import OrgSelect from './OrgSelect'

const FormItem = Form.Item

const RadioGroup = Radio.Group

const formItemLayout = {
  labelCol: {
    xs: { span: 6 },
  },
  wrapperCol: {
    xs: { span: 12 },
  },
}

const Component = ({
  form, dispatch, showCreateModal, orgs,
}) => {
  const { getFieldDecorator } = form


  return (
    <div>
      <Button type="primary"
        icon="plus"
        onClick={() => (dispatch({ type: 'User/setState', showCreateModal: true }))}
      >新增</Button>
      <Modal title="新增机构"
        visible={showCreateModal}
        onOk={() => {
          form.validateFieldsAndScroll((err, values) => {
            if (!err) {
              dispatch({ type: 'User/create', user: values, form })
            }
          })
        }}
        onCancel={() => {
          dispatch({ type: 'User/setState', showCreateModal: false })
          form.resetFields()
        }}
      >
        <Form>

          <FormItem
            {...formItemLayout}
            label="归属机构"
          >
            {getFieldDecorator('orgId', {
            rules: [{
              required: true, message: '请选择机构',
            }],
          })(<OrgSelect orgs={orgs} onFocus={() => dispatch({ type: 'User/orgTree' })} />)
           }
          </FormItem>

          <FormItem
            {...formItemLayout}
            label="用户名"
          >
            {getFieldDecorator('username', {
            rules: [{
              required: true, message: '请输入用户名',
            }],
          })(<Input />)}
          </FormItem>


          <FormItem
            {...formItemLayout}
            label="姓别"
          >
            {getFieldDecorator('gender', {
            rules: [{
              required: true, message: '请选择性别',
            }],
          })(<RadioGroup>
            <Radio value="M">男</Radio>
            <Radio value="F">女</Radio>
          </RadioGroup>)}
          </FormItem>

          <FormItem
            {...formItemLayout}
            label="姓名"
          >
            {getFieldDecorator('name', {
            rules: [{
              required: true, message: '请输入姓名',
            }],
          })(<Input />)}
          </FormItem>
          <FormItem
            {...formItemLayout}
            label="邮箱"
          >
            {getFieldDecorator('email', {
            rules: [{
              required: true, message: '请输入邮箱',
            }, {
              type: 'email', message: '请输入合法的邮箱!',
            }],
          })(<Input />)}
          </FormItem>

          <FormItem
            {...formItemLayout}
            label="电话号码"
          >
            {getFieldDecorator('phoneNo', {
            rules: [{ required: true, message: '请输入电话号码' }],
          })(<Input />)}
          </FormItem>

          <FormItem
            {...formItemLayout}
            label="密码"
          >
            {getFieldDecorator('password', {
            initialValue: '111111',
            rules: [{
              required: true, message: '请输入默认密码，默认为111111',
            }],
          })(<Input type="password" />)}
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
