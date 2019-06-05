// 
// Decompiled by Procyon v0.5.34
// 

package okhttp3.internal.http;

import java.net.HttpRetryException;
import java.io.Closeable;
import okhttp3.internal.Util;
import okhttp3.internal.http2.ConnectionShutdownException;
import okhttp3.internal.connection.RouteException;
import okhttp3.ResponseBody;
import okhttp3.Connection;
import javax.net.ssl.SSLPeerUnverifiedException;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLHandshakeException;
import java.net.SocketTimeoutException;
import java.io.InterruptedIOException;
import java.io.IOException;
import okhttp3.Route;
import okhttp3.internal.connection.RealConnection;
import okhttp3.RequestBody;
import java.net.ProtocolException;
import java.net.Proxy;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.CertificatePinner;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.Address;
import okhttp3.HttpUrl;
import okhttp3.internal.connection.StreamAllocation;
import okhttp3.OkHttpClient;
import okhttp3.Interceptor;

public final class RetryAndFollowUpInterceptor implements Interceptor
{
    private static final int MAX_FOLLOW_UPS = 20;
    private Object callStackTrace;
    private volatile boolean canceled;
    private final OkHttpClient client;
    private final boolean forWebSocket;
    private StreamAllocation streamAllocation;
    
    public RetryAndFollowUpInterceptor(final OkHttpClient client, final boolean forWebSocket) {
        this.client = client;
        this.forWebSocket = forWebSocket;
    }
    
    private Address createAddress(final HttpUrl httpUrl) {
        SSLSocketFactory sslSocketFactory = null;
        HostnameVerifier hostnameVerifier = null;
        CertificatePinner certificatePinner = null;
        if (httpUrl.isHttps()) {
            sslSocketFactory = this.client.sslSocketFactory();
            hostnameVerifier = this.client.hostnameVerifier();
            certificatePinner = this.client.certificatePinner();
        }
        return new Address(httpUrl.host(), httpUrl.port(), this.client.dns(), this.client.socketFactory(), sslSocketFactory, hostnameVerifier, certificatePinner, this.client.proxyAuthenticator(), this.client.proxy(), this.client.protocols(), this.client.connectionSpecs(), this.client.proxySelector());
    }
    
    private Request followUpRequest(final Response response) throws IOException {
        final Request request = null;
        if (response == null) {
            throw new IllegalStateException();
        }
        final RealConnection connection = this.streamAllocation.connection();
        Route route;
        if (connection != null) {
            route = connection.route();
        }
        else {
            route = null;
        }
        final int code = response.code();
        final String method = response.request().method();
        Request request2 = null;
        Label_0239: {
            switch (code) {
                default: {
                    request2 = request;
                    break;
                }
                case 407: {
                    Proxy proxy;
                    if (route != null) {
                        proxy = route.proxy();
                    }
                    else {
                        proxy = this.client.proxy();
                    }
                    if (proxy.type() != Proxy.Type.HTTP) {
                        throw new ProtocolException("Received HTTP_PROXY_AUTH (407) code while not using proxy");
                    }
                    request2 = this.client.proxyAuthenticator().authenticate(route, response);
                    break;
                }
                case 401: {
                    request2 = this.client.authenticator().authenticate(route, response);
                    break;
                }
                case 307:
                case 308: {
                    if (method.equals("GET")) {
                        break Label_0239;
                    }
                    request2 = request;
                    if (method.equals("HEAD")) {
                        break Label_0239;
                    }
                    break;
                }
                case 300:
                case 301:
                case 302:
                case 303: {
                    request2 = request;
                    if (!this.client.followRedirects()) {
                        break;
                    }
                    final String header = response.header("Location");
                    request2 = request;
                    if (header == null) {
                        break;
                    }
                    final HttpUrl resolve = response.request().url().resolve(header);
                    request2 = request;
                    if (resolve != null) {
                        if (!resolve.scheme().equals(response.request().url().scheme())) {
                            request2 = request;
                            if (!this.client.followSslRedirects()) {
                                break;
                            }
                        }
                        final Request.Builder builder = response.request().newBuilder();
                        if (HttpMethod.permitsRequestBody(method)) {
                            final boolean redirectsWithBody = HttpMethod.redirectsWithBody(method);
                            if (HttpMethod.redirectsToGet(method)) {
                                builder.method("GET", null);
                            }
                            else {
                                RequestBody body;
                                if (redirectsWithBody) {
                                    body = response.request().body();
                                }
                                else {
                                    body = null;
                                }
                                builder.method(method, body);
                            }
                            if (!redirectsWithBody) {
                                builder.removeHeader("Transfer-Encoding");
                                builder.removeHeader("Content-Length");
                                builder.removeHeader("Content-Type");
                            }
                        }
                        if (!this.sameConnection(response, resolve)) {
                            builder.removeHeader("Authorization");
                        }
                        request2 = builder.url(resolve).build();
                        break;
                    }
                    break;
                }
                case 408: {
                    request2 = request;
                    if (!(response.request().body() instanceof UnrepeatableRequestBody)) {
                        request2 = response.request();
                        break;
                    }
                    break;
                }
            }
        }
        return request2;
    }
    
