// 
// Decompiled by Procyon v0.5.34
// 

package okhttp3;

import okhttp3.internal.http.HttpHeaders;
import java.util.concurrent.TimeUnit;

public final class CacheControl
{
    public static final CacheControl FORCE_CACHE;
    public static final CacheControl FORCE_NETWORK;
    String headerValue;
    private final boolean isPrivate;
    private final boolean isPublic;
    private final int maxAgeSeconds;
    private final int maxStaleSeconds;
    private final int minFreshSeconds;
    private final boolean mustRevalidate;
    private final boolean noCache;
    private final boolean noStore;
    private final boolean noTransform;
    private final boolean onlyIfCached;
    private final int sMaxAgeSeconds;
    
    static {
        FORCE_NETWORK = new Builder().noCache().build();
        FORCE_CACHE = new Builder().onlyIfCached().maxStale(Integer.MAX_VALUE, TimeUnit.SECONDS).build();
    }
    
    CacheControl(final Builder builder) {
        this.noCache = builder.noCache;
        this.noStore = builder.noStore;
        this.maxAgeSeconds = builder.maxAgeSeconds;
        this.sMaxAgeSeconds = -1;
        this.isPrivate = false;
        this.isPublic = false;
        this.mustRevalidate = false;
        this.maxStaleSeconds = builder.maxStaleSeconds;
        this.minFreshSeconds = builder.minFreshSeconds;
        this.onlyIfCached = builder.onlyIfCached;
        this.noTransform = builder.noTransform;
    }
    
    private CacheControl(final boolean noCache, final boolean noStore, final int maxAgeSeconds, final int sMaxAgeSeconds, final boolean isPrivate, final boolean isPublic, final boolean mustRevalidate, final int maxStaleSeconds, final int minFreshSeconds, final boolean onlyIfCached, final boolean noTransform, final String headerValue) {
        this.noCache = noCache;
        this.noStore = noStore;
        this.maxAgeSeconds = maxAgeSeconds;
        this.sMaxAgeSeconds = sMaxAgeSeconds;
        this.isPrivate = isPrivate;
        this.isPublic = isPublic;
        this.mustRevalidate = mustRevalidate;
        this.maxStaleSeconds = maxStaleSeconds;
        this.minFreshSeconds = minFreshSeconds;
        this.onlyIfCached = onlyIfCached;
        this.noTransform = noTransform;
        this.headerValue = headerValue;
    }
    
    private String headerValue() {
        final StringBuilder sb = new StringBuilder();
        if (this.noCache) {
            sb.append("no-cache, ");
        }
        if (this.noStore) {
            sb.append("no-store, ");
        }
        if (this.maxAgeSeconds != -1) {
            sb.append("max-age=").append(this.maxAgeSeconds).append(", ");
        }
        if (this.sMaxAgeSeconds != -1) {
            sb.append("s-maxage=").append(this.sMaxAgeSeconds).append(", ");
        }
        if (this.isPrivate) {
            sb.append("private, ");
        }
        if (this.isPublic) {
            sb.append("public, ");
        }
        if (this.mustRevalidate) {
            sb.append("must-revalidate, ");
        }
        if (this.maxStaleSeconds != -1) {
            sb.append("max-stale=").append(this.maxStaleSeconds).append(", ");
        }
        if (this.minFreshSeconds != -1) {
            sb.append("min-fresh=").append(this.minFreshSeconds).append(", ");
        }
        if (this.onlyIfCached) {
            sb.append("only-if-cached, ");
        }
        if (this.noTransform) {
            sb.append("no-transform, ");
        }
        String string;
        if (sb.length() == 0) {
            string = "";
        }
        else {
            sb.delete(sb.length() - 2, sb.length());
            string = sb.toString();
        }
        return string;
    }
    
