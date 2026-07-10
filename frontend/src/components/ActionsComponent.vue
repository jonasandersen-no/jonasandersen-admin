<script setup lang="ts">
import HalForm from '@/components/HalForm.vue'
import { CommandConfigList } from '@/CommandConfigList'
import { CommandConfig } from '@/CommandConfig'

function buildActions(resource) {
  return Object.keys(resource.templates ?? {})
    .map((rel) => {
      const template = resource.getTemplate(rel)
      const link = resource.getLink(rel)

      if (!template || !link) {
        return undefined
      }

      return {
        name: rel,
        method: template.method,
        target: link.href,
        contentType: template.contentType,
        fields: (template.properties ?? []).map((property) => ({
          name: property.name,
          type: toFieldType(property),
          required: property.required,
          value: property.value,
        })),
      }
    })
    .filter((action) => action !== undefined)
}

function toFieldType(property) {
  if (property.type === 'number' || typeof property.value === 'number') {
    return 'number'
  }

  return 'text'
}

const props = defineProps<{ item: CommandConfig }>()
</script>

<template>
  <div class="actions" v-if="buildActions(item).length > 0">
    <HalForm
      v-for="action in buildActions(item)"
      :key="`${item.name}-${action.name}`"
      :form="action"
      :submit-label="action.name"
    />
  </div>
</template>

<style scoped>
.actions {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
</style>
