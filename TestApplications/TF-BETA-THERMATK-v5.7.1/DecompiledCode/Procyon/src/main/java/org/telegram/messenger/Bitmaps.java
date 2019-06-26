// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Rect;
import android.graphics.Canvas;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory$Options;
import android.os.Build$VERSION;
import android.graphics.Bitmap;
import android.graphics.Bitmap$Config;
import android.graphics.Matrix;

public class Bitmaps
{
    protected static byte[] footer;
    protected static byte[] header;
    private static final ThreadLocal<byte[]> jpegData;
    private static volatile Matrix sScaleMatrix;
    
    static {
        jpegData = new ThreadLocal<byte[]>() {
            @Override
            protected byte[] initialValue() {
                return new byte[] { -1, -40, -1, -37, 0, 67, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -64, 0, 17, 8, 0, 0, 0, 0, 3, 1, 34, 0, 2, 17, 0, 3, 17, 0, -1, -60, 0, 31, 0, 0, 1, 5, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, -1, -60, 0, -75, 16, 0, 2, 1, 3, 3, 2, 4, 3, 5, 5, 4, 4, 0, 0, 1, 125, 1, 2, 3, 0, 4, 17, 5, 18, 33, 49, 65, 6, 19, 81, 97, 7, 34, 113, 20, 50, -127, -111, -95, 8, 35, 66, -79, -63, 21, 82, -47, -16, 36, 51, 98, 114, -126, 9, 10, 22, 23, 24, 25, 26, 37, 38, 39, 40, 41, 42, 52, 53, 54, 55, 56, 57, 58, 67, 68, 69, 70, 71, 72, 73, 74, 83, 84, 85, 86, 87, 88, 89, 90, 99, 100, 101, 102, 103, 104, 105, 106, 115, 116, 117, 118, 119, 120, 121, 122, -125, -124, -123, -122, -121, -120, -119, -118, -110, -109, -108, -107, -106, -105, -104, -103, -102, -94, -93, -92, -91, -90, -89, -88, -87, -86, -78, -77, -76, -75, -74, -73, -72, -71, -70, -62, -61, -60, -59, -58, -57, -56, -55, -54, -46, -45, -44, -43, -42, -41, -40, -39, -38, -31, -30, -29, -28, -27, -26, -25, -24, -23, -22, -15, -14, -13, -12, -11, -10, -9, -8, -7, -6, -1, -60, 0, 31, 1, 0, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, -1, -60, 0, -75, 17, 0, 2, 1, 2, 4, 4, 3, 4, 7, 5, 4, 4, 0, 1, 2, 119, 0, 1, 2, 3, 17, 4, 5, 33, 49, 6, 18, 65, 81, 7, 97, 113, 19, 34, 50, -127, 8, 20, 66, -111, -95, -79, -63, 9, 35, 51, 82, -16, 21, 98, 114, -47, 10, 22, 36, 52, -31, 37, -15, 23, 24, 25, 26, 38, 39, 40, 41, 42, 53, 54, 55, 56, 57, 58, 67, 68, 69, 70, 71, 72, 73, 74, 83, 84, 85, 86, 87, 88, 89, 90, 99, 100, 101, 102, 103, 104, 105, 106, 115, 116, 117, 118, 119, 120, 121, 122, -126, -125, -124, -123, -122, -121, -120, -119, -118, -110, -109, -108, -107, -106, -105, -104, -103, -102, -94, -93, -92, -91, -90, -89, -88, -87, -86, -78, -77, -76, -75, -74, -73, -72, -71, -70, -62, -61, -60, -59, -58, -57, -56, -55, -54, -46, -45, -44, -43, -42, -41, -40, -39, -38, -30, -29, -28, -27, -26, -25, -24, -23, -22, -14, -13, -12, -11, -10, -9, -8, -7, -6, -1, -38, 0, 12, 3, 1, 0, 2, 17, 3, 17, 0, 63, 0, -114, -118, 40, -96, 15, -1, -39 };
            }
        };
        Bitmaps.header = new byte[] { -1, -40, -1, -32, 0, 16, 74, 70, 73, 70, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0, -1, -37, 0, 67, 0, 40, 28, 30, 35, 30, 25, 40, 35, 33, 35, 45, 43, 40, 48, 60, 100, 65, 60, 55, 55, 60, 123, 88, 93, 73, 100, -111, -128, -103, -106, -113, -128, -116, -118, -96, -76, -26, -61, -96, -86, -38, -83, -118, -116, -56, -1, -53, -38, -18, -11, -1, -1, -1, -101, -63, -1, -1, -1, -6, -1, -26, -3, -1, -8, -1, -37, 0, 67, 1, 43, 45, 45, 60, 53, 60, 118, 65, 65, 118, -8, -91, -116, -91, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -1, -64, 0, 17, 8, 0, 30, 0, 40, 3, 1, 34, 0, 2, 17, 1, 3, 17, 1, -1, -60, 0, 31, 0, 0, 1, 5, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, -1, -60, 0, -75, 16, 0, 2, 1, 3, 3, 2, 4, 3, 5, 5, 4, 4, 0, 0, 1, 125, 1, 2, 3, 0, 4, 17, 5, 18, 33, 49, 65, 6, 19, 81, 97, 7, 34, 113, 20, 50, -127, -111, -95, 8, 35, 66, -79, -63, 21, 82, -47, -16, 36, 51, 98, 114, -126, 9, 10, 22, 23, 24, 25, 26, 37, 38, 39, 40, 41, 42, 52, 53, 54, 55, 56, 57, 58, 67, 68, 69, 70, 71, 72, 73, 74, 83, 84, 85, 86, 87, 88, 89, 90, 99, 100, 101, 102, 103, 104, 105, 106, 115, 116, 117, 118, 119, 120, 121, 122, -125, -124, -123, -122, -121, -120, -119, -118, -110, -109, -108, -107, -106, -105, -104, -103, -102, -94, -93, -92, -91, -90, -89, -88, -87, -86, -78, -77, -76, -75, -74, -73, -72, -71, -70, -62, -61, -60, -59, -58, -57, -56, -55, -54, -46, -45, -44, -43, -42, -41, -40, -39, -38, -31, -30, -29, -28, -27, -26, -25, -24, -23, -22, -15, -14, -13, -12, -11, -10, -9, -8, -7, -6, -1, -60, 0, 31, 1, 0, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, -1, -60, 0, -75, 17, 0, 2, 1, 2, 4, 4, 3, 4, 7, 5, 4, 4, 0, 1, 2, 119, 0, 1, 2, 3, 17, 4, 5, 33, 49, 6, 18, 65, 81, 7, 97, 113, 19, 34, 50, -127, 8, 20, 66, -111, -95, -79, -63, 9, 35, 51, 82, -16, 21, 98, 114, -47, 10, 22, 36, 52, -31, 37, -15, 23, 24, 25, 26, 38, 39, 40, 41, 42, 53, 54, 55, 56, 57, 58, 67, 68, 69, 70, 71, 72, 73, 74, 83, 84, 85, 86, 87, 88, 89, 90, 99, 100, 101, 102, 103, 104, 105, 106, 115, 116, 117, 118, 119, 120, 121, 122, -126, -125, -124, -123, -122, -121, -120, -119, -118, -110, -109, -108, -107, -106, -105, -104, -103, -102, -94, -93, -92, -91, -90, -89, -88, -87, -86, -78, -77, -76, -75, -74, -73, -72, -71, -70, -62, -61, -60, -59, -58, -57, -56, -55, -54, -46, -45, -44, -43, -42, -41, -40, -39, -38, -30, -29, -28, -27, -26, -25, -24, -23, -22, -14, -13, -12, -11, -10, -9, -8, -7, -6, -1, -38, 0, 12, 3, 1, 0, 2, 17, 3, 17, 0, 63, 0 };
        Bitmaps.footer = new byte[] { -1, -39 };
    }
    
