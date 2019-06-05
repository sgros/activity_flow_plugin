package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;

/* compiled from: JobSupport.kt */
public final class ChildJob extends JobCancellationNode<JobSupport> {
    public final Job childJob;

    public ChildJob(JobSupport jobSupport, Job job) {
        Intrinsics.checkParameterIsNotNull(jobSupport, "parent");
        Intrinsics.checkParameterIsNotNull(job, "childJob");
        super(jobSupport);
        this.childJob = job;
    }

    public void invoke(Throwable th) {
        this.childJob.cancel(((JobSupport) this.job).getCancellationException());
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ChildJob[");
        stringBuilder.append(this.childJob);
        stringBuilder.append(']');
        return stringBuilder.toString();
    }
}
