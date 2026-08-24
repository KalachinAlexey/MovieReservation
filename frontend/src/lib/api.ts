import { getAccessToken } from './auth'

const BASE_URL = import.meta.env.VITE_API_URL ?? ''

export type Film = { id: number; title: string; description: string; genre: string }
export type Hall = { id: number; rows: number; columns: number }
export type FilmEvent = { id: number; hallId: number; filmId: number; date: string }
export type Place = { id: number; filmEventId: number; row: number; column: number; status: 'BLOCKED' | 'EMPTY' | 'BOOKED'; reservationId: number | null; price: number }
export type Reservation = { id: number; username: string | null; totalPrice: number; status: 'RESERVED' | 'PAID' | 'CANCELLED' | null }

async function request<T>(path: string, options?: RequestInit): Promise<T> {
  const token = await getAccessToken()
  const headers = new Headers(options?.headers)

  if (options?.body) headers.set('Content-Type', 'application/json')
  if (token) headers.set('Authorization', `Bearer ${token}`)

  const response = await fetch(`${BASE_URL}${path}`, {
    ...options,
    headers,
  })
  const text = await response.text()
  if (!response.ok) {
    let detail = text
    try {
      const body = JSON.parse(text)
      detail = body.message || body.error || text
    } catch { /* plain response */ }
    throw new Error(`${response.status} ${response.statusText}${detail ? `: ${detail}` : ''}`)
  }
  return text ? JSON.parse(text) as T : undefined as T
}

const post = <T>(path: string, body?: unknown) => request<T>(path, {
  method: 'POST',
  body: body === undefined ? undefined : JSON.stringify(body),
})

export const api = {
  getFilms: () => request<Film[]>('/films'),
  createFilm: (film: Omit<Film, 'id'>) => post<void>('/films', film),
  getHalls: () => request<Hall[]>('/halls'),
  createHall: (hall: Hall) => post<void>('/halls', hall),
  getEvents: () => request<FilmEvent[]>('/events'),
  createEvent: (event: Omit<FilmEvent, 'id'>) => post<void>('/events', event),
  getPlaces: (eventId: number) => request<Place[]>(`/places/${eventId}`),
  bookPlaces: (places: Place[]) => post<number>('/places', places),
  getReservation: (id: number) => request<Reservation>(`/reservations/${id}`),
  payReservation: (id: number) => post<Reservation>(`/reservations/${id}/pay`),
  cancelReservation: (id: number) => post<Reservation>(`/reservations/${id}/cancell`),
}
