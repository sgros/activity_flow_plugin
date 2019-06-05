package android.support.constraint.solver;

final class Pools {
   interface Pool {
      Object acquire();

      boolean release(Object var1);

      void releaseAll(Object[] var1, int var2);
   }

   static class SimplePool implements Pools.Pool {
      private final Object[] mPool;
      private int mPoolSize;

      SimplePool(int var1) {
         if (var1 > 0) {
            this.mPool = new Object[var1];
         } else {
            throw new IllegalArgumentException("The max pool size must be > 0");
         }
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
         if (this.mPoolSize < this.mPool.length) {
            this.mPool[this.mPoolSize] = var1;
            ++this.mPoolSize;
            return true;
         } else {
            return false;
         }
      }

      public void releaseAll(Object[] var1, int var2) {
         int var3 = var2;
         if (var2 > var1.length) {
            var3 = var1.length;
         }

         for(var2 = 0; var2 < var3; ++var2) {
            Object var4 = var1[var2];
            if (this.mPoolSize < this.mPool.length) {
               this.mPool[this.mPoolSize] = var4;
               ++this.mPoolSize;
            }
         }

      }
   }
}
