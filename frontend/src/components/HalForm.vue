<script setup>
import { reactive, watchEffect } from 'vue'
import DynamicField from '@/components/DynamicField.vue'

const props = defineProps({
  form: {
    type: Object,
    required: true,
  },
  submitLabel: {
    type: String,
    default: 'Submit',
  },
})

const values = reactive({})

watchEffect(() => {
  const fields = Array.isArray(props.form?.fields) ? props.form.fields : []

  fields.forEach((f) => {
    values[f.name] = f.value ?? ''
  })
})

async function submit() {
  const request = {
    method: props.form.method,
  }

  if (props.form.fields.length > 0) {
    request.headers = { 'Content-Type': props.form.contentType ?? 'application/json' }
    request.body = JSON.stringify(values)
  }

  await fetch(props.form.target, request)
}
</script>

<template>
  <form @submit.prevent="submit">
    <DynamicField
      v-for="field in props.form.fields"
      :key="field.name"
      :field="field"
      v-model="values[field.name]"
    />

    <button type="submit">{{ props.submitLabel }}</button>
  </form>
</template>
