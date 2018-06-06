import React from 'react'
import { Row } from 'antd'
import UserList from '../components/REAPRB0002/UserList'
import styles from './REAPRB0002.less'

export default ({
  page, orgs, dispatch, selected, search, effects, showCreateModal, user, selectedKeys, roles, showTransferModal,
}) => {
  return (
    <div className={styles.userContainer}>
      <Row>
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
      </Row>
    </div>
  )
}
