import client from './client'

export async function login (username, password) {
  const res = await client.post('/api/auth/login', { username, password })
  return res.data
}

export async function register (payload) {
  const res = await client.post('/api/auth/register', payload)
  return res.data
}

export async function me () {
  const res = await client.get('/api/auth/me')
  return res.data
}
