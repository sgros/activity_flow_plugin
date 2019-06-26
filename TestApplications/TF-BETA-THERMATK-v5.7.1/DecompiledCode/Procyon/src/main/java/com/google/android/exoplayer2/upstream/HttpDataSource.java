// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.upstream;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import com.google.android.exoplayer2.util.Predicate;

public interface HttpDataSource extends DataSource
{
    public static final Predicate<String> REJECT_PAYWALL_TYPES = _$$Lambda$HttpDataSource$fz_i4cgBB9tTB1JUdq8hmlAPFIw.INSTANCE;
    
    public abstract static class BaseFactory implements Factory
    {
        private final RequestProperties defaultRequestProperties;
        
        public BaseFactory() {
            this.defaultRequestProperties = new RequestProperties();
        }
        
        public final HttpDataSource createDataSource() {
            return this.createDataSourceInternal(this.defaultRequestProperties);
        }
        
        protected abstract HttpDataSource createDataSourceInternal(final RequestProperties p0);
    }
    
    public interface Factory extends DataSource.Factory
    {
    }
    
    public static class HttpDataSourceException extends IOException
    {
        public final DataSpec dataSpec;
        public final int type;
        
        public HttpDataSourceException(final IOException cause, final DataSpec dataSpec, final int type) {
            super(cause);
            this.dataSpec = dataSpec;
            this.type = type;
        }
        
        public HttpDataSourceException(final String message, final DataSpec dataSpec, final int type) {
            super(message);
            this.dataSpec = dataSpec;
            this.type = type;
        }
        
        public HttpDataSourceException(final String message, final IOException cause, final DataSpec dataSpec, final int type) {
            super(message, cause);
            this.dataSpec = dataSpec;
            this.type = type;
        }
    }
    
    public static final class InvalidContentTypeException extends HttpDataSourceException
    {
        public final String contentType;
        
        public InvalidContentTypeException(final String s, final DataSpec dataSpec) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid content type: ");
            sb.append(s);
            super(sb.toString(), dataSpec, 1);
            this.contentType = s;
        }
    }
    
    public static final class InvalidResponseCodeException extends HttpDataSourceException
    {
        public final Map<String, List<String>> headerFields;
        public final int responseCode;
        public final String responseMessage;
        
        public InvalidResponseCodeException(final int n, final String responseMessage, final Map<String, List<String>> headerFields, final DataSpec dataSpec) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Response code: ");
            sb.append(n);
            super(sb.toString(), dataSpec, 1);
            this.responseCode = n;
            this.responseMessage = responseMessage;
            this.headerFields = headerFields;
        }
    }
    
    public static final class RequestProperties
    {
        private final Map<String, String> requestProperties;
        private Map<String, String> requestPropertiesSnapshot;
        
        public RequestProperties() {
            this.requestProperties = new HashMap<String, String>();
        }
        
        public Map<String, String> getSnapshot() {
            synchronized (this) {
                if (this.requestPropertiesSnapshot == null) {
                    this.requestPropertiesSnapshot = Collections.unmodifiableMap((Map<? extends String, ? extends String>)new HashMap<String, String>(this.requestProperties));
                }
                return this.requestPropertiesSnapshot;
            }
        }
    }
}
