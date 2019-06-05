package kotlinx.coroutines.experimental;

import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: Builders.common.kt */
final /* synthetic */ class BuildersKt__Builders_commonKt {
    public static /* bridge */ /* synthetic */ Job launch$default(CoroutineContext coroutineContext, CoroutineStart coroutineStart, Job job, Function1 function1, Function2 function2, int i, Object obj) {
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
        return BuildersKt.launch(coroutineContext, coroutineStart, job, function1, function2);
    }

    public static final Job launch(CoroutineContext coroutineContext, CoroutineStart coroutineStart, Job job, Function1<? super Throwable, Unit> function1, Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ? extends Object> function2) {
        StandaloneCoroutine lazyStandaloneCoroutine;
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(coroutineStart, "start");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        coroutineContext = CoroutineContextKt.newCoroutineContext(coroutineContext, job);
        if (coroutineStart.isLazy()) {
            lazyStandaloneCoroutine = new LazyStandaloneCoroutine(coroutineContext, function2);
        } else {
            lazyStandaloneCoroutine = new StandaloneCoroutine(coroutineContext, true);
        }
        if (function1 != null) {
            lazyStandaloneCoroutine.invokeOnCompletion(function1);
        }
        lazyStandaloneCoroutine.start(coroutineStart, lazyStandaloneCoroutine, function2);
        return lazyStandaloneCoroutine;
    }
}
