package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.jvm.internal.CoroutineImpl;

/* compiled from: Deferred.kt */
final class DeferredCoroutine$await$1 extends CoroutineImpl {
    Object L$0;
    /* synthetic */ Object data;
    /* synthetic */ Throwable exception;
    final /* synthetic */ DeferredCoroutine this$0;

    DeferredCoroutine$await$1(DeferredCoroutine deferredCoroutine, Continuation continuation) {
        this.this$0 = deferredCoroutine;
        super(0, continuation);
    }

    public final Object doResume(Object obj, Throwable th) {
        this.data = obj;
        this.exception = th;
        this.label |= Integer.MIN_VALUE;
        return DeferredCoroutine.await$suspendImpl(this.this$0, this);
    }

    /* Access modifiers changed, original: final|synthetic */
    public final /* synthetic */ int getLabel() {
        return this.label;
    }

    /* Access modifiers changed, original: final|synthetic */
    public final /* synthetic */ void setLabel(int i) {
        this.label = i;
    }
}