    private boolean isRecoverable(final IOException ex, final boolean b) {
        final boolean b2 = true;
        final boolean b3 = false;
        boolean b4;
        if (ex instanceof ProtocolException) {
            b4 = b3;
        }
        else if (ex instanceof InterruptedIOException) {
            b4 = (ex instanceof SocketTimeoutException && !b && b2);
        }
        else {
            if (ex instanceof SSLHandshakeException) {
                b4 = b3;
                if (ex.getCause() instanceof CertificateException) {
                    return b4;
                }
            }
            b4 = b3;
            if (!(ex instanceof SSLPeerUnverifiedException)) {
                b4 = true;
            }
        }
        return b4;
    }
    
    private boolean recover(final IOException ex, final boolean b, final Request request) {
        final boolean b2 = false;
        this.streamAllocation.streamFailed(ex);
        boolean b3;
        if (!this.client.retryOnConnectionFailure()) {
            b3 = b2;
        }
        else {
            if (b) {
                b3 = b2;
                if (request.body() instanceof UnrepeatableRequestBody) {
                    return b3;
                }
            }
            b3 = b2;
            if (this.isRecoverable(ex, b)) {
                b3 = b2;
                if (this.streamAllocation.hasMoreRoutes()) {
                    b3 = true;
                }
            }
        }
        return b3;
    }
    
    private boolean sameConnection(final Response response, final HttpUrl httpUrl) {
        final HttpUrl url = response.request().url();
        return url.host().equals(httpUrl.host()) && url.port() == httpUrl.port() && url.scheme().equals(httpUrl.scheme());
    }
    
    public void cancel() {
        this.canceled = true;
        final StreamAllocation streamAllocation = this.streamAllocation;
        if (streamAllocation != null) {
            streamAllocation.cancel();
        }
    }
    
    @Override
    public Response intercept(final Chain chain) throws IOException {
        Object obj = chain.request();
        this.streamAllocation = new StreamAllocation(this.client.connectionPool(), this.createAddress(((Request)obj).url()), this.callStackTrace);
        int i = 0;
        Response response = null;
        while (!this.canceled) {
            Object o = null;
            try {
                o = ((RealInterceptorChain)chain).proceed((Request)obj, this.streamAllocation, null, null);
                if (false) {
                    this.streamAllocation.streamFailed(null);
                    this.streamAllocation.release();
                }
                obj = o;
                if (response != null) {
                    obj = ((Response)o).newBuilder().priorResponse(response.newBuilder().body(null).build()).build();
                }
                o = this.followUpRequest((Response)obj);
                if (o == null) {
                    if (!this.forWebSocket) {
                        this.streamAllocation.release();
                    }
                    return (Response)obj;
                }
            }
            catch (RouteException o) {
                if (!this.recover(((RouteException)o).getLastConnectException(), false, (Request)obj)) {
                    throw ((RouteException)o).getLastConnectException();
                }
                goto Label_0206;
            }
            catch (IOException o) {
                if (!this.recover((IOException)o, !(o instanceof ConnectionShutdownException), (Request)obj)) {
                    throw o;
                }
                if (false) {
                    this.streamAllocation.streamFailed(null);
                    this.streamAllocation.release();
                    continue;
                }
                continue;
            }
            finally {
                if (true) {
                    this.streamAllocation.streamFailed(null);
                    this.streamAllocation.release();
                }
            }
            Util.closeQuietly(((Response)obj).body());
            if (++i > 20) {
                this.streamAllocation.release();
                throw new ProtocolException("Too many follow-up requests: " + i);
            }
            if (((Request)o).body() instanceof UnrepeatableRequestBody) {
                this.streamAllocation.release();
                throw new HttpRetryException("Cannot retry streamed HTTP body", ((Response)obj).code());
            }
            if (!this.sameConnection((Response)obj, ((Request)o).url())) {
                this.streamAllocation.release();
                this.streamAllocation = new StreamAllocation(this.client.connectionPool(), this.createAddress(((Request)o).url()), this.callStackTrace);
            }
            else if (this.streamAllocation.codec() != null) {
                throw new IllegalStateException("Closing the body of " + obj + " didn't close its backing stream. Bad interceptor?");
            }
            response = (Response)obj;
            obj = o;
        }
        this.streamAllocation.release();
        throw new IOException("Canceled");
    }
    
    public boolean isCanceled() {
        return this.canceled;
    }
    
    public void setCallStackTrace(final Object callStackTrace) {
        this.callStackTrace = callStackTrace;
    }
    
    public StreamAllocation streamAllocation() {
        return this.streamAllocation;
    }
}
