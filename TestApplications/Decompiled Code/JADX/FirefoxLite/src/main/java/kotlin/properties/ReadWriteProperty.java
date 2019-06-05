package kotlin.properties;

import kotlin.reflect.KProperty;

/* compiled from: Interfaces.kt */
public interface ReadWriteProperty<R, T> {
    T getValue(R r, KProperty<?> kProperty);

    void setValue(R r, KProperty<?> kProperty, T t);
}
