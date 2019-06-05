// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.reflect;

public interface KCallable<R>
{
    R call(final Object... p0);
    
    String getName();
}
