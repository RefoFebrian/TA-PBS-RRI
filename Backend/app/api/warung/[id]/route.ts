import { prisma } from "@/app/api/bridgePrisma";
import { getResponse } from "@/app/api/bridgeResponse";
import { NextRequest } from "next/server";

export const GET = async (
  req: NextRequest,
  {
    params,
  }: {
    params: { id: string };
  }
) => {
  try {
    const param = params.id;

    const getWarungById = await prisma.warung.findFirst({
      where: {
        id: Number(param),
      },
      include: {
        menu: true,
        penjual: {
          select: { username: true },
        },
      },
    });

    if (!getWarungById) {
      return getResponse(1, "Warung tidak ditemukan", 404);
    }

    return getResponse(
      0,
      `data ${getWarungById.nama} berhasil didapatkan`,
      200,
      getWarungById
    );
  } catch (error: unknown) {
    console.error(error);
    return getResponse(1, `${error}`, 500, []);
  }
};
