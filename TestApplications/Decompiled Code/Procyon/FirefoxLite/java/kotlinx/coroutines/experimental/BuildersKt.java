// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.functions.Function2;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.coroutines.experimental.CoroutineContext;

public final class BuildersKt
{
    public static final Job launch(final CoroutineContext coroutineContext, final CoroutineStart coroutineStart, final Job job, final Function1<? super Throwable, Unit> function1, final Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ?> function2) {
        return BuildersKt__Builders_commonKt.launch(coroutineContext, coroutineStart, job, function1, function2);
    }
}
