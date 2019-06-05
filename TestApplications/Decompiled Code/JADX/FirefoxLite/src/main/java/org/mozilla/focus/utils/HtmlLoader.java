package org.mozilla.focus.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Base64;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;

public class HtmlLoader {
    private static final byte[] pngHeader = new byte[]{(byte) -119, (byte) 80, (byte) 78, (byte) 71, (byte) 13, (byte) 10, (byte) 26, (byte) 10};

    public static String loadResourceFile(Context context, int i, Map<String, String> map) {
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(i), StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    if (map != null) {
                        for (Entry entry : map.entrySet()) {
                            readLine = readLine.replace((CharSequence) entry.getKey(), (CharSequence) entry.getValue());
                        }
                    }
                    stringBuilder.append(readLine);
                } else {
                    String stringBuilder2 = stringBuilder.toString();
                    bufferedReader.close();
                    return stringBuilder2;
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load error page data", e);
        } catch (Throwable th) {
            r5.addSuppressed(th);
        }
    }

    public static String loadDrawableAsDataURI(Context context, int i, int i2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("data:image/png;base64,");
        Bitmap bitmap = DrawableUtils.getBitmap(DrawableUtils.loadAndTintDrawable(context, i, i2));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 0, byteArrayOutputStream);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        try {
            byte[] bArr = new byte[300];
            Object obj = null;
            while (true) {
                int read = byteArrayInputStream.read(bArr);
                if (read <= 0) {
                    return stringBuilder.toString();
                }
                if (obj == null) {
                    if (read >= 8) {
                        i2 = 0;
                        while (i2 < pngHeader.length) {
                            if (bArr[i2] == pngHeader[i2]) {
                                i2++;
                            } else {
                                throw new IllegalStateException("Invalid png detected");
                            }
                        }
                        obj = 1;
                    } else {
                        throw new IllegalStateException("Loaded drawable is improbably small");
                    }
                }
                stringBuilder.append(Base64.encodeToString(bArr, 0, read, 0));
            }
        } catch (IOException unused) {
            throw new IllegalStateException("Unable to load drawable data");
        }
    }

    /* JADX WARNING: Missing block: B:24:0x0050, code skipped:
            if (r7 == null) goto L_0x0055;
     */
    /* JADX WARNING: Missing block: B:26:?, code skipped:
            r7.close();
     */
    /* JADX WARNING: Missing block: B:28:0x0059, code skipped:
            return r0.toString();
     */
    public static java.lang.String loadPngAsDataURI(android.content.Context r7, int r8) {
        /*
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "data:image/png;base64,";
        r0.append(r1);
        r7 = r7.getResources();	 Catch:{ IOException -> 0x006f }
        r7 = r7.openRawResource(r8);	 Catch:{ IOException -> 0x006f }
        r8 = 0;
        r1 = 300; // 0x12c float:4.2E-43 double:1.48E-321;
        r1 = new byte[r1];	 Catch:{ Throwable -> 0x005c }
        r2 = 0;
        r3 = 0;
    L_0x0019:
        r4 = r7.read(r1);	 Catch:{ Throwable -> 0x005c }
        if (r4 <= 0) goto L_0x0050;
    L_0x001f:
        if (r3 != 0) goto L_0x0048;
    L_0x0021:
        r3 = 8;
        if (r4 < r3) goto L_0x0040;
    L_0x0025:
        r3 = 0;
    L_0x0026:
        r5 = pngHeader;	 Catch:{ Throwable -> 0x005c }
        r5 = r5.length;	 Catch:{ Throwable -> 0x005c }
        if (r3 >= r5) goto L_0x003e;
    L_0x002b:
        r5 = r1[r3];	 Catch:{ Throwable -> 0x005c }
        r6 = pngHeader;	 Catch:{ Throwable -> 0x005c }
        r6 = r6[r3];	 Catch:{ Throwable -> 0x005c }
        if (r5 != r6) goto L_0x0036;
    L_0x0033:
        r3 = r3 + 1;
        goto L_0x0026;
    L_0x0036:
        r0 = new java.lang.IllegalStateException;	 Catch:{ Throwable -> 0x005c }
        r1 = "Invalid png detected";
        r0.<init>(r1);	 Catch:{ Throwable -> 0x005c }
        throw r0;	 Catch:{ Throwable -> 0x005c }
    L_0x003e:
        r3 = 1;
        goto L_0x0048;
    L_0x0040:
        r0 = new java.lang.IllegalStateException;	 Catch:{ Throwable -> 0x005c }
        r1 = "Loaded drawable is improbably small";
        r0.<init>(r1);	 Catch:{ Throwable -> 0x005c }
        throw r0;	 Catch:{ Throwable -> 0x005c }
    L_0x0048:
        r4 = android.util.Base64.encodeToString(r1, r2, r4, r2);	 Catch:{ Throwable -> 0x005c }
        r0.append(r4);	 Catch:{ Throwable -> 0x005c }
        goto L_0x0019;
    L_0x0050:
        if (r7 == 0) goto L_0x0055;
    L_0x0052:
        r7.close();	 Catch:{ IOException -> 0x006f }
    L_0x0055:
        r7 = r0.toString();
        return r7;
    L_0x005a:
        r0 = move-exception;
        goto L_0x005e;
    L_0x005c:
        r8 = move-exception;
        throw r8;	 Catch:{ all -> 0x005a }
    L_0x005e:
        if (r7 == 0) goto L_0x006e;
    L_0x0060:
        if (r8 == 0) goto L_0x006b;
    L_0x0062:
        r7.close();	 Catch:{ Throwable -> 0x0066 }
        goto L_0x006e;
    L_0x0066:
        r7 = move-exception;
        r8.addSuppressed(r7);	 Catch:{ IOException -> 0x006f }
        goto L_0x006e;
    L_0x006b:
        r7.close();	 Catch:{ IOException -> 0x006f }
    L_0x006e:
        throw r0;	 Catch:{ IOException -> 0x006f }
    L_0x006f:
        r7 = new java.lang.IllegalStateException;
        r8 = "Unable to load png data";
        r7.<init>(r8);
        throw r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.focus.utils.HtmlLoader.loadPngAsDataURI(android.content.Context, int):java.lang.String");
    }
}
