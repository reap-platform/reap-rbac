import React from 'react'
import { Row, Col } from 'antd'
import OrgTree from '../components/REAPRB0001/OrgTree'
import OrgList from '../components/REAPRB0001/OrgList'
import styles from './REAPRB0001.less'

export default ({
  page, orgs, dispatch, selected, search, effects, showCreateModal,
}) => {
  return (
    <div className={styles.orgContainer}>
      <Row>
        <Row gutter={24}>
          <Col span={6}>
            <OrgTree orgs={orgs} dispatch={dispatch} loading={effects['REAPRB0001/orgTree']} />
          </Col>
          <Col span={18}>
            <OrgList page={page} dispatch={dispatch} selected={selected} search={search} showCreateModal={showCreateModal} loading={effects['REAPRB0001/query']} />
          </Col>
        </Row>
      </Row>
    </div>
  )
}
