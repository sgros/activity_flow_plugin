package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;

/* compiled from: JobSupport.kt */
final class ChildCompletion extends JobNode<Job> {
    private final ChildJob child;
    private final JobSupport parent;
    private final Object proposedUpdate;

    public ChildCompletion(JobSupport jobSupport, ChildJob childJob, Object obj) {
        Intrinsics.checkParameterIsNotNull(jobSupport, "parent");
        Intrinsics.checkParameterIsNotNull(childJob, "child");
        super(childJob.childJob);
        this.parent = jobSupport;
        this.child = childJob;
        this.proposedUpdate = obj;
    }

    public void invoke(Throwable th) {
        this.parent.continueCompleting$kotlinx_coroutines_core(this.child, this.proposedUpdate);
    }
}
