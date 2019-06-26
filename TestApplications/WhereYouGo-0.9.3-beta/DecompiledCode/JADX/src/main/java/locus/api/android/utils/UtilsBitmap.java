package locus.api.android.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Logger;
import locus.api.utils.Utils;

public class UtilsBitmap {
    private static final String TAG = "UtilsBitmap";

    public static Bitmap readBitmap(DataReaderBigEndian dr) {
        int size = dr.readInt();
        if (size > 0) {
            return getBitmap(dr.readBytes(size));
        }
        return null;
    }

    public static void writeBitmap(DataWriterBigEndian dw, Bitmap bitmap, CompressFormat format) throws IOException {
        if (bitmap == null) {
            dw.writeInt(0);
            return;
        }
        byte[] data = getBitmap(bitmap, format);
        if (data == null || data.length == 0) {
            Logger.logW(TAG, "writeBitmap(), unknown problem");
            dw.writeInt(0);
            return;
        }
        dw.writeInt(data.length);
        dw.write(data);
    }

    public static byte[] getBitmap(Bitmap bitmap, CompressFormat format) {
        Exception e;
        Throwable th;
        byte[] bArr = null;
        ByteArrayOutputStream baos = null;
        try {
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            try {
                if (bitmap.compress(format, 80, baos2)) {
                    bArr = baos2.toByteArray();
                    Utils.closeStream(baos2);
                    baos = baos2;
                } else {
                    Logger.logW(TAG, "Problem with converting image to byte[]");
                    Utils.closeStream(baos2);
                    baos = baos2;
                }
            } catch (Exception e2) {
                e = e2;
                baos = baos2;
                try {
                    Logger.logE(TAG, "getBitmap(" + bitmap + ")", e);
                    Utils.closeStream(baos);
                    return bArr;
                } catch (Throwable th2) {
                    th = th2;
                    Utils.closeStream(baos);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                baos = baos2;
                Utils.closeStream(baos);
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
            Logger.logE(TAG, "getBitmap(" + bitmap + ")", e);
            Utils.closeStream(baos);
            return bArr;
        }
        return bArr;
    }

    public static Bitmap getBitmap(byte[] data) {
        try {
            return BitmapFactory.decodeByteArray(data, 0, data.length);
        } catch (Exception e) {
            Logger.logE(TAG, "getBitmap(" + data + ")", e);
            return null;
        }
    }
}
