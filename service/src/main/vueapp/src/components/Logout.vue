<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const auth = useAuthStore()
const router = useRouter()
const error = ref('')

async function logout() {
  error.value = ''
  try {
    await auth.logout()
    await router.replace({ name: 'login' })
  } catch {
    error.value = 'Unable to log out. Please try again.'
  }
}
</script>

<template>
  <button type="button" class="btn" @click="logout">Log out</button>
  <p v-if="error">{{ error }}</p>
</template>