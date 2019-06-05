// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental.internal;

import kotlin.jvm.internal.Intrinsics;
import kotlin.TypeCastException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class LockFreeLinkedListNode
{
    static final AtomicReferenceFieldUpdater _next$FU;
    static final AtomicReferenceFieldUpdater _prev$FU;
    private static final AtomicReferenceFieldUpdater _removedRef$FU;
    volatile Object _next;
    volatile Object _prev;
    private volatile Object _removedRef;
    
    static {
        _next$FU = AtomicReferenceFieldUpdater.newUpdater(LockFreeLinkedListNode.class, Object.class, "_next");
        _prev$FU = AtomicReferenceFieldUpdater.newUpdater(LockFreeLinkedListNode.class, Object.class, "_prev");
        _removedRef$FU = AtomicReferenceFieldUpdater.newUpdater(LockFreeLinkedListNode.class, Object.class, "_removedRef");
    }
    
    public LockFreeLinkedListNode() {
        this._next = this;
        this._prev = this;
        this._removedRef = null;
    }
    
    private final LockFreeLinkedListNode correctPrev(LockFreeLinkedListNode unwrap, final OpDescriptor opDescriptor) {
        final LockFreeLinkedListNode lockFreeLinkedListNode = null;
        while (true) {
            LockFreeLinkedListNode lockFreeLinkedListNode2 = lockFreeLinkedListNode;
            while (true) {
                final Object next = unwrap._next;
                if (next == opDescriptor) {
                    return unwrap;
                }
                if (next instanceof OpDescriptor) {
                    ((OpDescriptor)next).perform(unwrap);
                }
                else if (next instanceof Removed) {
                    if (lockFreeLinkedListNode2 != null) {
                        unwrap.markPrev();
                        LockFreeLinkedListNode._next$FU.compareAndSet(lockFreeLinkedListNode2, unwrap, ((Removed)next).ref);
                        unwrap = lockFreeLinkedListNode2;
                        break;
                    }
                    unwrap = LockFreeLinkedListKt.unwrap(unwrap._prev);
                }
                else {
                    final Object prev = this._prev;
                    if (prev instanceof Removed) {
                        return null;
                    }
                    if (next != this) {
                        if (next == null) {
                            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
                        }
                        final LockFreeLinkedListNode lockFreeLinkedListNode3 = (LockFreeLinkedListNode)next;
                        lockFreeLinkedListNode2 = unwrap;
                        unwrap = lockFreeLinkedListNode3;
                    }
                    else {
                        if (prev == unwrap) {
                            return null;
                        }
                        if (LockFreeLinkedListNode._prev$FU.compareAndSet(this, prev, unwrap) && !(unwrap._prev instanceof Removed)) {
                            return null;
                        }
                        continue;
                    }
                }
            }
        }
    }
    
    private final LockFreeLinkedListNode findHead() {
        LockFreeLinkedListNode nextNode;
        final LockFreeLinkedListNode lockFreeLinkedListNode = nextNode = this;
        while (!(nextNode instanceof LockFreeLinkedListHead)) {
            nextNode = nextNode.getNextNode();
            if (nextNode != lockFreeLinkedListNode) {
                continue;
            }
            throw new IllegalStateException("Cannot loop to this while looking for list head".toString());
        }
        return nextNode;
    }
    
    private final void finishAdd(final LockFreeLinkedListNode lockFreeLinkedListNode) {
        Object prev;
        do {
            prev = lockFreeLinkedListNode._prev;
            if (!(prev instanceof Removed) && this.getNext() == lockFreeLinkedListNode) {
                continue;
            }
            return;
        } while (!LockFreeLinkedListNode._prev$FU.compareAndSet(lockFreeLinkedListNode, prev, this));
        if (this.getNext() instanceof Removed) {
            if (prev == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
            }
            lockFreeLinkedListNode.correctPrev((LockFreeLinkedListNode)prev, null);
        }
    }
    
    private final void finishRemove(final LockFreeLinkedListNode lockFreeLinkedListNode) {
        this.helpDelete();
        lockFreeLinkedListNode.correctPrev(LockFreeLinkedListKt.unwrap(this._prev), null);
    }
    
    private final LockFreeLinkedListNode markPrev() {
        Object prev;
        LockFreeLinkedListNode head;
        do {
            prev = this._prev;
            if (prev instanceof Removed) {
                return ((Removed)prev).ref;
            }
            if (prev == this) {
                head = this.findHead();
            }
            else {
                if (prev == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
                }
                head = (LockFreeLinkedListNode)prev;
            }
        } while (!LockFreeLinkedListNode._prev$FU.compareAndSet(this, prev, head.removed()));
        return (LockFreeLinkedListNode)prev;
    }
    
    private final Removed removed() {
        Removed removed = (Removed)this._removedRef;
        if (removed == null) {
            removed = new Removed(this);
            LockFreeLinkedListNode._removedRef$FU.lazySet(this, removed);
        }
        return removed;
    }
    
    public final boolean addOneIfEmpty(final LockFreeLinkedListNode lockFreeLinkedListNode) {
        Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "node");
        LockFreeLinkedListNode._prev$FU.lazySet(lockFreeLinkedListNode, this);
        LockFreeLinkedListNode._next$FU.lazySet(lockFreeLinkedListNode, this);
        while (this.getNext() == this) {
            if (LockFreeLinkedListNode._next$FU.compareAndSet(this, this, lockFreeLinkedListNode)) {
                lockFreeLinkedListNode.finishAdd(this);
                return true;
            }
        }
        return false;
    }
    
    public final Object getNext() {
        Object next;
        while (true) {
            next = this._next;
            if (!(next instanceof OpDescriptor)) {
                break;
            }
            ((OpDescriptor)next).perform(this);
        }
        return next;
    }
    
    public final LockFreeLinkedListNode getNextNode() {
        return LockFreeLinkedListKt.unwrap(this.getNext());
    }
    
    public final Object getPrev() {
        while (true) {
            final Object prev = this._prev;
            if (prev instanceof Removed) {
                return prev;
            }
            if (prev == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
            }
            final LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode)prev;
            if (lockFreeLinkedListNode.getNext() == this) {
                return prev;
            }
            this.correctPrev(lockFreeLinkedListNode, null);
        }
    }
    
    public final LockFreeLinkedListNode getPrevNode() {
        return LockFreeLinkedListKt.unwrap(this.getPrev());
    }
    
    public final void helpDelete() {
        final LockFreeLinkedListNode lockFreeLinkedListNode = null;
        LockFreeLinkedListNode lockFreeLinkedListNode2 = this.markPrev();
        final Object next = this._next;
        if (next == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Removed");
        }
        LockFreeLinkedListNode lockFreeLinkedListNode3 = ((Removed)next).ref;
        while (true) {
            LockFreeLinkedListNode lockFreeLinkedListNode4 = lockFreeLinkedListNode;
            while (true) {
                final Object next2 = lockFreeLinkedListNode3.getNext();
                if (next2 instanceof Removed) {
                    lockFreeLinkedListNode3.markPrev();
                    lockFreeLinkedListNode3 = ((Removed)next2).ref;
                }
                else {
                    final Object next3 = lockFreeLinkedListNode2.getNext();
                    if (next3 instanceof Removed) {
                        if (lockFreeLinkedListNode4 != null) {
                            lockFreeLinkedListNode2.markPrev();
                            LockFreeLinkedListNode._next$FU.compareAndSet(lockFreeLinkedListNode4, lockFreeLinkedListNode2, ((Removed)next3).ref);
                            lockFreeLinkedListNode2 = lockFreeLinkedListNode4;
                            break;
                        }
                        lockFreeLinkedListNode2 = LockFreeLinkedListKt.unwrap(lockFreeLinkedListNode2._prev);
                    }
                    else if (next3 != this) {
                        if (next3 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
                        }
                        final LockFreeLinkedListNode lockFreeLinkedListNode5 = (LockFreeLinkedListNode)next3;
                        if (lockFreeLinkedListNode5 == lockFreeLinkedListNode3) {
                            return;
                        }
                        lockFreeLinkedListNode4 = lockFreeLinkedListNode2;
                        lockFreeLinkedListNode2 = lockFreeLinkedListNode5;
                    }
                    else {
                        if (LockFreeLinkedListNode._next$FU.compareAndSet(lockFreeLinkedListNode2, this, lockFreeLinkedListNode3)) {
                            return;
                        }
                        continue;
                    }
                }
            }
        }
    }
    
    public final boolean isRemoved() {
        return this.getNext() instanceof Removed;
    }
    
    public boolean remove() {
        Object next;
        LockFreeLinkedListNode lockFreeLinkedListNode;
        do {
            next = this.getNext();
            if (next instanceof Removed) {
                return false;
            }
            if (next == this) {
                return false;
            }
            if (next == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
            }
            lockFreeLinkedListNode = (LockFreeLinkedListNode)next;
        } while (!LockFreeLinkedListNode._next$FU.compareAndSet(this, next, lockFreeLinkedListNode.removed()));
        this.finishRemove(lockFreeLinkedListNode);
        return true;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName());
        sb.append('@');
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        return sb.toString();
    }
    
    public final int tryCondAddNext(final LockFreeLinkedListNode lockFreeLinkedListNode, final LockFreeLinkedListNode oldNext, final CondAddOp condAddOp) {
        Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "node");
        Intrinsics.checkParameterIsNotNull(oldNext, "next");
        Intrinsics.checkParameterIsNotNull(condAddOp, "condAdd");
        LockFreeLinkedListNode._prev$FU.lazySet(lockFreeLinkedListNode, this);
        LockFreeLinkedListNode._next$FU.lazySet(lockFreeLinkedListNode, oldNext);
        condAddOp.oldNext = oldNext;
        if (!LockFreeLinkedListNode._next$FU.compareAndSet(this, oldNext, condAddOp)) {
            return 0;
        }
        int n;
        if (condAddOp.perform(this) == null) {
            n = 1;
        }
        else {
            n = 2;
        }
        return n;
    }
    
    public abstract static class CondAddOp extends AtomicOp<LockFreeLinkedListNode>
    {
        public final LockFreeLinkedListNode newNode;
        public LockFreeLinkedListNode oldNext;
        
        public CondAddOp(final LockFreeLinkedListNode newNode) {
            Intrinsics.checkParameterIsNotNull(newNode, "newNode");
            this.newNode = newNode;
        }
        
        @Override
        public void complete(LockFreeLinkedListNode oldNext, final Object o) {
            Intrinsics.checkParameterIsNotNull(oldNext, "affected");
            final boolean b = o == null;
            LockFreeLinkedListNode lockFreeLinkedListNode;
            if (b) {
                lockFreeLinkedListNode = this.newNode;
            }
            else {
                lockFreeLinkedListNode = this.oldNext;
            }
            if (lockFreeLinkedListNode != null && LockFreeLinkedListNode._next$FU.compareAndSet(oldNext, this, lockFreeLinkedListNode) && b) {
                final LockFreeLinkedListNode newNode = this.newNode;
                oldNext = this.oldNext;
                if (oldNext == null) {
                    Intrinsics.throwNpe();
                }
                newNode.finishAdd(oldNext);
            }
        }
    }
}
