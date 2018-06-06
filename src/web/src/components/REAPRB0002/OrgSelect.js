import React from 'react'
import { TreeSelect } from 'antd'

const { TreeNode } = TreeSelect

export default class OrgSelect extends React.Component {
  render () {
    const loop = data => data.map(org => ({
      key: org.id, title: org.name, isLeaf: org.leaf, childrens: org.childs,
    })).map((item) => {
      if (item.childrens) {
        return (
          <TreeNode key={item.key} title={item.title} value={item.key}>
            {loop(item.childrens)}
          </TreeNode>
        )
      }
      return <TreeNode key={item.key} title={item.title} value={item.key} />
    })
    return (
      <TreeSelect
        {...this.props}
        filterTreeNode={(input, treeNode) => input === null || treeNode.props.title.includes(input)}
        showSearch
        placeholder="请选择"
        allowClear
        treeDefaultExpandAll
      >
        {loop(this.props.orgs)}
      </TreeSelect>
    )
  }
}
