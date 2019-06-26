// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.upstream;

import android.net.Uri;
import java.util.Collections;
import java.util.List;
import java.io.InterruptedIOException;
import java.io.EOFException;
import java.lang.reflect.Method;
import com.google.android.exoplayer2.util.Util;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;
import java.net.NoRouteToHostException;
import java.io.IOException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.regex.Matcher;
import android.text.TextUtils;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Assertions;
import java.io.InputStream;
import com.google.android.exoplayer2.util.Predicate;
import java.net.HttpURLConnection;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

public class DefaultHttpDataSource extends BaseDataSource implements HttpDataSource
{
    private static final Pattern CONTENT_RANGE_HEADER;
    private static final AtomicReference<byte[]> skipBufferReference;
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
    
    static {
        CONTENT_RANGE_HEADER = Pattern.compile("^bytes (\\d+)-(\\d+)/(\\d+)$");
        skipBufferReference = new AtomicReference<byte[]>();
    }
    
    public DefaultHttpDataSource(final String userAgent, final Predicate<String> contentTypePredicate, final int connectTimeoutMillis, final int readTimeoutMillis, final boolean allowCrossProtocolRedirects, final RequestProperties defaultRequestProperties) {
        super(true);
        Assertions.checkNotEmpty(userAgent);
        this.userAgent = userAgent;
        this.contentTypePredicate = contentTypePredicate;
        this.requestProperties = new RequestProperties();
        this.connectTimeoutMillis = connectTimeoutMillis;
        this.readTimeoutMillis = readTimeoutMillis;
        this.allowCrossProtocolRedirects = allowCrossProtocolRedirects;
        this.defaultRequestProperties = defaultRequestProperties;
    }
    
    @Deprecated
    public DefaultHttpDataSource(final String s, final Predicate<String> predicate, final TransferListener transferListener, final int n, final int n2, final boolean b, final RequestProperties requestProperties) {
        this(s, predicate, n, n2, b, requestProperties);
        if (transferListener != null) {
            this.addTransferListener(transferListener);
        }
    }
    
    private void closeConnectionQuietly() {
        final HttpURLConnection connection = this.connection;
        if (connection != null) {
            try {
                connection.disconnect();
            }
            catch (Exception ex) {
                Log.e("DefaultHttpDataSource", "Unexpected error while disconnecting", ex);
            }
            this.connection = null;
        }
    }
    
