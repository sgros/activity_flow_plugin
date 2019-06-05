package kotlin.jvm.internal;

import kotlin.reflect.KCallable;
import kotlin.reflect.KMutableProperty1;
import kotlin.reflect.KProperty1.Getter;

public abstract class MutablePropertyReference1 extends MutablePropertyReference implements KMutableProperty1 {
    /* Access modifiers changed, original: protected */
    public KCallable computeReflected() {
        return Reflection.mutableProperty1(this);
    }

    public Object invoke(Object obj) {
        return get(obj);
    }

    public Getter getGetter() {
        return ((KMutableProperty1) getReflected()).getGetter();
    }
}
