package kotlinx.coroutines.experimental.internal;

import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

public final class LockFreeMPSCQueueCore {
   public static final LockFreeMPSCQueueCore.Companion Companion = new LockFreeMPSCQueueCore.Companion((DefaultConstructorMarker)null);
   public static final Symbol REMOVE_FROZEN = new Symbol("REMOVE_FROZEN");
   private static final AtomicReferenceFieldUpdater _next$FU = AtomicReferenceFieldUpdater.newUpdater(LockFreeMPSCQueueCore.class, Object.class, "_next");
   private static final AtomicLongFieldUpdater _state$FU = AtomicLongFieldUpdater.newUpdater(LockFreeMPSCQueueCore.class, "_state");
   private volatile Object _next;
   private volatile long _state;
   private final AtomicReferenceArray array;
   private final int capacity;
   private final int mask;

   public LockFreeMPSCQueueCore(int var1) {
      this.capacity = var1;
      var1 = this.capacity;
      boolean var2 = true;
      this.mask = var1 - 1;
      this._next = null;
      this._state = 0L;
      this.array = new AtomicReferenceArray(this.capacity);
      boolean var3;
      if (this.mask <= 1073741823) {
         var3 = true;
      } else {
         var3 = false;
      }

      if (var3) {
         if ((this.capacity & this.mask) == 0) {
            var3 = var2;
         } else {
            var3 = false;
         }

         if (!var3) {
            throw (Throwable)(new IllegalStateException("Check failed.".toString()));
         }
      } else {
         throw (Throwable)(new IllegalStateException("Check failed.".toString()));
      }
   }

   private final LockFreeMPSCQueueCore allocateNextCopy(long var1) {
      LockFreeMPSCQueueCore var3 = new LockFreeMPSCQueueCore(this.capacity * 2);
      LockFreeMPSCQueueCore.Companion var4 = Companion;
      int var5 = (int)((1073741823L & var1) >> 0);

      for(int var6 = (int)((1152921503533105152L & var1) >> 30); (this.mask & var5) != (this.mask & var6); ++var5) {
         AtomicReferenceArray var7 = var3.array;
         int var8 = var3.mask;
         Object var9 = this.array.get(this.mask & var5);
         if (var9 == null) {
            var9 = new LockFreeMPSCQueueCore.Placeholder(var5);
         }

         var7.set(var8 & var5, var9);
      }

      var3._state = Companion.wo(var1, 1152921504606846976L);
      return var3;
   }

   private final LockFreeMPSCQueueCore allocateOrGetNextCopy(long var1) {
      while(true) {
         LockFreeMPSCQueueCore var3 = (LockFreeMPSCQueueCore)this._next;
         if (var3 != null) {
            return var3;
         }

         _next$FU.compareAndSet(this, (Object)null, this.allocateNextCopy(var1));
      }
   }

   private final LockFreeMPSCQueueCore fillPlaceholder(int var1, Object var2) {
      Object var3 = this.array.get(this.mask & var1);
      if (var3 instanceof LockFreeMPSCQueueCore.Placeholder && ((LockFreeMPSCQueueCore.Placeholder)var3).index == var1) {
         this.array.set(var1 & this.mask, var2);
         return this;
      } else {
         return null;
      }
   }

   private final long markFrozen() {
      long var1;
      long var3;
      do {
         var1 = this._state;
         if ((var1 & 1152921504606846976L) != 0L) {
            return var1;
         }

         var3 = var1 | 1152921504606846976L;
      } while(!_state$FU.compareAndSet(this, var1, var3));

      return var3;
   }

