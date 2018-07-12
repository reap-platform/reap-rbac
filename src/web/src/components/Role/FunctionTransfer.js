import React from 'react'
import { Transfer, Modal } from 'antd'

export default ({
  dispatch, showTransferModal, functions, role, selectedKeys,
}) => {
  const handleChange = (nextTargetKeys) => {
    dispatch({
      type: 'REAPRB0003/setState',
      role: { ...role, functions: functions.filter(r => nextTargetKeys.includes(r.id)) },
    })
  }

  const handleSelectChange = (sourceSelectedKeys, targetSelectedKeys) => {
    dispatch({ type: 'REAPRB0003/setState', selectedKeys: [...sourceSelectedKeys, ...targetSelectedKeys] })
  }

  return (
    <Modal title="分配功能"
      width={800}
      visible={showTransferModal}
      onOk={() => {
        dispatch({ type: 'REAPRB0003/allocateFunction' })
  }}
      onCancel={() => {
    dispatch({ type: 'REAPRB0003/resetFunctionState' })
  }}
    >
      <Transfer
        dataSource={functions.map(r => ({ key: r.id, title: `${r.code}/${r.name}`, description: r.remark }))}
        titles={['可分配', '已分配']}
        targetKeys={role.functions && role.functions.map(r => r.id)}
        selectedKeys={selectedKeys}
        onChange={handleChange}
        onSelectChange={handleSelectChange}
        listStyle={{
          width: 350,
          height: 500,
        }}
        render={item => item.title}
      />
    </Modal>
  )
}

