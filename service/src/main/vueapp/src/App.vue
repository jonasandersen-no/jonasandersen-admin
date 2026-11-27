<script setup lang="ts">
import { ref, watch } from 'vue'
import { RouterView } from 'vue-router'
import BaseLayout from '@/components/BaseLayout.vue'
import NavBar from '@/components/NavBar.vue'
import { usePing } from '@/composables/usePing'
import { useAuthStore } from '@/stores/auth'
import type { components } from '@/generated/api'

const auth = useAuthStore()
const pingClient = usePing()

type PingResponse = components['schemas']['ping-response']

const ping = ref<PingResponse>()
let pingRequested = false

watch(() => auth.isAuthenticated, async (authenticated) => {
  if (!authenticated || pingRequested) return
  pingRequested = true
  const response = await pingClient.fetchPing()
  ping.value = response.data
}, { immediate: true })
</script>

<template>
  <BaseLayout>
    <template #header>
      <NavBar />
    </template>

    <RouterView />
    <p v-if="ping && auth.isAuthenticated">Data: {{ ping }}</p>
    <template #footer>
      <p>©️ Jonas Bjørge Andersen - version @{{ ping?.version }}</p>
    </template>
  </BaseLayout>
</template>
