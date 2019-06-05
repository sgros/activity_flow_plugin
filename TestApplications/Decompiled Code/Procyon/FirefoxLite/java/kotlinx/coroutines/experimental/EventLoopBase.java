// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.Unit;
import kotlinx.coroutines.experimental.internal.ThreadSafeHeapNode;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlinx.coroutines.experimental.internal.ThreadSafeHeap;
import kotlin.TypeCastException;
import kotlinx.coroutines.experimental.internal.LockFreeMPSCQueueCore;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public abstract class EventLoopBase extends CoroutineDispatcher
{
    static final AtomicReferenceFieldUpdater _delayed$FU;
    private static final AtomicReferenceFieldUpdater _queue$FU;
    volatile Object _delayed;
    private volatile Object _queue;
    
    static {
        _queue$FU = AtomicReferenceFieldUpdater.newUpdater(EventLoopBase.class, Object.class, "_queue");
        _delayed$FU = AtomicReferenceFieldUpdater.newUpdater(EventLoopBase.class, Object.class, "_delayed");
    }
    
    public EventLoopBase() {
        this._queue = null;
        this._delayed = null;
    }
    
    private final Runnable dequeue() {
        while (true) {
            final Object queue = this._queue;
            if (queue == null) {
                return null;
            }
            if (queue instanceof LockFreeMPSCQueueCore) {
                if (queue == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.Queue<kotlinx.coroutines.experimental.Runnable /* = java.lang.Runnable */> /* = kotlinx.coroutines.experimental.internal.LockFreeMPSCQueueCore<kotlinx.coroutines.experimental.Runnable /* = java.lang.Runnable */> */");
                }
                final LockFreeMPSCQueueCore lockFreeMPSCQueueCore = (LockFreeMPSCQueueCore)queue;
                final Object removeFirstOrNull = lockFreeMPSCQueueCore.removeFirstOrNull();
                if (removeFirstOrNull != LockFreeMPSCQueueCore.REMOVE_FROZEN) {
                    return (Runnable)removeFirstOrNull;
                }
                EventLoopBase._queue$FU.compareAndSet(this, queue, lockFreeMPSCQueueCore.next());
            }
            else {
                if (queue == EventLoopKt.access$getCLOSED_EMPTY$p()) {
                    return null;
                }
                if (!EventLoopBase._queue$FU.compareAndSet(this, queue, null)) {
                    continue;
                }
                if (queue != null) {
                    return (Runnable)queue;
                }
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.Runnable /* = java.lang.Runnable */");
            }
        }
    }
    
    private final boolean enqueueImpl(final Runnable runnable) {
        while (true) {
            final Object queue = this._queue;
            if (this.isCompleted()) {
                return false;
            }
            if (queue == null) {
                if (EventLoopBase._queue$FU.compareAndSet(this, null, runnable)) {
                    return true;
                }
                continue;
            }
            else if (queue instanceof LockFreeMPSCQueueCore) {
                if (queue == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.Queue<kotlinx.coroutines.experimental.Runnable /* = java.lang.Runnable */> /* = kotlinx.coroutines.experimental.internal.LockFreeMPSCQueueCore<kotlinx.coroutines.experimental.Runnable /* = java.lang.Runnable */> */");
                }
                final LockFreeMPSCQueueCore<Runnable> lockFreeMPSCQueueCore = (LockFreeMPSCQueueCore<Runnable>)queue;
                switch (lockFreeMPSCQueueCore.addLast(runnable)) {
                    default: {
                        continue;
                    }
                    case 2: {
                        return false;
                    }
                    case 1: {
                        EventLoopBase._queue$FU.compareAndSet(this, queue, lockFreeMPSCQueueCore.next());
                        continue;
                    }
                    case 0: {
                        return true;
                    }
                }
            }
            else {
                if (queue == EventLoopKt.access$getCLOSED_EMPTY$p()) {
                    return false;
                }
                final LockFreeMPSCQueueCore<Runnable> lockFreeMPSCQueueCore2 = new LockFreeMPSCQueueCore<Runnable>(8);
                if (queue == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.Runnable /* = java.lang.Runnable */");
                }
                lockFreeMPSCQueueCore2.addLast((Runnable)queue);
                lockFreeMPSCQueueCore2.addLast(runnable);
                if (EventLoopBase._queue$FU.compareAndSet(this, queue, lockFreeMPSCQueueCore2)) {
                    return true;
                }
                continue;
            }
        }
    }
    
    private final long getNextTime() {
        if (!this.isQueueEmpty()) {
            return 0L;
        }
        final ThreadSafeHeap threadSafeHeap = (ThreadSafeHeap)this._delayed;
        if (threadSafeHeap == null) {
            return Long.MAX_VALUE;
        }
        final DelayedTask delayedTask = threadSafeHeap.peek();
        if (delayedTask != null) {
            return RangesKt___RangesKt.coerceAtLeast(delayedTask.nanoTime - TimeSourceKt.getTimeSource().nanoTime(), 0L);
        }
        return Long.MAX_VALUE;
    }
    
    private final boolean isDelayedEmpty() {
        final ThreadSafeHeap threadSafeHeap = (ThreadSafeHeap)this._delayed;
        return threadSafeHeap == null || threadSafeHeap.isEmpty();
    }
    
    private final boolean isQueueEmpty() {
        final Object queue = this._queue;
        boolean empty = true;
        if (queue != null) {
            if (queue instanceof LockFreeMPSCQueueCore) {
                empty = ((LockFreeMPSCQueueCore)queue).isEmpty();
            }
            else if (queue != EventLoopKt.access$getCLOSED_EMPTY$p()) {
                empty = false;
            }
        }
        return empty;
    }
    
    @Override
    public void dispatch(final CoroutineContext coroutineContext, final Runnable runnable) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(runnable, "block");
        this.execute$kotlinx_coroutines_core(runnable);
    }
    
    public final void execute$kotlinx_coroutines_core(final Runnable runnable) {
        Intrinsics.checkParameterIsNotNull(runnable, "task");
        if (this.enqueueImpl(runnable)) {
            this.unpark();
        }
        else {
            DefaultExecutor.INSTANCE.execute$kotlinx_coroutines_core(runnable);
        }
    }
    
    protected abstract boolean isCompleted();
    
    protected abstract boolean isCorrectThread();
    
    protected final boolean isEmpty() {
        return this.isQueueEmpty() && this.isDelayedEmpty();
    }
    
    public long processNextEvent() {
        if (!this.isCorrectThread()) {
            return Long.MAX_VALUE;
        }
        final ThreadSafeHeap threadSafeHeap = (ThreadSafeHeap)this._delayed;
        if (threadSafeHeap != null && !threadSafeHeap.isEmpty()) {
            final long nanoTime = TimeSourceKt.getTimeSource().nanoTime();
            while (true) {
                synchronized (threadSafeHeap) {
                    final DelayedTask firstImpl = threadSafeHeap.firstImpl();
                    DelayedTask removeAtImpl = null;
                    if (firstImpl != null) {
                        final DelayedTask delayedTask = firstImpl;
                        final boolean b = delayedTask.timeToExecute(nanoTime) && this.enqueueImpl(delayedTask);
                        removeAtImpl = removeAtImpl;
                        if (b) {
                            removeAtImpl = threadSafeHeap.removeAtImpl(0);
                        }
                    }
                    // monitorexit(threadSafeHeap)
                    if (removeAtImpl != null) {
                        continue;
                    }
                }
                break;
            }
        }
        final Runnable dequeue = this.dequeue();
        if (dequeue != null) {
            dequeue.run();
        }
        return this.getNextTime();
    }
    
    public final void removeDelayedImpl$kotlinx_coroutines_core(final DelayedTask delayedTask) {
        Intrinsics.checkParameterIsNotNull(delayedTask, "delayedTask");
        final ThreadSafeHeap threadSafeHeap = (ThreadSafeHeap)this._delayed;
        if (threadSafeHeap != null) {
            threadSafeHeap.remove(delayedTask);
        }
    }
    
    protected final void resetAll() {
        this._queue = null;
        this._delayed = null;
    }
    
    protected abstract void unpark();
    
    public abstract class DelayedTask implements Comparable<DelayedTask>, Runnable, DisposableHandle, ThreadSafeHeapNode
    {
        private int index;
        public final long nanoTime;
        private int state;
        final /* synthetic */ EventLoopBase this$0;
        
        @Override
        public int compareTo(final DelayedTask delayedTask) {
            Intrinsics.checkParameterIsNotNull(delayedTask, "other");
            final long n = lcmp(this.nanoTime - delayedTask.nanoTime, 0L);
            int n2;
            if (n > 0) {
                n2 = 1;
            }
            else if (n < 0) {
                n2 = -1;
            }
            else {
                n2 = 0;
            }
            return n2;
        }
        
        @Override
        public final void dispose() {
            synchronized (this) {
                final int state = this.state;
                if (state != 0) {
                    if (state != 2) {
                        return;
                    }
                    DefaultExecutor.INSTANCE.removeDelayedImpl$kotlinx_coroutines_core(this);
                }
                else {
                    final ThreadSafeHeap threadSafeHeap = (ThreadSafeHeap)this.this$0._delayed;
                    if (threadSafeHeap != null) {
                        threadSafeHeap.remove(this);
                    }
                }
                this.state = 1;
                final Unit instance = Unit.INSTANCE;
            }
        }
        
        @Override
        public int getIndex() {
            return this.index;
        }
        
        @Override
        public void setIndex(final int index) {
            this.index = index;
        }
        
        public final boolean timeToExecute(final long n) {
            return n - this.nanoTime >= 0L;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("Delayed[nanos=");
            sb.append(this.nanoTime);
            sb.append(']');
            return sb.toString();
        }
    }
}
