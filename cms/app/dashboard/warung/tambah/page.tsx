"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import toast from "react-hot-toast";

export default function TambahWarungPage() {
  const router = useRouter();

  const [form, setForm] = useState({
    penjualId: "",
    nama: "",
    alamat: "",
    jam_buka: "",
    jam_tutup: "",
    no_telp: "",
  });
  const [image, setImage] = useState<File | null>(null);
  const [loading, setLoading] = useState(false);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleFile = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      setImage(e.target.files[0]);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    try {
      const formData = new FormData();
      for (const key in form) {
        formData.append(key, form[key as keyof typeof form]);
      }
      if (image) formData.append("image", image);

      const res = await fetch("http://localhost:3000/api/warung", {
        method: "POST",
        body: formData,
      });

      const result = await res.json();

      if (result.status === 0) {
        toast.success("Warung berhasil dibuat!");
        router.push("/dashboard/warung");
      } else {
        toast.error(result.message || "Gagal menambahkan warung");
      }
    } catch (err) {
      console.error(err);
      toast.error("Terjadi kesalahan saat mengirim data");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-xl mx-auto p-6 bg-white rounded shadow text-black">
      <h1 className="text-xl font-bold mb-4 ">Tambah Warung</h1>

      <form onSubmit={handleSubmit} className="space-y-4">
        <input
          name="penjualId"
          placeholder="ID Penjual"
          onChange={handleChange}
          required
          className="w-full border px-3 py-2 rounded"
        />
        <input
          name="nama"
          placeholder="Nama Warung"
          onChange={handleChange}
          required
          className="w-full border px-3 py-2 rounded"
        />
        <textarea
          name="alamat"
          placeholder="Alamat"
          onChange={handleChange}
          required
          className="w-full border px-3 py-2 rounded"
        />
        <div className="flex gap-2">
          <input
            name="jam_buka"
            type="time"
            onChange={handleChange}
            required
            className="w-full border px-3 py-2 rounded"
          />
          <input
            name="jam_tutup"
            type="time"
            onChange={handleChange}
            required
            className="w-full border px-3 py-2 rounded"
          />
        </div>
        <input
          name="no_telp"
          placeholder="No. Telp"
          onChange={handleChange}
          required
          className="w-full border px-3 py-2 rounded"
        />
        <input
          type="file"
          accept="image/*"
          onChange={handleFile}
          required
          className="w-full"
        />

        <button
          type="submit"
          disabled={loading}
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
        >
          {loading ? "Mengirim..." : "Simpan"}
        </button>
      </form>
    </div>
  );
}
