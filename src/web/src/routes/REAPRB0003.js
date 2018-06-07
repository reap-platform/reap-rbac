import React from 'react'
import { Row, Card } from 'antd'
import RoleList from '../components/REAPRB0003/RoleList'
import styles from './REAPRB0003.less'

export default ({
  page, dispatch, effects, showCreateModal, showTransferModal, role, functions, selectedKeys,
}) => {
  return (
    <div className={styles.roleContainer}>
      <Row>
        <Card title="角色管理" bordered={false}>
          <RoleList
            page={page}
            showTransferModal={showTransferModal}
            role={role}
            functions={functions}
            selectedKeys={selectedKeys}
            dispatch={dispatch}
            showCreateModal={showCreateModal}
            loading={effects['REAPRB0003/query']}
          />
        </Card>
      </Row>
    </div>
  )
}
