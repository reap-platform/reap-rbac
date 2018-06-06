import React from 'react'
import { Row } from 'antd'
import FunctionList from '../components/REAPRB0004/FunctionList'
import styles from './REAPRB0004.less'

export default ({
  page, dispatch, effects, showCreateModal,
}) => {
  return (
    <div className={styles.functionContainer}>
      <Row>
        <FunctionList page={page} dispatch={dispatch} showCreateModal={showCreateModal} loading={effects['REAPRB0004/query']} />
      </Row>
    </div>
  )
}
