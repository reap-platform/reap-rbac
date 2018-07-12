import React from 'react'
import { Row, Card } from 'antd'
import UserList from '../components/User/UserList'
import styles from './User.less'

export default ({
  page, orgs, dispatch, selected, search, effects, showCreateModal, user, selectedKeys, roles, showTransferModal,
}) => {
  return (
    <div className={styles.userContainer}>
      <Row>
        <Card title="用户管理" bordered={false}>
          <UserList
            user={user}
            roles={roles}
            showTransferModal={showTransferModal}
            selectedKeys={selectedKeys}
            orgs={orgs}
            page={page}
            dispatch={dispatch}
            selected={selected}
            search={search}
            showCreateModal={showCreateModal}
            loading={effects['REAPRB0002/query']}
          />
        </Card>
      </Row>

    </div>
  )
}
