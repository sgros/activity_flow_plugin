// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental.internal;

import kotlin.jvm.internal.Intrinsics;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public final class LockFreeMPSCQueueCore<E>
{
    public static final Companion Companion;
    public static final Symbol REMOVE_FROZEN;
    private static final AtomicReferenceFieldUpdater _next$FU;
    private static final AtomicLongFieldUpdater _state$FU;
    private volatile Object _next;
    private volatile long _state;
    private final AtomicReferenceArray<Object> array;
    private final int capacity;
    private final int mask;
    
    static {
        Companion = new Companion(null);
        REMOVE_FROZEN = new Symbol("REMOVE_FROZEN");
        _next$FU = AtomicReferenceFieldUpdater.newUpdater(LockFreeMPSCQueueCore.class, Object.class, "_next");
        _state$FU = AtomicLongFieldUpdater.newUpdater(LockFreeMPSCQueueCore.class, "_state");
    }
    
    public LockFreeMPSCQueueCore(int capacity) {
        this.capacity = capacity;
        capacity = this.capacity;
        final int n = 1;
        this.mask = capacity - 1;
        this._next = null;
        this._state = 0L;
        this.array = new AtomicReferenceArray<Object>(this.capacity);
        if (this.mask <= 1073741823) {
            capacity = 1;
        }
        else {
            capacity = 0;
        }
        if (capacity == 0) {
            throw new IllegalStateException("Check failed.".toString());
        }
        if ((this.capacity & this.mask) == 0x0) {
            capacity = n;
        }
        else {
            capacity = 0;
        }
        if (capacity != 0) {
            return;
        }
        throw new IllegalStateException("Check failed.".toString());
    }
    
    private final LockFreeMPSCQueueCore<E> allocateNextCopy(final long n) {
        final LockFreeMPSCQueueCore lockFreeMPSCQueueCore = new LockFreeMPSCQueueCore(this.capacity * 2);
        final Companion companion = LockFreeMPSCQueueCore.Companion;
        for (int n2 = (int)((0x3FFFFFFFL & n) >> 0); (this.mask & n2) != (this.mask & (int)((0xFFFFFFFC0000000L & n) >> 30)); ++n2) {
            final AtomicReferenceArray<Object> array = lockFreeMPSCQueueCore.array;
            final int mask = lockFreeMPSCQueueCore.mask;
            Object value = this.array.get(this.mask & n2);
            if (value == null) {
                value = new Placeholder(n2);
            }
            array.set(mask & n2, value);
        }
        lockFreeMPSCQueueCore._state = LockFreeMPSCQueueCore.Companion.wo(n, 1152921504606846976L);
        return lockFreeMPSCQueueCore;
    }
    
    private final LockFreeMPSCQueueCore<E> allocateOrGetNextCopy(final long n) {
        LockFreeMPSCQueueCore lockFreeMPSCQueueCore;
        while (true) {
            lockFreeMPSCQueueCore = (LockFreeMPSCQueueCore)this._next;
            if (lockFreeMPSCQueueCore != null) {
                break;
            }
            LockFreeMPSCQueueCore._next$FU.compareAndSet(this, null, this.allocateNextCopy(n));
        }
        return lockFreeMPSCQueueCore;
    }
    
    private final LockFreeMPSCQueueCore<E> fillPlaceholder(final int n, final E newValue) {
        final Placeholder value = this.array.get(this.mask & n);
        if (value instanceof Placeholder && value.index == n) {
            this.array.set(n & this.mask, newValue);
            return this;
        }
        return null;
    }
    
    private final long markFrozen() {
        long state;
        long n;
        do {
            state = this._state;
            if ((state & 0x1000000000000000L) != 0x0L) {
                return state;
            }
            n = (state | 0x1000000000000000L);
        } while (!LockFreeMPSCQueueCore._state$FU.compareAndSet(this, state, n));
        return n;
    }
    
    private final LockFreeMPSCQueueCore<E> removeSlowPath(final int n, final int n2) {
        long state;
        int n3;
        do {
            state = this._state;
            final Companion companion = LockFreeMPSCQueueCore.Companion;
            boolean b = false;
            n3 = (int)((0x3FFFFFFFL & state) >> 0);
            if (n3 == n) {
                b = true;
            }
            if (!b) {
                throw new IllegalStateException("This queue can have only one consumer".toString());
            }
            if ((0x1000000000000000L & state) != 0x0L) {
                return this.next();
            }
        } while (!LockFreeMPSCQueueCore._state$FU.compareAndSet(this, state, LockFreeMPSCQueueCore.Companion.updateHead(state, n2)));
        this.array.set(this.mask & n3, null);
        return null;
    }
    
    public final int addLast(final E newValue) {
        Intrinsics.checkParameterIsNotNull(newValue, "element");
        long state;
        int n;
        do {
            state = this._state;
            if ((0x3000000000000000L & state) != 0x0L) {
                return LockFreeMPSCQueueCore.Companion.addFailReason(state);
            }
            final Companion companion = LockFreeMPSCQueueCore.Companion;
            final int n2 = (int)((0x3FFFFFFFL & state) >> 0);
            n = (int)((0xFFFFFFFC0000000L & state) >> 30);
            if ((n + 2 & this.mask) == (n2 & this.mask)) {
                return 1;
            }
        } while (!LockFreeMPSCQueueCore._state$FU.compareAndSet(this, state, LockFreeMPSCQueueCore.Companion.updateTail(state, n + 1 & 0x3FFFFFFF)));
        this.array.set(this.mask & n, newValue);
        LockFreeMPSCQueueCore<Object> fillPlaceholder = (LockFreeMPSCQueueCore<Object>)this;
        while ((fillPlaceholder._state & 0x1000000000000000L) != 0x0L) {
            fillPlaceholder = fillPlaceholder.next().fillPlaceholder(n, newValue);
            if (fillPlaceholder != null) {
                continue;
            }
            return 0;
        }
        return 0;
    }
    
    public final boolean isEmpty() {
        final Companion companion = LockFreeMPSCQueueCore.Companion;
        final long state = this._state;
        boolean b = false;
        if ((int)((0x3FFFFFFFL & state) >> 0) == (int)((state & 0xFFFFFFFC0000000L) >> 30)) {
            b = true;
        }
        return b;
    }
    
    public final LockFreeMPSCQueueCore<E> next() {
        return this.allocateOrGetNextCopy(this.markFrozen());
    }
    
    public final Object removeFirstOrNull() {
        final long state = this._state;
        if ((0x1000000000000000L & state) != 0x0L) {
            return LockFreeMPSCQueueCore.REMOVE_FROZEN;
        }
        final Companion companion = LockFreeMPSCQueueCore.Companion;
        final int n = (int)((0x3FFFFFFFL & state) >> 0);
        if (((int)((0xFFFFFFFC0000000L & state) >> 30) & this.mask) == (this.mask & n)) {
            return null;
        }
        final Object value = this.array.get(this.mask & n);
        if (value == null) {
            return null;
        }
        if (value instanceof Placeholder) {
            return null;
        }
        final int n2 = n + 1 & 0x3FFFFFFF;
        if (LockFreeMPSCQueueCore._state$FU.compareAndSet(this, state, LockFreeMPSCQueueCore.Companion.updateHead(state, n2))) {
            this.array.set(this.mask & n, null);
            return value;
        }
        LockFreeMPSCQueueCore removeSlowPath = this;
        do {
            removeSlowPath = removeSlowPath.removeSlowPath(n, n2);
        } while (removeSlowPath != null);
        return value;
    }
    
    public static final class Companion
    {
        private Companion() {
        }
        
        private final int addFailReason(final long n) {
            int n2;
            if ((n & 0x2000000000000000L) != 0x0L) {
                n2 = 2;
            }
            else {
                n2 = 1;
            }
            return n2;
        }
        
        private final long updateHead(final long n, final int n2) {
            return this.wo(n, 1073741823L) | (long)n2 << 0;
        }
        
        private final long updateTail(final long n, final int n2) {
            return this.wo(n, 1152921503533105152L) | (long)n2 << 30;
        }
        
        private final long wo(final long n, final long n2) {
            return n & n2;
        }
    }
    
    private static final class Placeholder
    {
        public final int index;
        
        public Placeholder(final int index) {
            this.index = index;
        }
    }
}
