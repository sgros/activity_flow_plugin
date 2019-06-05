package kotlinx.coroutines.experimental;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlinx.coroutines.experimental.internal.LockFreeMPSCQueueCore;
import kotlinx.coroutines.experimental.internal.ThreadSafeHeap;
import kotlinx.coroutines.experimental.internal.ThreadSafeHeapNode;

public abstract class EventLoopBase extends CoroutineDispatcher {
   static final AtomicReferenceFieldUpdater _delayed$FU = AtomicReferenceFieldUpdater.newUpdater(EventLoopBase.class, Object.class, "_delayed");
   private static final AtomicReferenceFieldUpdater _queue$FU = AtomicReferenceFieldUpdater.newUpdater(EventLoopBase.class, Object.class, "_queue");
   volatile Object _delayed = null;
   private volatile Object _queue = null;

   private final Runnable dequeue() {
      while(true) {
         Object var1 = this._queue;
         if (var1 == null) {
            return null;
         }

         if (var1 instanceof LockFreeMPSCQueueCore) {
            if (var1 == null) {
               throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.Queue<kotlinx.coroutines.experimental.Runnable /* = java.lang.Runnable */> /* = kotlinx.coroutines.experimental.internal.LockFreeMPSCQueueCore<kotlinx.coroutines.experimental.Runnable /* = java.lang.Runnable */> */");
            }

            LockFreeMPSCQueueCore var2 = (LockFreeMPSCQueueCore)var1;
            Object var3 = var2.removeFirstOrNull();
            if (var3 != LockFreeMPSCQueueCore.REMOVE_FROZEN) {
               return (Runnable)var3;
            }

            _queue$FU.compareAndSet(this, var1, var2.next());
         } else {
            if (var1 == EventLoopKt.access$getCLOSED_EMPTY$p()) {
               return null;
            }

            if (_queue$FU.compareAndSet(this, var1, (Object)null)) {
               if (var1 != null) {
                  return (Runnable)var1;
               }

               throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.Runnable /* = java.lang.Runnable */");
            }
         }
      }
   }

   private final boolean enqueueImpl(Runnable var1) {
      while(true) {
         Object var2 = this._queue;
         if (this.isCompleted()) {
            return false;
         }

         if (var2 == null) {
            if (_queue$FU.compareAndSet(this, (Object)null, var1)) {
               return true;
            }
         } else {
            LockFreeMPSCQueueCore var3;
            if (var2 instanceof LockFreeMPSCQueueCore) {
               if (var2 == null) {
                  throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.Queue<kotlinx.coroutines.experimental.Runnable /* = java.lang.Runnable */> /* = kotlinx.coroutines.experimental.internal.LockFreeMPSCQueueCore<kotlinx.coroutines.experimental.Runnable /* = java.lang.Runnable */> */");
               }

               var3 = (LockFreeMPSCQueueCore)var2;
               switch(var3.addLast(var1)) {
               case 0:
                  return true;
               case 1:
                  _queue$FU.compareAndSet(this, var2, var3.next());
                  break;
               case 2:
                  return false;
               }
            } else {
               if (var2 == EventLoopKt.access$getCLOSED_EMPTY$p()) {
                  return false;
               }

               var3 = new LockFreeMPSCQueueCore(8);
               if (var2 != null) {
                  var3.addLast((Runnable)var2);
                  var3.addLast(var1);
                  if (!_queue$FU.compareAndSet(this, var2, var3)) {
                     continue;
                  }

                  return true;
               }

               throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.Runnable /* = java.lang.Runnable */");
            }
         }
      }
   }

   private final long getNextTime() {
      if (!this.isQueueEmpty()) {
         return 0L;
      } else {
         ThreadSafeHeap var1 = (ThreadSafeHeap)this._delayed;
         if (var1 != null) {
            EventLoopBase.DelayedTask var2 = (EventLoopBase.DelayedTask)var1.peek();
            return var2 != null ? RangesKt.coerceAtLeast(var2.nanoTime - TimeSourceKt.getTimeSource().nanoTime(), 0L) : Long.MAX_VALUE;
         } else {
            return Long.MAX_VALUE;
         }
      }
   }

