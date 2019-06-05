// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental.android;

import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import android.os.Handler;
import kotlinx.coroutines.experimental.CoroutineDispatcher;

public final class HandlerContext extends CoroutineDispatcher
{
    private final Handler handler;
    private final String name;
    
    public HandlerContext(final Handler handler, final String name) {
        Intrinsics.checkParameterIsNotNull(handler, "handler");
        this.handler = handler;
        this.name = name;
    }
    
    @Override
    public void dispatch(final CoroutineContext coroutineContext, final Runnable runnable) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(runnable, "block");
        this.handler.post(runnable);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof HandlerContext && ((HandlerContext)o).handler == this.handler;
    }
    
    @Override
    public int hashCode() {
        return System.identityHashCode(this.handler);
    }
    
    @Override
    public String toString() {
        String s = this.name;
        if (s == null) {
            s = this.handler.toString();
            Intrinsics.checkExpressionValueIsNotNull(s, "handler.toString()");
        }
        return s;
    }
}