    public static CacheControl parse(final Headers headers) {
        boolean b = false;
        boolean b2 = false;
        int seconds = -1;
        int seconds2 = -1;
        boolean b3 = false;
        boolean b4 = false;
        boolean b5 = false;
        int seconds3 = -1;
        int seconds4 = -1;
        boolean b6 = false;
        boolean b7 = false;
        int n = 1;
        String s = null;
        boolean b8;
        boolean b9;
        int n2;
        int n3;
        boolean b10;
        boolean b11;
        boolean b12;
        int n4;
        int n5;
        boolean b13;
        boolean b14;
        String s2;
        int n6;
        for (int i = 0; i < headers.size(); ++i, b = b8, b2 = b9, seconds = n2, seconds2 = n3, b3 = b10, b4 = b11, b5 = b12, seconds3 = n4, seconds4 = n5, b6 = b13, b7 = b14, s = s2, n = n6) {
            final String name = headers.name(i);
            final String value = headers.value(i);
            if (name.equalsIgnoreCase("Cache-Control")) {
                if (s != null) {
                    n = 0;
                }
                else {
                    s = value;
                }
            }
            else {
                b8 = b;
                b9 = b2;
                n2 = seconds;
                n3 = seconds2;
                b10 = b3;
                b11 = b4;
                b12 = b5;
                n4 = seconds3;
                n5 = seconds4;
                b13 = b6;
                b14 = b7;
                s2 = s;
                n6 = n;
                if (!name.equalsIgnoreCase("Pragma")) {
                    continue;
                }
                n = 0;
            }
            int beginIndex = 0;
            while (true) {
                b8 = b;
                b9 = b2;
                n2 = seconds;
                n3 = seconds2;
                b10 = b3;
                b11 = b4;
                b12 = b5;
                n4 = seconds3;
                n5 = seconds4;
                b13 = b6;
                b14 = b7;
                s2 = s;
                n6 = n;
                if (beginIndex >= value.length()) {
                    break;
                }
                int n7 = HttpHeaders.skipUntil(value, beginIndex, "=,;");
                final String trim = value.substring(beginIndex, n7).trim();
                String s3;
                if (n7 == value.length() || value.charAt(n7) == ',' || value.charAt(n7) == ';') {
                    ++n7;
                    s3 = null;
                }
                else {
                    final int skipWhitespace = HttpHeaders.skipWhitespace(value, n7 + 1);
                    if (skipWhitespace < value.length() && value.charAt(skipWhitespace) == '\"') {
                        final int beginIndex2 = skipWhitespace + 1;
                        final int skipUntil = HttpHeaders.skipUntil(value, beginIndex2, "\"");
                        s3 = value.substring(beginIndex2, skipUntil);
                        n7 = skipUntil + 1;
                    }
                    else {
                        n7 = HttpHeaders.skipUntil(value, skipWhitespace, ",;");
                        s3 = value.substring(skipWhitespace, n7).trim();
                    }
                }
                if ("no-cache".equalsIgnoreCase(trim)) {
                    b = true;
                    beginIndex = n7;
                }
                else if ("no-store".equalsIgnoreCase(trim)) {
                    b2 = true;
                    beginIndex = n7;
                }
                else if ("max-age".equalsIgnoreCase(trim)) {
                    seconds = HttpHeaders.parseSeconds(s3, -1);
                    beginIndex = n7;
                }
                else if ("s-maxage".equalsIgnoreCase(trim)) {
                    seconds2 = HttpHeaders.parseSeconds(s3, -1);
                    beginIndex = n7;
                }
                else if ("private".equalsIgnoreCase(trim)) {
                    b3 = true;
                    beginIndex = n7;
                }
                else if ("public".equalsIgnoreCase(trim)) {
                    b4 = true;
                    beginIndex = n7;
                }
                else if ("must-revalidate".equalsIgnoreCase(trim)) {
                    b5 = true;
                    beginIndex = n7;
                }
                else if ("max-stale".equalsIgnoreCase(trim)) {
                    seconds3 = HttpHeaders.parseSeconds(s3, Integer.MAX_VALUE);
                    beginIndex = n7;
                }
                else if ("min-fresh".equalsIgnoreCase(trim)) {
                    seconds4 = HttpHeaders.parseSeconds(s3, -1);
                    beginIndex = n7;
                }
                else if ("only-if-cached".equalsIgnoreCase(trim)) {
                    b6 = true;
                    beginIndex = n7;
                }
                else {
                    beginIndex = n7;
                    if (!"no-transform".equalsIgnoreCase(trim)) {
                        continue;
                    }
                    b7 = true;
                    beginIndex = n7;
                }
            }
        }
        if (n == 0) {
            s = null;
        }
        return new CacheControl(b, b2, seconds, seconds2, b3, b4, b5, seconds3, seconds4, b6, b7, s);
    }
    
