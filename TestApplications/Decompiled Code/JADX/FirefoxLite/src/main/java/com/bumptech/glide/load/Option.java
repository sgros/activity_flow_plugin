package com.bumptech.glide.load;

import com.bumptech.glide.util.Preconditions;
import java.security.MessageDigest;

public final class Option<T> {
    private static final CacheKeyUpdater<Object> EMPTY_UPDATER = new C03891();
    private final CacheKeyUpdater<T> cacheKeyUpdater;
    private final T defaultValue;
    private final String key;
    private volatile byte[] keyBytes;

    public interface CacheKeyUpdater<T> {
        void update(byte[] bArr, T t, MessageDigest messageDigest);
    }

    /* renamed from: com.bumptech.glide.load.Option$1 */
    static class C03891 implements CacheKeyUpdater<Object> {
        public void update(byte[] bArr, Object obj, MessageDigest messageDigest) {
        }

        C03891() {
        }
    }

    public static <T> Option<T> memory(String str) {
        return new Option(str, null, emptyUpdater());
    }

    public static <T> Option<T> memory(String str, T t) {
        return new Option(str, t, emptyUpdater());
    }

    public static <T> Option<T> disk(String str, T t, CacheKeyUpdater<T> cacheKeyUpdater) {
        return new Option(str, t, cacheKeyUpdater);
    }

    Option(String str, T t, CacheKeyUpdater<T> cacheKeyUpdater) {
        this.key = Preconditions.checkNotEmpty(str);
        this.defaultValue = t;
        this.cacheKeyUpdater = (CacheKeyUpdater) Preconditions.checkNotNull(cacheKeyUpdater);
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public void update(T t, MessageDigest messageDigest) {
        this.cacheKeyUpdater.update(getKeyBytes(), t, messageDigest);
    }

    private byte[] getKeyBytes() {
        if (this.keyBytes == null) {
            this.keyBytes = this.key.getBytes(Key.CHARSET);
        }
        return this.keyBytes;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Option)) {
            return false;
        }
        return this.key.equals(((Option) obj).key);
    }

    public int hashCode() {
        return this.key.hashCode();
    }

    private static <T> CacheKeyUpdater<T> emptyUpdater() {
        return EMPTY_UPDATER;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Option{key='");
        stringBuilder.append(this.key);
        stringBuilder.append('\'');
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
