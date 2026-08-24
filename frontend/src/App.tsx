import { FormEvent, useCallback, useEffect, useMemo, useState } from 'react'
import { User } from 'oidc-client-ts'
import { api, Film, FilmEvent, Hall, Place, Reservation } from './lib/api'
import { getCurrentUser, login, logout, register, userManager } from './lib/auth'

type Notice = { kind: 'ok' | 'error'; text: string } | null

function App() {
  const [films, setFilms] = useState<Film[]>([])
  const [halls, setHalls] = useState<Hall[]>([])
  const [events, setEvents] = useState<FilmEvent[]>([])
  const [eventId, setEventId] = useState<number | null>(null)
  const [places, setPlaces] = useState<Place[]>([])
  const [selected, setSelected] = useState<number[]>([])
  const [reservationId, setReservationId] = useState('')
  const [reservation, setReservation] = useState<Reservation | null>(null)
  const [notice, setNotice] = useState<Notice>(null)
  const [loading, setLoading] = useState(true)
  const [authUser, setAuthUser] = useState<User | null>(null)

  const refresh = useCallback(async () => {
    try {
      const [nextFilms, nextHalls, nextEvents] = await Promise.all([api.getFilms(), api.getHalls(), api.getEvents()])
      setFilms(nextFilms); setHalls(nextHalls); setEvents(nextEvents); setNotice(null)
    } catch (error) { setNotice({ kind: 'error', text: message(error) }) }
    finally { setLoading(false) }
  }, [])

  useEffect(() => { void refresh() }, [refresh])
  useEffect(() => {
    const syncUser = () => { void getCurrentUser().then(setAuthUser) }
    syncUser()
    userManager.events.addUserLoaded(setAuthUser)
    userManager.events.addUserUnloaded(syncUser)
    userManager.events.addAccessTokenExpired(syncUser)

    return () => {
      userManager.events.removeUserLoaded(setAuthUser)
      userManager.events.removeUserUnloaded(syncUser)
      userManager.events.removeAccessTokenExpired(syncUser)
    }
  }, [])
  useEffect(() => {
    if (eventId === null) return
    api.getPlaces(eventId).then((data) => { setPlaces(data); setSelected([]) })
      .catch((error) => setNotice({ kind: 'error', text: message(error) }))
  }, [eventId])

  const selectedPlaces = useMemo(() => places.filter((place) => selected.includes(place.id)), [places, selected])
  const total = selectedPlaces.reduce((sum, place) => sum + place.price, 0)

  async function addFilm(event: FormEvent<HTMLFormElement>) {
    event.preventDefault(); const formElement = event.currentTarget; const form = new FormData(formElement)
    await run(async () => {
      await api.createFilm({ title: String(form.get('title')), description: String(form.get('description')), genre: String(form.get('genre')) })
      formElement.reset(); await refresh(); return 'Фильм добавлен'
    })
  }

  async function createAccount(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    const form = new FormData(event.currentTarget)
    const username = String(form.get('username')).trim()
    const password = String(form.get('password'))

    await run(async () => {
      await register(username, password)
      return 'Пользователь создан'
    })
  }

  async function addHall(event: FormEvent<HTMLFormElement>) {
    event.preventDefault(); const formElement = event.currentTarget; const form = new FormData(formElement)
    await run(async () => {
      await api.createHall({ id: Number(form.get('id')), rows: Number(form.get('rows')), columns: Number(form.get('columns')) })
      formElement.reset(); await refresh(); return 'Зал добавлен'
    })
  }

  async function addEvent(event: FormEvent<HTMLFormElement>) {
    event.preventDefault(); const formElement = event.currentTarget; const form = new FormData(formElement)
    await run(async () => {
      await api.createEvent({ filmId: Number(form.get('filmId')), hallId: Number(form.get('hallId')), date: new Date(String(form.get('date'))).toISOString() })
      formElement.reset(); await refresh(); return 'Сеанс создан, места сгенерированы'
    })
  }

  async function book() {
    if (!selectedPlaces.length) return
    await run(async () => {
      const id = await api.bookPlaces(selectedPlaces)
      setReservationId(String(id)); setReservation(await api.getReservation(id))
      if (eventId !== null) setPlaces(await api.getPlaces(eventId))
      setSelected([]); return `Бронь №${id} создана`
    })
  }

  async function findReservation(event?: FormEvent) {
    event?.preventDefault(); const id = Number(reservationId); if (!id) return
    await run(async () => { setReservation(await api.getReservation(id)); return `Бронь №${id} загружена` })
  }

  async function changeReservation(action: 'pay' | 'cancel') {
    if (!reservation) return
    await run(async () => {
      const updated = action === 'pay' ? await api.payReservation(reservation.id) : await api.cancelReservation(reservation.id)
      setReservation(updated); if (eventId !== null) setPlaces(await api.getPlaces(eventId))
      return action === 'pay' ? 'Бронь оплачена' : 'Бронь отменена'
    })
  }

  async function run(operation: () => Promise<string>) {
    try { setNotice(null); setNotice({ kind: 'ok', text: await operation() }) }
    catch (error) { setNotice({ kind: 'error', text: message(error) }) }
  }

  function togglePlace(place: Place) {
    if (place.status !== 'EMPTY') return
    setSelected((current) => current.includes(place.id) ? current.filter((id) => id !== place.id) : [...current, place.id])
  }

  return <div className="min-h-screen bg-ink text-slate-100">
    <header className="sticky top-0 z-20 border-b border-white/10 bg-ink/90 backdrop-blur">
      <div className="mx-auto flex max-w-7xl items-center justify-between px-5 py-4">
        <div><p className="text-xs font-semibold uppercase tracking-[.28em] text-amber-400">Cinema API Lab</p><h1 className="text-xl font-semibold">Панель тестирования бронирования</h1></div>
        <div className="flex flex-wrap items-center justify-end gap-3">
          {authUser
            ? <><span className="user-badge">{authUser.profile.preferred_username ?? authUser.profile.sub}</span><button className="button-secondary" onClick={() => void logout()}>Выйти</button></>
            : <button className="button-primary" onClick={() => void login()}>Войти</button>}
          <button className="button-secondary" onClick={() => void refresh()}>Обновить данные</button>
        </div>
      </div>
    </header>

    <main className="mx-auto max-w-7xl space-y-8 px-5 py-8">
      {notice && <div className={`notice ${notice.kind}`}>{notice.text}</div>}
      {!authUser && <section className="panel auth-panel">
        <div>
          <span className="eyebrow">Аккаунт</span>
          <h2 className="text-xl font-semibold">Создайте пользователя</h2>
          <p className="mt-2 max-w-xl text-sm text-slate-400">После регистрации откроется защищённая страница входа сервиса авторизации.</p>
        </div>
        <form className="auth-form" onSubmit={createAccount}>
          <input name="username" minLength={3} maxLength={100} autoComplete="username" placeholder="Логин" required />
          <input name="password" type="password" minLength={8} maxLength={100} autoComplete="new-password" placeholder="Пароль (минимум 8 символов)" required />
          <button className="button-primary">Зарегистрироваться</button>
        </form>
      </section>}
      <section className="grid gap-5 lg:grid-cols-3">
        <ResourceCard title="Фильмы" count={films.length} items={films.map((film) => `${film.id}. ${film.title}`)}>
          <form className="form" onSubmit={addFilm}><input name="title" placeholder="Название" required /><input name="genre" placeholder="Жанр" required /><textarea name="description" placeholder="Описание" required /><button className="button-primary">Добавить фильм</button></form>
        </ResourceCard>
        <ResourceCard title="Залы" count={halls.length} items={halls.map((hall) => `Зал ${hall.id} · ${hall.rows}×${hall.columns}`)}>
          <form className="form" onSubmit={addHall}><input name="id" type="number" min="1" placeholder="ID зала" required /><div className="grid grid-cols-2 gap-3"><input name="rows" type="number" min="1" placeholder="Ряды" required /><input name="columns" type="number" min="1" placeholder="Места" required /></div><button className="button-primary">Добавить зал</button></form>
        </ResourceCard>
        <ResourceCard title="Сеансы" count={events.length} items={events.map((item) => `#${item.id} · ${films.find((film) => film.id === item.filmId)?.title ?? `Фильм ${item.filmId}`} · ${formatDate(item.date)}`)}>
          <form className="form" onSubmit={addEvent}>
            <select name="filmId" required defaultValue=""><option value="" disabled>Выберите фильм</option>{films.map((film) => <option key={film.id} value={film.id}>{film.title}</option>)}</select>
            <select name="hallId" required defaultValue=""><option value="" disabled>Выберите зал</option>{halls.map((hall) => <option key={hall.id} value={hall.id}>Зал {hall.id} ({hall.rows}×{hall.columns})</option>)}</select>
            <input name="date" type="datetime-local" required /><button className="button-primary">Создать сеанс</button>
          </form>
        </ResourceCard>
      </section>

      <section className="panel">
        <div className="section-heading"><div><span className="eyebrow">Шаг 2</span><h2>Выбор мест</h2></div><select value={eventId ?? ''} onChange={(e) => setEventId(Number(e.target.value))}><option value="" disabled>Выберите сеанс</option>{events.map((item) => <option key={item.id} value={item.id}>Сеанс #{item.id} · {formatDate(item.date)}</option>)}</select></div>
        {eventId === null ? <Empty text="Выберите сеанс, чтобы загрузить схему зала" /> : places.length === 0 ? <Empty text="Для этого сеанса нет мест. Сеансы из data.sql не вызывают addPlacesForEvent — создайте новый сеанс через форму выше." /> : <>
          <div className="screen">Экран</div>
          <div className="seat-map">{groupByRow(places).map(([row, rowPlaces]) => <div className="seat-row" key={row}><span className="row-label">{row}</span><div className="seat-list">{rowPlaces.map((place) => <button key={place.id} className={`seat ${place.status.toLowerCase()} ${selected.includes(place.id) ? 'selected' : ''}`} onClick={() => togglePlace(place)} title={`Ряд ${place.row}, место ${place.column} · ${place.status} · ${place.price} ₽`}>{place.column}</button>)}</div></div>)}</div>
          <div className="booking-bar"><div><strong>{selected.length}</strong> мест выбрано · <strong>{total} ₽</strong></div><button className="button-primary" disabled={!selected.length} onClick={() => void book()}>Забронировать</button></div>
        </>}
      </section>

      <section className="panel">
        <div className="section-heading"><div><span className="eyebrow">Шаг 3</span><h2>Управление бронью</h2></div><form className="lookup" onSubmit={findReservation}><input value={reservationId} onChange={(e) => setReservationId(e.target.value)} type="number" min="1" placeholder="ID брони" /><button className="button-secondary">Найти</button></form></div>
        {reservation ? <div className="reservation-card"><div><span>ID</span><strong>#{reservation.id}</strong></div><div><span>Статус</span><strong className="status">{reservation.status ?? '—'}</strong></div><div><span>Сумма</span><strong>{reservation.totalPrice} ₽</strong></div><div className="actions"><button className="button-primary" onClick={() => void changeReservation('pay')}>Оплатить</button><button className="button-danger" onClick={() => void changeReservation('cancel')}>Отменить</button></div></div> : <Empty text="Введите ID или создайте новую бронь" />}
      </section>
      {loading && <div className="fixed inset-0 z-50 grid place-items-center bg-ink/70 backdrop-blur-sm"><div className="loader" /></div>}
    </main>
  </div>
}

function ResourceCard({ title, count, items, children }: { title: string; count: number; items: string[]; children: React.ReactNode }) {
  return <article className="panel flex flex-col"><div className="section-heading"><h2>{title}</h2><span className="counter">{count}</span></div><div className="resource-list">{items.length ? items.map((item) => <div key={item}>{item}</div>) : <span>Пока пусто</span>}</div><div className="mt-auto border-t border-white/10 pt-5">{children}</div></article>
}

function Empty({ text }: { text: string }) { return <div className="empty-state">{text}</div> }
function message(error: unknown) { return error instanceof Error ? error.message : 'Неизвестная ошибка' }
function formatDate(value: string) { return new Date(value).toLocaleString('ru-RU', { dateStyle: 'short', timeStyle: 'short' }) }
function groupByRow(places: Place[]): Array<[number, Place[]]> {
  const rows = new Map<number, Place[]>()
  places.forEach((place) => rows.set(place.row, [...(rows.get(place.row) ?? []), place]))
  return [...rows.entries()].sort(([a], [b]) => a - b).map(([row, values]) => [row, values.sort((a, b) => a.column - b.column)])
}

export default App
