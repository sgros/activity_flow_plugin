// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

final class ChildCompletion extends JobNode<Job>
{
    private final ChildJob child;
    private final JobSupport parent;
    private final Object proposedUpdate;
    
    public ChildCompletion(final JobSupport parent, final ChildJob child, final Object proposedUpdate) {
        Intrinsics.checkParameterIsNotNull(parent, "parent");
        Intrinsics.checkParameterIsNotNull(child, "child");
        super(child.childJob);
        this.parent = parent;
        this.child = child;
        this.proposedUpdate = proposedUpdate;
    }
    
    @Override
    public void invoke(final Throwable t) {
        this.parent.continueCompleting$kotlinx_coroutines_core(this.child, this.proposedUpdate);
    }
}
