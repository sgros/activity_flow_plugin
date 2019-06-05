package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceEncoder;

public class BitmapEncoder implements ResourceEncoder<Bitmap> {
    public static final Option<CompressFormat> COMPRESSION_FORMAT = Option.memory("com.bumptech.glide.load.resource.bitmap.BitmapEncoder.CompressionFormat");
    public static final Option<Integer> COMPRESSION_QUALITY = Option.memory("com.bumptech.glide.load.resource.bitmap.BitmapEncoder.CompressionQuality", Integer.valueOf(90));

    /* JADX WARNING: Unknown top exception splitter block from list: {B:33:0x00d0=Splitter:B:33:0x00d0, B:24:0x0076=Splitter:B:24:0x0076} */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x007f A:{Catch:{ all -> 0x00d1 }} */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0076 */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x007f A:{Catch:{ all -> 0x00d1 }} */
    /* JADX WARNING: Missing exception handler attribute for start block: B:33:0x00d0 */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x006a A:{Catch:{ all -> 0x005e }} */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0073 A:{SYNTHETIC, Splitter:B:22:0x0073} */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x007f A:{Catch:{ all -> 0x00d1 }} */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00cd A:{SYNTHETIC, Splitter:B:31:0x00cd} */
    /* JADX WARNING: Can't wrap try/catch for region: R(9:1|2|3|(7:4|5|6|7|8|9|10)|24|25|(1:27)|28|29) */
    /* JADX WARNING: Can't wrap try/catch for region: R(4:15|(2:31|32)|33|34) */
    public boolean encode(com.bumptech.glide.load.engine.Resource<android.graphics.Bitmap> r8, java.io.File r9, com.bumptech.glide.load.Options r10) {
        /*
        r7 = this;
        r8 = r8.get();
        r8 = (android.graphics.Bitmap) r8;
        r0 = r7.getFormat(r8, r10);
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "encode: [";
        r1.append(r2);
        r2 = r8.getWidth();
        r1.append(r2);
        r2 = "x";
        r1.append(r2);
        r2 = r8.getHeight();
        r1.append(r2);
        r2 = "] ";
        r1.append(r2);
        r1.append(r0);
        r1 = r1.toString();
        android.support.p001v4.p003os.TraceCompat.beginSection(r1);
        r1 = com.bumptech.glide.util.LogTime.getLogTime();	 Catch:{ all -> 0x00d1 }
        r3 = COMPRESSION_QUALITY;	 Catch:{ all -> 0x00d1 }
        r3 = r10.get(r3);	 Catch:{ all -> 0x00d1 }
        r3 = (java.lang.Integer) r3;	 Catch:{ all -> 0x00d1 }
        r3 = r3.intValue();	 Catch:{ all -> 0x00d1 }
        r4 = 0;
        r5 = 0;
        r6 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x0060 }
        r6.<init>(r9);	 Catch:{ IOException -> 0x0060 }
        r8.compress(r0, r3, r6);	 Catch:{ IOException -> 0x005b, all -> 0x0058 }
        r6.close();	 Catch:{ IOException -> 0x005b, all -> 0x0058 }
        r4 = 1;
        r6.close();	 Catch:{ IOException -> 0x0076 }
        goto L_0x0076;
    L_0x0058:
        r8 = move-exception;
        r5 = r6;
        goto L_0x00cb;
    L_0x005b:
        r9 = move-exception;
        r5 = r6;
        goto L_0x0061;
    L_0x005e:
        r8 = move-exception;
        goto L_0x00cb;
    L_0x0060:
        r9 = move-exception;
    L_0x0061:
        r3 = "BitmapEncoder";
        r6 = 3;
        r3 = android.util.Log.isLoggable(r3, r6);	 Catch:{ all -> 0x005e }
        if (r3 == 0) goto L_0x0071;
    L_0x006a:
        r3 = "BitmapEncoder";
        r6 = "Failed to encode Bitmap";
        android.util.Log.d(r3, r6, r9);	 Catch:{ all -> 0x005e }
    L_0x0071:
        if (r5 == 0) goto L_0x0076;
    L_0x0073:
        r5.close();	 Catch:{ IOException -> 0x0076 }
    L_0x0076:
        r9 = "BitmapEncoder";
        r3 = 2;
        r9 = android.util.Log.isLoggable(r9, r3);	 Catch:{ all -> 0x00d1 }
        if (r9 == 0) goto L_0x00c7;
    L_0x007f:
        r9 = "BitmapEncoder";
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00d1 }
        r3.<init>();	 Catch:{ all -> 0x00d1 }
        r5 = "Compressed with type: ";
        r3.append(r5);	 Catch:{ all -> 0x00d1 }
        r3.append(r0);	 Catch:{ all -> 0x00d1 }
        r0 = " of size ";
        r3.append(r0);	 Catch:{ all -> 0x00d1 }
        r0 = com.bumptech.glide.util.Util.getBitmapByteSize(r8);	 Catch:{ all -> 0x00d1 }
        r3.append(r0);	 Catch:{ all -> 0x00d1 }
        r0 = " in ";
        r3.append(r0);	 Catch:{ all -> 0x00d1 }
        r0 = com.bumptech.glide.util.LogTime.getElapsedMillis(r1);	 Catch:{ all -> 0x00d1 }
        r3.append(r0);	 Catch:{ all -> 0x00d1 }
        r0 = ", options format: ";
        r3.append(r0);	 Catch:{ all -> 0x00d1 }
        r0 = COMPRESSION_FORMAT;	 Catch:{ all -> 0x00d1 }
        r10 = r10.get(r0);	 Catch:{ all -> 0x00d1 }
        r3.append(r10);	 Catch:{ all -> 0x00d1 }
        r10 = ", hasAlpha: ";
        r3.append(r10);	 Catch:{ all -> 0x00d1 }
        r8 = r8.hasAlpha();	 Catch:{ all -> 0x00d1 }
        r3.append(r8);	 Catch:{ all -> 0x00d1 }
        r8 = r3.toString();	 Catch:{ all -> 0x00d1 }
        android.util.Log.v(r9, r8);	 Catch:{ all -> 0x00d1 }
    L_0x00c7:
        android.support.p001v4.p003os.TraceCompat.endSection();
        return r4;
    L_0x00cb:
        if (r5 == 0) goto L_0x00d0;
    L_0x00cd:
        r5.close();	 Catch:{ IOException -> 0x00d0 }
    L_0x00d0:
        throw r8;	 Catch:{ all -> 0x00d1 }
    L_0x00d1:
        r8 = move-exception;
        android.support.p001v4.p003os.TraceCompat.endSection();
        throw r8;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.load.resource.bitmap.BitmapEncoder.encode(com.bumptech.glide.load.engine.Resource, java.io.File, com.bumptech.glide.load.Options):boolean");
    }

    private CompressFormat getFormat(Bitmap bitmap, Options options) {
        CompressFormat compressFormat = (CompressFormat) options.get(COMPRESSION_FORMAT);
        if (compressFormat != null) {
            return compressFormat;
        }
        if (bitmap.hasAlpha()) {
            return CompressFormat.PNG;
        }
        return CompressFormat.JPEG;
    }

    public EncodeStrategy getEncodeStrategy(Options options) {
        return EncodeStrategy.TRANSFORMED;
    }
}
