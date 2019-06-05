package android.support.v4.util;

public final class Pools {
   public interface Pool {
      Object acquire();

      boolean release(Object var1);
   }

   public static class SimplePool implements Pools.Pool {
      private final Object[] mPool;
      private int mPoolSize;

      public SimplePool(int var1) {
         if (var1 > 0) {
            this.mPool = new Object[var1];
         } else {
            throw new IllegalArgumentException("The max pool size must be > 0");
         }
      }

      private boolean isInPool(Object var1) {
         for(int var2 = 0; var2 < this.mPoolSize; ++var2) {
            if (this.mPool[var2] == var1) {
               return true;
            }
         }

         return false;
      }

      public Object acquire() {
         if (this.mPoolSize > 0) {
            int var1 = this.mPoolSize - 1;
            Object var2 = this.mPool[var1];
            this.mPool[var1] = null;
            --this.mPoolSize;
            return var2;
         } else {
            return null;
         }
      }

      public boolean release(Object var1) {
         if (!this.isInPool(var1)) {
            if (this.mPoolSize < this.mPool.length) {
               this.mPool[this.mPoolSize] = var1;
               ++this.mPoolSize;
               return true;
            } else {
               return false;
            }
         } else {
            throw new IllegalStateException("Already in the pool!");
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
