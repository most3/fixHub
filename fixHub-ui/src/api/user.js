import client from './client'

// 管理员创建用户接口：指向后端 API 路径 /api/users
export const createUser = (data) => {
  return client.post('/api/users', data)
}
