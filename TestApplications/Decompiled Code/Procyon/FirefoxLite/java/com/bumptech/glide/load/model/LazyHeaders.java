// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.model;

import java.util.Iterator;
import android.text.TextUtils;
import java.util.HashMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class LazyHeaders implements Headers
{
    private volatile Map<String, String> combinedHeaders;
    private final Map<String, List<LazyHeaderFactory>> headers;
    
    LazyHeaders(final Map<String, List<LazyHeaderFactory>> m) {
        this.headers = Collections.unmodifiableMap((Map<? extends String, ? extends List<LazyHeaderFactory>>)m);
    }
    
    private Map<String, String> generateHeaders() {
        final HashMap<Object, String> hashMap = (HashMap<Object, String>)new HashMap<String, String>();
        for (final Map.Entry<String, List<LazyHeaderFactory>> entry : this.headers.entrySet()) {
            final StringBuilder sb = new StringBuilder();
            final List<LazyHeaderFactory> list = entry.getValue();
            for (int size = list.size(), i = 0; i < size; ++i) {
                final String buildHeader = list.get(i).buildHeader();
                if (!TextUtils.isEmpty((CharSequence)buildHeader)) {
                    sb.append(buildHeader);
                    if (i != list.size() - 1) {
                        sb.append(',');
                    }
                }
            }
            if (!TextUtils.isEmpty((CharSequence)sb.toString())) {
                hashMap.put(entry.getKey(), sb.toString());
            }
        }
        return (Map<String, String>)hashMap;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof LazyHeaders && this.headers.equals(((LazyHeaders)o).headers);
    }
    
    @Override
    public Map<String, String> getHeaders() {
        if (this.combinedHeaders == null) {
            synchronized (this) {
                if (this.combinedHeaders == null) {
                    this.combinedHeaders = Collections.unmodifiableMap((Map<? extends String, ? extends String>)this.generateHeaders());
                }
            }
        }
        return this.combinedHeaders;
    }
    
    @Override
    public int hashCode() {
        return this.headers.hashCode();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("LazyHeaders{headers=");
        sb.append(this.headers);
        sb.append('}');
        return sb.toString();
    }
    
    public static final class Builder
    {
        private static final Map<String, List<LazyHeaderFactory>> DEFAULT_HEADERS;
        private static final String DEFAULT_USER_AGENT;
        private boolean copyOnModify;
        private Map<String, List<LazyHeaderFactory>> headers;
        private boolean isUserAgentDefault;
        
        static {
            DEFAULT_USER_AGENT = getSanitizedUserAgent();
            final HashMap<String, List<LazyHeaderFactory>> m = new HashMap<String, List<LazyHeaderFactory>>(2);
            if (!TextUtils.isEmpty((CharSequence)Builder.DEFAULT_USER_AGENT)) {
                m.put("User-Agent", Collections.singletonList(new StringHeaderFactory(Builder.DEFAULT_USER_AGENT)));
            }
            DEFAULT_HEADERS = Collections.unmodifiableMap((Map<?, ?>)m);
        }
        
        public Builder() {
            this.copyOnModify = true;
            this.headers = Builder.DEFAULT_HEADERS;
            this.isUserAgentDefault = true;
        }
        
        static String getSanitizedUserAgent() {
            final String property = System.getProperty("http.agent");
            if (TextUtils.isEmpty((CharSequence)property)) {
                return property;
            }
            final int length = property.length();
            final StringBuilder sb = new StringBuilder(property.length());
            for (int i = 0; i < length; ++i) {
                final char char1 = property.charAt(i);
                if ((char1 > '\u001f' || char1 == '\t') && char1 < '\u007f') {
                    sb.append(char1);
                }
                else {
                    sb.append('?');
                }
            }
            return sb.toString();
        }
        
        public LazyHeaders build() {
            this.copyOnModify = true;
            return new LazyHeaders(this.headers);
        }
    }
    
    static final class StringHeaderFactory implements LazyHeaderFactory
    {
        private final String value;
        
        StringHeaderFactory(final String value) {
            this.value = value;
        }
        
        @Override
        public String buildHeader() {
            return this.value;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof StringHeaderFactory && this.value.equals(((StringHeaderFactory)o).value);
        }
        
        @Override
        public int hashCode() {
            return this.value.hashCode();
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("StringHeaderFactory{value='");
            sb.append(this.value);
            sb.append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
}
