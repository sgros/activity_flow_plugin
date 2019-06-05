// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import java.util.concurrent.CancellationException;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

public final class ChildContinuation extends JobCancellationNode<Job>
{
    public final AbstractContinuation<?> child;
    
    public ChildContinuation(final Job job, final AbstractContinuation<?> child) {
        Intrinsics.checkParameterIsNotNull(job, "parent");
        Intrinsics.checkParameterIsNotNull(child, "child");
        super(job);
        this.child = child;
    }
    
    @Override
    public void invoke(final Throwable t) {
        this.child.cancel(this.job.getCancellationException());
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ChildContinuation[");
        sb.append(this.child);
        sb.append(']');
        return sb.toString();
    }
}
