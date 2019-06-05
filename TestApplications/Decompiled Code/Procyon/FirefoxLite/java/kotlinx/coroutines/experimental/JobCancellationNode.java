// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;

public abstract class JobCancellationNode<J extends Job> extends JobNode<J>
{
    public JobCancellationNode(final J n) {
        Intrinsics.checkParameterIsNotNull(n, "job");
        super(n);
    }
}
