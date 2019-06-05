package kotlinx.coroutines.experimental.android;

import android.support.annotation.Keep;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Method;
import kotlin.coroutines.experimental.AbstractCoroutineContextElement;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.CoroutineExceptionHandler;

@Keep
/* compiled from: AndroidExceptionPreHandler.kt */
public final class AndroidExceptionPreHandler extends AbstractCoroutineContextElement implements CoroutineExceptionHandler {
    public AndroidExceptionPreHandler() {
        super(CoroutineExceptionHandler.Key);
    }

    public void handleException(CoroutineContext coroutineContext, Throwable th) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(th, "exception");
        Method access$getGetter$p = AndroidExceptionPreHandlerKt.getter;
        Object invoke = access$getGetter$p != null ? access$getGetter$p.invoke(null, new Object[0]) : null;
        if (!(invoke instanceof UncaughtExceptionHandler)) {
            invoke = null;
        }
        UncaughtExceptionHandler uncaughtExceptionHandler = (UncaughtExceptionHandler) invoke;
        if (uncaughtExceptionHandler != null) {
            uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), th);
        }
    }
}
