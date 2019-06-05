// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import java.util.concurrent.CancellationException;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

public final class ChildJob extends JobCancellationNode<JobSupport>
{
    public final Job childJob;
    
    public ChildJob(final JobSupport jobSupport, final Job childJob) {
        Intrinsics.checkParameterIsNotNull(jobSupport, "parent");
        Intrinsics.checkParameterIsNotNull(childJob, "childJob");
        super(jobSupport);
        this.childJob = childJob;
    }
    
    @Override
    public void invoke(final Throwable t) {
        this.childJob.cancel(((JobSupport)this.job).getCancellationException());
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ChildJob[");
        sb.append(this.childJob);
        sb.append(']');
        return sb.toString();
    }
}
