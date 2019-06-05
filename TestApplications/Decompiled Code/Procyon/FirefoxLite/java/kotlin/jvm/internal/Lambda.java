// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.jvm.internal;

import java.io.Serializable;

public abstract class Lambda<R> implements Serializable, FunctionBase<R>
{
    private final int arity;
    
    public Lambda(final int arity) {
        this.arity = arity;
    }
    
    @Override
    public int getArity() {
        return this.arity;
    }
    
    @Override
    public String toString() {
        final String renderLambdaToString = Reflection.renderLambdaToString(this);
        Intrinsics.checkExpressionValueIsNotNull(renderLambdaToString, "Reflection.renderLambdaToString(this)");
        return renderLambdaToString;
    }
}
