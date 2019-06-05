package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;

/* compiled from: JobSupport.kt */
public abstract class JobCancellationNode<J extends Job> extends JobNode<J> {
    public JobCancellationNode(J j) {
        Intrinsics.checkParameterIsNotNull(j, "job");
        super(j);
    }
}
