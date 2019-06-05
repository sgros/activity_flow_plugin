package kotlin.coroutines.experimental.jvm.internal;

import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

/* compiled from: CoroutineImpl.kt */
public abstract class CoroutineImpl extends Lambda<Object> implements Continuation<Object> {
    private final CoroutineContext _context;
    private Continuation<Object> _facade;
    protected Continuation<Object> completion;
    protected int label;

    public abstract Object doResume(Object obj, Throwable th);

    public CoroutineImpl(int i, Continuation<Object> continuation) {
        super(i);
        this.completion = continuation;
        this.label = this.completion != null ? 0 : -1;
        Continuation continuation2 = this.completion;
        this._context = continuation2 != null ? continuation2.getContext() : null;
    }

    public CoroutineContext getContext() {
        CoroutineContext coroutineContext = this._context;
        if (coroutineContext == null) {
            Intrinsics.throwNpe();
        }
        return coroutineContext;
    }

    public final Continuation<Object> getFacade() {
        if (this._facade == null) {
            CoroutineContext coroutineContext = this._context;
            if (coroutineContext == null) {
                Intrinsics.throwNpe();
            }
            this._facade = CoroutineIntrinsics.interceptContinuationIfNeeded(coroutineContext, this);
        }
        Continuation continuation = this._facade;
        if (continuation == null) {
            Intrinsics.throwNpe();
        }
        return continuation;
    }

    public void resume(Object obj) {
        Continuation continuation = this.completion;
        if (continuation == null) {
            Intrinsics.throwNpe();
        }
        try {
            obj = doResume(obj, null);
            if (obj == IntrinsicsKt__IntrinsicsJvmKt.getCOROUTINE_SUSPENDED()) {
                return;
            }
            if (continuation != null) {
                continuation.resume(obj);
                return;
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.experimental.Continuation<kotlin.Any?>");
        } catch (Throwable th) {
            continuation.resumeWithException(th);
        }
    }

    public void resumeWithException(Throwable th) {
        Intrinsics.checkParameterIsNotNull(th, "exception");
        Continuation continuation = this.completion;
        if (continuation == null) {
            Intrinsics.throwNpe();
        }
        try {
            Object doResume = doResume(null, th);
            if (doResume == IntrinsicsKt__IntrinsicsJvmKt.getCOROUTINE_SUSPENDED()) {
                return;
            }
            if (continuation != null) {
                continuation.resume(doResume);
                return;
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.experimental.Continuation<kotlin.Any?>");
        } catch (Throwable th2) {
            continuation.resumeWithException(th2);
        }
    }

    public Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        throw new IllegalStateException("create(Any?;Continuation) has not been overridden");
    }
}