    public boolean isPrivate() {
        return this.isPrivate;
    }
    
    public boolean isPublic() {
        return this.isPublic;
    }
    
    public int maxAgeSeconds() {
        return this.maxAgeSeconds;
    }
    
    public int maxStaleSeconds() {
        return this.maxStaleSeconds;
    }
    
    public int minFreshSeconds() {
        return this.minFreshSeconds;
    }
    
    public boolean mustRevalidate() {
        return this.mustRevalidate;
    }
    
    public boolean noCache() {
        return this.noCache;
    }
    
    public boolean noStore() {
        return this.noStore;
    }
    
    public boolean noTransform() {
        return this.noTransform;
    }
    
    public boolean onlyIfCached() {
        return this.onlyIfCached;
    }
    
    public int sMaxAgeSeconds() {
        return this.sMaxAgeSeconds;
    }
    
    @Override
    public String toString() {
        String headerValue = this.headerValue;
        if (headerValue == null) {
            headerValue = this.headerValue();
            this.headerValue = headerValue;
        }
        return headerValue;
    }
    
    public static final class Builder
    {
        int maxAgeSeconds;
        int maxStaleSeconds;
        int minFreshSeconds;
        boolean noCache;
        boolean noStore;
        boolean noTransform;
        boolean onlyIfCached;
        
        public Builder() {
            this.maxAgeSeconds = -1;
            this.maxStaleSeconds = -1;
            this.minFreshSeconds = -1;
        }
        
        public CacheControl build() {
            return new CacheControl(this);
        }
        
        public Builder maxAge(int n, final TimeUnit timeUnit) {
            if (n < 0) {
                throw new IllegalArgumentException("maxAge < 0: " + n);
            }
            final long seconds = timeUnit.toSeconds(n);
            if (seconds > 2147483647L) {
                n = Integer.MAX_VALUE;
            }
            else {
                n = (int)seconds;
            }
            this.maxAgeSeconds = n;
            return this;
        }
        
        public Builder maxStale(int n, final TimeUnit timeUnit) {
            if (n < 0) {
                throw new IllegalArgumentException("maxStale < 0: " + n);
            }
            final long seconds = timeUnit.toSeconds(n);
            if (seconds > 2147483647L) {
                n = Integer.MAX_VALUE;
            }
            else {
                n = (int)seconds;
            }
            this.maxStaleSeconds = n;
            return this;
        }
        
        public Builder minFresh(int n, final TimeUnit timeUnit) {
            if (n < 0) {
                throw new IllegalArgumentException("minFresh < 0: " + n);
            }
            final long seconds = timeUnit.toSeconds(n);
            if (seconds > 2147483647L) {
                n = Integer.MAX_VALUE;
            }
            else {
                n = (int)seconds;
            }
            this.minFreshSeconds = n;
            return this;
        }
        
        public Builder noCache() {
            this.noCache = true;
            return this;
        }
        
        public Builder noStore() {
            this.noStore = true;
            return this;
        }
        
        public Builder noTransform() {
            this.noTransform = true;
            return this;
        }
        
        public Builder onlyIfCached() {
            this.onlyIfCached = true;
            return this;
        }
    }
}
