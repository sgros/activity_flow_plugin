package kotlinx.coroutines.experimental;

import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.DispatchedTask.DefaultImpls;

/* compiled from: AbstractContinuation.kt */
public abstract class AbstractContinuation<T> implements Continuation<T>, DispatchedTask<T> {
    private static final AtomicIntegerFieldUpdater _decision$FU = AtomicIntegerFieldUpdater.newUpdater(AbstractContinuation.class, "_decision");
    private static final AtomicReferenceFieldUpdater _state$FU = AtomicReferenceFieldUpdater.newUpdater(AbstractContinuation.class, Object.class, "_state");
    private volatile int _decision = 0;
    private volatile Object _state = AbstractContinuationKt.ACTIVE;
    private final Continuation<T> delegate;
    private volatile DisposableHandle parentHandle;
    private final int resumeMode;

    /* Access modifiers changed, original: protected */
    public boolean getUseCancellingState() {
        return false;
    }

    public AbstractContinuation(Continuation<? super T> continuation, int i) {
        Intrinsics.checkParameterIsNotNull(continuation, "delegate");
        this.delegate = continuation;
        this.resumeMode = i;
    }

    public Throwable getExceptionalResult(Object obj) {
        return DefaultImpls.getExceptionalResult(this, obj);
    }

    public <T> T getSuccessfulResult(Object obj) {
        return DefaultImpls.getSuccessfulResult(this, obj);
    }

    public void run() {
        DefaultImpls.run(this);
    }

    public final Continuation<T> getDelegate() {
        return this.delegate;
    }

    public final int getResumeMode() {
        return this.resumeMode;
    }

    public final Object getState$kotlinx_coroutines_core() {
        return this._state;
    }

    public final boolean isCompleted() {
        return !(getState$kotlinx_coroutines_core() instanceof NotCompleted);
    }

    public final void initParentJobInternal$kotlinx_coroutines_core(Job job) {
        if ((this.parentHandle == null ? 1 : null) == null) {
            throw new IllegalStateException("Check failed.".toString());
        } else if (job == null) {
            this.parentHandle = NonDisposableHandle.INSTANCE;
        } else {
            job.start();
            DisposableHandle invokeOnCompletion$default = Job.DefaultImpls.invokeOnCompletion$default(job, true, false, new ChildContinuation(job, this), 2, null);
            this.parentHandle = invokeOnCompletion$default;
            if (isCompleted()) {
                invokeOnCompletion$default.dispose();
                this.parentHandle = NonDisposableHandle.INSTANCE;
            }
        }
    }

    public Object takeState() {
        return getState$kotlinx_coroutines_core();
    }

    public final Object getResult() {
        if (trySuspend()) {
            return IntrinsicsKt__IntrinsicsJvmKt.getCOROUTINE_SUSPENDED();
        }
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        if (!(state$kotlinx_coroutines_core instanceof CompletedExceptionally)) {
            return getSuccessfulResult(state$kotlinx_coroutines_core);
        }
        throw ((CompletedExceptionally) state$kotlinx_coroutines_core).cause;
    }

    public void resume(T t) {
        resumeImpl(t, this.resumeMode);
    }

    public void resumeWithException(Throwable th) {
        Intrinsics.checkParameterIsNotNull(th, "exception");
        resumeImpl(new CompletedExceptionally(th), this.resumeMode);
    }

