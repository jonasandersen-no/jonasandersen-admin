<script setup>
import { onMounted, ref } from 'vue'
import axios from 'axios'
import { CommandConfigList } from '@/CommandConfigList.js'
import ActionsComponent from '@/components/ActionsComponent.vue'

const data = ref(undefined)

onMounted(async () => {
  data.value = await loadCommandConfigs()
})

async function loadCommandConfigs() {
  const response = await axios.get('http://localhost:8080/api/commands')
  return CommandConfigList.fromJson(response.data)
}

</script>

<template>
  <main v-if="data">
    <section v-for="item in data.items" :key="item.name" class="resource-card">
      <h3>{{ item.name }}</h3>
      <p><strong>type:</strong> {{ item.type }}</p>
      <p><strong>description:</strong> {{ item.metadata?.description }}</p>
      <pre>{{ item.spec }}</pre>

      <ActionsComponent :item="item"/>
    </section>
  </main>
</template>

<style scoped>
.resource-card {
  border: 1px solid #d4d4d4;
  border-radius: 8px;
  padding: 12px;
  margin-bottom: 12px;
}


</style>
