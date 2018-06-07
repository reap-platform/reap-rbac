import React from 'react'
import { Row, Card } from 'antd'
import FunctionList from '../components/REAPRB0004/FunctionList'
import styles from './REAPRB0004.less'

export default ({
  page, dispatch, effects, showCreateModal,
}) => {
  return (
    <div className={styles.functionContainer}>
      <Row>
        <Card title="功能码维护" bordered={false}>
          <FunctionList page={page} dispatch={dispatch} showCreateModal={showCreateModal} loading={effects['REAPRB0004/query']} />
        </Card>
      </Row>
    </div>
  )
}
