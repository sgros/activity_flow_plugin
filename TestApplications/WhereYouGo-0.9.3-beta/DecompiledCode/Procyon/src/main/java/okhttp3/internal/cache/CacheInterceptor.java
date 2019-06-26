// 
// Decompiled by Procyon v0.5.34
// 

package okhttp3.internal.cache;

import okhttp3.internal.http.HttpHeaders;
import okhttp3.Protocol;
import java.io.Closeable;
import okhttp3.internal.http.HttpMethod;
import okhttp3.Request;
import okhttp3.internal.Internal;
import okhttp3.Headers;
import okio.Sink;
import okhttp3.ResponseBody;
import okhttp3.internal.http.RealResponseBody;
import okio.Timeout;
import okio.Buffer;
import java.io.IOException;
import okhttp3.internal.Util;
import java.util.concurrent.TimeUnit;
import okio.BufferedSource;
import okio.BufferedSink;
import okio.Source;
import okio.Okio;
import okhttp3.Response;
import okhttp3.Interceptor;

public final class CacheInterceptor implements Interceptor
{
    final InternalCache cache;
    
    public CacheInterceptor(final InternalCache cache) {
        this.cache = cache;
    }
    
    private Response cacheWritingResponse(final CacheRequest cacheRequest, final Response response) throws IOException {
        Response build;
        if (cacheRequest == null) {
            build = response;
        }
        else {
            final Sink body = cacheRequest.body();
            build = response;
            if (body != null) {
                build = response.newBuilder().body(new RealResponseBody(response.headers(), Okio.buffer(new Source() {
                    boolean cacheRequestClosed;
                    final /* synthetic */ BufferedSink val$cacheBody = Okio.buffer(body);
                    final /* synthetic */ BufferedSource val$source = response.body().source();
                    
                    @Override
                    public void close() throws IOException {
                        if (!this.cacheRequestClosed && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
                            this.cacheRequestClosed = true;
                            cacheRequest.abort();
                        }
                        this.val$source.close();
                    }
                    
                    @Override
                    public long read(final Buffer buffer, long read) throws IOException {
                        while (true) {
                            try {
                                read = this.val$source.read(buffer, read);
                                if (read == -1L) {
                                    if (!this.cacheRequestClosed) {
                                        this.cacheRequestClosed = true;
                                        this.val$cacheBody.close();
                                    }
                                    read = -1L;
                                    return read;
                                }
                            }
                            catch (IOException ex) {
                                if (!this.cacheRequestClosed) {
                                    this.cacheRequestClosed = true;
                                    cacheRequest.abort();
                                }
                                throw ex;
                            }
                            buffer.copyTo(this.val$cacheBody.buffer(), buffer.size() - read, read);
                            this.val$cacheBody.emitCompleteSegments();
                            return read;
                        }
                    }
                    
                    @Override
                    public Timeout timeout() {
                        return this.val$source.timeout();
                    }
                }))).build();
            }
        }
        return build;
    }
    
    private static Headers combine(final Headers headers, final Headers headers2) {
        final Headers.Builder builder = new Headers.Builder();
        for (int i = 0; i < headers.size(); ++i) {
            final String name = headers.name(i);
            final String value = headers.value(i);
            if ((!"Warning".equalsIgnoreCase(name) || !value.startsWith("1")) && (!isEndToEnd(name) || headers2.get(name) == null)) {
                Internal.instance.addLenient(builder, name, value);
            }
        }
        for (int j = 0; j < headers2.size(); ++j) {
            final String name2 = headers2.name(j);
            if (!"Content-Length".equalsIgnoreCase(name2) && isEndToEnd(name2)) {
                Internal.instance.addLenient(builder, name2, headers2.value(j));
            }
        }
        return builder.build();
    }
    
    static boolean isEndToEnd(final String s) {
        return !"Connection".equalsIgnoreCase(s) && !"Keep-Alive".equalsIgnoreCase(s) && !"Proxy-Authenticate".equalsIgnoreCase(s) && !"Proxy-Authorization".equalsIgnoreCase(s) && !"TE".equalsIgnoreCase(s) && !"Trailers".equalsIgnoreCase(s) && !"Transfer-Encoding".equalsIgnoreCase(s) && !"Upgrade".equalsIgnoreCase(s);
    }
    
    private CacheRequest maybeCache(final Response response, final Request request, final InternalCache internalCache) throws IOException {
        final CacheRequest cacheRequest = null;
        CacheRequest put;
        if (internalCache == null) {
            put = cacheRequest;
        }
        else if (!CacheStrategy.isCacheable(response, request)) {
            put = cacheRequest;
            if (HttpMethod.invalidatesCache(request.method())) {
                try {
                    internalCache.remove(request);
                    put = cacheRequest;
                }
                catch (IOException ex) {
                    put = cacheRequest;
                }
            }
        }
        else {
            put = internalCache.put(response);
        }
        return put;
    }
    
    private static Response stripBody(final Response response) {
        Response build = response;
        if (response != null) {
            build = response;
            if (response.body() != null) {
                build = response.newBuilder().body(null).build();
            }
        }
        return build;
    }
    
    @Override
    public Response intercept(final Chain chain) throws IOException {
        Response value;
        if (this.cache != null) {
            value = this.cache.get(chain.request());
        }
        else {
            value = null;
        }
        final CacheStrategy value2 = new CacheStrategy.Factory(System.currentTimeMillis(), chain.request(), value).get();
        Object o = value2.networkRequest;
        final Response cacheResponse = value2.cacheResponse;
        if (this.cache != null) {
            this.cache.trackResponse(value2);
        }
        if (value != null && cacheResponse == null) {
            Util.closeQuietly(value.body());
        }
        Response response = null;
        if (o == null && cacheResponse == null) {
            response = new Response.Builder().request(chain.request()).protocol(Protocol.HTTP_1_1).code(504).message("Unsatisfiable Request (only-if-cached)").body(Util.EMPTY_RESPONSE).sentRequestAtMillis(-1L).receivedResponseAtMillis(System.currentTimeMillis()).build();
        }
        else if (o == null) {
            response = cacheResponse.newBuilder().cacheResponse(stripBody(cacheResponse)).build();
        }
        else {
            Label_0343: {
                try {
                    o = chain.proceed((Request)o);
                    if (o == null && value != null) {
                        Util.closeQuietly(value.body());
                    }
                    if (cacheResponse == null) {
                        break Label_0343;
                    }
                    if (((Response)o).code() == 304) {
                        final Response build = cacheResponse.newBuilder().headers(combine(cacheResponse.headers(), ((Response)o).headers())).sentRequestAtMillis(((Response)o).sentRequestAtMillis()).receivedResponseAtMillis(((Response)o).receivedResponseAtMillis()).cacheResponse(stripBody(cacheResponse)).networkResponse(stripBody((Response)o)).build();
                        ((Response)o).body().close();
                        this.cache.trackConditionalCacheHit();
                        this.cache.update(cacheResponse, build);
                        return response;
                    }
                }
                finally {
                    if (!false && value != null) {
                        Util.closeQuietly(value.body());
                    }
                }
                Util.closeQuietly(cacheResponse.body());
            }
            final Response response2 = response = ((Response)o).newBuilder().cacheResponse(stripBody(cacheResponse)).networkResponse(stripBody((Response)o)).build();
            if (HttpHeaders.hasBody(response2)) {
                response = this.cacheWritingResponse(this.maybeCache(response2, ((Response)o).request(), this.cache), response2);
            }
        }
        return response;
    }
}