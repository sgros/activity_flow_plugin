package kotlinx.coroutines.experimental.internal;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/* compiled from: Atomic.kt */
public abstract class AtomicOp<T> extends OpDescriptor {
    private static final AtomicReferenceFieldUpdater _consensus$FU = AtomicReferenceFieldUpdater.newUpdater(AtomicOp.class, Object.class, "_consensus");
    private volatile Object _consensus = AtomicKt.NO_DECISION;

    public abstract void complete(T t, Object obj);

    public abstract Object prepare(T t);

    public final boolean tryDecide(Object obj) {
        if ((obj != AtomicKt.NO_DECISION ? 1 : null) != null) {
            return _consensus$FU.compareAndSet(this, AtomicKt.NO_DECISION, obj);
        }
        throw new IllegalStateException("Check failed.".toString());
    }

    private final Object decide(Object obj) {
        return tryDecide(obj) ? obj : this._consensus;
    }

    public final Object perform(Object obj) {
        Object obj2 = this._consensus;
        if (obj2 == AtomicKt.NO_DECISION) {
            obj2 = decide(prepare(obj));
        }
        complete(obj, obj2);
        return obj2;
    }
}