   private final boolean isDelayedEmpty() {
      ThreadSafeHeap var1 = (ThreadSafeHeap)this._delayed;
      boolean var2;
      if (var1 != null && !var1.isEmpty()) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   private final boolean isQueueEmpty() {
      Object var1 = this._queue;
      boolean var2 = true;
      if (var1 != null) {
         if (var1 instanceof LockFreeMPSCQueueCore) {
            var2 = ((LockFreeMPSCQueueCore)var1).isEmpty();
         } else if (var1 != EventLoopKt.access$getCLOSED_EMPTY$p()) {
            var2 = false;
         }
      }

      return var2;
   }

   public void dispatch(CoroutineContext var1, Runnable var2) {
      Intrinsics.checkParameterIsNotNull(var1, "context");
      Intrinsics.checkParameterIsNotNull(var2, "block");
      this.execute$kotlinx_coroutines_core(var2);
   }

   public final void execute$kotlinx_coroutines_core(Runnable var1) {
      Intrinsics.checkParameterIsNotNull(var1, "task");
      if (this.enqueueImpl(var1)) {
         this.unpark();
      } else {
         DefaultExecutor.INSTANCE.execute$kotlinx_coroutines_core(var1);
      }

   }

   protected abstract boolean isCompleted();

   protected abstract boolean isCorrectThread();

   protected final boolean isEmpty() {
      boolean var1;
      if (this.isQueueEmpty() && this.isDelayedEmpty()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public long processNextEvent() {
      if (!this.isCorrectThread()) {
         return Long.MAX_VALUE;
      } else {
         ThreadSafeHeap var1 = (ThreadSafeHeap)this._delayed;
         if (var1 != null && !var1.isEmpty()) {
            long var2 = TimeSourceKt.getTimeSource().nanoTime();

            ThreadSafeHeapNode var6;
            do {
               synchronized(var1){}

               Throwable var10000;
               label258: {
                  ThreadSafeHeapNode var4;
                  boolean var10001;
                  try {
                     var4 = var1.firstImpl();
                  } catch (Throwable var19) {
                     var10000 = var19;
                     var10001 = false;
                     break label258;
                  }

                  Object var5 = null;
                  var6 = (ThreadSafeHeapNode)var5;
                  if (var4 == null) {
                     continue;
                  }

                  boolean var7;
                  label242: {
                     try {
                        EventLoopBase.DelayedTask var20 = (EventLoopBase.DelayedTask)var4;
                        if (var20.timeToExecute(var2)) {
                           var7 = this.enqueueImpl((Runnable)var20);
                           break label242;
                        }
                     } catch (Throwable var18) {
                        var10000 = var18;
                        var10001 = false;
                        break label258;
                     }

                     var7 = false;
                  }

                  var6 = (ThreadSafeHeapNode)var5;
                  if (!var7) {
                     continue;
                  }

                  label234:
                  try {
                     var6 = var1.removeAtImpl(0);
                     continue;
                  } catch (Throwable var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label234;
                  }
               }

               Throwable var21 = var10000;
               throw var21;
            } while((EventLoopBase.DelayedTask)var6 != null);
         }

         Runnable var22 = this.dequeue();
         if (var22 != null) {
            var22.run();
         }

         return this.getNextTime();
      }
   }

   public final void removeDelayedImpl$kotlinx_coroutines_core(EventLoopBase.DelayedTask var1) {
      Intrinsics.checkParameterIsNotNull(var1, "delayedTask");
      ThreadSafeHeap var2 = (ThreadSafeHeap)this._delayed;
      if (var2 != null) {
         var2.remove((ThreadSafeHeapNode)var1);
      }

   }

   protected final void resetAll() {
      this._queue = null;
      this._delayed = null;
   }

   protected abstract void unpark();

   public abstract class DelayedTask implements Comparable, Runnable, DisposableHandle, ThreadSafeHeapNode {
      private int index;
      public final long nanoTime;
      private int state;
      // $FF: synthetic field
      final EventLoopBase this$0;

      public int compareTo(EventLoopBase.DelayedTask var1) {
         Intrinsics.checkParameterIsNotNull(var1, "other");
         long var4;
         int var2 = (var4 = this.nanoTime - var1.nanoTime - 0L) == 0L ? 0 : (var4 < 0L ? -1 : 1);
         byte var3;
         if (var2 > 0) {
            var3 = 1;
         } else if (var2 < 0) {
            var3 = -1;
         } else {
            var3 = 0;
         }

         return var3;
      }

      public final void dispose() {
         synchronized(this){}

         Throwable var10000;
         label263: {
            int var1;
            boolean var10001;
            try {
               var1 = this.state;
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label263;
            }

            if (var1 != 0) {
               if (var1 != 2) {
                  return;
               }

               try {
                  DefaultExecutor.INSTANCE.removeDelayedImpl$kotlinx_coroutines_core(this);
               } catch (Throwable var31) {
                  var10000 = var31;
                  var10001 = false;
                  break label263;
               }
            } else {
               ThreadSafeHeap var2;
               try {
                  var2 = (ThreadSafeHeap)this.this$0._delayed;
               } catch (Throwable var30) {
                  var10000 = var30;
                  var10001 = false;
                  break label263;
               }

               if (var2 != null) {
                  try {
                     var2.remove((ThreadSafeHeapNode)this);
                  } catch (Throwable var29) {
                     var10000 = var29;
                     var10001 = false;
                     break label263;
                  }
               }
            }

            try {
               this.state = 1;
               Unit var34 = Unit.INSTANCE;
            } catch (Throwable var28) {
               var10000 = var28;
               var10001 = false;
               break label263;
            }

            return;
         }

         Throwable var33 = var10000;
         throw var33;
      }

      public int getIndex() {
         return this.index;
      }

      public void setIndex(int var1) {
         this.index = var1;
      }

      public final boolean timeToExecute(long var1) {
         boolean var3;
         if (var1 - this.nanoTime >= 0L) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("Delayed[nanos=");
         var1.append(this.nanoTime);
         var1.append(']');
         return var1.toString();
      }
   }
}
