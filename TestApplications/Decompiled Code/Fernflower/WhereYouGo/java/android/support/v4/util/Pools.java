package android.support.v4.util;

public final class Pools {
   private Pools() {
   }

   public interface Pool {
      Object acquire();

      boolean release(Object var1);
   }

   public static class SimplePool implements Pools.Pool {
      private final Object[] mPool;
      private int mPoolSize;

      public SimplePool(int var1) {
         if (var1 <= 0) {
            throw new IllegalArgumentException("The max pool size must be > 0");
         } else {
            this.mPool = new Object[var1];
         }
      }

      private boolean isInPool(Object var1) {
         int var2 = 0;

         boolean var3;
         while(true) {
            if (var2 >= this.mPoolSize) {
               var3 = false;
               break;
            }

            if (this.mPool[var2] == var1) {
               var3 = true;
               break;
            }

            ++var2;
         }

         return var3;
      }

      public Object acquire() {
         Object var2;
         if (this.mPoolSize > 0) {
            int var1 = this.mPoolSize - 1;
            var2 = this.mPool[var1];
            this.mPool[var1] = null;
            --this.mPoolSize;
         } else {
            var2 = null;
         }

         return var2;
      }

      public boolean release(Object var1) {
         if (this.isInPool(var1)) {
            throw new IllegalStateException("Already in the pool!");
         } else {
            boolean var2;
            if (this.mPoolSize < this.mPool.length) {
               this.mPool[this.mPoolSize] = var1;
               ++this.mPoolSize;
               var2 = true;
            } else {
               var2 = false;
            }

            return var2;
         }
      }
   }

   public static class SynchronizedPool extends Pools.SimplePool {
      private final Object mLock = new Object();

      public SynchronizedPool(int var1) {
         super(var1);
      }

      public Object acquire() {
         // $FF: Couldn't be decompiled
      }

      public boolean release(Object param1) {
         // $FF: Couldn't be decompiled
      }
   }
}
