package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;

/* compiled from: CompletedExceptionally.kt */
public final class Cancelled extends CompletedExceptionally {
    public Cancelled(Job job, Throwable th) {
        Intrinsics.checkParameterIsNotNull(job, "job");
        if (th == null) {
            th = new JobCancellationException("Job was cancelled normally", null, job);
        }
        super(th);
    }
}
