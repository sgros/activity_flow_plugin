// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import java.util.Iterator;
import java.util.ServiceLoader;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.experimental.CoroutineContext;

public final class CoroutineExceptionHandlerImplKt
{
    public static final void handleCoroutineExceptionImpl(final CoroutineContext coroutineContext, final Throwable t) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(t, "exception");
        final ServiceLoader<CoroutineExceptionHandler> load = ServiceLoader.load(CoroutineExceptionHandler.class);
        Intrinsics.checkExpressionValueIsNotNull(load, "ServiceLoader.load(Corou\u2026ptionHandler::class.java)");
        final Iterator<Object> iterator = load.iterator();
        while (iterator.hasNext()) {
            iterator.next().handleException(coroutineContext, t);
        }
        final Thread currentThread = Thread.currentThread();
        Intrinsics.checkExpressionValueIsNotNull(currentThread, "currentThread");
        currentThread.getUncaughtExceptionHandler().uncaughtException(currentThread, t);
    }
}
