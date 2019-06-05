package kotlinx.coroutines.experimental;

import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: AbstractCoroutine.kt */
public abstract class AbstractCoroutine<T> extends JobSupport implements Continuation<T>, CoroutineScope, Job {
    private final CoroutineContext context = this.parentContext.plus(this);
    private final CoroutineContext parentContext;

    public int getDefaultResumeMode$kotlinx_coroutines_core() {
        return 0;
    }

    /* Access modifiers changed, original: protected */
    public void onCancellation(Throwable th) {
    }

    /* Access modifiers changed, original: protected */
    public void onCompleted(T t) {
    }

    /* Access modifiers changed, original: protected */
    public void onCompletedExceptionally(Throwable th) {
        Intrinsics.checkParameterIsNotNull(th, "exception");
    }

    /* Access modifiers changed, original: protected */
    public void onStart() {
    }

    public AbstractCoroutine(CoroutineContext coroutineContext, boolean z) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "parentContext");
        super(z);
        this.parentContext = coroutineContext;
    }

    public final CoroutineContext getContext() {
        return this.context;
    }

    public final void initParentJob$kotlinx_coroutines_core() {
        initParentJobInternal$kotlinx_coroutines_core((Job) this.parentContext.get(Job.Key));
    }

    public final void onStartInternal$kotlinx_coroutines_core() {
        onStart();
    }

    public void onCancellationInternal$kotlinx_coroutines_core(CompletedExceptionally completedExceptionally) {
        onCancellation(completedExceptionally != null ? completedExceptionally.cause : null);
    }

    public void onCompletionInternal$kotlinx_coroutines_core(Object obj, int i) {
        if (obj instanceof CompletedExceptionally) {
            onCompletedExceptionally(((CompletedExceptionally) obj).cause);
        } else {
            onCompleted(obj);
        }
    }

    public final void resume(T t) {
        makeCompletingOnce$kotlinx_coroutines_core(t, getDefaultResumeMode$kotlinx_coroutines_core());
    }

    public final void resumeWithException(Throwable th) {
        Intrinsics.checkParameterIsNotNull(th, "exception");
        makeCompletingOnce$kotlinx_coroutines_core(new CompletedExceptionally(th), getDefaultResumeMode$kotlinx_coroutines_core());
    }

    public final void handleException$kotlinx_coroutines_core(Throwable th) {
        Intrinsics.checkParameterIsNotNull(th, "exception");
        CoroutineExceptionHandlerKt.handleCoroutineException(this.parentContext, th);
    }

    public String nameString$kotlinx_coroutines_core() {
        String coroutineName = CoroutineContextKt.getCoroutineName(this.context);
        if (coroutineName == null) {
            return super.nameString$kotlinx_coroutines_core();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('\"');
        stringBuilder.append(coroutineName);
        stringBuilder.append("\":");
        stringBuilder.append(super.nameString$kotlinx_coroutines_core());
        return stringBuilder.toString();
    }

    public final <R> void start(CoroutineStart coroutineStart, R r, Function2<? super R, ? super Continuation<? super T>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(coroutineStart, "start");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        initParentJob$kotlinx_coroutines_core();
        coroutineStart.invoke(function2, r, this);
    }

    public DisposableHandle invokeOnCompletion(boolean z, boolean z2, Function1<? super Throwable, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "handler");
        return super.invokeOnCompletion(z, z2, function1);
    }

    public boolean cancel(Throwable th) {
        return super.cancel(th);
    }
}
