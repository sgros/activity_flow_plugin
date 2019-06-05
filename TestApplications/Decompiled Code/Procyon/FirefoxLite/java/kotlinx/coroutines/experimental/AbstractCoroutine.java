// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.jvm.functions.Function2;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.coroutines.experimental.Continuation;

public abstract class AbstractCoroutine<T> extends JobSupport implements Continuation<T>, CoroutineScope, Job
{
    private final CoroutineContext context;
    private final CoroutineContext parentContext;
    
    public AbstractCoroutine(final CoroutineContext parentContext, final boolean b) {
        Intrinsics.checkParameterIsNotNull(parentContext, "parentContext");
        super(b);
        this.parentContext = parentContext;
        this.context = this.parentContext.plus(this);
    }
    
    @Override
    public boolean cancel(final Throwable t) {
        return super.cancel(t);
    }
    
    @Override
    public final CoroutineContext getContext() {
        return this.context;
    }
    
    public int getDefaultResumeMode$kotlinx_coroutines_core() {
        return 0;
    }
    
    @Override
    public final void handleException$kotlinx_coroutines_core(final Throwable t) {
        Intrinsics.checkParameterIsNotNull(t, "exception");
        CoroutineExceptionHandlerKt.handleCoroutineException(this.parentContext, t);
    }
    
    public final void initParentJob$kotlinx_coroutines_core() {
        this.initParentJobInternal$kotlinx_coroutines_core(this.parentContext.get((CoroutineContext.Key<Job>)Job.Key));
    }
    
    @Override
    public DisposableHandle invokeOnCompletion(final boolean b, final boolean b2, final Function1<? super Throwable, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "handler");
        return super.invokeOnCompletion(b, b2, function1);
    }
    
    @Override
    public String nameString$kotlinx_coroutines_core() {
        final String coroutineName = CoroutineContextKt.getCoroutineName(this.context);
        if (coroutineName != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append('\"');
            sb.append(coroutineName);
            sb.append("\":");
            sb.append(super.nameString$kotlinx_coroutines_core());
            return sb.toString();
        }
        return super.nameString$kotlinx_coroutines_core();
    }
    
    protected void onCancellation(final Throwable t) {
    }
    
    @Override
    public void onCancellationInternal$kotlinx_coroutines_core(final CompletedExceptionally completedExceptionally) {
        Throwable cause;
        if (completedExceptionally != null) {
            cause = completedExceptionally.cause;
        }
        else {
            cause = null;
        }
        this.onCancellation(cause);
    }
    
    protected void onCompleted(final T t) {
    }
    
    protected void onCompletedExceptionally(final Throwable t) {
        Intrinsics.checkParameterIsNotNull(t, "exception");
    }
    
    @Override
    public void onCompletionInternal$kotlinx_coroutines_core(final Object o, final int n) {
        if (o instanceof CompletedExceptionally) {
            this.onCompletedExceptionally(((CompletedExceptionally)o).cause);
        }
        else {
            this.onCompleted(o);
        }
    }
    
    protected void onStart() {
    }
    
    @Override
    public final void onStartInternal$kotlinx_coroutines_core() {
        this.onStart();
    }
    
    @Override
    public final void resume(final T t) {
        this.makeCompletingOnce$kotlinx_coroutines_core(t, this.getDefaultResumeMode$kotlinx_coroutines_core());
    }
    
    @Override
    public final void resumeWithException(final Throwable t) {
        Intrinsics.checkParameterIsNotNull(t, "exception");
        this.makeCompletingOnce$kotlinx_coroutines_core(new CompletedExceptionally(t), this.getDefaultResumeMode$kotlinx_coroutines_core());
    }
    
    public final <R> void start(final CoroutineStart coroutineStart, final R r, final Function2<? super R, ? super Continuation<? super T>, ?> function2) {
        Intrinsics.checkParameterIsNotNull(coroutineStart, "start");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        this.initParentJob$kotlinx_coroutines_core();
        coroutineStart.invoke((Function2<? super R, ? super Continuation<? super Object>, ?>)function2, r, (Continuation<? super Object>)this);
    }
}
