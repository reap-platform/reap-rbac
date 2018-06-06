import React from 'react'
import { Row } from 'antd'
import OrgTree from '../components/REAPRB0001/OrgTree'
import OrgList from '../components/REAPRB0001/OrgList'
import styles from './REAPRB0001.less'

export default ({
  page, orgs, dispatch, selected, search, effects, showCreateModal,
}) => {
  return (
    <div className={styles.orgContainer}>
      <Row>
        <Row gutter={48}>
          <OrgTree orgs={orgs} dispatch={dispatch} loading={effects['REAPRB0001/orgTree']} />
          <OrgList page={page} dispatch={dispatch} selected={selected} search={search} showCreateModal={showCreateModal} loading={effects['REAPRB0001/query']} />
        </Row>
      </Row>
    </div>
  )
}
