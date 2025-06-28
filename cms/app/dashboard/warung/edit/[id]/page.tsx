'use client';

import { useEffect, useState } from 'react';
import { useRouter, useParams } from 'next/navigation';
import { toast } from 'react-hot-toast';

export default function EditWarungPage() {
  const router = useRouter();
  const params = useParams();
  const id = Number(params?.id); // pastikan number

  const [loading, setLoading] = useState(true);
  const [form, setForm] = useState({
    nama: '',
    alamat: '',
    jam_buka: '',
    jam_tutup: '',
    no_telp: '',
    image: '',
  });

  useEffect(() => {
    if (!id || isNaN(id)) {
      toast.error('ID warung tidak valid');
      router.push('/dashboard/warung');
      return;
    }

    const fetchData = async () => {
      try {
        const res = await fetch(`http://localhost:3000/api/warung/${id}`);
        const result = await res.json();

        if (result.metadata?.error === 0) {
          const data = result.data;
          setForm({
            nama: data.nama,
            alamat: data.alamat,
            jam_buka: data.jam_buka,
            jam_tutup: data.jam_tutup,
            no_telp: data.no_telp,
            image: data.image,
          });
        } else {
          toast.error(result.metadata.message || 'Warung tidak ditemukan');
          router.push('/dashboard/warung');
        }
      } catch (error) {
        toast.error('Gagal memuat data');
        router.push('/dashboard/warung');
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [id, router]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    try {
      const res = await fetch(`http://localhost:3000/api/warung/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(form),
      });

      const result = await res.json();

      if (result.metadata.error === 0) {
        toast.success(result.metadata.message || 'Warung berhasil diperbarui');
        router.push('/dashboard/warung');
      } else {
        toast.error(result.metadata.message || 'Gagal memperbarui warung');
      }
    } catch (error) {
      toast.error('Terjadi kesalahan saat memperbarui');
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <p className="p-4">Memuat...</p>;

  return (
    <div className="max-w-xl mx-auto p-6 bg-white rounded shadow text-black">
      <h1 className="text-xl font-bold mb-4">Edit Warung</h1>

      <form onSubmit={handleSubmit} className="space-y-4">
        <input
          name="nama"
          value={form.nama}
          placeholder="Nama Warung"
          onChange={handleChange}
          required
          className="w-full border px-3 py-2 rounded"
        />
        <textarea
          name="alamat"
          value={form.alamat}
          placeholder="Alamat"
          onChange={handleChange}
          required
          className="w-full border px-3 py-2 rounded"
        />
        <div className="flex gap-2">
          <input
            name="jam_buka"
            type="time"
            value={form.jam_buka}
            onChange={handleChange}
            required
            className="w-full border px-3 py-2 rounded"
          />
          <input
            name="jam_tutup"
            type="time"
            value={form.jam_tutup}
            onChange={handleChange}
            required
            className="w-full border px-3 py-2 rounded"
          />
        </div>
        <input
          name="no_telp"
          value={form.no_telp}
          placeholder="No. Telp"
          onChange={handleChange}
          required
          className="w-full border px-3 py-2 rounded"
        />

        <button
          type="submit"
          disabled={loading}
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
        >
          {loading ? 'Menyimpan...' : 'Simpan Perubahan'}
        </button>
      </form>
    </div>
  );
}
