package org.mozilla.httprequest;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class HttpRequest {
    public static String get(URL url, String str) {
        return get(url, 2000, str);
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x002c  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0025  */
    public static java.lang.String get(java.net.URL r2, int r3, java.lang.String r4) {
        /*
        r0 = "";
        r1 = 0;
        r2 = r2.openConnection();	 Catch:{ IOException -> 0x0029, all -> 0x0021 }
        r2 = (java.net.HttpURLConnection) r2;	 Catch:{ IOException -> 0x0029, all -> 0x0021 }
        r1 = "User-Agent";
        r2.setRequestProperty(r1, r4);	 Catch:{ IOException -> 0x001f, all -> 0x001d }
        if (r3 <= 0) goto L_0x0013;
    L_0x0010:
        r2.setConnectTimeout(r3);	 Catch:{ IOException -> 0x001f, all -> 0x001d }
    L_0x0013:
        r3 = readLines(r2);	 Catch:{ IOException -> 0x001f, all -> 0x001d }
        if (r2 == 0) goto L_0x0030;
    L_0x0019:
        r2.disconnect();
        goto L_0x0030;
    L_0x001d:
        r3 = move-exception;
        goto L_0x0023;
        goto L_0x002a;
    L_0x0021:
        r3 = move-exception;
        r2 = r1;
    L_0x0023:
        if (r2 == 0) goto L_0x0028;
    L_0x0025:
        r2.disconnect();
    L_0x0028:
        throw r3;
    L_0x0029:
        r2 = r1;
    L_0x002a:
        if (r2 == 0) goto L_0x002f;
    L_0x002c:
        r2.disconnect();
    L_0x002f:
        r3 = r0;
    L_0x0030:
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.httprequest.HttpRequest.get(java.net.URL, int, java.lang.String):java.lang.String");
    }

    private static String readLines(URLConnection uRLConnection) throws IOException {
        Throwable th;
        try {
            InputStream inputStream = uRLConnection.getInputStream();
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader createReader = createReader(inputStream);
            while (true) {
                try {
                    String readLine = createReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    stringBuilder.append(readLine);
                    stringBuilder.append(10);
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            if (createReader != null) {
                createReader.close();
            }
            return stringBuilder.toString();
        } catch (IndexOutOfBoundsException unused) {
            return "";
        }
    }

    private static BufferedReader createReader(InputStream inputStream) throws IOException {
        return new BufferedReader(new InputStreamReader(new BufferedInputStream(inputStream), "utf-8"));
    }
}
