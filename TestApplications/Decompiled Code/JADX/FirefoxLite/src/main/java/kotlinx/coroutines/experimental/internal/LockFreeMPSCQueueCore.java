package kotlinx.coroutines.experimental.internal;

import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: LockFreeMPSCQueue.kt */
public final class LockFreeMPSCQueueCore<E> {
    public static final Companion Companion = new Companion();
    public static final Symbol REMOVE_FROZEN = new Symbol("REMOVE_FROZEN");
    private static final AtomicReferenceFieldUpdater _next$FU = AtomicReferenceFieldUpdater.newUpdater(LockFreeMPSCQueueCore.class, Object.class, "_next");
    private static final AtomicLongFieldUpdater _state$FU = AtomicLongFieldUpdater.newUpdater(LockFreeMPSCQueueCore.class, "_state");
    private volatile Object _next = null;
    private volatile long _state = 0;
    private final AtomicReferenceArray<Object> array = new AtomicReferenceArray(this.capacity);
    private final int capacity;
    private final int mask = (this.capacity - 1);

    /* compiled from: LockFreeMPSCQueue.kt */
    public static final class Companion {
        private final int addFailReason(long j) {
            return (j & 2305843009213693952L) != 0 ? 2 : 1;
        }

        /* renamed from: wo */
        private final long m10wo(long j, long j2) {
            return j & (~j2);
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private final long updateHead(long j, int i) {
            return m10wo(j, 1073741823) | (((long) i) << null);
        }

        private final long updateTail(long j, int i) {
            return m10wo(j, 1152921503533105152L) | (((long) i) << 30);
        }
    }

    /* compiled from: LockFreeMPSCQueue.kt */
    private static final class Placeholder {
        public final int index;

        public Placeholder(int i) {
            this.index = i;
        }
    }

    public LockFreeMPSCQueueCore(int i) {
        this.capacity = i;
        int i2 = 1;
        if ((this.mask <= 1073741823 ? 1 : null) != null) {
            if ((this.capacity & this.mask) != 0) {
                i2 = 0;
            }
            if (i2 == 0) {
                throw new IllegalStateException("Check failed.".toString());
            }
            return;
        }
        throw new IllegalStateException("Check failed.".toString());
    }

    public final boolean isEmpty() {
        Companion companion = Companion;
        long j = this._state;
        return ((int) ((1073741823 & j) >> 0)) == ((int) ((j & 1152921503533105152L) >> 30));
    }

    private final LockFreeMPSCQueueCore<E> fillPlaceholder(int i, E e) {
        Object obj = this.array.get(this.mask & i);
        if (!(obj instanceof Placeholder) || ((Placeholder) obj).index != i) {
            return null;
        }
        this.array.set(i & this.mask, e);
        return this;
    }

    public final LockFreeMPSCQueueCore<E> next() {
        return allocateOrGetNextCopy(markFrozen());
    }

    private final LockFreeMPSCQueueCore<E> allocateNextCopy(long j) {
        LockFreeMPSCQueueCore lockFreeMPSCQueueCore = new LockFreeMPSCQueueCore(this.capacity * 2);
        Companion companion = Companion;
        int i = (int) ((1152921503533105152L & j) >> 30);
        for (int i2 = (int) ((1073741823 & j) >> null); (this.mask & i2) != (this.mask & i); i2++) {
            AtomicReferenceArray atomicReferenceArray = lockFreeMPSCQueueCore.array;
            int i3 = lockFreeMPSCQueueCore.mask & i2;
            Object obj = this.array.get(this.mask & i2);
            if (obj == null) {
                obj = new Placeholder(i2);
            }
            atomicReferenceArray.set(i3, obj);
        }
        lockFreeMPSCQueueCore._state = Companion.m10wo(j, 1152921504606846976L);
        return lockFreeMPSCQueueCore;
    }

    public final int addLast(E e) {
        int i;
        Intrinsics.checkParameterIsNotNull(e, "element");
        long j;
        int i2;
        do {
            j = this._state;
            if ((3458764513820540928L & j) != 0) {
                return Companion.addFailReason(j);
            }
            Companion companion = Companion;
            i = (int) ((1152921503533105152L & j) >> 30);
            if (((i + 2) & this.mask) == (((int) ((1073741823 & j) >> 0)) & this.mask)) {
                return 1;
            }
            i2 = (i + 1) & 1073741823;
        } while (!_state$FU.compareAndSet(this, j, Companion.updateTail(j, i2)));
        this.array.set(this.mask & i, e);
        LockFreeMPSCQueueCore lockFreeMPSCQueueCore = this;
        while ((lockFreeMPSCQueueCore._state & 1152921504606846976L) != 0) {
            lockFreeMPSCQueueCore = lockFreeMPSCQueueCore.next().fillPlaceholder(i, e);
            if (lockFreeMPSCQueueCore == null) {
                break;
            }
        }
        return 0;
    }

    public final Object removeFirstOrNull() {
        long j = this._state;
        if ((1152921504606846976L & j) != 0) {
            return REMOVE_FROZEN;
        }
        Companion companion = Companion;
        int i = (int) ((1073741823 & j) >> null);
        if ((((int) ((1152921503533105152L & j) >> 30)) & this.mask) == (this.mask & i)) {
            return null;
        }
        Object obj = this.array.get(this.mask & i);
        if (obj == null || (obj instanceof Placeholder)) {
            return null;
        }
        int i2 = (i + 1) & 1073741823;
        if (_state$FU.compareAndSet(this, j, Companion.updateHead(j, i2))) {
            this.array.set(this.mask & i, null);
            return obj;
        }
        LockFreeMPSCQueueCore lockFreeMPSCQueueCore = this;
        while (true) {
            lockFreeMPSCQueueCore = lockFreeMPSCQueueCore.removeSlowPath(i, i2);
            if (lockFreeMPSCQueueCore == null) {
                return obj;
            }
        }
    }

    private final LockFreeMPSCQueueCore<E> removeSlowPath(int i, int i2) {
        int i3;
        long j;
        do {
            j = this._state;
            Companion companion = Companion;
            Object obj = null;
            i3 = (int) ((1073741823 & j) >> 0);
            if (i3 == i) {
                obj = 1;
            }
            if (obj == null) {
                throw new IllegalStateException("This queue can have only one consumer".toString());
            } else if ((1152921504606846976L & j) != 0) {
                return next();
            }
        } while (!_state$FU.compareAndSet(this, j, Companion.updateHead(j, i2)));
        this.array.set(this.mask & i3, null);
        return null;
    }

    private final long markFrozen() {
        long j;
        long j2;
        do {
            j2 = this._state;
            if ((j2 & 1152921504606846976L) != 0) {
                return j2;
            }
            j = j2 | 1152921504606846976L;
        } while (!_state$FU.compareAndSet(this, j2, j));
        return j;
    }

    private final LockFreeMPSCQueueCore<E> allocateOrGetNextCopy(long j) {
        while (true) {
            LockFreeMPSCQueueCore lockFreeMPSCQueueCore = (LockFreeMPSCQueueCore) this._next;
            if (lockFreeMPSCQueueCore != null) {
                return lockFreeMPSCQueueCore;
            }
            _next$FU.compareAndSet(this, null, allocateNextCopy(j));
        }
    }
}
