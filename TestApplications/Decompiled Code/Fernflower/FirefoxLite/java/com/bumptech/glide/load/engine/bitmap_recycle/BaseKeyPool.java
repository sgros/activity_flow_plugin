package com.bumptech.glide.load.engine.bitmap_recycle;

import com.bumptech.glide.util.Util;
import java.util.Queue;

abstract class BaseKeyPool {
   private final Queue keyPool = Util.createQueue(20);

   protected abstract Poolable create();

   protected Poolable get() {
      Poolable var1 = (Poolable)this.keyPool.poll();
      Poolable var2 = var1;
      if (var1 == null) {
         var2 = this.create();
      }

      return var2;
   }

   public void offer(Poolable var1) {
      if (this.keyPool.size() < 20) {
         this.keyPool.offer(var1);
      }

   }
}
