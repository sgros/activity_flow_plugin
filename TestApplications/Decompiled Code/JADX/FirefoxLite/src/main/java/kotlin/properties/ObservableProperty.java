package kotlin.properties;

import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KProperty;

/* compiled from: ObservableProperty.kt */
public abstract class ObservableProperty<T> implements ReadWriteProperty<Object, T> {
    private T value;

    /* Access modifiers changed, original: protected */
    public void afterChange(KProperty<?> kProperty, T t, T t2) {
        Intrinsics.checkParameterIsNotNull(kProperty, "property");
    }

    /* Access modifiers changed, original: protected */
    public boolean beforeChange(KProperty<?> kProperty, T t, T t2) {
        Intrinsics.checkParameterIsNotNull(kProperty, "property");
        return true;
    }

    public ObservableProperty(T t) {
        this.value = t;
    }

    public T getValue(Object obj, KProperty<?> kProperty) {
        Intrinsics.checkParameterIsNotNull(kProperty, "property");
        return this.value;
    }

    public void setValue(Object obj, KProperty<?> kProperty, T t) {
        Intrinsics.checkParameterIsNotNull(kProperty, "property");
        obj = this.value;
        if (beforeChange(kProperty, obj, t)) {
            this.value = t;
            afterChange(kProperty, obj, t);
        }
    }
}
