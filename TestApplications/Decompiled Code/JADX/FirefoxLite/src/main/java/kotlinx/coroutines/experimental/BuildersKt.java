package kotlinx.coroutines.experimental;

import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

public final class BuildersKt {
    public static final Job launch(CoroutineContext coroutineContext, CoroutineStart coroutineStart, Job job, Function1<? super Throwable, Unit> function1, Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ? extends Object> function2) {
        return BuildersKt__Builders_commonKt.launch(coroutineContext, coroutineStart, job, function1, function2);
    }
}