    private static void checkWidthHeight(final int n, final int n2) {
        if (n <= 0) {
            throw new IllegalArgumentException("width must be > 0");
        }
        if (n2 > 0) {
            return;
        }
        throw new IllegalArgumentException("height must be > 0");
    }
    
    private static void checkXYSign(final int n, final int n2) {
        if (n < 0) {
            throw new IllegalArgumentException("x must be >= 0");
        }
        if (n2 >= 0) {
            return;
        }
        throw new IllegalArgumentException("y must be >= 0");
    }
    
    public static Bitmap createBitmap(final int n, final int n2, final Bitmap$Config inPreferredConfig) {
        Bitmap bitmap;
        if (Build$VERSION.SDK_INT < 21) {
            final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
            bitmapFactory$Options.inDither = true;
            bitmapFactory$Options.inPreferredConfig = inPreferredConfig;
            bitmapFactory$Options.inPurgeable = true;
            bitmapFactory$Options.inSampleSize = 1;
            bitmapFactory$Options.inMutable = true;
            final byte[] array = Bitmaps.jpegData.get();
            array[76] = (byte)(n2 >> 8);
            array[77] = (byte)(n2 & 0xFF);
            array[78] = (byte)(n >> 8);
            array[79] = (byte)(n & 0xFF);
            bitmap = BitmapFactory.decodeByteArray(array, 0, array.length, bitmapFactory$Options);
            Utilities.pinBitmap(bitmap);
            bitmap.setHasAlpha(true);
            bitmap.eraseColor(0);
        }
        else {
            bitmap = Bitmap.createBitmap(n, n2, inPreferredConfig);
        }
        if (inPreferredConfig == Bitmap$Config.ARGB_8888 || inPreferredConfig == Bitmap$Config.ARGB_4444) {
            bitmap.eraseColor(0);
        }
        return bitmap;
    }
    
