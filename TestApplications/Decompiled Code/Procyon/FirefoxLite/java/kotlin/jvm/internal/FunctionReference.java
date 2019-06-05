// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.jvm.internal;

import kotlin.reflect.KCallable;
import kotlin.reflect.KFunction;

public class FunctionReference extends CallableReference implements FunctionBase, KFunction
{
    private final int arity;
    
    public FunctionReference(final int arity, final Object o) {
        super(o);
        this.arity = arity;
    }
    
    @Override
    protected KCallable computeReflected() {
        return Reflection.function(this);
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (o == this) {
            return true;
        }
        if (o instanceof FunctionReference) {
            final FunctionReference functionReference = (FunctionReference)o;
            if (this.getOwner() == null) {
                if (functionReference.getOwner() != null) {
                    return false;
                }
            }
            else if (!this.getOwner().equals(functionReference.getOwner())) {
                return false;
            }
            if (this.getName().equals(functionReference.getName()) && this.getSignature().equals(functionReference.getSignature()) && Intrinsics.areEqual(this.getBoundReceiver(), functionReference.getBoundReceiver())) {
                return b;
            }
            b = false;
            return b;
        }
        return o instanceof KFunction && o.equals(this.compute());
    }
    
    @Override
    public int getArity() {
        return this.arity;
    }
    
    @Override
    protected KFunction getReflected() {
        return (KFunction)super.getReflected();
    }
    
    @Override
    public int hashCode() {
        int n;
        if (this.getOwner() == null) {
            n = 0;
        }
        else {
            n = this.getOwner().hashCode() * 31;
        }
        return (n + this.getName().hashCode()) * 31 + this.getSignature().hashCode();
    }
    
    @Override
    public String toString() {
        final KCallable compute = this.compute();
        if (compute != this) {
            return compute.toString();
        }
        String string;
        if ("<init>".equals(this.getName())) {
            string = "constructor (Kotlin reflection is not available)";
        }
        else {
            final StringBuilder sb = new StringBuilder();
            sb.append("function ");
            sb.append(this.getName());
            sb.append(" (Kotlin reflection is not available)");
            string = sb.toString();
        }
        return string;
    }
}
