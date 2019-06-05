package kotlin.properties;

import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KProperty;

/* compiled from: Delegates.kt */
final class NotNullVar<T> implements ReadWriteProperty<Object, T> {
    private T value;

    public T getValue(Object obj, KProperty<?> kProperty) {
        Intrinsics.checkParameterIsNotNull(kProperty, "property");
        obj = this.value;
        if (obj != null) {
            return obj;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Property ");
        stringBuilder.append(kProperty.getName());
        stringBuilder.append(" should be initialized before get.");
        throw new IllegalStateException(stringBuilder.toString());
    }

    public void setValue(Object obj, KProperty<?> kProperty, T t) {
        Intrinsics.checkParameterIsNotNull(kProperty, "property");
        Intrinsics.checkParameterIsNotNull(t, "value");
        this.value = t;
    }
}
