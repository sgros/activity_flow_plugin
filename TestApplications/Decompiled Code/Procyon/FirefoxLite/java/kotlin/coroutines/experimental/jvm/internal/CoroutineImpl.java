// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.coroutines.experimental.jvm.internal;

import kotlin.TypeCastException;
import kotlin.coroutines.experimental.intrinsics.IntrinsicsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.Unit;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.internal.Lambda;

public abstract class CoroutineImpl extends Lambda<Object> implements Continuation<Object>
{
    private final CoroutineContext _context;
    private Continuation<Object> _facade;
    protected Continuation<Object> completion;
    protected int label;
    
    public CoroutineImpl(int label, final Continuation<Object> completion) {
        super(label);
        this.completion = completion;
        if (this.completion != null) {
            label = 0;
        }
        else {
            label = -1;
        }
        this.label = label;
        final Continuation<Object> completion2 = this.completion;
        CoroutineContext context;
        if (completion2 != null) {
            context = completion2.getContext();
        }
        else {
            context = null;
        }
        this._context = context;
    }
    
    public Continuation<Unit> create(final Object o, final Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        throw new IllegalStateException("create(Any?;Continuation) has not been overridden");
    }
    
    protected abstract Object doResume(final Object p0, final Throwable p1);
    
    @Override
    public CoroutineContext getContext() {
        final CoroutineContext context = this._context;
        if (context == null) {
            Intrinsics.throwNpe();
        }
        return context;
    }
    
    public final Continuation<Object> getFacade() {
        if (this._facade == null) {
            final CoroutineContext context = this._context;
            if (context == null) {
                Intrinsics.throwNpe();
            }
            this._facade = CoroutineIntrinsics.interceptContinuationIfNeeded(context, this);
        }
        final Continuation<Object> facade = this._facade;
        if (facade == null) {
            Intrinsics.throwNpe();
        }
        return facade;
    }
    
    @Override
    public void resume(Object doResume) {
        final Continuation<Object> completion = this.completion;
        if (completion == null) {
            Intrinsics.throwNpe();
        }
        try {
            doResume = this.doResume(doResume, null);
            if (doResume != IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                if (completion == null) {
                    doResume = new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.experimental.Continuation<kotlin.Any?>");
                    throw doResume;
                }
                completion.resume(doResume);
            }
        }
        catch (Throwable t) {
            completion.resumeWithException(t);
        }
    }
    
    @Override
    public void resumeWithException(final Throwable t) {
        Intrinsics.checkParameterIsNotNull(t, "exception");
        final Continuation<Object> completion = this.completion;
        if (completion == null) {
            Intrinsics.throwNpe();
        }
        try {
            final Object doResume = this.doResume(null, t);
            if (doResume != IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                if (completion == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.experimental.Continuation<kotlin.Any?>");
                }
                completion.resume(doResume);
            }
        }
        catch (Throwable t) {
            completion.resumeWithException(t);
        }
    }
}
