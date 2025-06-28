'use client'

import { useState, useEffect } from 'react'
import { useRouter } from 'next/navigation'

export default function LoginPage() {
  const router = useRouter()
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [loading, setLoading] = useState(false)
  const [message, setMessage] = useState('')
  const [checking, setChecking] = useState(true) // untuk menghindari flicker

  // Cegah user yang sudah login untuk kembali ke halaman login
  useEffect(() => {
    const tokenExists =
      localStorage.getItem('token') ||
      document.cookie.split('; ').some(cookie => cookie.startsWith('token='))

    if (tokenExists) {
      router.push('/dashboard')
    } else {
      setChecking(false) // hanya tampilkan login jika tidak login
    }
  }, [])

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault()
    setLoading(true)
    setMessage('')

    try {
      const res = await fetch('https://pecel-lele-connect.vercel.app/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password })
      })

      const data = await res.json()
      console.log('Response data:', data)

      if (!res.ok) throw new Error(data.metadata?.message || 'Login gagal')

      // Simpan token ke cookie (manual)
      document.cookie = `token=${data.data.token}; path=/; max-age=86400`

      // Optional: juga simpan ke localStorage
      localStorage.setItem('token', data.data.token)

      // Redirect ke dashboard
      router.push('/dashboard')
    } catch (err: any) {
      setMessage(err.message || 'Terjadi kesalahan')
    } finally {
      setLoading(false)
    }
  }

  // Jangan tampilkan apapun sampai pengecekan selesai
  if (checking) return null

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <form onSubmit={handleLogin} className="bg-white p-6 rounded shadow-md w-full max-w-sm text-black">
        <h1 className="text-xl font-bold mb-4 text-center">Login</h1>

        <input
          type="text"
          placeholder="Username"
          className="w-full mb-3 p-2 border border-black rounded"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />

        <input
          type="password"
          placeholder="Password"
          className="w-full mb-3 p-2 border border-black rounded"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />

        <button
          type="submit"
          disabled={loading}
          className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700"
        >
          {loading ? 'Memproses...' : 'Login'}
        </button>

        {message && <p className="mt-3 text-sm text-red-600 text-center">{message}</p>}

        <div className="mt-4 text-sm text-center">
          <p>
            Belum punya akun?{' '}
            <a href="/register" className="text-blue-600 underline">Daftar</a>
          </p>
          <p>
            Lupa password?{' '}
            <a href="/forgot-password" className="text-blue-600 underline">Reset</a>
          </p>
        </div>
      </form>
    </div>
  )
}
