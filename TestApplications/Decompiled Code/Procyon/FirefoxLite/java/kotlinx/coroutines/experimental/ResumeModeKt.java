// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.experimental.Continuation;

public final class ResumeModeKt
{
    public static final boolean isCancellableMode(final int n) {
        boolean b = true;
        if (n != 1) {
            b = false;
        }
        return b;
    }
    
    public static final boolean isDispatchedMode(final int n) {
        boolean b = true;
        if (n != 0) {
            b = (n == 1 && b);
        }
        return b;
    }
    
    public static final <T> void resumeMode(Continuation<? super T> updateThreadContext, final T t, final int i) {
        Intrinsics.checkParameterIsNotNull(updateThreadContext, "$receiver");
        switch (i) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Invalid mode ");
                sb.append(i);
                throw new IllegalStateException(sb.toString().toString());
            }
            case 4: {}
            case 3: {
                final DispatchedContinuation dispatchedContinuation = (DispatchedContinuation)updateThreadContext;
                updateThreadContext = CoroutineContextKt.updateThreadContext(dispatchedContinuation.getContext());
                try {
                    dispatchedContinuation.continuation.resume((T)t);
                    final Unit instance = Unit.INSTANCE;
                    return;
                }
                finally {
                    CoroutineContextKt.restoreThreadContext(updateThreadContext);
                }
            }
            case 2: {
                DispatchedKt.resumeDirect((Continuation<? super T>)updateThreadContext, t);
            }
            case 1: {
                DispatchedKt.resumeCancellable((Continuation<? super T>)updateThreadContext, t);
            }
            case 0: {
                ((Continuation<T>)updateThreadContext).resume(t);
            }
        }
    }
    
    public static final <T> void resumeWithExceptionMode(Continuation<? super T> updateThreadContext, final Throwable t, final int i) {
        Intrinsics.checkParameterIsNotNull(updateThreadContext, "$receiver");
        Intrinsics.checkParameterIsNotNull(t, "exception");
        switch (i) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Invalid mode ");
                sb.append(i);
                throw new IllegalStateException(sb.toString().toString());
            }
            case 4: {}
            case 3: {
                final DispatchedContinuation dispatchedContinuation = (DispatchedContinuation)updateThreadContext;
                updateThreadContext = CoroutineContextKt.updateThreadContext(dispatchedContinuation.getContext());
                try {
                    dispatchedContinuation.continuation.resumeWithException(t);
                    final Unit instance = Unit.INSTANCE;
                    return;
                }
                finally {
                    CoroutineContextKt.restoreThreadContext(updateThreadContext);
                }
            }
            case 2: {
                DispatchedKt.resumeDirectWithException((Continuation<? super Object>)updateThreadContext, t);
            }
            case 1: {
                DispatchedKt.resumeCancellableWithException((Continuation<? super Object>)updateThreadContext, t);
            }
            case 0: {
                ((Continuation)updateThreadContext).resumeWithException(t);
            }
        }
    }
}
