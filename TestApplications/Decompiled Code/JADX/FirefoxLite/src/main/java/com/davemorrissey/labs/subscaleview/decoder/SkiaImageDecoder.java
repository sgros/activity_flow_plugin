package com.davemorrissey.labs.subscaleview.decoder;

public class SkiaImageDecoder implements ImageDecoder {
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00d4 A:{SYNTHETIC, Splitter:B:42:0x00d4} */
    public android.graphics.Bitmap decode(android.content.Context r9, android.net.Uri r10) throws java.lang.Exception {
        /*
        r8 = this;
        r0 = r10.toString();
        r1 = new android.graphics.BitmapFactory$Options;
        r1.<init>();
        r2 = android.graphics.Bitmap.Config.RGB_565;
        r1.inPreferredConfig = r2;
        r2 = "android.resource://";
        r2 = r0.startsWith(r2);
        if (r2 == 0) goto L_0x007a;
    L_0x0015:
        r0 = r10.getAuthority();
        r2 = r9.getPackageName();
        r2 = r2.equals(r0);
        if (r2 == 0) goto L_0x0028;
    L_0x0023:
        r2 = r9.getResources();
        goto L_0x0030;
    L_0x0028:
        r2 = r9.getPackageManager();
        r2 = r2.getResourcesForApplication(r0);
    L_0x0030:
        r10 = r10.getPathSegments();
        r3 = r10.size();
        r4 = 2;
        r5 = 1;
        r6 = 0;
        if (r3 != r4) goto L_0x0058;
    L_0x003d:
        r4 = r10.get(r6);
        r4 = (java.lang.String) r4;
        r7 = "drawable";
        r4 = r4.equals(r7);
        if (r4 == 0) goto L_0x0058;
    L_0x004b:
        r10 = r10.get(r5);
        r10 = (java.lang.String) r10;
        r3 = "drawable";
        r6 = r2.getIdentifier(r10, r3, r0);
        goto L_0x0071;
    L_0x0058:
        if (r3 != r5) goto L_0x0071;
    L_0x005a:
        r0 = r10.get(r6);
        r0 = (java.lang.CharSequence) r0;
        r0 = android.text.TextUtils.isDigitsOnly(r0);
        if (r0 == 0) goto L_0x0071;
    L_0x0066:
        r10 = r10.get(r6);	 Catch:{ NumberFormatException -> 0x0071 }
        r10 = (java.lang.String) r10;	 Catch:{ NumberFormatException -> 0x0071 }
        r10 = java.lang.Integer.parseInt(r10);	 Catch:{ NumberFormatException -> 0x0071 }
        r6 = r10;
    L_0x0071:
        r9 = r9.getResources();
        r9 = android.graphics.BitmapFactory.decodeResource(r9, r6, r1);
        goto L_0x00c3;
    L_0x007a:
        r2 = "file:///android_asset/";
        r2 = r0.startsWith(r2);
        r3 = 0;
        if (r2 == 0) goto L_0x009a;
    L_0x0083:
        r10 = "file:///android_asset/";
        r10 = r10.length();
        r10 = r0.substring(r10);
        r9 = r9.getAssets();
        r9 = r9.open(r10);
        r9 = android.graphics.BitmapFactory.decodeStream(r9, r3, r1);
        goto L_0x00c3;
    L_0x009a:
        r2 = "file://";
        r2 = r0.startsWith(r2);
        if (r2 == 0) goto L_0x00b1;
    L_0x00a2:
        r9 = "file://";
        r9 = r9.length();
        r9 = r0.substring(r9);
        r9 = android.graphics.BitmapFactory.decodeFile(r9, r1);
        goto L_0x00c3;
    L_0x00b1:
        r9 = r9.getContentResolver();	 Catch:{ all -> 0x00d1 }
        r9 = r9.openInputStream(r10);	 Catch:{ all -> 0x00d1 }
        r10 = android.graphics.BitmapFactory.decodeStream(r9, r3, r1);	 Catch:{ all -> 0x00ce }
        if (r9 == 0) goto L_0x00c2;
    L_0x00bf:
        r9.close();	 Catch:{ Exception -> 0x00c2 }
    L_0x00c2:
        r9 = r10;
    L_0x00c3:
        if (r9 == 0) goto L_0x00c6;
    L_0x00c5:
        return r9;
    L_0x00c6:
        r9 = new java.lang.RuntimeException;
        r10 = "Skia image region decoder returned null bitmap - image format may not be supported";
        r9.<init>(r10);
        throw r9;
    L_0x00ce:
        r10 = move-exception;
        r3 = r9;
        goto L_0x00d2;
    L_0x00d1:
        r10 = move-exception;
    L_0x00d2:
        if (r3 == 0) goto L_0x00d7;
    L_0x00d4:
        r3.close();	 Catch:{ Exception -> 0x00d7 }
    L_0x00d7:
        throw r10;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.davemorrissey.labs.subscaleview.decoder.SkiaImageDecoder.decode(android.content.Context, android.net.Uri):android.graphics.Bitmap");
    }
}
