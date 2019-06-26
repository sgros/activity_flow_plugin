package com.google.android.exoplayer2.upstream;

import android.net.Uri;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import com.google.android.exoplayer2.upstream.HttpDataSource.HttpDataSourceException;
import com.google.android.exoplayer2.upstream.HttpDataSource.InvalidContentTypeException;
import com.google.android.exoplayer2.upstream.HttpDataSource.InvalidResponseCodeException;
import com.google.android.exoplayer2.upstream.HttpDataSource.RequestProperties;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Predicate;
import com.google.android.exoplayer2.util.Util;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

public class DefaultHttpDataSource extends BaseDataSource implements HttpDataSource {
    private static final Pattern CONTENT_RANGE_HEADER = Pattern.compile("^bytes (\\d+)-(\\d+)/(\\d+)$");
    private static final AtomicReference<byte[]> skipBufferReference = new AtomicReference();
    private final boolean allowCrossProtocolRedirects;
    private long bytesRead;
    private long bytesSkipped;
    private long bytesToRead;
    private long bytesToSkip;
    private final int connectTimeoutMillis;
    private HttpURLConnection connection;
    private final Predicate<String> contentTypePredicate;
    private DataSpec dataSpec;
    private final RequestProperties defaultRequestProperties;
    private InputStream inputStream;
    private boolean opened;
    private final int readTimeoutMillis;
    private final RequestProperties requestProperties;
    private final String userAgent;

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:33:0x00be in {3, 11, 21, 26, 27, 28, 29, 30, 32} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    private java.net.HttpURLConnection makeConnection(com.google.android.exoplayer2.upstream.DataSpec r24) throws java.io.IOException {
        /*
        r23 = this;
        r0 = r24;
        r1 = new java.net.URL;
        r2 = r0.uri;
        r2 = r2.toString();
        r1.<init>(r2);
        r2 = r0.httpMethod;
        r3 = r0.httpBody;
        r14 = r0.position;
        r12 = r0.length;
        r10 = 1;
        r16 = r0.isFlagSet(r10);
        r11 = 2;
        r17 = r0.isFlagSet(r11);
        r9 = r23;
        r0 = r9.allowCrossProtocolRedirects;
        if (r0 != 0) goto L_0x0033;
        r10 = 1;
        r0 = r23;
        r4 = r14;
        r6 = r12;
        r8 = r16;
        r9 = r17;
        r0 = r0.makeConnection(r1, r2, r3, r4, r6, r8, r9, r10);
        return r0;
        r0 = 0;
        r8 = r0 + 1;
        r4 = 20;
        if (r0 > r4) goto L_0x00a3;
        r0 = 0;
        r4 = r23;
        r5 = r1;
        r6 = r2;
        r7 = r3;
        r18 = r8;
        r8 = r14;
        r10 = r12;
        r19 = r12;
        r12 = r16;
        r13 = r17;
        r21 = r14;
        r14 = r0;
        r0 = r4.makeConnection(r5, r6, r7, r8, r10, r12, r13, r14);
        r4 = r0.getResponseCode();
        r5 = "Location";
        r5 = r0.getHeaderField(r5);
        r6 = 303; // 0x12f float:4.25E-43 double:1.497E-321;
        r7 = 302; // 0x12e float:4.23E-43 double:1.49E-321;
        r8 = 301; // 0x12d float:4.22E-43 double:1.487E-321;
        r9 = 300; // 0x12c float:4.2E-43 double:1.48E-321;
        r10 = 1;
        if (r2 == r10) goto L_0x006c;
        r11 = 3;
        if (r2 != r11) goto L_0x006a;
        goto L_0x006c;
        r11 = 2;
        goto L_0x007d;
        if (r4 == r9) goto L_0x0093;
        if (r4 == r8) goto L_0x0093;
        if (r4 == r7) goto L_0x0093;
        if (r4 == r6) goto L_0x0093;
        r11 = 307; // 0x133 float:4.3E-43 double:1.517E-321;
        if (r4 == r11) goto L_0x0093;
        r11 = 308; // 0x134 float:4.32E-43 double:1.52E-321;
        if (r4 != r11) goto L_0x006a;
        goto L_0x0093;
        if (r2 != r11) goto L_0x0092;
        if (r4 == r9) goto L_0x0087;
        if (r4 == r8) goto L_0x0087;
        if (r4 == r7) goto L_0x0087;
        if (r4 != r6) goto L_0x0092;
        r0.disconnect();
        r0 = 0;
        r1 = handleRedirect(r1, r5);
        r3 = r0;
        r2 = 1;
        goto L_0x009c;
        return r0;
        r11 = 2;
        r0.disconnect();
        r0 = handleRedirect(r1, r5);
        r1 = r0;
        r0 = r18;
        r12 = r19;
        r14 = r21;
        goto L_0x0034;
        r18 = r8;
        r0 = new java.net.NoRouteToHostException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Too many redirects: ";
        r1.append(r2);
        r2 = r18;
        r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.upstream.DefaultHttpDataSource.makeConnection(com.google.android.exoplayer2.upstream.DataSpec):java.net.HttpURLConnection");
    }

    public DefaultHttpDataSource(String str, Predicate<String> predicate, int i, int i2, boolean z, RequestProperties requestProperties) {
        super(true);
        Assertions.checkNotEmpty(str);
        this.userAgent = str;
        this.contentTypePredicate = predicate;
        this.requestProperties = new RequestProperties();
        this.connectTimeoutMillis = i;
        this.readTimeoutMillis = i2;
        this.allowCrossProtocolRedirects = z;
        this.defaultRequestProperties = requestProperties;
    }

    @Deprecated
    public DefaultHttpDataSource(String str, Predicate<String> predicate, TransferListener transferListener, int i, int i2, boolean z, RequestProperties requestProperties) {
        this(str, predicate, i, i2, z, requestProperties);
        if (transferListener != null) {
            addTransferListener(transferListener);
        }
    }

    public Uri getUri() {
        HttpURLConnection httpURLConnection = this.connection;
        return httpURLConnection == null ? null : Uri.parse(httpURLConnection.getURL().toString());
    }

    public Map<String, List<String>> getResponseHeaders() {
        HttpURLConnection httpURLConnection = this.connection;
        return httpURLConnection == null ? Collections.emptyMap() : httpURLConnection.getHeaderFields();
    }

    public long open(DataSpec dataSpec) throws HttpDataSourceException {
        StringBuilder stringBuilder;
        String str = "Unable to connect to ";
        this.dataSpec = dataSpec;
        long j = 0;
        this.bytesRead = 0;
        this.bytesSkipped = 0;
        transferInitializing(dataSpec);
        try {
            this.connection = makeConnection(dataSpec);
            try {
                int responseCode = this.connection.getResponseCode();
                str = this.connection.getResponseMessage();
                if (responseCode < Callback.DEFAULT_DRAG_ANIMATION_DURATION || responseCode > 299) {
                    Map headerFields = this.connection.getHeaderFields();
                    closeConnectionQuietly();
                    InvalidResponseCodeException invalidResponseCodeException = new InvalidResponseCodeException(responseCode, str, headerFields, dataSpec);
                    if (responseCode == 416) {
                        invalidResponseCodeException.initCause(new DataSourceException(0));
                    }
                    throw invalidResponseCodeException;
                }
                str = this.connection.getContentType();
                Predicate predicate = this.contentTypePredicate;
                if (predicate == null || predicate.evaluate(str)) {
                    long j2;
                    if (responseCode == Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                        j2 = dataSpec.position;
                        if (j2 != 0) {
                            j = j2;
                        }
                    }
                    this.bytesToSkip = j;
                    if (dataSpec.isFlagSet(1)) {
                        this.bytesToRead = dataSpec.length;
                    } else {
                        long j3 = dataSpec.length;
                        j2 = -1;
                        if (j3 != -1) {
                            this.bytesToRead = j3;
                        } else {
                            j3 = getContentLength(this.connection);
                            if (j3 != -1) {
                                j2 = j3 - this.bytesToSkip;
                            }
                            this.bytesToRead = j2;
                        }
                    }
                    try {
                        this.inputStream = this.connection.getInputStream();
                        this.opened = true;
                        transferStarted(dataSpec);
                        return this.bytesToRead;
                    } catch (IOException e) {
                        closeConnectionQuietly();
                        throw new HttpDataSourceException(e, dataSpec, 1);
                    }
                }
                closeConnectionQuietly();
                throw new InvalidContentTypeException(str, dataSpec);
            } catch (IOException e2) {
                closeConnectionQuietly();
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(dataSpec.uri.toString());
                throw new HttpDataSourceException(stringBuilder.toString(), e2, dataSpec, 1);
            }
        } catch (IOException e22) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(dataSpec.uri.toString());
            throw new HttpDataSourceException(stringBuilder.toString(), e22, dataSpec, 1);
        }
    }

    public int read(byte[] bArr, int i, int i2) throws HttpDataSourceException {
        try {
            skipInternal();
            return readInternal(bArr, i, i2);
        } catch (IOException e) {
            throw new HttpDataSourceException(e, this.dataSpec, 2);
        }
    }

    public void close() throws HttpDataSourceException {
        try {
            if (this.inputStream != null) {
                maybeTerminateInputStream(this.connection, bytesRemaining());
                this.inputStream.close();
            }
            this.inputStream = null;
            closeConnectionQuietly();
            if (this.opened) {
                this.opened = false;
                transferEnded();
            }
        } catch (IOException e) {
            throw new HttpDataSourceException(e, this.dataSpec, 3);
        } catch (Throwable th) {
            this.inputStream = null;
            closeConnectionQuietly();
            if (this.opened) {
                this.opened = false;
                transferEnded();
            }
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final long bytesRemaining() {
        long j = this.bytesToRead;
        return j == -1 ? j : j - this.bytesRead;
    }

    private HttpURLConnection makeConnection(URL url, int i, byte[] bArr, long j, long j2, boolean z, boolean z2, boolean z3) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setConnectTimeout(this.connectTimeoutMillis);
        httpURLConnection.setReadTimeout(this.readTimeoutMillis);
        RequestProperties requestProperties = this.defaultRequestProperties;
        if (requestProperties != null) {
            for (Entry entry : requestProperties.getSnapshot().entrySet()) {
                httpURLConnection.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
            }
        }
        for (Entry entry2 : this.requestProperties.getSnapshot().entrySet()) {
            httpURLConnection.setRequestProperty((String) entry2.getKey(), (String) entry2.getValue());
        }
        if (!(j == 0 && j2 == -1)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("bytes=");
            stringBuilder.append(j);
            stringBuilder.append("-");
            String stringBuilder2 = stringBuilder.toString();
            if (j2 != -1) {
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append(stringBuilder2);
                stringBuilder3.append((j + j2) - 1);
                stringBuilder2 = stringBuilder3.toString();
            }
            httpURLConnection.setRequestProperty("Range", stringBuilder2);
        }
        httpURLConnection.setRequestProperty("User-Agent", this.userAgent);
        if (!z) {
            httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
        }
        if (z2) {
            httpURLConnection.setRequestProperty("Icy-MetaData", "1");
        }
        httpURLConnection.setInstanceFollowRedirects(z3);
        httpURLConnection.setDoOutput(bArr != null);
        httpURLConnection.setRequestMethod(DataSpec.getStringForHttpMethod(i));
        if (bArr != null) {
            httpURLConnection.setFixedLengthStreamingMode(bArr.length);
            httpURLConnection.connect();
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(bArr);
            outputStream.close();
        } else {
            httpURLConnection.connect();
        }
        return httpURLConnection;
    }

    private static URL handleRedirect(URL url, String str) throws IOException {
        if (str != null) {
            URL url2 = new URL(url, str);
            String protocol = url2.getProtocol();
            if ("https".equals(protocol) || "http".equals(protocol)) {
                return url2;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported protocol redirect: ");
            stringBuilder.append(protocol);
            throw new ProtocolException(stringBuilder.toString());
        }
        throw new ProtocolException("Null location redirect");
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x003a  */
    private static long getContentLength(java.net.HttpURLConnection r10) {
        /*
        r0 = "Content-Length";
        r0 = r10.getHeaderField(r0);
        r1 = android.text.TextUtils.isEmpty(r0);
        r2 = "]";
        r3 = "DefaultHttpDataSource";
        if (r1 != 0) goto L_0x002c;
    L_0x0010:
        r4 = java.lang.Long.parseLong(r0);	 Catch:{ NumberFormatException -> 0x0015 }
        goto L_0x002e;
    L_0x0015:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r4 = "Unexpected Content-Length [";
        r1.append(r4);
        r1.append(r0);
        r1.append(r2);
        r1 = r1.toString();
        com.google.android.exoplayer2.util.Log.m14e(r3, r1);
    L_0x002c:
        r4 = -1;
    L_0x002e:
        r1 = "Content-Range";
        r10 = r10.getHeaderField(r1);
        r1 = android.text.TextUtils.isEmpty(r10);
        if (r1 != 0) goto L_0x00a4;
    L_0x003a:
        r1 = CONTENT_RANGE_HEADER;
        r1 = r1.matcher(r10);
        r6 = r1.find();
        if (r6 == 0) goto L_0x00a4;
    L_0x0046:
        r6 = 2;
        r6 = r1.group(r6);	 Catch:{ NumberFormatException -> 0x008d }
        r6 = java.lang.Long.parseLong(r6);	 Catch:{ NumberFormatException -> 0x008d }
        r8 = 1;
        r1 = r1.group(r8);	 Catch:{ NumberFormatException -> 0x008d }
        r8 = java.lang.Long.parseLong(r1);	 Catch:{ NumberFormatException -> 0x008d }
        r6 = r6 - r8;
        r8 = 1;
        r6 = r6 + r8;
        r8 = 0;
        r1 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1));
        if (r1 >= 0) goto L_0x0064;
    L_0x0062:
        r4 = r6;
        goto L_0x00a4;
    L_0x0064:
        r1 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r1 == 0) goto L_0x00a4;
    L_0x0068:
        r1 = new java.lang.StringBuilder;	 Catch:{ NumberFormatException -> 0x008d }
        r1.<init>();	 Catch:{ NumberFormatException -> 0x008d }
        r8 = "Inconsistent headers [";
        r1.append(r8);	 Catch:{ NumberFormatException -> 0x008d }
        r1.append(r0);	 Catch:{ NumberFormatException -> 0x008d }
        r0 = "] [";
        r1.append(r0);	 Catch:{ NumberFormatException -> 0x008d }
        r1.append(r10);	 Catch:{ NumberFormatException -> 0x008d }
        r1.append(r2);	 Catch:{ NumberFormatException -> 0x008d }
        r0 = r1.toString();	 Catch:{ NumberFormatException -> 0x008d }
        com.google.android.exoplayer2.util.Log.m18w(r3, r0);	 Catch:{ NumberFormatException -> 0x008d }
        r0 = java.lang.Math.max(r4, r6);	 Catch:{ NumberFormatException -> 0x008d }
        r4 = r0;
        goto L_0x00a4;
    L_0x008d:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Unexpected Content-Range [";
        r0.append(r1);
        r0.append(r10);
        r0.append(r2);
        r10 = r0.toString();
        com.google.android.exoplayer2.util.Log.m14e(r3, r10);
    L_0x00a4:
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.upstream.DefaultHttpDataSource.getContentLength(java.net.HttpURLConnection):long");
    }

    private void skipInternal() throws IOException {
        if (this.bytesSkipped != this.bytesToSkip) {
            byte[] bArr = (byte[]) skipBufferReference.getAndSet(null);
            if (bArr == null) {
                bArr = new byte[4096];
            }
            while (true) {
                long j = this.bytesSkipped;
                long j2 = this.bytesToSkip;
                if (j != j2) {
                    int read = this.inputStream.read(bArr, 0, (int) Math.min(j2 - j, (long) bArr.length));
                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedIOException();
                    } else if (read != -1) {
                        this.bytesSkipped += (long) read;
                        bytesTransferred(read);
                    } else {
                        throw new EOFException();
                    }
                }
                skipBufferReference.set(bArr);
                return;
            }
        }
    }

    private int readInternal(byte[] bArr, int i, int i2) throws IOException {
        if (i2 == 0) {
            return 0;
        }
        long j = this.bytesToRead;
        if (j != -1) {
            j -= this.bytesRead;
            if (j == 0) {
                return -1;
            }
            i2 = (int) Math.min((long) i2, j);
        }
        int read = this.inputStream.read(bArr, i, i2);
        if (read != -1) {
            this.bytesRead += (long) read;
            bytesTransferred(read);
            return read;
        } else if (this.bytesToRead == -1) {
            return -1;
        } else {
            throw new EOFException();
        }
    }

    private static void maybeTerminateInputStream(HttpURLConnection httpURLConnection, long j) {
        int i = Util.SDK_INT;
        if (i == 19 || i == 20) {
            try {
                InputStream inputStream = httpURLConnection.getInputStream();
                if (j == -1) {
                    if (inputStream.read() == -1) {
                        return;
                    }
                } else if (j <= 2048) {
                    return;
                }
                String name = inputStream.getClass().getName();
                if ("com.android.okhttp.internal.http.HttpTransport$ChunkedInputStream".equals(name) || "com.android.okhttp.internal.http.HttpTransport$FixedLengthInputStream".equals(name)) {
                    Method declaredMethod = inputStream.getClass().getSuperclass().getDeclaredMethod("unexpectedEndOfInput", new Class[0]);
                    declaredMethod.setAccessible(true);
                    declaredMethod.invoke(inputStream, new Object[0]);
                }
            } catch (Exception unused) {
            }
        }
    }

    private void closeConnectionQuietly() {
        HttpURLConnection httpURLConnection = this.connection;
        if (httpURLConnection != null) {
            try {
                httpURLConnection.disconnect();
            } catch (Exception e) {
                Log.m15e("DefaultHttpDataSource", "Unexpected error while disconnecting", e);
            }
            this.connection = null;
        }
    }
}
