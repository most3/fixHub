import axios from 'axios'

const client = axios.create({
  baseURL: '/',
  timeout: 5000
})

// Request interceptor to add token
client.interceptors.request.use(config => {
  const token = localStorage.getItem('fixhub_token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export default client
