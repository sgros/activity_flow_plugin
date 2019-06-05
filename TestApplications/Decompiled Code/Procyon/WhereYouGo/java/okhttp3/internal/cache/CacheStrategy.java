// 
// Decompiled by Procyon v0.5.34
// 

package okhttp3.internal.cache;

import okhttp3.internal.Internal;
import okhttp3.CacheControl;
import java.util.concurrent.TimeUnit;
import okhttp3.Headers;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http.HttpDate;
import java.util.Date;
import okhttp3.Request;
import okhttp3.Response;

public final class CacheStrategy
{
    public final Response cacheResponse;
    public final Request networkRequest;
    
    CacheStrategy(final Request networkRequest, final Response cacheResponse) {
        this.networkRequest = networkRequest;
        this.cacheResponse = cacheResponse;
    }
    
    public static boolean isCacheable(final Response response, final Request request) {
        final boolean b = false;
        boolean b2 = false;
        Label_0166: {
            switch (response.code()) {
                default: {
                    b2 = b;
                    break;
                }
                case 302:
                case 307: {
                    if (response.header("Expires") != null || response.cacheControl().maxAgeSeconds() != -1 || response.cacheControl().isPublic()) {
                        break Label_0166;
                    }
                    b2 = b;
                    if (response.cacheControl().isPrivate()) {
                        break Label_0166;
                    }
                    break;
                }
                case 200:
                case 203:
                case 204:
                case 300:
                case 301:
                case 308:
                case 404:
                case 405:
                case 410:
                case 414:
                case 501: {
                    b2 = b;
                    if (response.cacheControl().noStore()) {
                        break;
                    }
                    b2 = b;
                    if (!request.cacheControl().noStore()) {
                        b2 = true;
                        break;
                    }
                    break;
                }
            }
        }
        return b2;
    }
    
    public static class Factory
    {
        private int ageSeconds;
        final Response cacheResponse;
        private String etag;
        private Date expires;
        private Date lastModified;
        private String lastModifiedString;
        final long nowMillis;
        private long receivedResponseMillis;
        final Request request;
        private long sentRequestMillis;
        private Date servedDate;
        private String servedDateString;
        
        public Factory(final long nowMillis, final Request request, final Response cacheResponse) {
            this.ageSeconds = -1;
            this.nowMillis = nowMillis;
            this.request = request;
            this.cacheResponse = cacheResponse;
            if (cacheResponse != null) {
                this.sentRequestMillis = cacheResponse.sentRequestAtMillis();
                this.receivedResponseMillis = cacheResponse.receivedResponseAtMillis();
                final Headers headers = cacheResponse.headers();
                for (int i = 0; i < headers.size(); ++i) {
                    final String name = headers.name(i);
                    final String value = headers.value(i);
                    if ("Date".equalsIgnoreCase(name)) {
                        this.servedDate = HttpDate.parse(value);
                        this.servedDateString = value;
                    }
                    else if ("Expires".equalsIgnoreCase(name)) {
                        this.expires = HttpDate.parse(value);
                    }
                    else if ("Last-Modified".equalsIgnoreCase(name)) {
                        this.lastModified = HttpDate.parse(value);
                        this.lastModifiedString = value;
                    }
                    else if ("ETag".equalsIgnoreCase(name)) {
                        this.etag = value;
                    }
                    else if ("Age".equalsIgnoreCase(name)) {
                        this.ageSeconds = HttpHeaders.parseSeconds(value, -1);
                    }
                }
            }
        }
        
        private long cacheResponseAge() {
            long a = 0L;
            if (this.servedDate != null) {
                a = Math.max(0L, this.receivedResponseMillis - this.servedDate.getTime());
            }
            if (this.ageSeconds != -1) {
                a = Math.max(a, TimeUnit.SECONDS.toMillis(this.ageSeconds));
            }
            return a + (this.receivedResponseMillis - this.sentRequestMillis) + (this.nowMillis - this.receivedResponseMillis);
        }
        
