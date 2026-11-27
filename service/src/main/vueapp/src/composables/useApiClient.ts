import createClient from 'openapi-fetch'
import type { paths } from '@/generated/api'
import { useAuthStore } from '@/stores/auth'
import { router } from '@/router'

export function getCsrfToken() {
  if (typeof document === 'undefined') return ''
  const cookie = document.cookie
    .split('; ')
    .find((row) => row.startsWith('XSRF-TOKEN='))
  return cookie ? decodeURIComponent(cookie.slice('XSRF-TOKEN='.length)) : ''
}

export const apiClient = createClient<paths>({
  baseUrl: '/api',
  credentials: 'same-origin',
})

const MUTATING_METHODS = new Set(['post', 'put', 'patch', 'delete'])

export async function csrfFetch(input: RequestInfo | URL, init: RequestInit = {}) {
  const headers = new Headers(init.headers)
  const method = (init.method ?? (input instanceof Request ? input.method : 'GET')).toLowerCase()
  const token = getCsrfToken()

  if (MUTATING_METHODS.has(method) && token) {
    headers.set('X-XSRF-TOKEN', token)
  }

  return fetch(input, {
    ...init,
    credentials: init.credentials ?? 'same-origin',
    headers,
  })
}

apiClient.use({
  async onRequest({ request }) {
    if (MUTATING_METHODS.has(request.method.toLowerCase())) {
      const token = getCsrfToken()
      if (token) {
        request.headers.set('X-XSRF-TOKEN', token)
      }
    }
    return request
  },

  async onResponse({ request, response }) {
    const url = new URL(request.url)
    const isAuthCheck = url.pathname === '/api/me' || url.pathname === '/api/login'

    if (response.status === 401 && !isAuthCheck) {
      const auth = useAuthStore()
      auth.setUser(null)
      if (router.currentRoute.value.name !== 'login') {
        await router.push({
          name: 'login',
          query: { redirect: router.currentRoute.value.fullPath },
        })
      }
    }

    return response
  },
})

export default function useApiClient() {
  return apiClient
}