<script setup lang="ts">
import { computed } from 'vue'
import { fieldRegistry, FieldType } from '@/fieldRegistry'

const props = defineProps<{
  field: {
    name: string
    type: FieldType
    required?: boolean
    value?: any
  }
  modelValue: any
}>()

const emit = defineEmits<{
  'update:modelValue': [value: any]
}>()

const component = computed(() => fieldRegistry[props.field.type])
</script>

<template>
  <component
    :is="component"
    :name="field.name"
    :required="field.required"
    :modelValue="modelValue"
    @update:modelValue="emit('update:modelValue', $event)"
  />
</template>
