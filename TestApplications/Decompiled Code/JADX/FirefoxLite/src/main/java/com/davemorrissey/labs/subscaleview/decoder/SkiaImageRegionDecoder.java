package com.davemorrissey.labs.subscaleview.decoder;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;

public class SkiaImageRegionDecoder implements ImageRegionDecoder {
    private BitmapRegionDecoder decoder;
    private final Object decoderLock = new Object();

    /* JADX WARNING: Removed duplicated region for block: B:40:0x00de A:{SYNTHETIC, Splitter:B:40:0x00de} */
    public android.graphics.Point init(android.content.Context r8, android.net.Uri r9) throws java.lang.Exception {
        /*
        r7 = this;
        r0 = r9.toString();
        r1 = "android.resource://";
        r1 = r0.startsWith(r1);
        r2 = 1;
        r3 = 0;
        if (r1 == 0) goto L_0x0078;
    L_0x000e:
        r0 = r9.getAuthority();
        r1 = r8.getPackageName();
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x0021;
    L_0x001c:
        r1 = r8.getResources();
        goto L_0x0029;
    L_0x0021:
        r1 = r8.getPackageManager();
        r1 = r1.getResourcesForApplication(r0);
    L_0x0029:
        r9 = r9.getPathSegments();
        r4 = r9.size();
        r5 = 2;
        if (r4 != r5) goto L_0x004f;
    L_0x0034:
        r5 = r9.get(r3);
        r5 = (java.lang.String) r5;
        r6 = "drawable";
        r5 = r5.equals(r6);
        if (r5 == 0) goto L_0x004f;
    L_0x0042:
        r9 = r9.get(r2);
        r9 = (java.lang.String) r9;
        r2 = "drawable";
        r9 = r1.getIdentifier(r9, r2, r0);
        goto L_0x0069;
    L_0x004f:
        if (r4 != r2) goto L_0x0068;
    L_0x0051:
        r0 = r9.get(r3);
        r0 = (java.lang.CharSequence) r0;
        r0 = android.text.TextUtils.isDigitsOnly(r0);
        if (r0 == 0) goto L_0x0068;
    L_0x005d:
        r9 = r9.get(r3);	 Catch:{ NumberFormatException -> 0x0068 }
        r9 = (java.lang.String) r9;	 Catch:{ NumberFormatException -> 0x0068 }
        r9 = java.lang.Integer.parseInt(r9);	 Catch:{ NumberFormatException -> 0x0068 }
        goto L_0x0069;
    L_0x0068:
        r9 = 0;
    L_0x0069:
        r8 = r8.getResources();
        r8 = r8.openRawResource(r9);
        r8 = android.graphics.BitmapRegionDecoder.newInstance(r8, r3);
        r7.decoder = r8;
        goto L_0x00c6;
    L_0x0078:
        r1 = "file:///android_asset/";
        r1 = r0.startsWith(r1);
        if (r1 == 0) goto L_0x0099;
    L_0x0080:
        r9 = "file:///android_asset/";
        r9 = r9.length();
        r9 = r0.substring(r9);
        r8 = r8.getAssets();
        r8 = r8.open(r9, r2);
        r8 = android.graphics.BitmapRegionDecoder.newInstance(r8, r3);
        r7.decoder = r8;
        goto L_0x00c6;
    L_0x0099:
        r1 = "file://";
        r1 = r0.startsWith(r1);
        if (r1 == 0) goto L_0x00b2;
    L_0x00a1:
        r8 = "file://";
        r8 = r8.length();
        r8 = r0.substring(r8);
        r8 = android.graphics.BitmapRegionDecoder.newInstance(r8, r3);
        r7.decoder = r8;
        goto L_0x00c6;
    L_0x00b2:
        r0 = 0;
        r8 = r8.getContentResolver();	 Catch:{ all -> 0x00da }
        r8 = r8.openInputStream(r9);	 Catch:{ all -> 0x00da }
        r9 = android.graphics.BitmapRegionDecoder.newInstance(r8, r3);	 Catch:{ all -> 0x00d8 }
        r7.decoder = r9;	 Catch:{ all -> 0x00d8 }
        if (r8 == 0) goto L_0x00c6;
    L_0x00c3:
        r8.close();	 Catch:{ Exception -> 0x00c6 }
    L_0x00c6:
        r8 = new android.graphics.Point;
        r9 = r7.decoder;
        r9 = r9.getWidth();
        r0 = r7.decoder;
        r0 = r0.getHeight();
        r8.<init>(r9, r0);
        return r8;
    L_0x00d8:
        r9 = move-exception;
        goto L_0x00dc;
    L_0x00da:
        r9 = move-exception;
        r8 = r0;
    L_0x00dc:
        if (r8 == 0) goto L_0x00e1;
    L_0x00de:
        r8.close();	 Catch:{ Exception -> 0x00e1 }
    L_0x00e1:
        throw r9;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.davemorrissey.labs.subscaleview.decoder.SkiaImageRegionDecoder.init(android.content.Context, android.net.Uri):android.graphics.Point");
    }

    public Bitmap decodeRegion(Rect rect, int i) {
        Bitmap decodeRegion;
        synchronized (this.decoderLock) {
            Options options = new Options();
            options.inSampleSize = i;
            options.inPreferredConfig = Config.RGB_565;
            decodeRegion = this.decoder.decodeRegion(rect, options);
            if (decodeRegion != null) {
            } else {
                throw new RuntimeException("Skia image decoder returned null bitmap - image format may not be supported");
            }
        }
        return decodeRegion;
    }

    public boolean isReady() {
        return (this.decoder == null || this.decoder.isRecycled()) ? false : true;
    }

    public void recycle() {
        this.decoder.recycle();
    }
}
