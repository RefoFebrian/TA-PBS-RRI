import { prisma } from "@/app/api/bridgePrisma";
import { getResponse } from "@/app/api/bridgeResponse";
import { supabase } from "@/app/api/bridgeSupabase";
import { randomUUID } from "crypto";
import { NextRequest } from "next/server";

export const POST = async (req: NextRequest) => {
  try {
    const formData = await req.formData();
    const getUserId = formData.get("penjualId") as string;
    const getName = formData.get("nama") as string;
    const getAlamat = formData.get("alamat") as string;
    const getImage = formData.get("image") as File;
    const getTelp = formData.get("no_telp") as string;
    const getStoreOpen = formData.get("jam_buka") as string;
    const getStoreClose = formData.get("jam_tutup") as string;

    // cek apakah id merupakan role penjual
    const userIsPenjual = await prisma.user.findUnique({
      where: {
        id: getUserId,
      },
    });

    if (userIsPenjual?.role !== "penjual") {
      return getResponse(1, `Hanya penjual yang bisa membuat warung`, 409, []);
    }

    if (!getName || !getAlamat || !getTelp || !getStoreClose || !getStoreOpen) {
      return getResponse(1, "Silakan isi semua field", 409, []);
    }

    if (!getImage) {
      return getResponse(1, "Silakan masukan image", 409, []);
    }

    const buffer = await getImage.arrayBuffer();
    const path = `penjual/warung/${randomUUID()}.jpg`;

    const { error } = await supabase.storage
      .from("image-warung")
      .upload(path, buffer, { upsert: false });

    const getError = error?.message;

    if (error) {
      return getResponse(1, `${getError}`, 409);
    }

    const { data } = supabase.storage.from("image-warung").getPublicUrl(path);

    const publicUrl = data.publicUrl;

    const createWarung = await prisma.warung.create({
      data: {
        nama: getName,
        alamat: getAlamat,
        jam_buka: getStoreOpen,
        jam_tutup: getStoreClose,
        image: publicUrl,
        no_telp: getTelp,
        penjualId: getUserId,
      },
    });

    return getResponse(0, "Berhasil menambahkan warung", 201, createWarung);
  } catch (error: unknown) {
    return getResponse(1, `${error}`, 500);
  }
};

export const GET = async () => {
  try {
    const warungs = await prisma.warung.findMany({
      include: {
        menu: true,
        penjual: {
          select: { username: true },
        },
      },
    });

    if (warungs.length == 0) {
      return getResponse(1, "Warung is empty", 404, []);
    }

    return getResponse(0, "Berhasil mendapatkan data warung", 200, warungs);
  } catch (error: unknown) {
    console.log(error);
    return getResponse(1, `${error}`, 500, []);
  }
};
