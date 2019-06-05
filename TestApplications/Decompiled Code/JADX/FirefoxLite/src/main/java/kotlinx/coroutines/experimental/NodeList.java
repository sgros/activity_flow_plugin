package kotlinx.coroutines.experimental;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListHead;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode;

/* compiled from: JobSupport.kt */
public final class NodeList extends LockFreeLinkedListHead implements Incomplete {
    private static final AtomicIntegerFieldUpdater _active$FU = AtomicIntegerFieldUpdater.newUpdater(NodeList.class, "_active");
    private volatile int _active;

    public NodeList getList() {
        return this;
    }

    public NodeList(boolean z) {
        this._active = z;
    }

    public boolean isActive() {
        return this._active != 0;
    }

    public final int tryMakeActive() {
        if (this._active != 0) {
            return 0;
        }
        return _active$FU.compareAndSet(this, 0, 1) ? 1 : -1;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("List");
        stringBuilder.append(isActive() ? "{Active}" : "{New}");
        stringBuilder.append("[");
        Object next = getNext();
        if (next != null) {
            Object obj = 1;
            for (next = (LockFreeLinkedListNode) next; (Intrinsics.areEqual(next, this) ^ 1) != 0; next = next.getNextNode()) {
                if (next instanceof JobNode) {
                    JobNode jobNode = (JobNode) next;
                    if (obj != null) {
                        obj = null;
                    } else {
                        stringBuilder.append(", ");
                    }
                    stringBuilder.append(jobNode);
                }
            }
            stringBuilder.append("]");
            String stringBuilder2 = stringBuilder.toString();
            Intrinsics.checkExpressionValueIsNotNull(stringBuilder2, "StringBuilder().apply(builderAction).toString()");
            return stringBuilder2;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
    }
}
