// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.functions.Function2;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.coroutines.experimental.CoroutineContext;

public final class DeferredKt
{
    public static final <T> Deferred<T> async(CoroutineContext coroutineContext, final CoroutineStart coroutineStart, final Job job, final Function1<? super Throwable, Unit> function1, final Function2<? super CoroutineScope, ? super Continuation<? super T>, ?> function2) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(coroutineStart, "start");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        coroutineContext = CoroutineContextKt.newCoroutineContext(coroutineContext, job);
        CoroutineScope coroutineScope;
        if (coroutineStart.isLazy()) {
            coroutineScope = new LazyDeferredCoroutine<Object>(coroutineContext, function2);
        }
        else {
            coroutineScope = new DeferredCoroutine<Object>(coroutineContext, true);
        }
        if (function1 != null) {
            ((JobSupport)coroutineScope).invokeOnCompletion(function1);
        }
        ((AbstractCoroutine<Object>)coroutineScope).start(coroutineStart, coroutineScope, (Function2<? super DeferredCoroutine<Object>, ? super Continuation<? super Object>, ?>)function2);
        return (DeferredCoroutine<T>)coroutineScope;
    }
}
