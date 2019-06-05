package com.bumptech.glide.load.model;

import android.text.TextUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class LazyHeaders implements Headers {
    private volatile Map<String, String> combinedHeaders;
    private final Map<String, List<LazyHeaderFactory>> headers;

    public static final class Builder {
        private static final Map<String, List<LazyHeaderFactory>> DEFAULT_HEADERS;
        private static final String DEFAULT_USER_AGENT = getSanitizedUserAgent();
        private boolean copyOnModify = true;
        private Map<String, List<LazyHeaderFactory>> headers = DEFAULT_HEADERS;
        private boolean isUserAgentDefault = true;

        static {
            HashMap hashMap = new HashMap(2);
            if (!TextUtils.isEmpty(DEFAULT_USER_AGENT)) {
                hashMap.put("User-Agent", Collections.singletonList(new StringHeaderFactory(DEFAULT_USER_AGENT)));
            }
            DEFAULT_HEADERS = Collections.unmodifiableMap(hashMap);
        }

        public LazyHeaders build() {
            this.copyOnModify = true;
            return new LazyHeaders(this.headers);
        }

        static String getSanitizedUserAgent() {
            String property = System.getProperty("http.agent");
            if (TextUtils.isEmpty(property)) {
                return property;
            }
            int length = property.length();
            StringBuilder stringBuilder = new StringBuilder(property.length());
            for (int i = 0; i < length; i++) {
                char charAt = property.charAt(i);
                if ((charAt > 31 || charAt == 9) && charAt < 127) {
                    stringBuilder.append(charAt);
                } else {
                    stringBuilder.append('?');
                }
            }
            return stringBuilder.toString();
        }
    }

    static final class StringHeaderFactory implements LazyHeaderFactory {
        private final String value;

        StringHeaderFactory(String str) {
            this.value = str;
        }

        public String buildHeader() {
            return this.value;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("StringHeaderFactory{value='");
            stringBuilder.append(this.value);
            stringBuilder.append('\'');
            stringBuilder.append('}');
            return stringBuilder.toString();
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof StringHeaderFactory)) {
                return false;
            }
            return this.value.equals(((StringHeaderFactory) obj).value);
        }

        public int hashCode() {
            return this.value.hashCode();
        }
    }

    LazyHeaders(Map<String, List<LazyHeaderFactory>> map) {
        this.headers = Collections.unmodifiableMap(map);
    }

    public Map<String, String> getHeaders() {
        if (this.combinedHeaders == null) {
            synchronized (this) {
                if (this.combinedHeaders == null) {
                    this.combinedHeaders = Collections.unmodifiableMap(generateHeaders());
                }
            }
        }
        return this.combinedHeaders;
    }

    private Map<String, String> generateHeaders() {
        HashMap hashMap = new HashMap();
        for (Entry entry : this.headers.entrySet()) {
            StringBuilder stringBuilder = new StringBuilder();
            List list = (List) entry.getValue();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                String buildHeader = ((LazyHeaderFactory) list.get(i)).buildHeader();
                if (!TextUtils.isEmpty(buildHeader)) {
                    stringBuilder.append(buildHeader);
                    if (i != list.size() - 1) {
                        stringBuilder.append(',');
                    }
                }
            }
            if (!TextUtils.isEmpty(stringBuilder.toString())) {
                hashMap.put(entry.getKey(), stringBuilder.toString());
            }
        }
        return hashMap;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("LazyHeaders{headers=");
        stringBuilder.append(this.headers);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof LazyHeaders)) {
            return false;
        }
        return this.headers.equals(((LazyHeaders) obj).headers);
    }

    public int hashCode() {
        return this.headers.hashCode();
    }
}
