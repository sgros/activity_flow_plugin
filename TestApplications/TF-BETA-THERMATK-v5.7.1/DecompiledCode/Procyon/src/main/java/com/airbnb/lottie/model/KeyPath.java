// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class KeyPath
{
    private final List<String> keys;
    private KeyPathElement resolvedElement;
    
    private KeyPath(final KeyPath keyPath) {
        this.keys = new ArrayList<String>(keyPath.keys);
        this.resolvedElement = keyPath.resolvedElement;
    }
    
    public KeyPath(final String... a) {
        this.keys = Arrays.asList(a);
    }
    
    private boolean endsWithGlobstar() {
        final List<String> keys = this.keys;
        return keys.get(keys.size() - 1).equals("**");
    }
    
    private boolean isContainer(final String anObject) {
        return "__container".equals(anObject);
    }
    
    public KeyPath addKey(final String s) {
        final KeyPath keyPath = new KeyPath(this);
        keyPath.keys.add(s);
        return keyPath;
    }
    
    public boolean fullyResolvesTo(final String anObject, int n) {
        final int size = this.keys.size();
        final boolean b = false;
        final boolean b2 = false;
        if (n >= size) {
            return false;
        }
        final boolean b3 = n == this.keys.size() - 1;
        final String s = this.keys.get(n);
        if (!s.equals("**")) {
            final boolean b4 = s.equals(anObject) || s.equals("*");
            if (!b3) {
                boolean b5 = b2;
                if (n != this.keys.size() - 2) {
                    return b5;
                }
                b5 = b2;
                if (!this.endsWithGlobstar()) {
                    return b5;
                }
            }
            boolean b5 = b2;
            if (b4) {
                b5 = true;
            }
            return b5;
        }
        if (!b3 && this.keys.get(n + 1).equals(anObject)) {
            if (n != this.keys.size() - 2) {
                boolean b6 = b;
                if (n != this.keys.size() - 3) {
                    return b6;
                }
                b6 = b;
                if (!this.endsWithGlobstar()) {
                    return b6;
                }
            }
            return true;
        }
        return b3 || (++n >= this.keys.size() - 1 && this.keys.get(n).equals(anObject));
    }
    
    public KeyPathElement getResolvedElement() {
        return this.resolvedElement;
    }
    
    public int incrementDepthBy(final String anObject, final int n) {
        if (this.isContainer(anObject)) {
            return 0;
        }
        if (!this.keys.get(n).equals("**")) {
            return 1;
        }
        if (n == this.keys.size() - 1) {
            return 0;
        }
        if (this.keys.get(n + 1).equals(anObject)) {
            return 2;
        }
        return 0;
    }
    
    public boolean matches(final String anObject, final int n) {
        return this.isContainer(anObject) || (n < this.keys.size() && (this.keys.get(n).equals(anObject) || this.keys.get(n).equals("**") || this.keys.get(n).equals("*")));
    }
    
    public boolean propagateToChildren(final String anObject, final int n) {
        final boolean equals = "__container".equals(anObject);
        final boolean b = true;
        if (equals) {
            return true;
        }
        boolean b2 = b;
        if (n >= this.keys.size() - 1) {
            b2 = (this.keys.get(n).equals("**") && b);
        }
        return b2;
    }
    
    public KeyPath resolve(final KeyPathElement resolvedElement) {
        final KeyPath keyPath = new KeyPath(this);
        keyPath.resolvedElement = resolvedElement;
        return keyPath;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("KeyPath{keys=");
        sb.append(this.keys);
        sb.append(",resolved=");
        sb.append(this.resolvedElement != null);
        sb.append('}');
        return sb.toString();
    }
}
