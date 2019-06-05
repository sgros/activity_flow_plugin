// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental.internal;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public abstract class AtomicOp<T> extends OpDescriptor
{
    private static final AtomicReferenceFieldUpdater _consensus$FU;
    private volatile Object _consensus;
    
    static {
        _consensus$FU = AtomicReferenceFieldUpdater.newUpdater(AtomicOp.class, Object.class, "_consensus");
    }
    
    public AtomicOp() {
        this._consensus = AtomicKt.access$getNO_DECISION$p();
    }
    
    private final Object decide(Object consensus) {
        if (!this.tryDecide(consensus)) {
            consensus = this._consensus;
        }
        return consensus;
    }
    
    public abstract void complete(final T p0, final Object p1);
    
    @Override
    public final Object perform(final Object o) {
        Object o2;
        if ((o2 = this._consensus) == AtomicKt.access$getNO_DECISION$p()) {
            o2 = this.decide(this.prepare(o));
        }
        this.complete(o, o2);
        return o2;
    }
    
    public abstract Object prepare(final T p0);
    
    public final boolean tryDecide(final Object o) {
        if (o != AtomicKt.access$getNO_DECISION$p()) {
            return AtomicOp._consensus$FU.compareAndSet(this, AtomicKt.access$getNO_DECISION$p(), o);
        }
        throw new IllegalStateException("Check failed.".toString());
    }
}
