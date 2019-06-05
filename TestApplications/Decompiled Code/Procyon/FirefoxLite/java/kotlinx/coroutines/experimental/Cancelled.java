// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;

public final class Cancelled extends CompletedExceptionally
{
    public Cancelled(final Job job, Throwable t) {
        Intrinsics.checkParameterIsNotNull(job, "job");
        if (t == null) {
            t = new JobCancellationException("Job was cancelled normally", null, job);
        }
        super(t);
    }
}