   private final LockFreeMPSCQueueCore removeSlowPath(int var1, int var2) {
      while(true) {
         long var3 = this._state;
         LockFreeMPSCQueueCore.Companion var5 = Companion;
         boolean var6 = false;
         int var7 = (int)((1073741823L & var3) >> 0);
         if (var7 == var1) {
            var6 = true;
         }

         if (var6) {
            if ((1152921504606846976L & var3) != 0L) {
               return this.next();
            }

            if (!_state$FU.compareAndSet(this, var3, Companion.updateHead(var3, var2))) {
               continue;
            }

            this.array.set(this.mask & var7, (Object)null);
            return null;
         }

         throw (Throwable)(new IllegalStateException("This queue can have only one consumer".toString()));
      }
   }

   public final int addLast(Object var1) {
      Intrinsics.checkParameterIsNotNull(var1, "element");

      long var2;
      int var6;
      do {
         var2 = this._state;
         if ((3458764513820540928L & var2) != 0L) {
            return Companion.addFailReason(var2);
         }

         LockFreeMPSCQueueCore.Companion var4 = Companion;
         int var5 = (int)((1073741823L & var2) >> 0);
         var6 = (int)((1152921503533105152L & var2) >> 30);
         if ((var6 + 2 & this.mask) == (var5 & this.mask)) {
            return 1;
         }
      } while(!_state$FU.compareAndSet(this, var2, Companion.updateTail(var2, var6 + 1 & 1073741823)));

      this.array.set(this.mask & var6, var1);
      LockFreeMPSCQueueCore var7 = (LockFreeMPSCQueueCore)this;

      while((var7._state & 1152921504606846976L) != 0L) {
         var7 = var7.next().fillPlaceholder(var6, var1);
         if (var7 == null) {
            break;
         }
      }

      return 0;
   }

   public final boolean isEmpty() {
      LockFreeMPSCQueueCore.Companion var1 = Companion;
      long var2 = this._state;
      boolean var4 = false;
      if ((int)((1073741823L & var2) >> 0) == (int)((var2 & 1152921503533105152L) >> 30)) {
         var4 = true;
      }

      return var4;
   }

   public final LockFreeMPSCQueueCore next() {
      return this.allocateOrGetNextCopy(this.markFrozen());
   }

   public final Object removeFirstOrNull() {
      long var1 = this._state;
      if ((1152921504606846976L & var1) != 0L) {
         return REMOVE_FROZEN;
      } else {
         LockFreeMPSCQueueCore.Companion var3 = Companion;
         int var4 = (int)((1073741823L & var1) >> 0);
         if (((int)((1152921503533105152L & var1) >> 30) & this.mask) == (this.mask & var4)) {
            return null;
         } else {
            Object var5 = this.array.get(this.mask & var4);
            if (var5 == null) {
               return null;
            } else if (var5 instanceof LockFreeMPSCQueueCore.Placeholder) {
               return null;
            } else {
               int var6 = var4 + 1 & 1073741823;
               if (_state$FU.compareAndSet(this, var1, Companion.updateHead(var1, var6))) {
                  this.array.set(this.mask & var4, (Object)null);
                  return var5;
               } else {
                  LockFreeMPSCQueueCore var7 = (LockFreeMPSCQueueCore)this;

                  do {
                     var7 = var7.removeSlowPath(var4, var6);
                  } while(var7 != null);

                  return var5;
               }
            }
         }
      }
   }

   public static final class Companion {
      private Companion() {
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }

      private final int addFailReason(long var1) {
         byte var3;
         if ((var1 & 2305843009213693952L) != 0L) {
            var3 = 2;
         } else {
            var3 = 1;
         }

         return var3;
      }

      private final long updateHead(long var1, int var3) {
         return ((LockFreeMPSCQueueCore.Companion)this).wo(var1, 1073741823L) | (long)var3 << 0;
      }

      private final long updateTail(long var1, int var3) {
         return ((LockFreeMPSCQueueCore.Companion)this).wo(var1, 1152921503533105152L) | (long)var3 << 30;
      }

      private final long wo(long var1, long var3) {
         return var1 & var3;
      }
   }

   private static final class Placeholder {
      public final int index;

      public Placeholder(int var1) {
         this.index = var1;
      }
   }
}
