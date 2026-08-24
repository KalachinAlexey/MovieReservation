import { User, UserManager, WebStorageStateStore } from 'oidc-client-ts'

const AUTH_URL = import.meta.env.VITE_AUTH_URL ?? 'http://localhost:9000'

export const userManager = new UserManager({
  authority: AUTH_URL,
  client_id: 'cinema-frontend',
  redirect_uri: `${window.location.origin}/oauth/callback`,
  post_logout_redirect_uri: window.location.origin,
  response_type: 'code',
  scope: [
    'openid',
    'profile',
    'films:read',
    'films:write',
    'events:read',
    'events:write',
    'halls:read',
    'halls:write',
    'reservations:read',
    'reservations:write',
    'admin',
  ].join(' '),
  loadUserInfo: false,
  userStore: new WebStorageStateStore({ store: window.sessionStorage }),
})

export async function login() {
  await userManager.signinRedirect()
}

let callbackPromise: Promise<User> | null = null

export function completeLogin() {
  callbackPromise ??= userManager.signinRedirectCallback()
  return callbackPromise
}

export async function logout() {
  await userManager.signoutRedirect()
}

export async function register(username: string, password: string) {
  const response = await fetch(`${AUTH_URL}/auth/register`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password }),
  })

  if (response.status === 409) {
    throw new Error('Пользователь с таким именем уже существует')
  }

  if (!response.ok) {
    const detail = await response.text()
    throw new Error(`Не удалось зарегистрироваться (${response.status})${detail ? `: ${detail}` : ''}`)
  }

  await userManager.signinRedirect({
    extraQueryParams: { login_hint: username },
  })
}

export async function getCurrentUser(): Promise<User | null> {
  const user = await userManager.getUser()
  return user && !user.expired ? user : null
}

export async function getAccessToken(): Promise<string | null> {
  return (await getCurrentUser())?.access_token ?? null
}
