// 
// Decompiled by Procyon v0.5.34
// 

package com.squareup.leakcanary;

public final class RefWatcher
{
    public static final RefWatcher DISABLED;
    
    static {
        DISABLED = new RefWatcher();
    }
    
    private RefWatcher() {
    }
    
    public void watch(final Object o) {
    }
    
    public void watch(final Object o, final String s) {
    }
}
