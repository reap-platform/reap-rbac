import React from 'react'
import { Row, Card } from 'antd'
import FunctionList from '../components/Function/FunctionList'
import styles from './Function.less'

export default ({
  page, dispatch, effects, showCreateModal,
}) => {
  return (
    <div className={styles.functionContainer}>
      <Row>
        <Card title="功能码维护" bordered={false}>
          <FunctionList page={page} dispatch={dispatch} showCreateModal={showCreateModal} loading={effects['Function/query']} />
        </Card>
      </Row>
    </div>
  )
}
