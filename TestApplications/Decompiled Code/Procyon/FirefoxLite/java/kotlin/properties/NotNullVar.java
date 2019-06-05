// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.properties;

import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KProperty;

final class NotNullVar<T> implements ReadWriteProperty<Object, T>
{
    private T value;
    
    public NotNullVar() {
    }
    
    @Override
    public T getValue(Object value, final KProperty<?> kProperty) {
        Intrinsics.checkParameterIsNotNull(kProperty, "property");
        value = this.value;
        if (value != null) {
            return (T)value;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Property ");
        sb.append(kProperty.getName());
        sb.append(" should be initialized before get.");
        throw new IllegalStateException(sb.toString());
    }
    
    @Override
    public void setValue(final Object o, final KProperty<?> kProperty, final T value) {
        Intrinsics.checkParameterIsNotNull(kProperty, "property");
        Intrinsics.checkParameterIsNotNull(value, "value");
        this.value = value;
    }
}
