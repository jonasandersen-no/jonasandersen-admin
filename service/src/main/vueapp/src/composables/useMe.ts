import { useAuthStore } from '@/stores/auth'

export const useMe = () => {
  const auth = useAuthStore()

  return { me: auth.loadUser, user: auth.user }
}
