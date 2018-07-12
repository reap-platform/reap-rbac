import React from 'react'
import { Row, Card } from 'antd'
import RoleList from '../components/Role/RoleList'
import styles from './Role.less'

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
            loading={effects['Role/query']}
          />
        </Card>
      </Row>
    </div>
  )
}
