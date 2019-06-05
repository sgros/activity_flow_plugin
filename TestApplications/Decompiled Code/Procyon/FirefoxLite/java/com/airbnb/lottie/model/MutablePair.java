// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model;

import android.support.v4.util.Pair;

public class MutablePair<T>
{
    T first;
    T second;
    
    private static boolean objectsEqual(final Object o, final Object obj) {
        return o == obj || (o != null && o.equals(obj));
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof Pair;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final Pair pair = (Pair)o;
        boolean b3 = b2;
        if (objectsEqual(pair.first, this.first)) {
            b3 = b2;
            if (objectsEqual(pair.second, this.second)) {
                b3 = true;
            }
        }
        return b3;
    }
    
    @Override
    public int hashCode() {
        final T first = this.first;
        int hashCode = 0;
        int hashCode2;
        if (first == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = this.first.hashCode();
        }
        if (this.second != null) {
            hashCode = this.second.hashCode();
        }
        return hashCode2 ^ hashCode;
    }
    
    public void set(final T first, final T second) {
        this.first = first;
        this.second = second;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Pair{");
        sb.append(String.valueOf(this.first));
        sb.append(" ");
        sb.append(String.valueOf(this.second));
        sb.append("}");
        return sb.toString();
    }
}
