// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental.android;

import java.lang.reflect.Method;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.experimental.CoroutineContext;
import android.support.annotation.Keep;
import kotlinx.coroutines.experimental.CoroutineExceptionHandler;
import kotlin.coroutines.experimental.AbstractCoroutineContextElement;

@Keep
public final class AndroidExceptionPreHandler extends AbstractCoroutineContextElement implements CoroutineExceptionHandler
{
    public AndroidExceptionPreHandler() {
        super(CoroutineExceptionHandler.Key);
    }
    
    @Override
    public void handleException(final CoroutineContext coroutineContext, final Throwable t) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(t, "exception");
        final Method access$getGetter$p = AndroidExceptionPreHandlerKt.access$getGetter$p();
        Object invoke;
        if (access$getGetter$p != null) {
            invoke = access$getGetter$p.invoke(null, new Object[0]);
        }
        else {
            invoke = null;
        }
        Object o = invoke;
        if (!(invoke instanceof Thread.UncaughtExceptionHandler)) {
            o = null;
        }
        final Thread.UncaughtExceptionHandler uncaughtExceptionHandler = (Thread.UncaughtExceptionHandler)o;
        if (uncaughtExceptionHandler != null) {
            uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), t);
        }
    }
}
