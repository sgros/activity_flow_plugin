// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.jvm.internal;

import kotlin.reflect.KCallable;
import kotlin.reflect.KProperty1;

public abstract class PropertyReference1 extends PropertyReference implements KProperty1
{
    @Override
    protected KCallable computeReflected() {
        return Reflection.property1(this);
    }
    
    @Override
    public KProperty1.Getter getGetter() {
        return (KProperty1.Getter)((KProperty1)this.getReflected()).getGetter();
    }
    
    @Override
    public Object invoke(final Object o) {
        return this.get(o);
    }
}
