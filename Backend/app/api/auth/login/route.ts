import { prisma } from "@/app/api/bridgePrisma";
import { compare } from "bcrypt-ts";
import { NextRequest, NextResponse } from "next/server";

export const POST = async (req: NextRequest) => {
  try {
    // Ambil Input Username dan Password dari request
    const { username, password } = await req.json();

    // Cek apakah username dan password ada
    const userFound = await prisma.user.findFirst({
      where: {
        username: username,
      },
    });

    // cek apakah ada usernamenya
    if (!userFound) {
      return NextResponse.json(
        {
          metadata: {
            error: 1,
            message: "Username not registered, please register first",
            status: 404,
          },
        },
        { status: 404 }
      );
    }

    // ambil hash password dari database
    const hash = userFound.password;

    // cek apakah passwordnya sesuai
    const isValidPass = await compare(password, hash);

    if (!isValidPass) {
      return NextResponse.json(
        {
          metadata: {
            error: 1,
            message: "Password is incorrect",
            status: 401,
          },
        },
        { status: 401 }
      );
    }

    return NextResponse.json(
      { metadata: { error: 0, message: "Login successful", status: 200 } },
      { status: 200 }
    );
  } catch (error: unknown) {
    console.error("Login error:", error);
    return NextResponse.json(
      {
        metadata: {
          error: 1,
          message: "Internal server error",
          status: 500,
        },
      },
      { status: 500 }
    );
  }
};
