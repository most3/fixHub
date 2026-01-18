import client from './client'

export async function createOrder(userId, data) {
  const res = await client.post('/api/repairs', data, { params: { userId } })
  return res
}

export async function getMyOrders(userId) {
  const res = await client.get('/api/repairs/my', { params: { userId } })
  return res
}

export async function getAssignedOrders(technicianId) {
  const res = await client.get('/api/repairs/assigned', { params: { technicianId } })
  return res
}

export async function getAllOrders() {
  const res = await client.get('/api/repairs')
  return res
}

export async function assignOrder(orderId, technicianId) {
  const res = await client.post(`/api/repairs/${orderId}/assign`, null, { params: { technicianId } })
  return res
}

export async function completeOrder(orderId, technicianId, resultDesc) {
  const res = await client.post(`/api/repairs/${orderId}/complete`, resultDesc, { 
    params: { technicianId },
    headers: { 'Content-Type': 'text/plain' }
  })
  return res
}
