<script setup lang="ts">
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { isPasskeySupported, usePasskey } from '@/composables/usePasskey'

const username = ref('')
const password = ref('')
const error = ref('')
const auth = useAuthStore()
const route = useRoute()
const router = useRouter()
const passkey = usePasskey()
const passkeySupported = isPasskeySupported()

function redirectTarget() {
  const redirect = route.query.redirect
  return typeof redirect === 'string' && redirect.startsWith('/') && !redirect.startsWith('//')
    ? redirect
    : '/'
}

async function login() {
  error.value = ''

  try {
    await auth.login(username.value, password.value)
    await router.replace(redirectTarget())
  } catch {
    error.value = 'Invalid username or password.'
  }
}

async function loginWithPasskey() {
  error.value = ''

  try {
    await passkey.loginWithPasskey()
    await router.replace(redirectTarget())
  } catch {
    error.value = 'Passkey sign-in failed. Please try again or use your password.'
  }
}
</script>

<template>
  <form class="login-form" @submit.prevent="login">
    <input
      v-model="username"
      type="text"
      placeholder="Username"
      autocomplete="username"
      required
    />
    <input
      v-model="password"
      type="password"
      placeholder="Password"
      autocomplete="current-password"
      required
    />
    <button type="submit" class="btn login-form__submit">Log in</button>
    <button
      v-if="passkeySupported"
      type="button"
      class="btn login-form__submit"
      @click="loginWithPasskey"
    >
      Sign in with a passkey
    </button>
    <p v-if="error" class="login-form__error">{{ error }}</p>
  </form>
</template>

<style scoped>
.login-form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  width: 100%;
  max-width: 320px;
}

.login-form__submit {
  width: 100%;
}

.login-form__error {
  color: #d64545;
}
</style>