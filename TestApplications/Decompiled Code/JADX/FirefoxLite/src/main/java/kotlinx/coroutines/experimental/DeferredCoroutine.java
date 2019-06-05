package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: Deferred.kt */
class DeferredCoroutine<T> extends AbstractCoroutine<T> implements Deferred<T> {
    public Object await(Continuation<? super T> continuation) {
        return await$suspendImpl(this, continuation);
    }

    public DeferredCoroutine(CoroutineContext coroutineContext, boolean z) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "parentContext");
        super(coroutineContext, z);
    }

    /* JADX WARNING: Removed duplicated region for block: B:8:0x002d  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x003d  */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x0035  */
    static /* synthetic */ java.lang.Object await$suspendImpl(kotlinx.coroutines.experimental.DeferredCoroutine r4, kotlin.coroutines.experimental.Continuation r5) {
        /*
        r0 = r5 instanceof kotlinx.coroutines.experimental.DeferredCoroutine$await$1;
        if (r0 == 0) goto L_0x0019;
    L_0x0004:
        r0 = r5;
        r0 = (kotlinx.coroutines.experimental.DeferredCoroutine$await$1) r0;
        r1 = r0.getLabel();
        r2 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r1 = r1 & r2;
        if (r1 == 0) goto L_0x0019;
    L_0x0010:
        r5 = r0.getLabel();
        r5 = r5 - r2;
        r0.setLabel(r5);
        goto L_0x001e;
    L_0x0019:
        r0 = new kotlinx.coroutines.experimental.DeferredCoroutine$await$1;
        r0.<init>(r4, r5);
    L_0x001e:
        r5 = r0.data;
        r1 = r0.exception;
        r2 = kotlin.coroutines.experimental.intrinsics.IntrinsicsKt__IntrinsicsJvmKt.getCOROUTINE_SUSPENDED();
        r3 = r0.getLabel();
        switch(r3) {
            case 0: goto L_0x003d;
            case 1: goto L_0x0035;
            default: goto L_0x002d;
        };
    L_0x002d:
        r4 = new java.lang.IllegalStateException;
        r5 = "call to 'resume' before 'invoke' with coroutine";
        r4.<init>(r5);
        throw r4;
    L_0x0035:
        r4 = r0.L$0;
        r4 = (kotlinx.coroutines.experimental.DeferredCoroutine) r4;
        if (r1 != 0) goto L_0x003c;
    L_0x003b:
        goto L_0x004c;
    L_0x003c:
        throw r1;
    L_0x003d:
        if (r1 != 0) goto L_0x004d;
    L_0x003f:
        r0.L$0 = r4;
        r5 = 1;
        r0.setLabel(r5);
        r5 = r4.awaitInternal$kotlinx_coroutines_core(r0);
        if (r5 != r2) goto L_0x004c;
    L_0x004b:
        return r2;
    L_0x004c:
        return r5;
    L_0x004d:
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.experimental.DeferredCoroutine.await$suspendImpl(kotlinx.coroutines.experimental.DeferredCoroutine, kotlin.coroutines.experimental.Continuation):java.lang.Object");
    }
}
