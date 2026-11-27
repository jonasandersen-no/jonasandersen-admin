import useApiClient from '@/composables/useApiClient'

export const usePing = () => {
  const client = useApiClient()

  const fetchPing = async () => {
    return client.GET('/ping')
  }

  return { fetchPing }
}