    private static long getContentLength(HttpURLConnection headerField) {
        final String headerField2 = headerField.getHeaderField("Content-Length");
        long long1 = 0L;
        Label_0070: {
            if (!TextUtils.isEmpty((CharSequence)headerField2)) {
                try {
                    long1 = Long.parseLong(headerField2);
                    break Label_0070;
                }
                catch (NumberFormatException ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unexpected Content-Length [");
                    sb.append(headerField2);
                    sb.append("]");
                    Log.e("DefaultHttpDataSource", sb.toString());
                }
            }
            long1 = -1L;
        }
        headerField = (HttpURLConnection)headerField.getHeaderField("Content-Range");
        long max = long1;
        if (!TextUtils.isEmpty((CharSequence)headerField)) {
            final Matcher matcher = DefaultHttpDataSource.CONTENT_RANGE_HEADER.matcher((CharSequence)headerField);
            max = long1;
            if (matcher.find()) {
                try {
                    final long b = Long.parseLong(matcher.group(2)) - Long.parseLong(matcher.group(1)) + 1L;
                    if (long1 < 0L) {
                        max = b;
                    }
                    else {
                        max = long1;
                        if (long1 != b) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("Inconsistent headers [");
                            sb2.append(headerField2);
                            sb2.append("] [");
                            sb2.append((String)headerField);
                            sb2.append("]");
                            Log.w("DefaultHttpDataSource", sb2.toString());
                            max = Math.max(long1, b);
                        }
                    }
                }
                catch (NumberFormatException ex2) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("Unexpected Content-Range [");
                    sb3.append((String)headerField);
                    sb3.append("]");
                    Log.e("DefaultHttpDataSource", sb3.toString());
                    max = long1;
                }
            }
        }
        return max;
    }
    
    private static URL handleRedirect(final URL context, final String spec) throws IOException {
        if (spec == null) {
            throw new ProtocolException("Null location redirect");
        }
        final URL url = new URL(context, spec);
        final String protocol = url.getProtocol();
        if (!"https".equals(protocol) && !"http".equals(protocol)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unsupported protocol redirect: ");
            sb.append(protocol);
            throw new ProtocolException(sb.toString());
        }
        return url;
    }
    
    private HttpURLConnection makeConnection(final DataSpec dataSpec) throws IOException {
        URL url = new URL(dataSpec.uri.toString());
        int httpMethod = dataSpec.httpMethod;
        final byte[] httpBody = dataSpec.httpBody;
        final long position = dataSpec.position;
        final long length = dataSpec.length;
        final boolean flagSet = dataSpec.isFlagSet(1);
        final boolean flagSet2 = dataSpec.isFlagSet(2);
        if (!this.allowCrossProtocolRedirects) {
            return this.makeConnection(url, httpMethod, httpBody, position, length, flagSet, flagSet2, true);
        }
        int n = 0;
        byte[] array = httpBody;
        while (true) {
            final int i = n + 1;
            if (n > 20) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Too many redirects: ");
                sb.append(i);
                throw new NoRouteToHostException(sb.toString());
            }
            final HttpURLConnection connection = this.makeConnection(url, httpMethod, array, position, length, flagSet, flagSet2, false);
            final int responseCode = connection.getResponseCode();
            final String headerField = connection.getHeaderField("Location");
            byte[] array2;
            URL handleRedirect2;
            if ((httpMethod == 1 || httpMethod == 3) && (responseCode == 300 || responseCode == 301 || responseCode == 302 || responseCode == 303 || responseCode == 307 || responseCode == 308)) {
                connection.disconnect();
                final URL handleRedirect = handleRedirect(url, headerField);
                array2 = array;
                handleRedirect2 = handleRedirect;
            }
            else {
                if (httpMethod != 2 || (responseCode != 300 && responseCode != 301 && responseCode != 302 && responseCode != 303)) {
                    return connection;
                }
                connection.disconnect();
                handleRedirect2 = handleRedirect(url, headerField);
                array2 = null;
                httpMethod = 1;
            }
            n = i;
            url = handleRedirect2;
            array = array2;
        }
    }
    
    private HttpURLConnection makeConnection(final URL url, final int n, final byte[] b, final long lng, final long n2, final boolean b2, final boolean b3, final boolean instanceFollowRedirects) throws IOException {
        final HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
        httpURLConnection.setConnectTimeout(this.connectTimeoutMillis);
        httpURLConnection.setReadTimeout(this.readTimeoutMillis);
        final RequestProperties defaultRequestProperties = this.defaultRequestProperties;
        if (defaultRequestProperties != null) {
            for (final Map.Entry<String, String> entry : defaultRequestProperties.getSnapshot().entrySet()) {
                httpURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        for (final Map.Entry<String, String> entry2 : this.requestProperties.getSnapshot().entrySet()) {
            httpURLConnection.setRequestProperty(entry2.getKey(), entry2.getValue());
        }
        if (lng != 0L || n2 != -1L) {
            final StringBuilder sb = new StringBuilder();
            sb.append("bytes=");
            sb.append(lng);
            sb.append("-");
            String s = sb.toString();
            if (n2 != -1L) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(s);
                sb2.append(lng + n2 - 1L);
                s = sb2.toString();
            }
            httpURLConnection.setRequestProperty("Range", s);
        }
        httpURLConnection.setRequestProperty("User-Agent", this.userAgent);
        if (!b2) {
            httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
        }
        if (b3) {
            httpURLConnection.setRequestProperty("Icy-MetaData", "1");
        }
        httpURLConnection.setInstanceFollowRedirects(instanceFollowRedirects);
        httpURLConnection.setDoOutput(b != null);
        httpURLConnection.setRequestMethod(DataSpec.getStringForHttpMethod(n));
        if (b != null) {
            httpURLConnection.setFixedLengthStreamingMode(b.length);
            httpURLConnection.connect();
            final OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(b);
            outputStream.close();
        }
        else {
            httpURLConnection.connect();
        }
        return httpURLConnection;
    }
    
    private static void maybeTerminateInputStream(final HttpURLConnection httpURLConnection, final long n) {
        final int sdk_INT = Util.SDK_INT;
        if (sdk_INT != 19 && sdk_INT != 20) {
            return;
        }
        try {
            final InputStream inputStream = httpURLConnection.getInputStream();
            if (n == -1L) {
                if (inputStream.read() == -1) {
                    return;
                }
            }
            else if (n <= 2048L) {
                return;
            }
            final String name = inputStream.getClass().getName();
            if ("com.android.okhttp.internal.http.HttpTransport$ChunkedInputStream".equals(name) || "com.android.okhttp.internal.http.HttpTransport$FixedLengthInputStream".equals(name)) {
                final Method declaredMethod = inputStream.getClass().getSuperclass().getDeclaredMethod("unexpectedEndOfInput", (Class<?>[])new Class[0]);
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(inputStream, new Object[0]);
            }
        }
        catch (Exception ex) {}
    }
    
    private int readInternal(final byte[] b, int read, final int n) throws IOException {
        if (n == 0) {
            return 0;
        }
        final long bytesToRead = this.bytesToRead;
        int len = n;
        if (bytesToRead != -1L) {
            final long b2 = bytesToRead - this.bytesRead;
            if (b2 == 0L) {
                return -1;
            }
            len = (int)Math.min(n, b2);
        }
        read = this.inputStream.read(b, read, len);
        if (read != -1) {
            this.bytesRead += read;
            this.bytesTransferred(read);
            return read;
        }
        if (this.bytesToRead == -1L) {
            return -1;
        }
        throw new EOFException();
    }
    
    private void skipInternal() throws IOException {
        if (this.bytesSkipped == this.bytesToSkip) {
            return;
        }
        byte[] array;
        if ((array = DefaultHttpDataSource.skipBufferReference.getAndSet(null)) == null) {
            array = new byte[4096];
        }
        while (true) {
            final long bytesSkipped = this.bytesSkipped;
            final long bytesToSkip = this.bytesToSkip;
            if (bytesSkipped == bytesToSkip) {
                DefaultHttpDataSource.skipBufferReference.set(array);
                return;
            }
            final int read = this.inputStream.read(array, 0, (int)Math.min(bytesToSkip - bytesSkipped, array.length));
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedIOException();
            }
            if (read == -1) {
                throw new EOFException();
            }
            this.bytesSkipped += read;
            this.bytesTransferred(read);
        }
    }
    
    protected final long bytesRemaining() {
        long bytesToRead = this.bytesToRead;
        if (bytesToRead != -1L) {
            bytesToRead -= this.bytesRead;
        }
        return bytesToRead;
    }
    
    @Override
    public void close() throws HttpDataSourceException {
        try {
            if (this.inputStream != null) {
                maybeTerminateInputStream(this.connection, this.bytesRemaining());
                try {
                    this.inputStream.close();
                }
                catch (IOException ex) {
                    throw new HttpDataSourceException(ex, this.dataSpec, 3);
                }
            }
        }
        finally {
            this.inputStream = null;
            this.closeConnectionQuietly();
            if (this.opened) {
                this.opened = false;
                this.transferEnded();
            }
        }
    }
    
    @Override
    public Map<String, List<String>> getResponseHeaders() {
        final HttpURLConnection connection = this.connection;
        Map<String, List<String>> map;
        if (connection == null) {
            map = Collections.emptyMap();
        }
        else {
            map = connection.getHeaderFields();
        }
        return map;
    }
    
    @Override
    public Uri getUri() {
        final HttpURLConnection connection = this.connection;
        Uri parse;
        if (connection == null) {
            parse = null;
        }
        else {
            parse = Uri.parse(connection.getURL().toString());
        }
        return parse;
    }
    
    @Override
    public long open(DataSpec dataSpec) throws HttpDataSourceException {
        this.dataSpec = dataSpec;
        final long n = 0L;
        this.bytesRead = 0L;
        this.bytesSkipped = 0L;
        this.transferInitializing(dataSpec);
        try {
            this.connection = this.makeConnection(dataSpec);
            try {
                final int responseCode = this.connection.getResponseCode();
                final String responseMessage = this.connection.getResponseMessage();
                if (responseCode >= 200) {
                    if (responseCode <= 299) {
                        final String contentType = this.connection.getContentType();
                        final Predicate<String> contentTypePredicate = this.contentTypePredicate;
                        if (contentTypePredicate != null && !contentTypePredicate.evaluate(contentType)) {
                            this.closeConnectionQuietly();
                            throw new InvalidContentTypeException(contentType, dataSpec);
                        }
                        long bytesToSkip = n;
                        if (responseCode == 200) {
                            final long position = dataSpec.position;
                            bytesToSkip = n;
                            if (position != 0L) {
                                bytesToSkip = position;
                            }
                        }
                        this.bytesToSkip = bytesToSkip;
                        if (!dataSpec.isFlagSet(1)) {
                            final long length = dataSpec.length;
                            long bytesToRead = -1L;
                            if (length != -1L) {
                                this.bytesToRead = length;
                            }
                            else {
                                final long contentLength = getContentLength(this.connection);
                                if (contentLength != -1L) {
                                    bytesToRead = contentLength - this.bytesToSkip;
                                }
                                this.bytesToRead = bytesToRead;
                            }
                        }
                        else {
                            this.bytesToRead = dataSpec.length;
                        }
                        try {
                            this.inputStream = this.connection.getInputStream();
                            this.opened = true;
                            this.transferStarted(dataSpec);
                            return this.bytesToRead;
                        }
                        catch (IOException ex) {
                            this.closeConnectionQuietly();
                            throw new HttpDataSourceException(ex, dataSpec, 1);
                        }
                    }
                }
                final Map<String, List<String>> headerFields = this.connection.getHeaderFields();
                this.closeConnectionQuietly();
                dataSpec = (DataSpec)new InvalidResponseCodeException(responseCode, responseMessage, headerFields, dataSpec);
                if (responseCode == 416) {
                    ((Throwable)dataSpec).initCause(new DataSourceException(0));
                }
                throw dataSpec;
            }
            catch (IOException ex2) {
                this.closeConnectionQuietly();
                final StringBuilder sb = new StringBuilder();
                sb.append("Unable to connect to ");
                sb.append(dataSpec.uri.toString());
                throw new HttpDataSourceException(sb.toString(), ex2, dataSpec, 1);
            }
        }
        catch (IOException ex3) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Unable to connect to ");
            sb2.append(dataSpec.uri.toString());
            throw new HttpDataSourceException(sb2.toString(), ex3, dataSpec, 1);
        }
    }
    
    @Override
    public int read(final byte[] array, int internal, final int n) throws HttpDataSourceException {
        try {
            this.skipInternal();
            internal = this.readInternal(array, internal, n);
            return internal;
        }
        catch (IOException ex) {
            throw new HttpDataSourceException(ex, this.dataSpec, 2);
        }
    }
}
