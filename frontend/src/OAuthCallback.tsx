import { useEffect, useState } from 'react'
import { completeLogin } from './lib/auth'

export default function OAuthCallback() {
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    let active = true

    completeLogin()
      .then(() => window.location.replace('/'))
      .catch((reason: unknown) => {
        if (active) {
          setError(reason instanceof Error ? reason.message : 'Не удалось завершить вход')
        }
      })

    return () => { active = false }
  }, [])

  return <main className="auth-callback">
    <div className="panel max-w-lg text-center">
      {error
        ? <><h1 className="text-xl font-semibold">Ошибка входа</h1><p className="mt-3 text-rose-300">{error}</p><a className="button-secondary mt-5 inline-block" href="/">Вернуться</a></>
        : <><div className="loader mx-auto" /><p className="mt-4 text-slate-300">Завершаем безопасный вход…</p></>}
    </div>
  </main>
}
