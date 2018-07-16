import React from 'react'
import { Row, Col } from 'antd'
import OrgTree from '../components/Org/OrgTree'
import OrgList from '../components/Org/OrgList'
import styles from './Org.less'

export default ({
  page, orgs, dispatch, selected, search, effects, showCreateModal, businessTypes,
}) => {
  return (
    <div className={styles.orgContainer}>
      <Row>
        <Row gutter={24}>
          <Col span={6}>
            <OrgTree orgs={orgs} dispatch={dispatch} loading={effects['Org/orgTree']} />
          </Col>
          <Col span={18}>
            <OrgList businessTypes={businessTypes} page={page} dispatch={dispatch} selected={selected} search={search} showCreateModal={showCreateModal} loading={effects['Org/query']} />
          </Col>
        </Row>
      </Row>
    </div>
  )
}
