
export default {
  'PUT /apis/reap-rbac/org': ({ succ, body }) => {
    succ(body)
  },
  'GET /apis/reap-rbac/orgs': ({ succ }) => {
    succ({
      size: 20,
      page: 0,
      content: [
        {
          id: '1',
          name: '机构1',
          remark: '备注1',
          leaf: false,
        },
      ],
    })
  },
  'GET /apis/reap-rbac/orgs/tree': ({ succ }) => {
    succ([
      {
        id: '1',
        name: '机构1',
        remark: '备注1',
        leaf: false,
        childrens: [{
          id: '11',
          name: '机构11',
          remark: '备注11',
        },
        ],
      }, {
        id: '2',
        name: '机构2',
        remark: '备注2',
        leaf: true,
      },
    ])
  },
}
