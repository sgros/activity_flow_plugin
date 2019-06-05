package kotlinx.coroutines.experimental;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.internal.LockFreeMPSCQueueCore;
import kotlinx.coroutines.experimental.internal.Symbol;
import kotlinx.coroutines.experimental.internal.ThreadSafeHeap;
import kotlinx.coroutines.experimental.internal.ThreadSafeHeapNode;

/* compiled from: EventLoop.kt */
public abstract class EventLoopBase extends CoroutineDispatcher {
    static final AtomicReferenceFieldUpdater _delayed$FU = AtomicReferenceFieldUpdater.newUpdater(EventLoopBase.class, Object.class, "_delayed");
    private static final AtomicReferenceFieldUpdater _queue$FU = AtomicReferenceFieldUpdater.newUpdater(EventLoopBase.class, Object.class, "_queue");
    volatile Object _delayed = null;
    private volatile Object _queue = null;

    /* compiled from: EventLoop.kt */
    public abstract class DelayedTask implements Comparable<DelayedTask>, Runnable, DisposableHandle, ThreadSafeHeapNode {
        private int index;
        public final long nanoTime;
        private int state;
        final /* synthetic */ EventLoopBase this$0;

        public int getIndex() {
            return this.index;
        }

        public void setIndex(int i) {
            this.index = i;
        }

        public int compareTo(DelayedTask delayedTask) {
            Intrinsics.checkParameterIsNotNull(delayedTask, "other");
            int i = ((this.nanoTime - delayedTask.nanoTime) > 0 ? 1 : ((this.nanoTime - delayedTask.nanoTime) == 0 ? 0 : -1));
            if (i > 0) {
                return 1;
            }
            return i < 0 ? -1 : 0;
        }

        public final boolean timeToExecute(long j) {
            return j - this.nanoTime >= 0;
        }

        public final void dispose() {
            synchronized (this) {
                int i = this.state;
                if (i == 0) {
                    ThreadSafeHeap threadSafeHeap = (ThreadSafeHeap) this.this$0._delayed;
                    if (threadSafeHeap != null) {
                        threadSafeHeap.remove(this);
                    }
                } else if (i != 2) {
                    return;
                } else {
                    DefaultExecutor.INSTANCE.removeDelayedImpl$kotlinx_coroutines_core(this);
                }
                this.state = 1;
                Unit unit = Unit.INSTANCE;
            }
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Delayed[nanos=");
            stringBuilder.append(this.nanoTime);
            stringBuilder.append(']');
            return stringBuilder.toString();
        }
    }

    public abstract boolean isCompleted();

    public abstract boolean isCorrectThread();

    public abstract void unpark();

    /* Access modifiers changed, original: protected|final */
    public final boolean isEmpty() {
        return isQueueEmpty() && isDelayedEmpty();
    }

    private final boolean isQueueEmpty() {
        Symbol symbol = this._queue;
        if (symbol == null) {
            return true;
        }
        if (symbol instanceof LockFreeMPSCQueueCore) {
            return ((LockFreeMPSCQueueCore) symbol).isEmpty();
        }
        if (symbol == EventLoopKt.CLOSED_EMPTY) {
            return true;
        }
        return false;
    }

    private final boolean isDelayedEmpty() {
        ThreadSafeHeap threadSafeHeap = (ThreadSafeHeap) this._delayed;
        return threadSafeHeap == null || threadSafeHeap.isEmpty();
    }

    private final long getNextTime() {
        if (!isQueueEmpty()) {
            return 0;
        }
        ThreadSafeHeap threadSafeHeap = (ThreadSafeHeap) this._delayed;
        if (threadSafeHeap == null) {
            return Long.MAX_VALUE;
        }
        DelayedTask delayedTask = (DelayedTask) threadSafeHeap.peek();
        if (delayedTask != null) {
            return RangesKt___RangesKt.coerceAtLeast(delayedTask.nanoTime - TimeSourceKt.getTimeSource().nanoTime(), 0);
        }
        return Long.MAX_VALUE;
    }

    public void dispatch(CoroutineContext coroutineContext, Runnable runnable) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(runnable, "block");
        execute$kotlinx_coroutines_core(runnable);
    }

