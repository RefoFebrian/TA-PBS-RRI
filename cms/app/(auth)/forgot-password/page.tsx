'use client'

import { useState } from 'react'

export default function ForgotPasswordPage() {
  const [email, setEmail] = useState('')
  const [message, setMessage] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setLoading(true)
    setMessage('')

    try {
      const res = await fetch('http://localhost:3000/api/auth/forgot-password', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email })
      })

      const data = await res.json()
      if (!res.ok) throw new Error(data.metadata?.message || 'Gagal mengirim')

      setMessage(data.metadata?.message || 'Link reset telah dikirim')
    } catch (err: any) {
      setMessage(err.message || 'Terjadi kesalahan')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <form onSubmit={handleSubmit} className="bg-white p-6 rounded shadow-md w-full max-w-sm text-black">
        <h1 className="text-xl font-bold mb-4 text-center">Lupa Password</h1>

        <input
          type="email"
          placeholder="Email terdaftar"
          className="w-full mb-3 p-2 border rounded"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />

        <button
          type="submit"
          disabled={loading}
          className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700"
        >
          {loading ? 'Mengirim...' : 'Kirim Link Reset'}
        </button>

        {message && <p className="mt-3 text-sm text-green-600 text-center">{message}</p>}

        <div className="mt-4 text-sm text-center">
          <p>
            Kembali ke <a href="/login" className="text-blue-600 underline">Login</a>
          </p>
        </div>
      </form>
    </div>
  )
}