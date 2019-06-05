// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListHead;

public final class NodeList extends LockFreeLinkedListHead implements Incomplete
{
    private static final AtomicIntegerFieldUpdater _active$FU;
    private volatile int _active;
    
    static {
        _active$FU = AtomicIntegerFieldUpdater.newUpdater(NodeList.class, "_active");
    }
    
    public NodeList(final boolean active) {
        this._active = (active ? 1 : 0);
    }
    
    @Override
    public NodeList getList() {
        return this;
    }
    
    @Override
    public boolean isActive() {
        return this._active != 0;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("List");
        String str;
        if (this.isActive()) {
            str = "{Active}";
        }
        else {
            str = "{New}";
        }
        sb.append(str);
        sb.append("[");
        final Object next = this.getNext();
        if (next != null) {
            LockFreeLinkedListNode nextNode = (LockFreeLinkedListNode)next;
            int n = 1;
            while (Intrinsics.areEqual(nextNode, this) ^ true) {
                int n2 = n;
                if (nextNode instanceof JobNode) {
                    final JobNode obj = (JobNode)nextNode;
                    if (n != 0) {
                        n = 0;
                    }
                    else {
                        sb.append(", ");
                    }
                    sb.append(obj);
                    n2 = n;
                }
                nextNode = nextNode.getNextNode();
                n = n2;
            }
            sb.append("]");
            final String string = sb.toString();
            Intrinsics.checkExpressionValueIsNotNull(string, "StringBuilder().apply(builderAction).toString()");
            return string;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
    }
    
    public final int tryMakeActive() {
        if (this._active != 0) {
            return 0;
        }
        if (NodeList._active$FU.compareAndSet(this, 0, 1)) {
            return 1;
        }
        return -1;
    }
}