    public final void invokeOnCancellation(Function1<? super Throwable, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "handler");
        Object obj = null;
        CancelHandler cancelHandler = (CancelHandler) null;
        Object state$kotlinx_coroutines_core;
        do {
            state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if (state$kotlinx_coroutines_core instanceof Active) {
                if (cancelHandler == null) {
                    cancelHandler = makeHandler(function1);
                }
            } else if (state$kotlinx_coroutines_core instanceof CancelHandler) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("It's prohibited to register multiple handlers, tried to register ");
                stringBuilder.append(function1);
                stringBuilder.append(", already has ");
                stringBuilder.append(state$kotlinx_coroutines_core);
                throw new IllegalStateException(stringBuilder.toString().toString());
            } else if (state$kotlinx_coroutines_core instanceof CancelledContinuation) {
                if (!(state$kotlinx_coroutines_core instanceof CompletedExceptionally)) {
                    state$kotlinx_coroutines_core = null;
                }
                CompletedExceptionally completedExceptionally = (CompletedExceptionally) state$kotlinx_coroutines_core;
                if (completedExceptionally != null) {
                    obj = completedExceptionally.cause;
                }
                function1.invoke(obj);
                return;
            } else if (state$kotlinx_coroutines_core instanceof Cancelling) {
                throw new IllegalStateException("Cancellation handlers for continuations with 'Cancelling' state are not supported".toString());
            } else {
                return;
            }
        } while (!_state$FU.compareAndSet(this, state$kotlinx_coroutines_core, cancelHandler));
    }

    private final CancelHandler makeHandler(Function1<? super Throwable, Unit> function1) {
        return function1 instanceof CancelHandler ? (CancelHandler) function1 : new InvokeOnCancel(function1);
    }

    private final boolean tryCancel(NotCompleted notCompleted, Throwable th) {
        int i = 0;
        if (!getUseCancellingState()) {
            return updateStateToFinal(notCompleted, new CancelledContinuation(this, th), 0);
        }
        if (!(notCompleted instanceof CancelHandler)) {
            i = 1;
        }
        if (i != 0) {
            return _state$FU.compareAndSet(this, notCompleted, new Cancelling(new CancelledContinuation(this, th)));
        }
        throw new IllegalArgumentException("Invariant: 'Cancelling' state and cancellation handlers cannot be used together".toString());
    }

    private final void onCompletionInternal(int i) {
        if (!tryResume()) {
            DispatchedKt.dispatch(this, i);
        }
    }

    private final void coerceWithException(Cancelling cancelling, CompletedExceptionally completedExceptionally) {
        CancelledContinuation cancelledContinuation = cancelling.cancel;
        Throwable th = cancelledContinuation.cause;
        Throwable th2 = completedExceptionally.cause;
        Object obj = ((cancelledContinuation.cause instanceof CancellationException) && th.getCause() == th2.getCause()) ? 1 : null;
        if (obj == null && th.getCause() != th2) {
            ExceptionsKt__ExceptionsKt.addSuppressed(completedExceptionally.cause, th);
        }
    }

    private final boolean updateStateToFinal(NotCompleted notCompleted, Object obj, int i) {
        if (!tryUpdateStateToFinal(notCompleted, obj)) {
            return false;
        }
        completeStateUpdate(notCompleted, obj, i);
        return true;
    }

    /* Access modifiers changed, original: protected|final */
    public final boolean tryUpdateStateToFinal(NotCompleted notCompleted, Object obj) {
        Intrinsics.checkParameterIsNotNull(notCompleted, "expect");
        if ((!(obj instanceof NotCompleted) ? 1 : null) == null) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        } else if (!_state$FU.compareAndSet(this, notCompleted, obj)) {
            return false;
        } else {
            DisposableHandle disposableHandle = this.parentHandle;
            if (disposableHandle != null) {
                disposableHandle.dispose();
                this.parentHandle = NonDisposableHandle.INSTANCE;
            }
            return true;
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void completeStateUpdate(NotCompleted notCompleted, Object obj, int i) {
        Intrinsics.checkParameterIsNotNull(notCompleted, "expect");
        Throwable th = null;
        CompletedExceptionally completedExceptionally = (CompletedExceptionally) (!(obj instanceof CompletedExceptionally) ? null : obj);
        onCompletionInternal(i);
        if ((obj instanceof CancelledContinuation) && (notCompleted instanceof CancelHandler)) {
            try {
                CancelHandler cancelHandler = (CancelHandler) notCompleted;
                if (completedExceptionally != null) {
                    th = completedExceptionally.cause;
                }
                cancelHandler.invoke(th);
            } catch (Throwable th2) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Exception in completion handler ");
                stringBuilder.append(notCompleted);
                stringBuilder.append(" for ");
                stringBuilder.append(this);
                handleException(new CompletionHandlerException(stringBuilder.toString(), th2));
            }
        }
    }

    private final void handleException(Throwable th) {
        CoroutineExceptionHandlerKt.handleCoroutineException(getContext(), th);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(nameString());
        stringBuilder.append('{');
        stringBuilder.append(stateString());
        stringBuilder.append("}@");
        stringBuilder.append(DebugKt.getHexAddress(this));
        return stringBuilder.toString();
    }

    /* Access modifiers changed, original: protected */
    public String nameString() {
        return DebugKt.getClassSimpleName(this);
    }

    private final String stateString() {
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        if (state$kotlinx_coroutines_core instanceof NotCompleted) {
            return "Active";
        }
        if (state$kotlinx_coroutines_core instanceof CancelledContinuation) {
            return "Cancelled";
        }
        return state$kotlinx_coroutines_core instanceof CompletedExceptionally ? "CompletedExceptionally" : "Completed";
    }

    public final boolean cancel(Throwable th) {
        Object state$kotlinx_coroutines_core;
        do {
            state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if (!(state$kotlinx_coroutines_core instanceof NotCompleted) || (state$kotlinx_coroutines_core instanceof Cancelling)) {
                return false;
            }
        } while (!tryCancel((NotCompleted) state$kotlinx_coroutines_core, th));
        return true;
    }

    private final boolean trySuspend() {
        do {
            int i = this._decision;
            if (i != 0) {
                if (i == 2) {
                    return false;
                }
                throw new IllegalStateException("Already suspended".toString());
            }
        } while (!_decision$FU.compareAndSet(this, 0, 1));
        return true;
    }

    private final boolean tryResume() {
        do {
            switch (this._decision) {
                case 0:
                    break;
                case 1:
                    return false;
                default:
                    throw new IllegalStateException("Already resumed".toString());
            }
        } while (!_decision$FU.compareAndSet(this, 0, 2));
        return true;
    }

    /* Access modifiers changed, original: protected|final */
    public final void resumeImpl(Object obj, int i) {
        while (true) {
            Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            StringBuilder stringBuilder;
            if (state$kotlinx_coroutines_core instanceof Cancelling) {
                if (obj instanceof CompletedExceptionally) {
                    Object obj2 = (CompletedExceptionally) obj;
                    if (obj2.cause instanceof CancellationException) {
                        coerceWithException((Cancelling) state$kotlinx_coroutines_core, obj2);
                    } else {
                        Throwable th = obj2.cause;
                        Throwable th2 = ((Cancelling) state$kotlinx_coroutines_core).cancel.cause;
                        if (!((th2 instanceof CancellationException) && th2.getCause() == th)) {
                            ExceptionsKt__ExceptionsKt.addSuppressed(th, th2);
                        }
                        obj2 = new CompletedExceptionally(th);
                    }
                    if (updateStateToFinal((NotCompleted) state$kotlinx_coroutines_core, obj2, i)) {
                        return;
                    }
                } else {
                    if (updateStateToFinal((NotCompleted) state$kotlinx_coroutines_core, ((Cancelling) state$kotlinx_coroutines_core).cancel, i)) {
                        return;
                    }
                }
            } else if (state$kotlinx_coroutines_core instanceof NotCompleted) {
                if (updateStateToFinal((NotCompleted) state$kotlinx_coroutines_core, obj, i)) {
                    return;
                }
            } else if (!(state$kotlinx_coroutines_core instanceof CancelledContinuation)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Already resumed, but proposed with update ");
                stringBuilder.append(obj);
                throw new IllegalStateException(stringBuilder.toString().toString());
            } else if ((obj instanceof NotCompleted) || (obj instanceof CompletedExceptionally)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Unexpected update, state: ");
                stringBuilder.append(state$kotlinx_coroutines_core);
                stringBuilder.append(", update: ");
                stringBuilder.append(obj);
                throw new IllegalStateException(stringBuilder.toString().toString());
            } else {
                return;
            }
        }
    }
}