    public long processNextEvent() {
        if (!isCorrectThread()) {
            return Long.MAX_VALUE;
        }
        ThreadSafeHeap threadSafeHeap = (ThreadSafeHeap) this._delayed;
        if (threadSafeHeap != null && !threadSafeHeap.isEmpty()) {
            long nanoTime = TimeSourceKt.getTimeSource().nanoTime();
            while (true) {
                ThreadSafeHeapNode threadSafeHeapNode;
                synchronized (threadSafeHeap) {
                    ThreadSafeHeapNode firstImpl = threadSafeHeap.firstImpl();
                    threadSafeHeapNode = null;
                    if (firstImpl != null) {
                        DelayedTask delayedTask = (DelayedTask) firstImpl;
                        if (delayedTask.timeToExecute(nanoTime) ? enqueueImpl(delayedTask) : false) {
                            threadSafeHeapNode = threadSafeHeap.removeAtImpl(0);
                        }
                    }
                }
                if (((DelayedTask) threadSafeHeapNode) == null) {
                    break;
                }
            }
        }
        Runnable dequeue = dequeue();
        if (dequeue != null) {
            dequeue.run();
        }
        return getNextTime();
    }

    public final void execute$kotlinx_coroutines_core(Runnable runnable) {
        Intrinsics.checkParameterIsNotNull(runnable, "task");
        if (enqueueImpl(runnable)) {
            unpark();
        } else {
            DefaultExecutor.INSTANCE.execute$kotlinx_coroutines_core(runnable);
        }
    }

    public final void removeDelayedImpl$kotlinx_coroutines_core(DelayedTask delayedTask) {
        Intrinsics.checkParameterIsNotNull(delayedTask, "delayedTask");
        ThreadSafeHeap threadSafeHeap = (ThreadSafeHeap) this._delayed;
        if (threadSafeHeap != null) {
            threadSafeHeap.remove(delayedTask);
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void resetAll() {
        this._queue = null;
        this._delayed = null;
    }

    private final boolean enqueueImpl(Runnable runnable) {
        while (true) {
            Symbol symbol = this._queue;
            if (isCompleted()) {
                return false;
            }
            if (symbol == null) {
                if (_queue$FU.compareAndSet(this, null, runnable)) {
                    return true;
                }
            } else if (symbol instanceof LockFreeMPSCQueueCore) {
                if (symbol != null) {
                    LockFreeMPSCQueueCore lockFreeMPSCQueueCore = (LockFreeMPSCQueueCore) symbol;
                    switch (lockFreeMPSCQueueCore.addLast(runnable)) {
                        case 0:
                            return true;
                        case 1:
                            _queue$FU.compareAndSet(this, symbol, lockFreeMPSCQueueCore.next());
                            break;
                        case 2:
                            return false;
                        default:
                            break;
                    }
                }
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.Queue<kotlinx.coroutines.experimental.Runnable /* = java.lang.Runnable */> /* = kotlinx.coroutines.experimental.internal.LockFreeMPSCQueueCore<kotlinx.coroutines.experimental.Runnable /* = java.lang.Runnable */> */");
            } else if (symbol == EventLoopKt.CLOSED_EMPTY) {
                return false;
            } else {
                LockFreeMPSCQueueCore lockFreeMPSCQueueCore2 = new LockFreeMPSCQueueCore(8);
                if (symbol != null) {
                    lockFreeMPSCQueueCore2.addLast((Runnable) symbol);
                    lockFreeMPSCQueueCore2.addLast(runnable);
                    if (_queue$FU.compareAndSet(this, symbol, lockFreeMPSCQueueCore2)) {
                        return true;
                    }
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.Runnable /* = java.lang.Runnable */");
                }
            }
        }
    }

    private final Runnable dequeue() {
        while (true) {
            Symbol symbol = this._queue;
            if (symbol == null) {
                return null;
            }
            if (symbol instanceof LockFreeMPSCQueueCore) {
                if (symbol != null) {
                    LockFreeMPSCQueueCore lockFreeMPSCQueueCore = (LockFreeMPSCQueueCore) symbol;
                    Symbol removeFirstOrNull = lockFreeMPSCQueueCore.removeFirstOrNull();
                    if (removeFirstOrNull != LockFreeMPSCQueueCore.REMOVE_FROZEN) {
                        return (Runnable) removeFirstOrNull;
                    }
                    _queue$FU.compareAndSet(this, symbol, lockFreeMPSCQueueCore.next());
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.Queue<kotlinx.coroutines.experimental.Runnable /* = java.lang.Runnable */> /* = kotlinx.coroutines.experimental.internal.LockFreeMPSCQueueCore<kotlinx.coroutines.experimental.Runnable /* = java.lang.Runnable */> */");
                }
            } else if (symbol == EventLoopKt.CLOSED_EMPTY) {
                return null;
            } else {
                if (_queue$FU.compareAndSet(this, symbol, null)) {
                    if (symbol != null) {
                        return (Runnable) symbol;
                    }
                    throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.Runnable /* = java.lang.Runnable */");
                }
            }
        }
    }
}
