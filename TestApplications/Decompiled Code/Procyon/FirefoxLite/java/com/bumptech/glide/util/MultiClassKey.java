// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.util;

public class MultiClassKey
{
    private Class<?> first;
    private Class<?> second;
    private Class<?> third;
    
    public MultiClassKey() {
    }
    
    public MultiClassKey(final Class<?> clazz, final Class<?> clazz2) {
        this.set(clazz, clazz2);
    }
    
    public MultiClassKey(final Class<?> clazz, final Class<?> clazz2, final Class<?> clazz3) {
        this.set(clazz, clazz2, clazz3);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o != null && this.getClass() == o.getClass()) {
            final MultiClassKey multiClassKey = (MultiClassKey)o;
            return this.first.equals(multiClassKey.first) && this.second.equals(multiClassKey.second) && Util.bothNullOrEqual(this.third, multiClassKey.third);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final int hashCode = this.first.hashCode();
        final int hashCode2 = this.second.hashCode();
        int hashCode3;
        if (this.third != null) {
            hashCode3 = this.third.hashCode();
        }
        else {
            hashCode3 = 0;
        }
        return (hashCode * 31 + hashCode2) * 31 + hashCode3;
    }
    
    public void set(final Class<?> clazz, final Class<?> clazz2) {
        this.set(clazz, clazz2, null);
    }
    
    public void set(final Class<?> first, final Class<?> second, final Class<?> third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("MultiClassKey{first=");
        sb.append(this.first);
        sb.append(", second=");
        sb.append(this.second);
        sb.append('}');
        return sb.toString();
    }
}
