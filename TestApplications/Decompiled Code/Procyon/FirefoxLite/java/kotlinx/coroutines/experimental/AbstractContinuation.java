// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import java.util.concurrent.CancellationException;
import kotlin.jvm.internal.Intrinsics;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import kotlin.coroutines.experimental.Continuation;

public abstract class AbstractContinuation<T> implements Continuation<T>, DispatchedTask<T>
{
    private static final AtomicIntegerFieldUpdater _decision$FU;
    private static final AtomicReferenceFieldUpdater _state$FU;
    private volatile int _decision;
    private volatile Object _state;
    private final Continuation<T> delegate;
    private volatile DisposableHandle parentHandle;
    private final int resumeMode;
    
    static {
        _decision$FU = AtomicIntegerFieldUpdater.newUpdater(AbstractContinuation.class, "_decision");
        _state$FU = AtomicReferenceFieldUpdater.newUpdater(AbstractContinuation.class, Object.class, "_state");
    }
    
    public AbstractContinuation(final Continuation<? super T> delegate, final int resumeMode) {
        Intrinsics.checkParameterIsNotNull(delegate, "delegate");
        this.delegate = (Continuation<T>)delegate;
        this.resumeMode = resumeMode;
        this._decision = 0;
        this._state = AbstractContinuationKt.access$getACTIVE$p();
    }
    
    private final void coerceWithException(final Cancelling cancelling, final CompletedExceptionally completedExceptionally) {
        final CancelledContinuation cancel = cancelling.cancel;
        final Throwable cause = cancel.cause;
        final Throwable cause2 = completedExceptionally.cause;
        if ((!(cancel.cause instanceof CancellationException) || cause.getCause() != cause2.getCause()) && cause.getCause() != cause2) {
            ExceptionsKt__ExceptionsKt.addSuppressed(completedExceptionally.cause, cause);
        }
    }
    
    private final void handleException(final Throwable t) {
        CoroutineExceptionHandlerKt.handleCoroutineException(this.getContext(), t);
    }
    
    private final CancelHandler makeHandler(final Function1<? super Throwable, Unit> function1) {
        CancelHandler cancelHandler;
        if (function1 instanceof CancelHandler) {
            cancelHandler = (CancelHandler)function1;
        }
        else {
            cancelHandler = new InvokeOnCancel(function1);
        }
        return cancelHandler;
    }
    
    private final void onCompletionInternal(final int n) {
        if (this.tryResume()) {
            return;
        }
        DispatchedKt.dispatch((DispatchedTask<? super Object>)this, n);
    }
    
    private final String stateString() {
        final Object state$kotlinx_coroutines_core = this.getState$kotlinx_coroutines_core();
        String s;
        if (state$kotlinx_coroutines_core instanceof NotCompleted) {
            s = "Active";
        }
        else if (state$kotlinx_coroutines_core instanceof CancelledContinuation) {
            s = "Cancelled";
        }
        else if (state$kotlinx_coroutines_core instanceof CompletedExceptionally) {
            s = "CompletedExceptionally";
        }
        else {
            s = "Completed";
        }
        return s;
    }
    
    private final boolean tryCancel(final NotCompleted notCompleted, final Throwable t) {
        final boolean useCancellingState = this.getUseCancellingState();
        boolean b = false;
        if (!useCancellingState) {
            return this.updateStateToFinal(notCompleted, new CancelledContinuation(this, t), 0);
        }
        if (!(notCompleted instanceof CancelHandler)) {
            b = true;
        }
        if (b) {
            return AbstractContinuation._state$FU.compareAndSet(this, notCompleted, new Cancelling(new CancelledContinuation(this, t)));
        }
        throw new IllegalArgumentException("Invariant: 'Cancelling' state and cancellation handlers cannot be used together".toString());
    }
    
    private final boolean tryResume() {
        do {
            switch (this._decision) {
                default: {
                    throw new IllegalStateException("Already resumed".toString());
                }
                case 1: {
                    return false;
                }
                case 0: {
                    continue;
                }
            }
        } while (!AbstractContinuation._decision$FU.compareAndSet(this, 0, 2));
        return true;
    }
    
    private final boolean trySuspend() {
        do {
            final int decision = this._decision;
            if (decision != 0) {
                if (decision == 2) {
                    return false;
                }
                throw new IllegalStateException("Already suspended".toString());
            }
        } while (!AbstractContinuation._decision$FU.compareAndSet(this, 0, 1));
        return true;
    }
    
