<script setup lang="ts">
import { useAuthStore } from '@/stores/auth'

const auth = useAuthStore()
</script>

<template>
  <section>
    <h2>Home</h2>
    <p>Welcome, {{ auth.user?.username }}.</p>
  </section>
</template>