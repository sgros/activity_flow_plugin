package com.bumptech.glide.load;

import android.support.p001v4.util.ArrayMap;
import java.security.MessageDigest;
import java.util.Map.Entry;

public final class Options implements Key {
    private final ArrayMap<Option<?>, Object> values = new ArrayMap();

    public void putAll(Options options) {
        this.values.putAll(options.values);
    }

    public <T> Options set(Option<T> option, T t) {
        this.values.put(option, t);
        return this;
    }

    public <T> T get(Option<T> option) {
        return this.values.containsKey(option) ? this.values.get(option) : option.getDefaultValue();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Options)) {
            return false;
        }
        return this.values.equals(((Options) obj).values);
    }

    public int hashCode() {
        return this.values.hashCode();
    }

    public void updateDiskCacheKey(MessageDigest messageDigest) {
        for (Entry entry : this.values.entrySet()) {
            updateDiskCacheKey((Option) entry.getKey(), entry.getValue(), messageDigest);
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Options{values=");
        stringBuilder.append(this.values);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    private static <T> void updateDiskCacheKey(Option<T> option, Object obj, MessageDigest messageDigest) {
        option.update(obj, messageDigest);
    }
}
