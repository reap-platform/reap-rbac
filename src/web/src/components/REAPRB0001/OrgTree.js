import React from 'react'
import { Col, Tree, Spin, Icon } from 'antd'

const { TreeNode } = Tree

const Component = ({
  orgs, dispatch, loading,
}) => {
  const loop = data => data.map(org => ({
    key: org.id, title: org.name, isLeaf: org.leaf, childrens: org.childs,
  })).map((item) => {
    if (item.childrens) {
      return (
        <TreeNode key={item.key} title={item.title} data={item}>
          {loop(item.childrens)}
        </TreeNode>
      )
    }
    return <TreeNode key={item.key} title={item.title} data={item} />
  })
  return (
    <Col span={6} >
      <Col span={20} push={2}>
        <Spin spinning={loading}>
          <Tree
            defaultExpandedKeys={['root']}
            showIcon
            showLine
            onSelect={(selectedKeys, { selected, selectedNodes }) => {
                    dispatch({ type: 'REAPRB0001/select', selected: selected ? selectedNodes[0].props.data : null })
                  }}
          >
            <TreeNode
              key="root"
              onSelect={() => dispatch({ type: 'REAPRB0001/select', selected: null })}
              icon={<Icon type="home" />}
              title="机构列表"
            >
              {loop(orgs)}
            </TreeNode>
          </Tree>
        </Spin>
      </Col>
    </Col>
  )
}
export default Component
