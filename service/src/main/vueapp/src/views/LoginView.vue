<script setup lang="ts">
import Login from '@/components/Login.vue'
</script>

<template>
  <section class="login-view">
    <h2>Log in</h2>
    <Login />
  </section>
</template>

<style scoped>
.login-view {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1.5rem;
  max-width: 320px;
  margin: 3rem auto 0;
}
</style>