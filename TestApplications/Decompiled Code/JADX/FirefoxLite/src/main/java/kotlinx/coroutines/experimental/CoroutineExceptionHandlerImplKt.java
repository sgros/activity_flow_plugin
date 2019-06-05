package kotlinx.coroutines.experimental;

import java.util.ServiceLoader;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: CoroutineExceptionHandlerImpl.kt */
public final class CoroutineExceptionHandlerImplKt {
    public static final void handleCoroutineExceptionImpl(CoroutineContext coroutineContext, Throwable th) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(th, "exception");
        ServiceLoader<CoroutineExceptionHandler> load = ServiceLoader.load(CoroutineExceptionHandler.class);
        Intrinsics.checkExpressionValueIsNotNull(load, "ServiceLoader.load(Corou…ptionHandler::class.java)");
        for (CoroutineExceptionHandler handleException : load) {
            handleException.handleException(coroutineContext, th);
        }
        Thread currentThread = Thread.currentThread();
        Intrinsics.checkExpressionValueIsNotNull(currentThread, "currentThread");
        currentThread.getUncaughtExceptionHandler().uncaughtException(currentThread, th);
    }
}
