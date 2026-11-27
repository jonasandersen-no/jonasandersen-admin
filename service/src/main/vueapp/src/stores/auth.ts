import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import type { components } from '@/generated/api'
import { apiClient, csrfFetch, getCsrfToken } from '@/composables/useApiClient'

type UserResponse = components['schemas']['user-response']

export const useAuthStore = defineStore('auth', () => {
  const user = ref<UserResponse | null>(null)
  const authLoaded = ref(false)
  let inflightCheck: Promise<UserResponse | null> | null = null

  const isAuthenticated = computed(() => user.value !== null)
  const hasRole = (role: string) => user.value?.roles?.includes(role) ?? false

  function setUser(value: UserResponse | null) {
    user.value = value
    authLoaded.value = true
  }

  async function loadUser(force = false) {
    if (authLoaded.value && !force) return user.value
    if (inflightCheck) return inflightCheck

    inflightCheck = apiClient.GET('/me')
      .then(({ data, response }) => {
        if (response.status === 401) {
          setUser(null)
          return null
        }
        if (!response.ok || !data) {
          throw new Error(`Unexpected /api/me status: ${response.status}`)
        }
        setUser(data)
        return data
      })
      .catch((error: unknown) => {
        authLoaded.value = false
        throw error
      })
      .finally(() => {
        inflightCheck = null
      })

    return inflightCheck
  }

  async function login(username: string, password: string) {
    if (!getCsrfToken()) {
      await loadUser(true)
    }

    const body = new URLSearchParams({ username, password })
    const response = await csrfFetch('/api/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body,
    })

    if (!response.ok) {
      throw new Error('Invalid username or password')
    }

    await loadUser(true)
  }

  async function logout() {
    const response = await csrfFetch('/api/logout', {
      method: 'POST',
    })

    if (!response.ok) {
      throw new Error(`Unable to log out: ${response.status}`)
    }

    setUser(null)
  }

  return { user, authLoaded, isAuthenticated, hasRole, setUser, loadUser, login, logout }
})