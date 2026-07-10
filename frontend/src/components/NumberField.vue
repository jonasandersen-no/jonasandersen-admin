<script setup lang="ts">
const props = defineProps<{
  modelValue: number | null
  name: string
  required?: boolean
}>()

const emit = defineEmits<{
  'update:modelValue': [value: number | null]
}>()
</script>

<template>
  <input
    type="number"
    :name="name"
    :required="required"
    :value="modelValue ?? ''"
    @input="emit('update:modelValue', Number(($event.target as HTMLInputElement).value))"
  />
</template>
