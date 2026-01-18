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

// Response interceptor: 统一将错误信息映射为中文友好提示
client.interceptors.response.use(
  response => {
    // 适配后端统一响应结构 Result<T>
    const res = response.data
    // 如果是 Result 结构 (包含 code 字段)
    if (res && typeof res.code !== 'undefined') {
      if (res.code === 200) {
        return res.data // 直接返回数据载荷
      } else {
        // 业务异常，抛出错误进入 error handler
        const error = new Error(res.message || '系统错误')
        error.response = response
        return Promise.reject(error)
      }
    }
    // 非统一响应（如静态资源），原样返回
    return res
  },
  error => {
    const defaultMsg = '网络错误，请稍后重试'
    const resp = error.response
    let human = defaultMsg
    if (resp) {
      // 优先使用后端返回的数据中的 message
      if (resp.data && resp.data.message) {
        human = resp.data.message
      } else if (resp.data && resp.data.errors) {
        // Bean Validation 返回的 errors 字段，组装为可读的中文提示
        try {
          const errs = resp.data.errors
          const parts = []
          for (const key in errs) {
            if (Object.prototype.hasOwnProperty.call(errs, key)) {
              parts.push(`${errs[key]}`)
            }
          }
          human = parts.length ? parts.join('；') : '请求参数错误，请检查输入'
        } catch (e) {
          human = '请求参数错误，请检查输入'
        }
      } else {
        switch (resp.status) {
          case 400:
            human = '请求参数错误，请检查输入'
            break
          case 401:
            human = '未授权：用户名或密码不正确'
            break
          case 403:
            human = '没有权限访问该资源'
            break
          case 404:
            human = '请求的资源不存在'
            break
          case 500:
            human = '服务器内部错误，请稍后重试'
            break
          default:
            human = `错误：${resp.status} ${resp.statusText}`
        }
      }
    } else if (error.code === 'ECONNABORTED') {
      human = '请求超时，请重试'
    }

    // 将中文提示挂到 error 对象，方便上层使用
    error.humanMessage = human
    error.message = human
    return Promise.reject(error)
  }
)

export default client
