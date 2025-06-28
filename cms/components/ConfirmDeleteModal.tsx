"use client";

type ConfirmDeleteModalProps = {
  visible: boolean;
  onCancel: () => void;
  onConfirm: () => void;
};

export default function ConfirmDeleteModal({
  visible,
  onCancel,
  onConfirm,
}: ConfirmDeleteModalProps) {
  if (!visible) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50">
      <div className="bg-white p-6 rounded shadow-lg max-w-sm w-full">
        <h2 className="text-lg font-semibold mb-2">Konfirmasi Hapus</h2>
        <p className="mb-4">Apakah Anda yakin ingin menghapus warung ini?</p>
        <div className="flex justify-end gap-3">
          <button
            onClick={onCancel}
            className="px-4 py-2 border rounded hover:bg-gray-100"
          >
            Tidak
          </button>
          <button
            onClick={onConfirm}
            className="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700"
          >
            Ya, Hapus
          </button>
        </div>
      </div>
    </div>
  );
}
