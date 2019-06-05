package kotlinx.coroutines.experimental;

import java.util.concurrent.atomic.AtomicLong;
import kotlin.coroutines.experimental.ContinuationInterceptor;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: CoroutineContext.kt */
public final class CoroutineContextKt {
    private static final AtomicLong COROUTINE_ID = new AtomicLong();
    private static final boolean DEBUG;
    private static final CoroutineDispatcher DefaultDispatcher = CommonPool.INSTANCE;

    public static final boolean getDEBUG() {
        return DEBUG;
    }

    /* JADX WARNING: Missing block: B:14:0x0025, code skipped:
            if (r0.equals("auto") != false) goto L_0x0027;
     */
    /* JADX WARNING: Missing block: B:20:0x003e, code skipped:
            if (r0.equals("on") != false) goto L_0x0049;
     */
    /* JADX WARNING: Missing block: B:22:0x0047, code skipped:
            if (r0.equals("") != false) goto L_0x0049;
     */
    static {
        /*
        r0 = "kotlinx.coroutines.debug";
        r0 = java.lang.System.getProperty(r0);	 Catch:{ SecurityException -> 0x0007 }
        goto L_0x0008;
    L_0x0007:
        r0 = 0;
    L_0x0008:
        if (r0 != 0) goto L_0x000b;
    L_0x000a:
        goto L_0x0027;
    L_0x000b:
        r1 = r0.hashCode();
        if (r1 == 0) goto L_0x0041;
    L_0x0011:
        r2 = 3551; // 0xddf float:4.976E-42 double:1.7544E-320;
        if (r1 == r2) goto L_0x0038;
    L_0x0015:
        r2 = 109935; // 0x1ad6f float:1.54052E-40 double:5.4315E-319;
        if (r1 == r2) goto L_0x002e;
    L_0x001a:
        r2 = 3005871; // 0x2dddaf float:4.212122E-39 double:1.4850976E-317;
        if (r1 != r2) goto L_0x005a;
    L_0x001f:
        r1 = "auto";
        r1 = r0.equals(r1);
        if (r1 == 0) goto L_0x005a;
    L_0x0027:
        r0 = kotlinx.coroutines.experimental.CoroutineId.class;
        r0 = r0.desiredAssertionStatus();
        goto L_0x004a;
    L_0x002e:
        r1 = "off";
        r1 = r0.equals(r1);
        if (r1 == 0) goto L_0x005a;
    L_0x0036:
        r0 = 0;
        goto L_0x004a;
    L_0x0038:
        r1 = "on";
        r1 = r0.equals(r1);
        if (r1 == 0) goto L_0x005a;
    L_0x0040:
        goto L_0x0049;
    L_0x0041:
        r1 = "";
        r1 = r0.equals(r1);
        if (r1 == 0) goto L_0x005a;
    L_0x0049:
        r0 = 1;
    L_0x004a:
        DEBUG = r0;
        r0 = new java.util.concurrent.atomic.AtomicLong;
        r0.<init>();
        COROUTINE_ID = r0;
        r0 = kotlinx.coroutines.experimental.CommonPool.INSTANCE;
        r0 = (kotlinx.coroutines.experimental.CoroutineDispatcher) r0;
        DefaultDispatcher = r0;
        return;
    L_0x005a:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "System property 'kotlinx.coroutines.debug' has unrecognized value '";
        r1.append(r2);
        r1.append(r0);
        r0 = 39;
        r1.append(r0);
        r0 = r1.toString();
        r1 = new java.lang.IllegalStateException;
        r0 = r0.toString();
        r1.<init>(r0);
        r1 = (java.lang.Throwable) r1;
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.experimental.CoroutineContextKt.<clinit>():void");
    }

    public static final CoroutineDispatcher getDefaultDispatcher() {
        return DefaultDispatcher;
    }

    public static final CoroutineContext newCoroutineContext(CoroutineContext coroutineContext, Job job) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        CoroutineContext plus = DEBUG ? coroutineContext.plus(new CoroutineId(COROUTINE_ID.incrementAndGet())) : coroutineContext;
        if (job != null) {
            plus = plus.plus(job);
        }
        return (coroutineContext == DefaultDispatcher || coroutineContext.get(ContinuationInterceptor.Key) != null) ? plus : plus.plus(DefaultDispatcher);
    }

    /* JADX WARNING: Missing block: B:8:0x0027, code skipped:
            if (r7 != null) goto L_0x002c;
     */
    public static final java.lang.String updateThreadContext(kotlin.coroutines.experimental.CoroutineContext r7) {
        /*
        r0 = "$receiver";
        kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r7, r0);
        r0 = DEBUG;
        r1 = 0;
        if (r0 != 0) goto L_0x000b;
    L_0x000a:
        return r1;
    L_0x000b:
        r0 = kotlinx.coroutines.experimental.CoroutineId.Key;
        r0 = (kotlin.coroutines.experimental.CoroutineContext.Key) r0;
        r0 = r7.get(r0);
        r0 = (kotlinx.coroutines.experimental.CoroutineId) r0;
        if (r0 == 0) goto L_0x006d;
    L_0x0017:
        r1 = kotlinx.coroutines.experimental.CoroutineName.Key;
        r1 = (kotlin.coroutines.experimental.CoroutineContext.Key) r1;
        r7 = r7.get(r1);
        r7 = (kotlinx.coroutines.experimental.CoroutineName) r7;
        if (r7 == 0) goto L_0x002a;
    L_0x0023:
        r7 = r7.getName();
        if (r7 == 0) goto L_0x002a;
    L_0x0029:
        goto L_0x002c;
    L_0x002a:
        r7 = "coroutine";
    L_0x002c:
        r1 = java.lang.Thread.currentThread();
        r2 = "currentThread";
        kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r1, r2);
        r2 = r1.getName();
        r3 = r2.length();
        r4 = r7.length();
        r3 = r3 + r4;
        r3 = r3 + 10;
        r4 = new java.lang.StringBuilder;
        r4.<init>(r3);
        r4.append(r2);
        r3 = " @";
        r4.append(r3);
        r4.append(r7);
        r7 = 35;
        r4.append(r7);
        r5 = r0.getId();
        r4.append(r5);
        r7 = r4.toString();
        r0 = "StringBuilder(capacity).…builderAction).toString()";
        kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r7, r0);
        r1.setName(r7);
        return r2;
    L_0x006d:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.experimental.CoroutineContextKt.updateThreadContext(kotlin.coroutines.experimental.CoroutineContext):java.lang.String");
    }

    /* JADX WARNING: Missing block: B:8:0x0027, code skipped:
            if (r4 != null) goto L_0x002c;
     */
    public static final java.lang.String getCoroutineName(kotlin.coroutines.experimental.CoroutineContext r4) {
        /*
        r0 = "$receiver";
        kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r4, r0);
        r0 = DEBUG;
        r1 = 0;
        if (r0 != 0) goto L_0x000b;
    L_0x000a:
        return r1;
    L_0x000b:
        r0 = kotlinx.coroutines.experimental.CoroutineId.Key;
        r0 = (kotlin.coroutines.experimental.CoroutineContext.Key) r0;
        r0 = r4.get(r0);
        r0 = (kotlinx.coroutines.experimental.CoroutineId) r0;
        if (r0 == 0) goto L_0x0045;
    L_0x0017:
        r1 = kotlinx.coroutines.experimental.CoroutineName.Key;
        r1 = (kotlin.coroutines.experimental.CoroutineContext.Key) r1;
        r4 = r4.get(r1);
        r4 = (kotlinx.coroutines.experimental.CoroutineName) r4;
        if (r4 == 0) goto L_0x002a;
    L_0x0023:
        r4 = r4.getName();
        if (r4 == 0) goto L_0x002a;
    L_0x0029:
        goto L_0x002c;
    L_0x002a:
        r4 = "coroutine";
    L_0x002c:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r1.append(r4);
        r4 = 35;
        r1.append(r4);
        r2 = r0.getId();
        r1.append(r2);
        r4 = r1.toString();
        return r4;
    L_0x0045:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.experimental.CoroutineContextKt.getCoroutineName(kotlin.coroutines.experimental.CoroutineContext):java.lang.String");
    }

    public static final void restoreThreadContext(String str) {
        if (str != null) {
            Thread currentThread = Thread.currentThread();
            Intrinsics.checkExpressionValueIsNotNull(currentThread, "Thread.currentThread()");
            currentThread.setName(str);
        }
    }
}
