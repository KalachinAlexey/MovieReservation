import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/films': { target: 'http://localhost:8080', changeOrigin: true },
      '/halls': { target: 'http://localhost:8080', changeOrigin: true },
      '/events': { target: 'http://localhost:8080', changeOrigin: true },
      '/places': { target: 'http://localhost:8080', changeOrigin: true },
      '/reservations': { target: 'http://localhost:8080', changeOrigin: true },
    },
  },
})
