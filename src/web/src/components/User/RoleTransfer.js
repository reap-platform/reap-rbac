import React from 'react'
import { Transfer, Modal } from 'antd'

export default ({
  dispatch, showTransferModal, roles, user, selectedKeys,
}) => {
  const handleChange = (nextTargetKeys) => {
    dispatch({
      type: 'User/setState',
      user: { ...user, roles: roles.filter(r => nextTargetKeys.includes(r.id)) },
    })
  }

  const handleSelectChange = (sourceSelectedKeys, targetSelectedKeys) => {
    dispatch({ type: 'User/setState', selectedKeys: [...sourceSelectedKeys, ...targetSelectedKeys] })
  }

  return (
    <Modal title="分配角色"
      visible={showTransferModal}
      onOk={() => {
        dispatch({ type: 'User/allocateRole' })
  }}
      onCancel={() => {
    dispatch({ type: 'User/resetRoleState' })
  }}
    >
      <Transfer
        dataSource={roles.map(r => ({ key: r.id, title: r.name, description: r.remark }))}
        titles={['可分配', '已分配']}
        targetKeys={user.roles && user.roles.map(r => r.id)}
        selectedKeys={selectedKeys}
        onChange={handleChange}
        onSelectChange={handleSelectChange}
        render={item => item.title}
      />
    </Modal>
  )
}

