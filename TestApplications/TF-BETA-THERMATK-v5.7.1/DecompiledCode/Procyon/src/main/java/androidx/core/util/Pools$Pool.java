// 
// Decompiled by Procyon v0.5.34
// 

package androidx.core.util;

public interface Pools$Pool<T>
{
    T acquire();
    
    boolean release(final T p0);
}
