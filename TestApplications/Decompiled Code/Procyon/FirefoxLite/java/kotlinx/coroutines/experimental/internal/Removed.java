// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental.internal;

import kotlin.jvm.internal.Intrinsics;

final class Removed
{
    public final LockFreeLinkedListNode ref;
    
    public Removed(final LockFreeLinkedListNode ref) {
        Intrinsics.checkParameterIsNotNull(ref, "ref");
        this.ref = ref;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Removed[");
        sb.append(this.ref);
        sb.append(']');
        return sb.toString();
    }
}
