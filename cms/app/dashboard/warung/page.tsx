'use client'

import Image from 'next/image'
import { useEffect, useState } from 'react'

interface Warung {
  id: number
  nama: string
  alamat: string
  jam_buka: string
  jam_tutup: string
  image: string
  no_telp: string
  penjual: { username: string }
}

export default function WarungPage() {
  const [warungs, setWarungs] = useState<Warung[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const fetchWarung = async () => {
      try {
        const res = await fetch('http://localhost:3000/api/warung') // sesuaikan jika pakai domain lain

        const contentType = res.headers.get("content-type")
        if (!res.ok) {
          setError(`Server error: ${res.status}`)
          return
        }

        if (contentType && contentType.includes("application/json")) {
          const data = await res.json()
          console.log("RESPON API:", data)

          if (Array.isArray(data.data)) {
            setWarungs(data.data) //  ambil array dari key "data"
          } else {
            setError("Format data API tidak sesuai")
          }
        } else {
          const text = await res.text()
          setError(`Respon bukan JSON: ${text}`)
        }
      } catch (err: any) {
        console.error("Fetch error:", err)
        setError("Terjadi kesalahan saat mengambil data")
      } finally {
        setLoading(false)
      }
    }

    fetchWarung()
  }, [])

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-4">Daftar Warung</h1>

      {loading && <p>Loading...</p>}
      {error && <p className="text-red-500">{error}</p>}

      {!loading && !error && warungs.length === 0 && (
        <p>Belum ada warung.</p>
      )}

      {!loading && !error && warungs.length > 0 && (
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
          {warungs.map((warung) => (
            <div key={warung.id} className="bg-white rounded shadow p-4">
              <Image
                src={warung.image}
                alt={warung.nama}
                width={400}
                height={250}
                className="rounded object-cover mb-3"
              />
              <h2 className="font-semibold text-lg">{warung.nama}</h2>
              <p className="text-gray-600 text-sm">{warung.alamat}</p>
              <p className="text-gray-600 text-sm">
                Jam: {warung.jam_buka} - {warung.jam_tutup}
              </p>
              <p className="text-gray-600 text-sm">Telp: {warung.no_telp}</p>
              <p className="text-gray-600 text-sm italic">
                Penjual: {warung.penjual.username}
              </p>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
