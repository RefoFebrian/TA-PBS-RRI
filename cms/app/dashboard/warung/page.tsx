"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import toast from "react-hot-toast";
import Image from "next/image";

export default function WarungPage() {
  const [warungs, setWarungs] = useState([]);
  const [loading, setLoading] = useState(true);
  const router = useRouter();

  useEffect(() => {
    const fetchWarung = async () => {
      try {
        const res = await fetch("https://pecel-lele-connect.vercel.app/api/warung");
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

  const handleDelete = async (id: string) => {
    const konfirmasi = confirm("Yakin ingin menghapus warung ini?");
    if (!konfirmasi) return;

    try {
      const res = await fetch(`/api/warung/${id}`, {
        method: "DELETE",
      });
      const result = await res.json();

      if (result.metadata.error === 0) {
        toast.success("Warung berhasil dihapus");
        setWarungs((prev) => prev.filter((w: any) => w.id !== id));
      } else {
        toast.error(result.metadata.message || "Gagal menghapus");
      }
    } catch (err) {
      toast.error("Gagal koneksi saat menghapus");
    }
  };

  if (loading) return <p className="p-4">Memuat data warung...</p>;

  return (
    <div className="min-h-screen bg-gray-50 text-gray-900">
      {/* Header */}
      <div className="flex items-center justify-between px-6 py-4 bg-white border-b shadow">
        <h1 className="text-xl font-bold text-blue-600">Pecel Lele Connect</h1>
        <button
          onClick={() => router.push("/dashboard/warung/tambah")}
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
        >
          + Tambah Warung
        </button>
      </div>

      {/* Daftar Warung */}
      <main className="p-6">
        <h2 className="text-2xl font-semibold mb-4">Daftar Warung</h2>
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
          {warungs.map((warung: any) => (
            <div key={warung.id} className="border rounded-lg p-4 bg-white shadow">
              <div className="relative w-full h-40 mb-3">
                <Image
                  src={warung.image || "https://via.placeholder.com/400x200?text=No+Image"}
                  alt={warung.nama}
                  fill
                  className="rounded object-cover"
                  sizes="(max-width: 768px) 100vw, 400px"
                />
              </div>
              <h3 className="text-lg font-semibold">{warung.nama}</h3>
              <p className="text-sm">Alamat: {warung.alamat}</p>
              <p className="text-sm">Jam: {warung.jam_buka} - {warung.jam_tutup}</p>
              <p className="text-sm">Telp: {warung.no_telp}</p>
              <p className="text-sm">Penjual: {warung.user?.username || "-"}</p>

              <div className="flex gap-3 mt-3">
                <button
                  onClick={() => router.push(`/dashboard/warung/edit/${warung.id}`)}
                  className="text-blue-600 hover:underline"
                >
                  Edit
                </button>
                <button
                  onClick={() => handleDelete(warung.id)}
                  className="text-red-600 hover:underline"
                >
                  Hapus
                </button>
              </div>
            </div>
          ))}
        </div>
      </main>
    </div>
  );
}
