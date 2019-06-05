package kotlinx.coroutines.experimental;

import java.util.concurrent.CancellationException;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: Exceptions.kt */
public final class JobCancellationException extends CancellationException {
    public final Job job;

    public JobCancellationException(String str, Throwable th, Job job) {
        Intrinsics.checkParameterIsNotNull(str, "message");
        Intrinsics.checkParameterIsNotNull(job, "job");
        super(str);
        this.job = job;
        if (th != null) {
            initCause(th);
        }
    }

    public Throwable fillInStackTrace() {
        if (!CoroutineContextKt.getDEBUG()) {
            return this;
        }
        Throwable fillInStackTrace = super.fillInStackTrace();
        Intrinsics.checkExpressionValueIsNotNull(fillInStackTrace, "super.fillInStackTrace()");
        return fillInStackTrace;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString());
        stringBuilder.append("; job=");
        stringBuilder.append(this.job);
        return stringBuilder.toString();
    }

    /* JADX WARNING: Missing block: B:9:0x002f, code skipped:
            if (kotlin.jvm.internal.Intrinsics.areEqual(r3.getCause(), getCause()) != false) goto L_0x0034;
     */
    public boolean equals(java.lang.Object r3) {
        /*
        r2 = this;
        r0 = r2;
        r0 = (kotlinx.coroutines.experimental.JobCancellationException) r0;
        if (r3 == r0) goto L_0x0034;
    L_0x0005:
        r0 = r3 instanceof kotlinx.coroutines.experimental.JobCancellationException;
        if (r0 == 0) goto L_0x0032;
    L_0x0009:
        r3 = (kotlinx.coroutines.experimental.JobCancellationException) r3;
        r0 = r3.getMessage();
        r1 = r2.getMessage();
        r0 = kotlin.jvm.internal.Intrinsics.areEqual(r0, r1);
        if (r0 == 0) goto L_0x0032;
    L_0x0019:
        r0 = r3.job;
        r1 = r2.job;
        r0 = kotlin.jvm.internal.Intrinsics.areEqual(r0, r1);
        if (r0 == 0) goto L_0x0032;
    L_0x0023:
        r3 = r3.getCause();
        r0 = r2.getCause();
        r3 = kotlin.jvm.internal.Intrinsics.areEqual(r3, r0);
        if (r3 == 0) goto L_0x0032;
    L_0x0031:
        goto L_0x0034;
    L_0x0032:
        r3 = 0;
        goto L_0x0035;
    L_0x0034:
        r3 = 1;
    L_0x0035:
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.experimental.JobCancellationException.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        String message = getMessage();
        if (message == null) {
            Intrinsics.throwNpe();
        }
        int hashCode = ((message.hashCode() * 31) + this.job.hashCode()) * 31;
        Throwable cause = getCause();
        return hashCode + (cause != null ? cause.hashCode() : 0);
    }
}
