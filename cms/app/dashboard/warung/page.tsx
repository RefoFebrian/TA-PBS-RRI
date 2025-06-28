"use client";

import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import Image from "next/image";

export default function WarungPage() {
  const [warungs, setWarungs] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchWarung = async () => {
      try {
        const res = await fetch("http://localhost:3000/api/warung");
        const result = await res.json();

        if (result.metadata.error === 0) {
          setWarungs(result.data);
        } else {
          toast.error(result.metadata.message || "Gagal memuat warung");
        }
      } catch (error) {
        console.error(error);
        toast.error("Gagal mengambil data dari server");
      } finally {
        setLoading(false);
      }
    };

    fetchWarung();
  }, []);

  if (loading) return <p className="p-4">Memuat data warung...</p>;

  return (
    <div className="p-6">
  <h1 className="text-2xl font-bold mb-4 text-gray-900">Daftar Warung</h1>
  <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
    {warungs.map((warung: any) => (
      <div key={warung.id} className="border p-4 rounded shadow bg-white text-gray-800">
        <Image
          src={warung.image}
          alt={warung.nama}
          width={400}
          height={200}
          className="rounded object-cover"
        />
        <h2 className="text-lg font-semibold">{warung.nama}</h2>
        <p>Alamat: {warung.alamat}</p>
        <p>Penjual: {warung.user?.username || "Tidak diketahui"}</p>
        <p>Jam: {warung.jam_buka} - {warung.jam_tutup}</p>
        <p>No. Telp: {warung.no_telp}</p>
      </div>
    ))}
  </div>
</div>
  );
}
