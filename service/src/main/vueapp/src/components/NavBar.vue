<script setup lang="ts">
import { RouterLink } from 'vue-router'
import Logout from '@/components/Logout.vue'
import { useAuthStore } from '@/stores/auth'

const auth = useAuthStore()
</script>

<template>
  <nav class="nav-bar">
    <RouterLink to="/" class="nav-bar__brand">Jonas Andersen Admin</RouterLink>

    <div v-if="auth.isAuthenticated" class="nav-bar__links">
      <RouterLink to="/" class="nav-bar__link">Home</RouterLink>
      <RouterLink to="/settings" class="nav-bar__link">Settings</RouterLink>
    </div>

    <div v-if="auth.isAuthenticated" class="nav-bar__user">
      <span class="nav-bar__username">{{ auth.user?.username }}</span>
      <Logout />
    </div>
  </nav>
</template>

<style scoped>
.nav-bar {
  display: flex;
  align-items: center;
  gap: 1.5rem;
  padding: 0.9rem 2rem;
  border-bottom: 1px solid var(--color-border);
}

.nav-bar__brand {
  font-size: 1.1rem;
  font-weight: 700;
  color: var(--color-text);
}

.nav-bar__brand:hover {
  text-decoration: none;
}

.nav-bar__links {
  display: flex;
  flex: 1;
  gap: 1.25rem;
}

.nav-bar__link {
  color: var(--color-text);
  font-weight: 500;
  padding-bottom: 0.2rem;
  border-bottom: 2px solid transparent;
}

.nav-bar__link:hover {
  color: var(--color-primary);
  text-decoration: none;
}

.nav-bar__link.router-link-exact-active {
  color: var(--color-primary);
  border-bottom-color: var(--color-primary);
}

.nav-bar__user {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.nav-bar__username {
  color: var(--color-text);
  font-size: 0.95rem;
}
</style>
