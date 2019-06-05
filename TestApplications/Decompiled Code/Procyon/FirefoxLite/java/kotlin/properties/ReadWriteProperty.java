// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.properties;

import kotlin.reflect.KProperty;

public interface ReadWriteProperty<R, T>
{
    T getValue(final R p0, final KProperty<?> p1);
    
    void setValue(final R p0, final KProperty<?> p1, final T p2);
}
