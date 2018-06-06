import React from 'react'
import { Transfer, Modal } from 'antd'

export default ({
  dispatch, showTransferModal, roles, user, selectedKeys,
}) => {
  const handleChange = (nextTargetKeys) => {
    dispatch({
      type: 'REAPRB0002/setState',
      user: { ...user, roles: roles.filter(r => nextTargetKeys.includes(r.id)) },
    })
  }

  const handleSelectChange = (sourceSelectedKeys, targetSelectedKeys) => {
    dispatch({ type: 'REAPRB0002/setState', selectedKeys: [...sourceSelectedKeys, ...targetSelectedKeys] })
  }

  return (
    <Modal title="分配角色"
      visible={showTransferModal}
      onOk={() => {
        dispatch({ type: 'REAPRB0002/allocateRole' })
  }}
      onCancel={() => {
    dispatch({ type: 'REAPRB0002/resetRoleState' })
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

