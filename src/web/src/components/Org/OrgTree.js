import React from 'react'
import { Tree, Spin, Icon, Card } from 'antd'

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
    <Card title="机构树" bordered={false}>
      {
        orgs && orgs.length > 0 ? (
          <Spin spinning={loading}>
            <Tree
              defaultExpandedKeys={['root']}
              showIcon
              showLine
              onSelect={(selectedKeys, { selected, selectedNodes }) => dispatch({ type: 'Org/select', selected: selected ? selectedNodes[0].props.data : null })}
            >
              <TreeNode
                key="root"
                onSelect={() => dispatch({ type: 'Org/select', selected: null })}
                icon={<Icon type="home" />}
                title=""
              >
                {loop(orgs)}
              </TreeNode>
            </Tree>
          </Spin>
        ) : <div>暂无数据</div>
      }

    </Card>
  )
}
export default Component
