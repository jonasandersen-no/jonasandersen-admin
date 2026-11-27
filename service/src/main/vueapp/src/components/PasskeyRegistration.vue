<script setup lang="ts">
import { ref } from 'vue'
import { isPasskeySupported, usePasskey } from '@/composables/usePasskey'

const label = ref('')
const error = ref('')
const success = ref(false)
const supported = isPasskeySupported()
const passkey = usePasskey()

async function register() {
  error.value = ''
  success.value = false

  try {
    await passkey.registerPasskey(label.value)
    success.value = true
    label.value = ''
  } catch {
    error.value = 'Could not register passkey. Please try again.'
  }
}
</script>

<template>
  <section v-if="supported" class="passkey-registration">
    <h3>Passkeys</h3>
    <form class="passkey-registration__form" @submit.prevent="register">
      <input v-model="label" type="text" placeholder="Passkey name" required />
      <button type="submit" class="btn">Register a passkey</button>
    </form>
    <p v-if="success">Passkey registered successfully.</p>
    <p v-if="error" class="passkey-registration__error">{{ error }}</p>
  </section>
</template>

<style scoped>
.passkey-registration {
  margin-top: 1.5rem;
}

.passkey-registration__form {
  display: flex;
  gap: 0.5rem;
  max-width: 320px;
}

.passkey-registration__form input {
  flex: 1;
}

.passkey-registration__error {
  color: #d64545;
}
</style>
