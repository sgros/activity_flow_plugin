package com.bumptech.glide.util;

public class MultiClassKey {
    private Class<?> first;
    private Class<?> second;
    private Class<?> third;

    public MultiClassKey(Class<?> cls, Class<?> cls2) {
        set(cls, cls2);
    }

    public MultiClassKey(Class<?> cls, Class<?> cls2, Class<?> cls3) {
        set(cls, cls2, cls3);
    }

    public void set(Class<?> cls, Class<?> cls2) {
        set(cls, cls2, null);
    }

    public void set(Class<?> cls, Class<?> cls2, Class<?> cls3) {
        this.first = cls;
        this.second = cls2;
        this.third = cls3;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("MultiClassKey{first=");
        stringBuilder.append(this.first);
        stringBuilder.append(", second=");
        stringBuilder.append(this.second);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MultiClassKey multiClassKey = (MultiClassKey) obj;
        return this.first.equals(multiClassKey.first) && this.second.equals(multiClassKey.second) && Util.bothNullOrEqual(this.third, multiClassKey.third);
    }

    public int hashCode() {
        return (((this.first.hashCode() * 31) + this.second.hashCode()) * 31) + (this.third != null ? this.third.hashCode() : 0);
    }
}