    public static Bitmap createBitmap(final Bitmap bitmap, final int n, final int n2, final int n3, final int n4) {
        return createBitmap(bitmap, n, n2, n3, n4, null, false);
    }
    
    public static Bitmap createBitmap(final Bitmap bitmap, int n, int round, int round2, final int n2, final Matrix matrix, final boolean filterBitmap) {
        if (Build$VERSION.SDK_INT >= 21) {
            return Bitmap.createBitmap(bitmap, n, round, round2, n2, matrix, filterBitmap);
        }
        checkXYSign(n, round);
        checkWidthHeight(round2, n2);
        final int n3 = n + round2;
        Label_0441: {
            if (n3 > bitmap.getWidth()) {
                break Label_0441;
            }
            final int n4 = round + n2;
            Label_0430: {
                if (n4 > bitmap.getHeight()) {
                    break Label_0430;
                }
                if (!bitmap.isMutable() && n == 0 && round == 0 && round2 == bitmap.getWidth() && n2 == bitmap.getHeight() && (matrix == null || matrix.isIdentity())) {
                    return bitmap;
                }
                final Canvas canvas = new Canvas();
                final Rect rect = new Rect(n, round, n3, n4);
                final RectF rectF = new RectF(0.0f, 0.0f, (float)round2, (float)n2);
                Bitmap$Config bitmap$Config = Bitmap$Config.ARGB_8888;
                final Bitmap$Config config = bitmap.getConfig();
                if (config != null) {
                    n = Bitmaps$2.$SwitchMap$android$graphics$Bitmap$Config[config.ordinal()];
                    if (n != 1) {
                        if (n != 2) {
                            bitmap$Config = Bitmap$Config.ARGB_8888;
                        }
                        else {
                            bitmap$Config = Bitmap$Config.ALPHA_8;
                        }
                    }
                    else {
                        bitmap$Config = Bitmap$Config.ARGB_8888;
                    }
                }
                Bitmap bitmap3;
                Paint paint2;
                if (matrix != null && !matrix.isIdentity()) {
                    n = ((matrix.rectStaysRect() ^ true) ? 1 : 0);
                    final RectF rectF2 = new RectF();
                    matrix.mapRect(rectF2, rectF);
                    round = Math.round(rectF2.width());
                    round2 = Math.round(rectF2.height());
                    if (n != 0) {
                        bitmap$Config = Bitmap$Config.ARGB_8888;
                    }
                    final Bitmap bitmap2 = createBitmap(round, round2, bitmap$Config);
                    canvas.translate(-rectF2.left, -rectF2.top);
                    canvas.concat(matrix);
                    final Paint paint = new Paint();
                    paint.setFilterBitmap(filterBitmap);
                    bitmap3 = bitmap2;
                    paint2 = paint;
                    if (n != 0) {
                        paint.setAntiAlias(true);
                        bitmap3 = bitmap2;
                        paint2 = paint;
                    }
                }
                else {
                    bitmap3 = createBitmap(round2, n2, bitmap$Config);
                    paint2 = null;
                }
                bitmap3.setDensity(bitmap.getDensity());
                bitmap3.setHasAlpha(bitmap.hasAlpha());
                if (Build$VERSION.SDK_INT >= 19) {
                    bitmap3.setPremultiplied(bitmap.isPremultiplied());
                }
                canvas.setBitmap(bitmap3);
                canvas.drawBitmap(bitmap, rect, rectF, paint2);
                try {
                    canvas.setBitmap((Bitmap)null);
                    return bitmap3;
                    throw new IllegalArgumentException("x + width must be <= bitmap.width()");
                    throw new IllegalArgumentException("y + height must be <= bitmap.height()");
                }
                catch (Exception ex) {
                    return bitmap3;
                }
            }
        }
    }
    
    public static Bitmap createScaledBitmap(Bitmap bitmap, final int n, final int n2, final boolean b) {
        if (Build$VERSION.SDK_INT >= 21) {
            return Bitmap.createScaledBitmap(bitmap, n, n2, b);
        }
        synchronized (Bitmap.class) {
            final Matrix sScaleMatrix = Bitmaps.sScaleMatrix;
            Bitmaps.sScaleMatrix = null;
            // monitorexit(Bitmap.class)
            Matrix sScaleMatrix2 = sScaleMatrix;
            if (sScaleMatrix == null) {
                sScaleMatrix2 = new Matrix();
            }
            final int width = bitmap.getWidth();
            final int height = bitmap.getHeight();
            sScaleMatrix2.setScale(n / (float)width, n2 / (float)height);
            bitmap = createBitmap(bitmap, 0, 0, width, height, sScaleMatrix2, b);
            synchronized (Bitmap.class) {
                if (Bitmaps.sScaleMatrix == null) {
                    Bitmaps.sScaleMatrix = sScaleMatrix2;
                }
                return bitmap;
            }
        }
    }
}
