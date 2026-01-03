import client from './client'

export async function getDashboardStats() {
  const res = await client.get('/api/stats/dashboard')
  return res.data
}
