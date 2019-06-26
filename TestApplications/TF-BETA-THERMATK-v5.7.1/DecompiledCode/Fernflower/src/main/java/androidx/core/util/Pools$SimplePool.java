package androidx.core.util;

public class Pools$SimplePool implements Pools$Pool {
   private final Object[] mPool;
   private int mPoolSize;

   public Pools$SimplePool(int var1) {
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
      int var1 = this.mPoolSize;
      if (var1 > 0) {
         int var2 = var1 - 1;
         Object[] var3 = this.mPool;
         Object var4 = var3[var2];
         var3[var2] = null;
         this.mPoolSize = var1 - 1;
         return var4;
      } else {
         return null;
      }
   }

   public boolean release(Object var1) {
      if (!this.isInPool(var1)) {
         int var2 = this.mPoolSize;
         Object[] var3 = this.mPool;
         if (var2 < var3.length) {
            var3[var2] = var1;
            this.mPoolSize = var2 + 1;
            return true;
         } else {
            return false;
         }
      } else {
         throw new IllegalStateException("Already in the pool!");
      }
   }
}
