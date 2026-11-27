<script setup lang="ts">
</script>

<template>
  <div class="base-layout">
    <header class="base-layout__header">
      <slot name="header"></slot>
    </header>

    <main class="base-layout__main">
      <slot></slot>
    </main>

    <footer class="base-layout__footer">
      <slot name="footer"></slot>
    </footer>
  </div>
</template>

<style scoped>
.base-layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.base-layout__header {
  padding: 0;
}

.base-layout__main {
  flex: 1;
  padding: 2rem;
}

.base-layout__footer {
  padding: 1rem 2rem;
  border-top: 1px solid var(--color-border);
}
</style>
