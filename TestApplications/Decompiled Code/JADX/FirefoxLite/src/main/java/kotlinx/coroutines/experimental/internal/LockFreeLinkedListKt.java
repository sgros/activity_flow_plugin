package kotlinx.coroutines.experimental.internal;

import kotlin.jvm.internal.Intrinsics;

/* compiled from: LockFreeLinkedList.kt */
public final class LockFreeLinkedListKt {
    private static final Object ALREADY_REMOVED = new Symbol("ALREADY_REMOVED");
    private static final Object CONDITION_FALSE = new Symbol("CONDITION_FALSE");
    private static final Object LIST_EMPTY = new Symbol("LIST_EMPTY");
    private static final Object REMOVE_PREPARED = new Symbol("REMOVE_PREPARED");

    public static final Object getCONDITION_FALSE() {
        return CONDITION_FALSE;
    }

    public static final LockFreeLinkedListNode unwrap(Object obj) {
        Intrinsics.checkParameterIsNotNull(obj, "$receiver");
        Removed removed = (Removed) (!(obj instanceof Removed) ? null : obj);
        if (removed != null) {
            LockFreeLinkedListNode lockFreeLinkedListNode = removed.ref;
            if (lockFreeLinkedListNode != null) {
                return lockFreeLinkedListNode;
            }
        }
        return (LockFreeLinkedListNode) obj;
    }
}
