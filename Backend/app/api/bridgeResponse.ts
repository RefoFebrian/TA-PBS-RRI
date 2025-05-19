import { NextResponse } from "next/server";


// fungsi jika response error
export function getResponse(
  error: number,
  message: string,
  status: number,
  data?: unknown
) {
  return NextResponse.json(
    {
      metadata: {
        error,
        message,
        status,
      },
      data: data || null,
    },
    { status }
  );
}
