// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.reflect;

import kotlin.jvm.functions.Function1;

public interface KProperty1<T, R> extends Function1<T, R>, KProperty<R>
{
    R get(final T p0);
    
    Getter<T, R> getGetter();
    
    public interface Getter<T, R> extends Function1<T, R>, KProperty.Getter<R>
    {
    }
}
