<script setup lang="ts">
import { ref } from 'vue'
import { useAuthStore } from '@/stores/auth'
import PasskeyRegistration from '@/components/PasskeyRegistration.vue'

type Tab = 'profile' | 'security'

const auth = useAuthStore()
const activeTab = ref<Tab>('profile')

const tabs: { id: Tab; label: string }[] = [
  { id: 'profile', label: 'Profile' },
  { id: 'security', label: 'Security' },
]
</script>

<template>
  <section class="settings-view">
    <h2>Settings</h2>

    <nav class="settings-view__tabs">
      <button
        v-for="tab in tabs"
        :key="tab.id"
        type="button"
        class="settings-view__tab"
        :class="{ 'settings-view__tab--active': activeTab === tab.id }"
        @click="activeTab = tab.id"
      >
        {{ tab.label }}
      </button>
    </nav>

    <div v-if="activeTab === 'profile'" class="settings-view__panel">
      <p>Username: {{ auth.user?.username }}</p>
    </div>

    <div v-else-if="activeTab === 'security'" class="settings-view__panel">
      <PasskeyRegistration />
    </div>
  </section>
</template>

<style scoped>
.settings-view {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  max-width: 480px;
}

.settings-view__tabs {
  display: flex;
  gap: 0.5rem;
  border-bottom: 1px solid var(--color-border);
}

.settings-view__tab {
  border: none;
  background: none;
  color: var(--color-text);
  font-size: 1rem;
  font-family: inherit;
  padding: 0.6rem 0.9rem;
  cursor: pointer;
  border-bottom: 2px solid transparent;
}

.settings-view__tab--active {
  color: var(--color-primary);
  border-bottom-color: var(--color-primary);
  font-weight: 600;
}

.settings-view__panel {
  display: flex;
  flex-direction: column;
}
</style>
