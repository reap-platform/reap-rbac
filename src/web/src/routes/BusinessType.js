import React from 'react'
import { Row } from 'antd'
import List from '../components/BusinessType/List'
import styles from './BusinessType.less'

export default ({
  page, dispatch, effects, showModal, showTransferModal, businessType, functions, selectedKeys,
}) => {
  return (
    <Row className={styles.BusinessTypeContainer}>
      <List showTransferModal={showTransferModal}
        businessType={businessType}
        functions={functions}
        selectedKeys={selectedKeys}
        page={page}
        dispatch={dispatch}
        showModal={showModal}
        loading={effects['BusinessType/search']}
      />
    </Row>
  )
}
