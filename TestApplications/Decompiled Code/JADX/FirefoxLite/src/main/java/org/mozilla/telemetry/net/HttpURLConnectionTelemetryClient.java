package org.mozilla.telemetry.net;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import mozilla.components.support.base.log.logger.Logger;
import org.mozilla.telemetry.util.IOUtils;

public class HttpURLConnectionTelemetryClient implements TelemetryClient {
    private Logger logger = new Logger("telemetry/client");

    /* JADX WARNING: Unknown top exception splitter block from list: {B:40:0x00bc=Splitter:B:40:0x00bc, B:34:0x00ae=Splitter:B:34:0x00ae} */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00c5  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00b7  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00cb  */
    public boolean uploadPing(org.mozilla.telemetry.config.TelemetryConfiguration r6, java.lang.String r7, java.lang.String r8) {
        /*
        r5 = this;
        r0 = 0;
        r1 = 1;
        r2 = 0;
        r3 = r6.getServerEndpoint();	 Catch:{ MalformedURLException -> 0x00bb, IOException -> 0x00ad }
        r7 = r5.openConnectionConnection(r3, r7);	 Catch:{ MalformedURLException -> 0x00bb, IOException -> 0x00ad }
        r3 = r6.getConnectTimeout();	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        r7.setConnectTimeout(r3);	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        r3 = r6.getReadTimeout();	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        r7.setReadTimeout(r3);	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        r3 = "Content-Type";
        r4 = "application/json; charset=utf-8";
        r7.setRequestProperty(r3, r4);	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        r3 = "User-Agent";
        r6 = r6.getUserAgent();	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        r7.setRequestProperty(r3, r6);	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        r6 = "Date";
        r3 = r5.createDateHeaderValue();	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        r7.setRequestProperty(r6, r3);	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        r6 = "POST";
        r7.setRequestMethod(r6);	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        r7.setDoOutput(r1);	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        r6 = r5.upload(r7, r8);	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        r8 = r5.logger;	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        r3 = new java.lang.StringBuilder;	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        r3.<init>();	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        r4 = "Ping upload: ";
        r3.append(r4);	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        r3.append(r6);	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        r3 = r3.toString();	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        r8.debug(r3, r2);	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        r8 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r6 < r8) goto L_0x0062;
    L_0x0058:
        r8 = 299; // 0x12b float:4.19E-43 double:1.477E-321;
        if (r6 > r8) goto L_0x0062;
    L_0x005c:
        if (r7 == 0) goto L_0x0061;
    L_0x005e:
        r7.disconnect();
    L_0x0061:
        return r1;
    L_0x0062:
        r8 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
        if (r6 < r8) goto L_0x0086;
    L_0x0066:
        r8 = 499; // 0x1f3 float:6.99E-43 double:2.465E-321;
        if (r6 > r8) goto L_0x0086;
    L_0x006a:
        r8 = r5.logger;	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        r3 = new java.lang.StringBuilder;	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        r3.<init>();	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        r4 = "Server returned client error code: ";
        r3.append(r4);	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        r3.append(r6);	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        r6 = r3.toString();	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        r8.error(r6, r2);	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        if (r7 == 0) goto L_0x0085;
    L_0x0082:
        r7.disconnect();
    L_0x0085:
        return r1;
    L_0x0086:
        r8 = r5.logger;	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        r3 = new java.lang.StringBuilder;	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        r3.<init>();	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        r4 = "Server returned response code: ";
        r3.append(r4);	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        r3.append(r6);	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        r6 = r3.toString();	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        r8.warn(r6, r2);	 Catch:{ MalformedURLException -> 0x00a7, IOException -> 0x00a4, all -> 0x00a2 }
        if (r7 == 0) goto L_0x00a1;
    L_0x009e:
        r7.disconnect();
    L_0x00a1:
        return r0;
    L_0x00a2:
        r6 = move-exception;
        goto L_0x00c9;
    L_0x00a4:
        r6 = move-exception;
        r2 = r7;
        goto L_0x00ae;
    L_0x00a7:
        r6 = move-exception;
        r2 = r7;
        goto L_0x00bc;
    L_0x00aa:
        r6 = move-exception;
        r7 = r2;
        goto L_0x00c9;
    L_0x00ad:
        r6 = move-exception;
    L_0x00ae:
        r7 = r5.logger;	 Catch:{ all -> 0x00aa }
        r8 = "IOException while uploading ping";
        r7.warn(r8, r6);	 Catch:{ all -> 0x00aa }
        if (r2 == 0) goto L_0x00ba;
    L_0x00b7:
        r2.disconnect();
    L_0x00ba:
        return r0;
    L_0x00bb:
        r6 = move-exception;
    L_0x00bc:
        r7 = r5.logger;	 Catch:{ all -> 0x00aa }
        r8 = "Could not upload telemetry due to malformed URL";
        r7.error(r8, r6);	 Catch:{ all -> 0x00aa }
        if (r2 == 0) goto L_0x00c8;
    L_0x00c5:
        r2.disconnect();
    L_0x00c8:
        return r1;
    L_0x00c9:
        if (r7 == 0) goto L_0x00ce;
    L_0x00cb:
        r7.disconnect();
    L_0x00ce:
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.telemetry.net.HttpURLConnectionTelemetryClient.uploadPing(org.mozilla.telemetry.config.TelemetryConfiguration, java.lang.String, java.lang.String):boolean");
    }

    /* Access modifiers changed, original: 0000 */
    public int upload(HttpURLConnection httpURLConnection, String str) throws IOException {
        Throwable e;
        Closeable closeable = null;
        try {
            OutputStream outputStream = httpURLConnection.getOutputStream();
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedWriter.write(str);
                bufferedWriter.flush();
                bufferedWriter.close();
                int responseCode = httpURLConnection.getResponseCode();
                IOUtils.safeClose(outputStream);
                return responseCode;
            } catch (ArrayIndexOutOfBoundsException e2) {
                e = e2;
                closeable = outputStream;
                try {
                    throw new IOException(e);
                } catch (Throwable th) {
                    e = th;
                    IOUtils.safeClose(closeable);
                    throw e;
                }
            } catch (Throwable th2) {
                e = th2;
                closeable = outputStream;
                IOUtils.safeClose(closeable);
                throw e;
            }
        } catch (ArrayIndexOutOfBoundsException e3) {
            e = e3;
            throw new IOException(e);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public HttpURLConnection openConnectionConnection(String str, String str2) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(str2);
        return (HttpURLConnection) new URL(stringBuilder.toString()).openConnection();
    }

    /* Access modifiers changed, original: 0000 */
    public String createDateHeaderValue() {
        Calendar instance = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return simpleDateFormat.format(instance.getTime());
    }
}
