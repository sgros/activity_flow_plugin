// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

public abstract class JobNode<J extends Job> extends CompletionHandlerBase implements DisposableHandle, Incomplete
{
    public final J job;
    
    public JobNode(final J job) {
        Intrinsics.checkParameterIsNotNull(job, "job");
        this.job = job;
    }
    
    @Override
    public void dispose() {
        final Job job = this.job;
        if (job != null) {
            ((JobSupport)job).removeNode$kotlinx_coroutines_core(this);
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.JobSupport");
    }
    
    @Override
    public NodeList getList() {
        return null;
    }
    
    @Override
    public boolean isActive() {
        return true;
    }
}
