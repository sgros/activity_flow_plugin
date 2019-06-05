package kotlinx.coroutines.experimental;

import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: Deferred.kt */
public final class DeferredKt {
    public static /* bridge */ /* synthetic */ Deferred async$default(CoroutineContext coroutineContext, CoroutineStart coroutineStart, Job job, Function1 function1, Function2 function2, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = CoroutineContextKt.getDefaultDispatcher();
        }
        if ((i & 2) != 0) {
            coroutineStart = CoroutineStart.DEFAULT;
        }
        if ((i & 4) != 0) {
            job = (Job) null;
        }
        if ((i & 8) != 0) {
            function1 = (Function1) null;
        }
        return async(coroutineContext, coroutineStart, job, function1, function2);
    }

    public static final <T> Deferred<T> async(CoroutineContext coroutineContext, CoroutineStart coroutineStart, Job job, Function1<? super Throwable, Unit> function1, Function2<? super CoroutineScope, ? super Continuation<? super T>, ? extends Object> function2) {
        DeferredCoroutine lazyDeferredCoroutine;
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(coroutineStart, "start");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        coroutineContext = CoroutineContextKt.newCoroutineContext(coroutineContext, job);
        if (coroutineStart.isLazy()) {
            lazyDeferredCoroutine = new LazyDeferredCoroutine(coroutineContext, function2);
        } else {
            lazyDeferredCoroutine = new DeferredCoroutine(coroutineContext, true);
        }
        if (function1 != null) {
            lazyDeferredCoroutine.invokeOnCompletion(function1);
        }
        lazyDeferredCoroutine.start(coroutineStart, lazyDeferredCoroutine, function2);
        return lazyDeferredCoroutine;
    }
}
