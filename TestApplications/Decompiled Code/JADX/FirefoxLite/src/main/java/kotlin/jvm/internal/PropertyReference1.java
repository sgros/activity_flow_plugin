package kotlin.jvm.internal;

import kotlin.reflect.KCallable;
import kotlin.reflect.KProperty1;
import kotlin.reflect.KProperty1.Getter;

public abstract class PropertyReference1 extends PropertyReference implements KProperty1 {
    /* Access modifiers changed, original: protected */
    public KCallable computeReflected() {
        return Reflection.property1(this);
    }

    public Object invoke(Object obj) {
        return get(obj);
    }

    public Getter getGetter() {
        return ((KProperty1) getReflected()).getGetter();
    }
}
