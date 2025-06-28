'use client'

import { useState } from 'react'
import { useRouter } from 'next/navigation'
import LogoutModal from './LogoutModal'

export default function Navbar() {
  const router = useRouter()
  const [showLogoutModal, setShowLogoutModal] = useState(false)

  const confirmLogout = () => {
    localStorage.removeItem('token')
    document.cookie = 'token=; Max-Age=0; path=/;'
    router.push('/login')
  }

  return (
    <>
      <nav className="bg-white shadow-md px-6 py-4 flex justify-between items-center">
        <div
          className="text-xl font-bold text-blue-600 cursor-pointer"
          onClick={() => router.push('/dashboard')}
        >
          Pecel Lele Connect
        </div>

        <div className="flex gap-6 items-center text-gray-700">
          <button
            onClick={() => router.push('/dashboard/warung')}
            className="hover:text-blue-600 transition"
          >
            Warung
          </button>
          <button
            onClick={() => router.push('/dashboard/pesanan')}
            className="hover:text-blue-600 transition"
          >
            Pesanan
          </button>
          <button
            onClick={() => setShowLogoutModal(true)}
            className="bg-red-600 text-white px-3 py-1 rounded hover:bg-red-700 transition"
          >
            Logout
          </button>
        </div>
      </nav>

      {showLogoutModal && (
        <LogoutModal
          onConfirm={confirmLogout}
          onCancel={() => setShowLogoutModal(false)}
        />
      )}
    </>
  )
}
