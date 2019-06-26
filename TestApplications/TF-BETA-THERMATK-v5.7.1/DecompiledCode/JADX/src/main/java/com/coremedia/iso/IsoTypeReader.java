package com.coremedia.iso;

import com.google.android.exoplayer2.util.NalUnitUtil;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import org.telegram.p004ui.ActionBar.Theme;

public final class IsoTypeReader {
    public static int byte2int(byte b) {
        return b < (byte) 0 ? b + 256 : b;
    }

    public static long readUInt32(ByteBuffer byteBuffer) {
        long j = (long) byteBuffer.getInt();
        return j < 0 ? j + 4294967296L : j;
    }

    public static int readUInt24(ByteBuffer byteBuffer) {
        return ((readUInt16(byteBuffer) << 8) + 0) + byte2int(byteBuffer.get());
    }

    public static int readUInt16(ByteBuffer byteBuffer) {
        return ((byte2int(byteBuffer.get()) << 8) + 0) + byte2int(byteBuffer.get());
    }

    public static int readUInt8(ByteBuffer byteBuffer) {
        return byte2int(byteBuffer.get());
    }

    public static String readString(ByteBuffer byteBuffer, int i) {
        byte[] bArr = new byte[i];
        byteBuffer.get(bArr);
        return Utf8.convert(bArr);
    }

    public static long readUInt64(ByteBuffer byteBuffer) {
        long readUInt32 = (readUInt32(byteBuffer) << 32) + 0;
        if (readUInt32 >= 0) {
            return readUInt32 + readUInt32(byteBuffer);
        }
        throw new RuntimeException("I don't know how to deal with UInt64! long is not sufficient and I don't want to use BigInt");
    }

    public static double readFixedPoint1616(ByteBuffer byteBuffer) {
        byte[] bArr = new byte[4];
        byteBuffer.get(bArr);
        double d = (double) ((((0 | ((bArr[0] << 24) & Theme.ACTION_BAR_VIDEO_EDIT_COLOR)) | ((bArr[1] << 16) & 16711680)) | ((bArr[2] << 8) & 65280)) | (bArr[3] & NalUnitUtil.EXTENDED_SAR));
        Double.isNaN(d);
        return d / 65536.0d;
    }

    public static double readFixedPoint0230(ByteBuffer byteBuffer) {
        byte[] bArr = new byte[4];
        byteBuffer.get(bArr);
        double d = (double) ((((0 | ((bArr[0] << 24) & Theme.ACTION_BAR_VIDEO_EDIT_COLOR)) | ((bArr[1] << 16) & 16711680)) | ((bArr[2] << 8) & 65280)) | (bArr[3] & NalUnitUtil.EXTENDED_SAR));
        Double.isNaN(d);
        return d / 1.073741824E9d;
    }

    public static float readFixedPoint88(ByteBuffer byteBuffer) {
        byte[] bArr = new byte[2];
        byteBuffer.get(bArr);
        return ((float) ((short) (((short) (0 | ((bArr[0] << 8) & 65280))) | (bArr[1] & NalUnitUtil.EXTENDED_SAR)))) / 256.0f;
    }

    public static String readIso639(ByteBuffer byteBuffer) {
        int readUInt16 = readUInt16(byteBuffer);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            stringBuilder.append((char) (((readUInt16 >> ((2 - i) * 5)) & 31) + 96));
        }
        return stringBuilder.toString();
    }

    public static String read4cc(ByteBuffer byteBuffer) {
        byte[] bArr = new byte[4];
        byteBuffer.get(bArr);
        try {
            return new String(bArr, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
