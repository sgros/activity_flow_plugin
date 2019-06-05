package kotlinx.coroutines.experimental.internal;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: LockFreeLinkedList.kt */
public class LockFreeLinkedListNode {
    static final AtomicReferenceFieldUpdater _next$FU = AtomicReferenceFieldUpdater.newUpdater(LockFreeLinkedListNode.class, Object.class, "_next");
    static final AtomicReferenceFieldUpdater _prev$FU = AtomicReferenceFieldUpdater.newUpdater(LockFreeLinkedListNode.class, Object.class, "_prev");
    private static final AtomicReferenceFieldUpdater _removedRef$FU = AtomicReferenceFieldUpdater.newUpdater(LockFreeLinkedListNode.class, Object.class, "_removedRef");
    volatile Object _next = this;
    volatile Object _prev = this;
    private volatile Object _removedRef = null;

    /* compiled from: LockFreeLinkedList.kt */
    public static abstract class CondAddOp extends AtomicOp<LockFreeLinkedListNode> {
        public final LockFreeLinkedListNode newNode;
        public LockFreeLinkedListNode oldNext;

        public CondAddOp(LockFreeLinkedListNode lockFreeLinkedListNode) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "newNode");
            this.newNode = lockFreeLinkedListNode;
        }

        public void complete(LockFreeLinkedListNode lockFreeLinkedListNode, Object obj) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "affected");
            obj = obj == null ? 1 : null;
            Object obj2 = obj != null ? this.newNode : this.oldNext;
            if (obj2 != null && LockFreeLinkedListNode._next$FU.compareAndSet(lockFreeLinkedListNode, this, obj2) && obj != null) {
                lockFreeLinkedListNode = this.newNode;
                LockFreeLinkedListNode lockFreeLinkedListNode2 = this.oldNext;
                if (lockFreeLinkedListNode2 == null) {
                    Intrinsics.throwNpe();
                }
                lockFreeLinkedListNode.finishAdd(lockFreeLinkedListNode2);
            }
        }
    }

    private final Removed removed() {
        Removed removed = (Removed) this._removedRef;
        if (removed != null) {
            return removed;
        }
        removed = new Removed(this);
        _removedRef$FU.lazySet(this, removed);
        return removed;
    }

    public final boolean isRemoved() {
        return getNext() instanceof Removed;
    }

    public final LockFreeLinkedListNode getNextNode() {
        return LockFreeLinkedListKt.unwrap(getNext());
    }

    public final LockFreeLinkedListNode getPrevNode() {
        return LockFreeLinkedListKt.unwrap(getPrev());
    }

    public final boolean addOneIfEmpty(LockFreeLinkedListNode lockFreeLinkedListNode) {
        Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "node");
        _prev$FU.lazySet(lockFreeLinkedListNode, this);
        _next$FU.lazySet(lockFreeLinkedListNode, this);
        while (getNext() == this) {
            if (_next$FU.compareAndSet(this, this, lockFreeLinkedListNode)) {
                lockFreeLinkedListNode.finishAdd(this);
                return true;
            }
        }
        return false;
    }

    public final int tryCondAddNext(LockFreeLinkedListNode lockFreeLinkedListNode, LockFreeLinkedListNode lockFreeLinkedListNode2, CondAddOp condAddOp) {
        Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "node");
        Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode2, "next");
        Intrinsics.checkParameterIsNotNull(condAddOp, "condAdd");
        _prev$FU.lazySet(lockFreeLinkedListNode, this);
        _next$FU.lazySet(lockFreeLinkedListNode, lockFreeLinkedListNode2);
        condAddOp.oldNext = lockFreeLinkedListNode2;
        if (!_next$FU.compareAndSet(this, lockFreeLinkedListNode2, condAddOp)) {
            return 0;
        }
        return condAddOp.perform(this) == null ? 1 : 2;
    }

    public boolean remove() {
        LockFreeLinkedListNode lockFreeLinkedListNode;
        LockFreeLinkedListNode next;
        do {
            next = getNext();
            if ((next instanceof Removed) || next == this) {
                return false;
            }
            if (next != null) {
                lockFreeLinkedListNode = next;
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
            }
        } while (!_next$FU.compareAndSet(this, next, lockFreeLinkedListNode.removed()));
        finishRemove(lockFreeLinkedListNode);
        return true;
    }

    private final void finishRemove(LockFreeLinkedListNode lockFreeLinkedListNode) {
        helpDelete();
        lockFreeLinkedListNode.correctPrev(LockFreeLinkedListKt.unwrap(this._prev), null);
    }

    private final LockFreeLinkedListNode findHead() {
        LockFreeLinkedListNode lockFreeLinkedListNode = this;
        LockFreeLinkedListNode lockFreeLinkedListNode2 = lockFreeLinkedListNode;
        while (!(lockFreeLinkedListNode2 instanceof LockFreeLinkedListHead)) {
            lockFreeLinkedListNode2 = lockFreeLinkedListNode2.getNextNode();
            if ((lockFreeLinkedListNode2 != lockFreeLinkedListNode ? 1 : null) == null) {
                throw new IllegalStateException("Cannot loop to this while looking for list head".toString());
            }
        }
        return lockFreeLinkedListNode2;
    }

    public final void helpDelete() {
        LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) null;
        LockFreeLinkedListNode markPrev = markPrev();
        Object obj = this._next;
        if (obj != null) {
            LockFreeLinkedListNode lockFreeLinkedListNode2 = ((Removed) obj).ref;
            while (true) {
                LockFreeLinkedListNode next;
                Object obj2 = lockFreeLinkedListNode;
                while (true) {
                    Object next2 = lockFreeLinkedListNode2.getNext();
                    if (next2 instanceof Removed) {
                        lockFreeLinkedListNode2.markPrev();
                        lockFreeLinkedListNode2 = ((Removed) next2).ref;
                    } else {
                        next = markPrev.getNext();
                        if (next instanceof Removed) {
                            if (obj2 != null) {
                                break;
                            }
                            markPrev = LockFreeLinkedListKt.unwrap(markPrev._prev);
                        } else if (next != this) {
                            if (next != null) {
                                LockFreeLinkedListNode lockFreeLinkedListNode3 = next;
                                if (lockFreeLinkedListNode3 != lockFreeLinkedListNode2) {
                                    LockFreeLinkedListNode lockFreeLinkedListNode4 = lockFreeLinkedListNode3;
                                    obj2 = markPrev;
                                    markPrev = lockFreeLinkedListNode4;
                                } else {
                                    return;
                                }
                            }
                            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
                        } else if (_next$FU.compareAndSet(markPrev, this, lockFreeLinkedListNode2)) {
                            return;
                        }
                    }
                }
                markPrev.markPrev();
                _next$FU.compareAndSet(obj2, markPrev, ((Removed) next).ref);
                markPrev = obj2;
            }
        } else {
            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Removed");
        }
    }

    private final LockFreeLinkedListNode correctPrev(LockFreeLinkedListNode lockFreeLinkedListNode, OpDescriptor opDescriptor) {
        LockFreeLinkedListNode lockFreeLinkedListNode2 = null;
        while (true) {
            OpDescriptor opDescriptor2;
            Object obj = lockFreeLinkedListNode2;
            while (true) {
                opDescriptor2 = lockFreeLinkedListNode._next;
                if (opDescriptor2 == opDescriptor) {
                    return lockFreeLinkedListNode;
                }
                if (opDescriptor2 instanceof OpDescriptor) {
                    opDescriptor2.perform(lockFreeLinkedListNode);
                } else if (!(opDescriptor2 instanceof Removed)) {
                    LockFreeLinkedListNode lockFreeLinkedListNode3 = this._prev;
                    if (lockFreeLinkedListNode3 instanceof Removed) {
                        return null;
                    }
                    if (opDescriptor2 != this) {
                        if (opDescriptor2 != null) {
                            obj = lockFreeLinkedListNode;
                            lockFreeLinkedListNode = (LockFreeLinkedListNode) opDescriptor2;
                        } else {
                            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
                        }
                    } else if (lockFreeLinkedListNode3 == lockFreeLinkedListNode) {
                        return null;
                    } else {
                        if (_prev$FU.compareAndSet(this, lockFreeLinkedListNode3, lockFreeLinkedListNode) && !(lockFreeLinkedListNode._prev instanceof Removed)) {
                            return null;
                        }
                    }
                } else if (obj != null) {
                    break;
                } else {
                    lockFreeLinkedListNode = LockFreeLinkedListKt.unwrap(lockFreeLinkedListNode._prev);
                }
            }
            lockFreeLinkedListNode.markPrev();
            _next$FU.compareAndSet(obj, lockFreeLinkedListNode, ((Removed) opDescriptor2).ref);
            lockFreeLinkedListNode = obj;
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getClass().getSimpleName());
        stringBuilder.append('@');
        stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
        return stringBuilder.toString();
    }

    public final Object getNext() {
        while (true) {
            Object obj = this._next;
            if (!(obj instanceof OpDescriptor)) {
                return obj;
            }
            ((OpDescriptor) obj).perform(this);
        }
    }

    public final Object getPrev() {
        while (true) {
            Object obj = this._prev;
            if (obj instanceof Removed) {
                return obj;
            }
            if (obj != null) {
                LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) obj;
                if (lockFreeLinkedListNode.getNext() == this) {
                    return obj;
                }
                correctPrev(lockFreeLinkedListNode, null);
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
            }
        }
    }

    private final void finishAdd(LockFreeLinkedListNode lockFreeLinkedListNode) {
        Object obj;
        do {
            obj = lockFreeLinkedListNode._prev;
            if ((obj instanceof Removed) || getNext() != lockFreeLinkedListNode) {
                return;
            }
        } while (!_prev$FU.compareAndSet(lockFreeLinkedListNode, obj, this));
        if (getNext() instanceof Removed) {
            if (obj != null) {
                lockFreeLinkedListNode.correctPrev((LockFreeLinkedListNode) obj, null);
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
            }
        }
    }

    private final LockFreeLinkedListNode markPrev() {
        LockFreeLinkedListNode lockFreeLinkedListNode;
        LockFreeLinkedListNode findHead;
        do {
            lockFreeLinkedListNode = this._prev;
            if (lockFreeLinkedListNode instanceof Removed) {
                return ((Removed) lockFreeLinkedListNode).ref;
            }
            if (lockFreeLinkedListNode == this) {
                findHead = findHead();
            } else if (lockFreeLinkedListNode != null) {
                findHead = lockFreeLinkedListNode;
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
            }
        } while (!_prev$FU.compareAndSet(this, lockFreeLinkedListNode, findHead.removed()));
        return lockFreeLinkedListNode;
    }
}
