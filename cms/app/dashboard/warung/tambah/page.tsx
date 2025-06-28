"use client";

import { useEffect, useState } from "react";
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

  // ⬇️ Ambil user info dari token di localStorage
  useEffect(() => {
    const fetchUser = async () => {
      const token = localStorage.getItem("token");
      if (!token) return;

      try {
        const res = await fetch("https://pecel-lele-connect.vercel.app/api/auth/me", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        const data = await res.json();
        if (data.user) {
          setForm((prev) => ({
            ...prev,
            penjualId: data.user.id, // Isi otomatis penjualId
          }));
        } else {
          toast.error("Gagal mendapatkan data pengguna");
        }
      } catch (err) {
        console.error("Error ambil user:", err);
        toast.error("Tidak bisa mengambil info pengguna");
      }
    };

    fetchUser();
  }, []);

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

      const res = await fetch("https://pecel-lele-connect.vercel.app/api/warung", {
        method: "POST",
        body: formData,
      });

      const contentType = res.headers.get("content-type");
      if (!res.ok || !contentType?.includes("application/json")) {
        const text = await res.text();
        toast.error("Gagal mengirim: " + text.slice(0, 50));
        return;
      }

      const result = await res.json();
      console.log("RESPON BACKEND:", result);

      if (result.metadata.error === 0) {
        toast.success(result.metadata.message || "Warung berhasil dibuat!");
        router.push("/dashboard/warung");
      } else {
        toast.error(result.metadata.message || "Gagal menambahkan warung");
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
      <h1 className="text-xl font-bold mb-4">Tambah Warung</h1>

      <form onSubmit={handleSubmit} className="space-y-4">
        {/* penjualId disembunyikan dari input */}
        <input type="hidden" name="penjualId" value={form.penjualId} />

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