    private final boolean updateStateToFinal(final NotCompleted notCompleted, final Object o, final int n) {
        if (!this.tryUpdateStateToFinal(notCompleted, o)) {
            return false;
        }
        this.completeStateUpdate(notCompleted, o, n);
        return true;
    }
    
    public final boolean cancel(final Throwable t) {
        Object state$kotlinx_coroutines_core;
        do {
            state$kotlinx_coroutines_core = this.getState$kotlinx_coroutines_core();
            if (!(state$kotlinx_coroutines_core instanceof NotCompleted)) {
                return false;
            }
            if (state$kotlinx_coroutines_core instanceof Cancelling) {
                return false;
            }
        } while (!this.tryCancel((NotCompleted)state$kotlinx_coroutines_core, t));
        return true;
    }
    
    protected final void completeStateUpdate(final NotCompleted obj, final Object o, final int n) {
        Intrinsics.checkParameterIsNotNull(obj, "expect");
        final boolean b = o instanceof CompletedExceptionally;
        final Throwable t = null;
        Object o2;
        if (!b) {
            o2 = null;
        }
        else {
            o2 = o;
        }
        final CompletedExceptionally completedExceptionally = (CompletedExceptionally)o2;
        this.onCompletionInternal(n);
        if (o instanceof CancelledContinuation && obj instanceof CancelHandler) {
            try {
                final CancelHandler cancelHandler = (CancelHandler)obj;
                Throwable cause = t;
                if (completedExceptionally != null) {
                    cause = completedExceptionally.cause;
                }
                cancelHandler.invoke(cause);
            }
            catch (Throwable t2) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Exception in completion handler ");
                sb.append(obj);
                sb.append(" for ");
                sb.append(this);
                this.handleException(new CompletionHandlerException(sb.toString(), t2));
            }
        }
    }
    
    @Override
    public final Continuation<T> getDelegate() {
        return this.delegate;
    }
    
    @Override
    public Throwable getExceptionalResult(final Object o) {
        return DefaultImpls.getExceptionalResult((DispatchedTask<? super Object>)this, o);
    }
    
    public final Object getResult() {
        if (this.trySuspend()) {
            return IntrinsicsKt__IntrinsicsJvmKt.getCOROUTINE_SUSPENDED();
        }
        final Object state$kotlinx_coroutines_core = this.getState$kotlinx_coroutines_core();
        if (!(state$kotlinx_coroutines_core instanceof CompletedExceptionally)) {
            return this.getSuccessfulResult(state$kotlinx_coroutines_core);
        }
        throw ((CompletedExceptionally)state$kotlinx_coroutines_core).cause;
    }
    
    @Override
    public final int getResumeMode() {
        return this.resumeMode;
    }
    
    public final Object getState$kotlinx_coroutines_core() {
        return this._state;
    }
    
    @Override
    public <T> T getSuccessfulResult(final Object o) {
        return DefaultImpls.getSuccessfulResult((DispatchedTask<? super Object>)this, o);
    }
    
    protected boolean getUseCancellingState() {
        return false;
    }
    
    public final void initParentJobInternal$kotlinx_coroutines_core(final Job job) {
        if (this.parentHandle != null) {
            throw new IllegalStateException("Check failed.".toString());
        }
        if (job == null) {
            this.parentHandle = NonDisposableHandle.INSTANCE;
            return;
        }
        job.start();
        final DisposableHandle invokeOnCompletion$default = Job.DefaultImpls.invokeOnCompletion$default(job, true, false, (Function1)new ChildContinuation(job, this), 2, (Object)null);
        this.parentHandle = invokeOnCompletion$default;
        if (this.isCompleted()) {
            invokeOnCompletion$default.dispose();
            this.parentHandle = NonDisposableHandle.INSTANCE;
        }
    }
    
    public final void invokeOnCancellation(final Function1<? super Throwable, Unit> obj) {
        Intrinsics.checkParameterIsNotNull(obj, "handler");
        final Throwable t = null;
        CancelHandler cancelHandler = null;
        Object state$kotlinx_coroutines_core;
        CancelHandler handler;
        do {
            state$kotlinx_coroutines_core = this.getState$kotlinx_coroutines_core();
            if (state$kotlinx_coroutines_core instanceof Active) {
                if (cancelHandler != null) {
                    handler = cancelHandler;
                }
                else {
                    handler = this.makeHandler(obj);
                }
                cancelHandler = handler;
            }
            else {
                if (state$kotlinx_coroutines_core instanceof CancelHandler) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("It's prohibited to register multiple handlers, tried to register ");
                    sb.append(obj);
                    sb.append(", already has ");
                    sb.append(state$kotlinx_coroutines_core);
                    throw new IllegalStateException(sb.toString().toString());
                }
                if (state$kotlinx_coroutines_core instanceof CancelledContinuation) {
                    Object o = state$kotlinx_coroutines_core;
                    if (!(state$kotlinx_coroutines_core instanceof CompletedExceptionally)) {
                        o = null;
                    }
                    final CompletedExceptionally completedExceptionally = (CompletedExceptionally)o;
                    Throwable cause = t;
                    if (completedExceptionally != null) {
                        cause = completedExceptionally.cause;
                    }
                    obj.invoke(cause);
                    return;
                }
                if (!(state$kotlinx_coroutines_core instanceof Cancelling)) {
                    return;
                }
                throw new IllegalStateException("Cancellation handlers for continuations with 'Cancelling' state are not supported".toString());
            }
        } while (!AbstractContinuation._state$FU.compareAndSet(this, state$kotlinx_coroutines_core, handler));
    }
    
    public final boolean isCompleted() {
        return !(this.getState$kotlinx_coroutines_core() instanceof NotCompleted);
    }
    
    protected String nameString() {
        return DebugKt.getClassSimpleName(this);
    }
    
    @Override
    public void resume(final T t) {
        this.resumeImpl(t, this.resumeMode);
    }
    
    protected final void resumeImpl(final Object o, final int n) {
        while (true) {
            final Object state$kotlinx_coroutines_core = this.getState$kotlinx_coroutines_core();
            if (state$kotlinx_coroutines_core instanceof Cancelling) {
                if (!(o instanceof CompletedExceptionally)) {
                    if (this.updateStateToFinal((NotCompleted)state$kotlinx_coroutines_core, ((Cancelling)state$kotlinx_coroutines_core).cancel, n)) {
                        return;
                    }
                    continue;
                }
                else {
                    CompletedExceptionally completedExceptionally = (CompletedExceptionally)o;
                    if (completedExceptionally.cause instanceof CancellationException) {
                        this.coerceWithException((Cancelling)state$kotlinx_coroutines_core, completedExceptionally);
                    }
                    else {
                        final Throwable cause = completedExceptionally.cause;
                        final Throwable cause2 = ((Cancelling)state$kotlinx_coroutines_core).cancel.cause;
                        if (!(cause2 instanceof CancellationException) || cause2.getCause() != cause) {
                            ExceptionsKt__ExceptionsKt.addSuppressed(cause, cause2);
                        }
                        completedExceptionally = new CompletedExceptionally(cause);
                    }
                    if (this.updateStateToFinal((NotCompleted)state$kotlinx_coroutines_core, completedExceptionally, n)) {
                        return;
                    }
                    continue;
                }
            }
            else if (state$kotlinx_coroutines_core instanceof NotCompleted) {
                if (this.updateStateToFinal((NotCompleted)state$kotlinx_coroutines_core, o, n)) {
                    return;
                }
                continue;
            }
            else {
                if (!(state$kotlinx_coroutines_core instanceof CancelledContinuation)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Already resumed, but proposed with update ");
                    sb.append(o);
                    throw new IllegalStateException(sb.toString().toString());
                }
                if (!(o instanceof NotCompleted) && !(o instanceof CompletedExceptionally)) {
                    return;
                }
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Unexpected update, state: ");
                sb2.append(state$kotlinx_coroutines_core);
                sb2.append(", update: ");
                sb2.append(o);
                throw new IllegalStateException(sb2.toString().toString());
            }
        }
    }
    
    @Override
    public void resumeWithException(final Throwable t) {
        Intrinsics.checkParameterIsNotNull(t, "exception");
        this.resumeImpl(new CompletedExceptionally(t), this.resumeMode);
    }
    
    @Override
    public void run() {
        DefaultImpls.run((DispatchedTask<? super Object>)this);
    }
    
    @Override
    public Object takeState() {
        return this.getState$kotlinx_coroutines_core();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.nameString());
        sb.append('{');
        sb.append(this.stateString());
        sb.append("}@");
        sb.append(DebugKt.getHexAddress(this));
        return sb.toString();
    }
    
    protected final boolean tryUpdateStateToFinal(final NotCompleted notCompleted, final Object o) {
        Intrinsics.checkParameterIsNotNull(notCompleted, "expect");
        if (o instanceof NotCompleted) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        if (!AbstractContinuation._state$FU.compareAndSet(this, notCompleted, o)) {
            return false;
        }
        final DisposableHandle parentHandle = this.parentHandle;
        if (parentHandle != null) {
            parentHandle.dispose();
            this.parentHandle = NonDisposableHandle.INSTANCE;
        }
        return true;
    }
}
