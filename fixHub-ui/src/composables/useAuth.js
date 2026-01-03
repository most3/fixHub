import { ref, computed } from 'vue'

const TOKEN_KEY = 'fixhub_token'
const USER_KEY = 'fixhub_user'

const token = ref(localStorage.getItem(TOKEN_KEY) || '')
const user = ref(readUser())

function readUser () {
  try {
    const cached = localStorage.getItem(USER_KEY)
    return cached ? JSON.parse(cached) : null
  } catch (e) {
    console.error('failed to parse cached user', e)
    return null
  }
}

function persistToken (value) {
  try {
    if (value) localStorage.setItem(TOKEN_KEY, value)
    else localStorage.removeItem(TOKEN_KEY)
  } catch (e) {
    console.error('storage error', e)
  }
}

function persistUser (value) {
  try {
    if (value) localStorage.setItem(USER_KEY, JSON.stringify(value))
    else localStorage.removeItem(USER_KEY)
  } catch (e) {
    console.error('storage error', e)
  }
}

function setToken (t, persist = true) {
  token.value = t || ''
  if (persist) persistToken(token.value)
}

function setUser (u, persist = true) {
  user.value = u || null
  if (persist) persistUser(user.value)
}

function generateFallbackToken (sessionUser) {
  const base = sessionUser?.username || sessionUser?.userId || 'anonymous'
  const rand = Math.random().toString(36).slice(2, 10)
  return `${base}-${Date.now()}-${rand}`
}

function setSession (session) {
  if (!session) {
    clearToken()
    return
  }
  const { token: backendToken, message, ...userData } = session
  const sessionToken = backendToken || generateFallbackToken(userData)
  setToken(sessionToken)
  setUser(userData)
}

function clearToken () {
  token.value = ''
  user.value = null
  persistToken('')
  persistUser(null)
}

function getToken () {
  return token.value
}

const isAuthenticated = computed(() => !!token.value && !!user.value)

// listen cross-tab changes
window.addEventListener('storage', (e) => {
  if (e.key === TOKEN_KEY) {
    token.value = e.newValue || ''
  }
  if (e.key === USER_KEY) {
    if (!e.newValue) {
      user.value = null
    } else {
      try {
        user.value = JSON.parse(e.newValue)
      } catch (err) {
        console.error('failed to sync cached user', err)
        user.value = null
      }
    }
  }
})

export default function useAuth () {
  return { token, user, setToken, setUser, setSession, clearToken, isAuthenticated, getToken }
}

export { token, user, setToken, setUser, setSession, clearToken, isAuthenticated, getToken }
