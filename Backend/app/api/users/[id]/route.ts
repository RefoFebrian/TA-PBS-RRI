import { prisma } from "../../bridgePrisma";
import { NextRequest, NextResponse } from "next/server";

// fungsi untuk mendapatkan user berdasarkan id
export const GET = async (
  request: NextRequest,
  { params }: { params: { id: string } }
) => {
  try {
    const userId = params.id;

    const user = await prisma.user.findUnique({
      where: {
        id: userId,
      },
    });

    if (!user) {
      return NextResponse.json(
        {
          metadata: {
            error: 1,
            message: `User with ID '${userId}' not found`,
            status: 404,
          },
        },
        { status: 404 }
      );
    }

    return NextResponse.json(
      {
        metadata: {
          error: 0,
          message: "User Found",
          status: 200,
        },
        data: user,
      },
      { status: 200 }
    );
  } catch (err: unknown) {
    console.error(`Error fetching user: ${err}`);

    return NextResponse.json(
      {
        metadata: {
          error: 1,
          message: `${err}` || "Internal Server Error",
          status: 500,
        },
      },
      { status: 500 }
    );
  }
};

// fungsi untuk mengupdate user berdasarkan id
export const PUT = async (
  req: NextRequest,
  { params }: { params: { id: string } }
) => {
  try {
    const userId = params.id;
    const { username, password, role } = await req.json();

    const checkUser = await prisma.user.findUnique({
      where: {
        id: userId,
      },
    });

    if (!checkUser) {
      return NextResponse.json(
        {
          metadata: {
            error: 1,
            message: "User not found",
            status: 404,
          },
        },
        { status: 404 }
      );
    }

    const updatedUser = await prisma.user.update({
      where: {
        id: userId,
      },
      data: {
        username,
        password,
        role,
      },
    });

    return NextResponse.json(
      {
        metadata: {
          error: 0,
          message: "User updated successfully",
          status: 200,
        },
        data: updatedUser,
      },
      {
        status: 200,
      }
    );
  } catch (err: unknown) {
    console.error(`Error fetching user: ${err}`);

    return NextResponse.json(
      {
        metadata: {
          error: 1,
          message: `${err}` || "Internal Server Error",
          status: 500,
        },
      },
      { status: 500 }
    );
  }
};

// fungsi untuk menghapus user berdasarkan id
export const DELETE = async (
  req: NextRequest,
  { params }: { params: { id: string } }
) => {
  try {
    const userId = params.id;

    const checkUser = await prisma.user.findUnique({
      where: {
        id: userId,
      },
    });

    if (!checkUser) {
      return NextResponse.json(
        {
          metadata: {
            error: 1,
            message: "User not found",
            status: 404,
          },
        },
        { status: 404 }
      );
    }

    await prisma.user.delete({
      where: {
        id: userId,
      },
    });

    return NextResponse.json(
      {
        metadata: {
          error: 0,
          message: "User deleted successfully",
          status: 200,
        },
      },
      { status: 200 }
    );
  } catch (err) {
    console.error(`Error deleting user: ${err}`);

    return NextResponse.json(
      {
        metadata: {
          error: 1,
          message: `${err}` || "Internal Server Error",
          status: 500,
        },
      },
      { status: 500 }
    );
  }
};