        private long computeFreshnessLifetime() {
            final long n = 0L;
            final CacheControl cacheControl = this.cacheResponse.cacheControl();
            long millis;
            if (cacheControl.maxAgeSeconds() != -1) {
                millis = TimeUnit.SECONDS.toMillis(cacheControl.maxAgeSeconds());
            }
            else if (this.expires != null) {
                long n2;
                if (this.servedDate != null) {
                    n2 = this.servedDate.getTime();
                }
                else {
                    n2 = this.receivedResponseMillis;
                }
                millis = this.expires.getTime() - n2;
                if (millis <= 0L) {
                    millis = 0L;
                }
            }
            else {
                millis = n;
                if (this.lastModified != null) {
                    millis = n;
                    if (this.cacheResponse.request().url().query() == null) {
                        long n3;
                        if (this.servedDate != null) {
                            n3 = this.servedDate.getTime();
                        }
                        else {
                            n3 = this.sentRequestMillis;
                        }
                        final long n4 = n3 - this.lastModified.getTime();
                        millis = n;
                        if (n4 > 0L) {
                            millis = n4 / 10L;
                        }
                    }
                }
            }
            return millis;
        }
        
        private CacheStrategy getCandidate() {
            CacheStrategy cacheStrategy;
            if (this.cacheResponse == null) {
                cacheStrategy = new CacheStrategy(this.request, null);
            }
            else if (this.request.isHttps() && this.cacheResponse.handshake() == null) {
                cacheStrategy = new CacheStrategy(this.request, null);
            }
            else if (!CacheStrategy.isCacheable(this.cacheResponse, this.request)) {
                cacheStrategy = new CacheStrategy(this.request, null);
            }
            else {
                final CacheControl cacheControl = this.request.cacheControl();
                if (cacheControl.noCache() || hasConditions(this.request)) {
                    cacheStrategy = new CacheStrategy(this.request, null);
                }
                else {
                    final long cacheResponseAge = this.cacheResponseAge();
                    long n;
                    final long a = n = this.computeFreshnessLifetime();
                    if (cacheControl.maxAgeSeconds() != -1) {
                        n = Math.min(a, TimeUnit.SECONDS.toMillis(cacheControl.maxAgeSeconds()));
                    }
                    long millis = 0L;
                    if (cacheControl.minFreshSeconds() != -1) {
                        millis = TimeUnit.SECONDS.toMillis(cacheControl.minFreshSeconds());
                    }
                    final long n2 = 0L;
                    final CacheControl cacheControl2 = this.cacheResponse.cacheControl();
                    long millis2 = n2;
                    if (!cacheControl2.mustRevalidate()) {
                        millis2 = n2;
                        if (cacheControl.maxStaleSeconds() != -1) {
                            millis2 = TimeUnit.SECONDS.toMillis(cacheControl.maxStaleSeconds());
                        }
                    }
                    if (!cacheControl2.noCache() && cacheResponseAge + millis < n + millis2) {
                        final Response.Builder builder = this.cacheResponse.newBuilder();
                        if (cacheResponseAge + millis >= n) {
                            builder.addHeader("Warning", "110 HttpURLConnection \"Response is stale\"");
                        }
                        if (cacheResponseAge > 86400000L && this.isFreshnessLifetimeHeuristic()) {
                            builder.addHeader("Warning", "113 HttpURLConnection \"Heuristic expiration\"");
                        }
                        cacheStrategy = new CacheStrategy(null, builder.build());
                    }
                    else {
                        String s;
                        String s2;
                        if (this.etag != null) {
                            s = "If-None-Match";
                            s2 = this.etag;
                        }
                        else if (this.lastModified != null) {
                            s = "If-Modified-Since";
                            s2 = this.lastModifiedString;
                        }
                        else {
                            if (this.servedDate == null) {
                                cacheStrategy = new CacheStrategy(this.request, null);
                                return cacheStrategy;
                            }
                            s = "If-Modified-Since";
                            s2 = this.servedDateString;
                        }
                        final Headers.Builder builder2 = this.request.headers().newBuilder();
                        Internal.instance.addLenient(builder2, s, s2);
                        cacheStrategy = new CacheStrategy(this.request.newBuilder().headers(builder2.build()).build(), this.cacheResponse);
                    }
                }
            }
            return cacheStrategy;
        }
        
        private static boolean hasConditions(final Request request) {
            return request.header("If-Modified-Since") != null || request.header("If-None-Match") != null;
        }
        
        private boolean isFreshnessLifetimeHeuristic() {
            return this.cacheResponse.cacheControl().maxAgeSeconds() == -1 && this.expires == null;
        }
        
        public CacheStrategy get() {
            CacheStrategy candidate;
            final CacheStrategy cacheStrategy = candidate = this.getCandidate();
            if (cacheStrategy.networkRequest != null) {
                candidate = cacheStrategy;
                if (this.request.cacheControl().onlyIfCached()) {
                    candidate = new CacheStrategy(null, null);
                }
            }
            return candidate;
        }
    }
}
