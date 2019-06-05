package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;

/* compiled from: JobSupport.kt */
public final class ChildContinuation extends JobCancellationNode<Job> {
    public final AbstractContinuation<?> child;

    public ChildContinuation(Job job, AbstractContinuation<?> abstractContinuation) {
        Intrinsics.checkParameterIsNotNull(job, "parent");
        Intrinsics.checkParameterIsNotNull(abstractContinuation, "child");
        super(job);
        this.child = abstractContinuation;
    }

    public void invoke(Throwable th) {
        this.child.cancel(this.job.getCancellationException());
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ChildContinuation[");
        stringBuilder.append(this.child);
        stringBuilder.append(']');
        return stringBuilder.toString();
    }
}
