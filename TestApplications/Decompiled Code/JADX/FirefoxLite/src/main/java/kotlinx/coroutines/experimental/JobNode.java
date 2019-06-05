package kotlinx.coroutines.experimental;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: JobSupport.kt */
public abstract class JobNode<J extends Job> extends CompletionHandlerBase implements DisposableHandle, Incomplete {
    public final J job;

    public NodeList getList() {
        return null;
    }

    public boolean isActive() {
        return true;
    }

    public JobNode(J j) {
        Intrinsics.checkParameterIsNotNull(j, "job");
        this.job = j;
    }

    public void dispose() {
        Job job = this.job;
        if (job != null) {
            ((JobSupport) job).removeNode$kotlinx_coroutines_core(this);
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.JobSupport");
    }
}
