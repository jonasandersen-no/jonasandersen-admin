import TextField from '@/components/TextField.vue'
import NumberField from '@/components/NumberField.vue'

export const fieldRegistry = {
  text: TextField,
  number: NumberField,
} as const

export type FieldType = keyof typeof fieldRegistry
