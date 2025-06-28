// app/dashboard/page.tsx
'use client'

import { useEffect, useState } from 'react'
import { useRouter } from 'next/navigation'
import Navbar from '@/components/Navbar'

export default function DashboardPage() {
  const router = useRouter()
  const [checking, setChecking] = useState(true)

  useEffect(() => {
    const tokenExists =
      localStorage.getItem('token') ||
      document.cookie.split('; ').some(cookie => cookie.startsWith('token='))

    if (!tokenExists) {
      // Jika token tidak ada (sudah logout), redirect paksa ke login
      router.push('/login')
    } else {
      setChecking(false) // token valid
    }
  }, [])

  if (checking) return null // tampilkan kosong saat cek token

  return (
    <div className="min-h-screen bg-gray-50">
      <Navbar />

      <div className="flex items-center justify-center py-10">
        <div className="text-center">
          <h1 className="text-3xl font-bold mb-2">Selamat Datang di Dashboard Penjual</h1>
          <p className="text-gray-600 mb-4">Anda berhasil login sebagai penjual.</p>
        </div>
      </div>
    </div>
  )
}
