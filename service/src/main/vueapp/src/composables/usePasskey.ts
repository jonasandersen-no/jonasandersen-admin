import { csrfFetch } from '@/composables/useApiClient'
import { useAuthStore } from '@/stores/auth'

function base64urlEncode(buffer: ArrayBuffer): string {
  let binary = ''
  new Uint8Array(buffer).forEach((byte) => (binary += String.fromCharCode(byte)))
  return window.btoa(binary).replace(/=/g, '').replace(/\+/g, '-').replace(/\//g, '_')
}

function base64urlDecode(value: string): ArrayBuffer {
  const base64 = value.replace(/-/g, '+').replace(/_/g, '/')
  const binary = window.atob(base64)
  const bytes = new Uint8Array(binary.length)
  for (let i = 0; i < binary.length; i++) {
    bytes[i] = binary.charCodeAt(i)
  }
  return bytes.buffer
}

interface CredentialDescriptorJSON {
  id: string
  type: string
  transports?: string[]
}

interface RegistrationOptionsJSON {
  rp: { id: string; name: string }
  user: { id: string; name: string; displayName: string }
  challenge: string
  pubKeyCredParams: { type: string; alg: number }[]
  timeout?: number
  excludeCredentials?: CredentialDescriptorJSON[]
  authenticatorSelection?: unknown
  attestation?: string
  extensions?: unknown
}

interface AuthenticationOptionsJSON {
  challenge: string
  timeout?: number
  rpId?: string
  allowCredentials?: CredentialDescriptorJSON[]
  userVerification?: string
  extensions?: unknown
}

export function isPasskeySupported(): boolean {
  return typeof window !== 'undefined' && !!window.PublicKeyCredential
}

async function fetchJson<T>(url: string): Promise<T> {
  const response = await csrfFetch(url, { method: 'POST' })
  if (!response.ok) {
    throw new Error(`Request to ${url} failed: HTTP ${response.status}`)
  }
  return response.json() as Promise<T>
}

export function usePasskey() {
  const auth = useAuthStore()

  async function registerPasskey(label: string) {
    if (!label) {
      throw new Error('A label for the passkey is required')
    }

    const options = await fetchJson<RegistrationOptionsJSON>('/webauthn/register/options')

    const publicKey = {
      ...options,
      user: { ...options.user, id: base64urlDecode(options.user.id) },
      challenge: base64urlDecode(options.challenge),
      excludeCredentials: (options.excludeCredentials ?? []).map((credential) => ({
        ...credential,
        id: base64urlDecode(credential.id),
      })),
    } as unknown as PublicKeyCredentialCreationOptions

    const credential = (await navigator.credentials.create({ publicKey })) as PublicKeyCredential
    const response = credential.response as AuthenticatorAttestationResponse

    const registrationRequest = {
      publicKey: {
        credential: {
          id: credential.id,
          rawId: base64urlEncode(credential.rawId),
          response: {
            attestationObject: base64urlEncode(response.attestationObject),
            clientDataJSON: base64urlEncode(response.clientDataJSON),
            transports: response.getTransports ? response.getTransports() : [],
          },
          type: credential.type,
          clientExtensionResults: credential.getClientExtensionResults(),
          authenticatorAttachment: credential.authenticatorAttachment,
        },
        label,
      },
    }

    const verifyResponse = await csrfFetch('/webauthn/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(registrationRequest),
    })
    if (!verifyResponse.ok) {
      throw new Error(`Could not register passkey: HTTP ${verifyResponse.status}`)
    }
    const verification = (await verifyResponse.json()) as { success?: boolean }
    if (!verification.success) {
      throw new Error('Registration failed')
    }
  }

  async function loginWithPasskey() {
    const options = await fetchJson<AuthenticationOptionsJSON>('/webauthn/authenticate/options')

    const publicKey = {
      ...options,
      challenge: base64urlDecode(options.challenge),
      allowCredentials: (options.allowCredentials ?? []).map((credential) => ({
        ...credential,
        id: base64urlDecode(credential.id),
      })),
    } as unknown as PublicKeyCredentialRequestOptions

    const credential = (await navigator.credentials.get({ publicKey })) as PublicKeyCredential
    const response = credential.response as AuthenticatorAssertionResponse

    const body = {
      id: credential.id,
      rawId: base64urlEncode(credential.rawId),
      response: {
        authenticatorData: base64urlEncode(response.authenticatorData),
        clientDataJSON: base64urlEncode(response.clientDataJSON),
        signature: base64urlEncode(response.signature),
        userHandle: response.userHandle ? base64urlEncode(response.userHandle) : undefined,
      },
      credType: credential.type,
      clientExtensionResults: credential.getClientExtensionResults(),
      authenticatorAttachment: credential.authenticatorAttachment,
    }

    const authResponse = await csrfFetch('/login/webauthn', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body),
    })
    if (!authResponse.ok) {
      throw new Error(`Passkey sign-in failed: HTTP ${authResponse.status}`)
    }
    const result = (await authResponse.json()) as { authenticated?: boolean }
    if (!result.authenticated) {
      throw new Error('Passkey sign-in failed')
    }

    await auth.loadUser(true)
  }

  return { isPasskeySupported, registerPasskey, loginWithPasskey }
}
