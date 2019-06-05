package kotlinx.coroutines.experimental.internal;

import kotlin.jvm.internal.Intrinsics;

/* compiled from: LockFreeLinkedList.kt */
final class Removed {
    public final LockFreeLinkedListNode ref;

    public Removed(LockFreeLinkedListNode lockFreeLinkedListNode) {
        Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "ref");
        this.ref = lockFreeLinkedListNode;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Removed[");
        stringBuilder.append(this.ref);
        stringBuilder.append(']');
        return stringBuilder.toString();
    }
}
