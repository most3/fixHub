import client from './client'

export const createUser = (data) => {
  return client.post('/users', data)
}
