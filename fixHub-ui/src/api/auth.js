import axios from 'axios'

const client = axios.create({ baseURL: '/', timeout: 5000 })

export async function login (username, password) {
  const res = await client.post('/api/auth/login', { username, password })
  return res.data
}

export async function register (payload) {
  const res = await client.post('/api/auth/register', payload)
  return res.data
}

export async function me () {
  const token = localStorage.getItem('fixhub_token')
  if (!token) return null
  const res = await client.get('/api/auth/me', { headers: { Authorization: `Bearer ${token}` } })
  return res.data
}
