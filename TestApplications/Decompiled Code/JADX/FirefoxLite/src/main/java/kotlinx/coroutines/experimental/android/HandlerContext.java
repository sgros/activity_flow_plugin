package kotlinx.coroutines.experimental.android;

import android.os.Handler;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.CoroutineDispatcher;

/* compiled from: HandlerContext.kt */
public final class HandlerContext extends CoroutineDispatcher {
    private final Handler handler;
    private final String name;

    public HandlerContext(Handler handler, String str) {
        Intrinsics.checkParameterIsNotNull(handler, "handler");
        this.handler = handler;
        this.name = str;
    }

    public void dispatch(CoroutineContext coroutineContext, Runnable runnable) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(runnable, "block");
        this.handler.post(runnable);
    }

    public String toString() {
        String str = this.name;
        if (str != null) {
            return str;
        }
        str = this.handler.toString();
        Intrinsics.checkExpressionValueIsNotNull(str, "handler.toString()");
        return str;
    }

    public boolean equals(Object obj) {
        return (obj instanceof HandlerContext) && ((HandlerContext) obj).handler == this.handler;
    }

    public int hashCode() {
        return System.identityHashCode(this.handler);
    }
}
