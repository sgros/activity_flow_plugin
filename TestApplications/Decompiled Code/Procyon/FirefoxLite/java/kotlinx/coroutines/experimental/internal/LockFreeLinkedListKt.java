// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental.internal;

import kotlin.jvm.internal.Intrinsics;

public final class LockFreeLinkedListKt
{
    private static final Object ALREADY_REMOVED;
    private static final Object CONDITION_FALSE;
    private static final Object LIST_EMPTY;
    private static final Object REMOVE_PREPARED;
    
    static {
        CONDITION_FALSE = new Symbol("CONDITION_FALSE");
        ALREADY_REMOVED = new Symbol("ALREADY_REMOVED");
        LIST_EMPTY = new Symbol("LIST_EMPTY");
        REMOVE_PREPARED = new Symbol("REMOVE_PREPARED");
    }
    
    public static final Object getCONDITION_FALSE() {
        return LockFreeLinkedListKt.CONDITION_FALSE;
    }
    
    public static final LockFreeLinkedListNode unwrap(final Object o) {
        Intrinsics.checkParameterIsNotNull(o, "$receiver");
        Object o2;
        if (!(o instanceof Removed)) {
            o2 = null;
        }
        else {
            o2 = o;
        }
        final Removed removed = (Removed)o2;
        if (removed != null) {
            final LockFreeLinkedListNode ref = removed.ref;
            if (ref != null) {
                return ref;
            }
        }
        return (LockFreeLinkedListNode)o;
    }
}
