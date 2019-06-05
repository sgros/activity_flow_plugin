// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.jvm.internal;

import kotlin.reflect.KProperty1;
import kotlin.reflect.KCallable;
import kotlin.reflect.KMutableProperty1;

public abstract class MutablePropertyReference1 extends MutablePropertyReference implements KMutableProperty1
{
    @Override
    protected KCallable computeReflected() {
        return Reflection.mutableProperty1(this);
    }
    
    @Override
    public KProperty1.Getter getGetter() {
        return (KProperty1.Getter)((KMutableProperty1)this.getReflected()).getGetter();
    }
    
    @Override
    public Object invoke(final Object o) {
        return this.get(o);
    }
}
