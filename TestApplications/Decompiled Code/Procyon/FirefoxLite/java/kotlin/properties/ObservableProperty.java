// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.properties;

import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KProperty;

public abstract class ObservableProperty<T> implements ReadWriteProperty<Object, T>
{
    private T value;
    
    public ObservableProperty(final T value) {
        this.value = value;
    }
    
    protected void afterChange(final KProperty<?> kProperty, final T t, final T t2) {
        Intrinsics.checkParameterIsNotNull(kProperty, "property");
    }
    
    protected boolean beforeChange(final KProperty<?> kProperty, final T t, final T t2) {
        Intrinsics.checkParameterIsNotNull(kProperty, "property");
        return true;
    }
    
    @Override
    public T getValue(final Object o, final KProperty<?> kProperty) {
        Intrinsics.checkParameterIsNotNull(kProperty, "property");
        return this.value;
    }
    
    @Override
    public void setValue(Object value, final KProperty<?> kProperty, final T value2) {
        Intrinsics.checkParameterIsNotNull(kProperty, "property");
        value = this.value;
        if (!this.beforeChange(kProperty, value, value2)) {
            return;
        }
        this.afterChange(kProperty, value, this.value = value2);
    }
}
