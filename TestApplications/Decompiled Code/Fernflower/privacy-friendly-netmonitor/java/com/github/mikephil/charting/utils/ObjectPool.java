package com.github.mikephil.charting.utils;

import java.util.List;

public class ObjectPool {
   private static int ids;
   private int desiredCapacity;
   private ObjectPool.Poolable modelObject;
   private Object[] objects;
   private int objectsPointer;
   private int poolId;
   private float replenishPercentage;

   private ObjectPool(int var1, ObjectPool.Poolable var2) {
      if (var1 <= 0) {
         throw new IllegalArgumentException("Object Pool must be instantiated with a capacity greater than 0!");
      } else {
         this.desiredCapacity = var1;
         this.objects = new Object[this.desiredCapacity];
         this.objectsPointer = 0;
         this.modelObject = var2;
         this.replenishPercentage = 1.0F;
         this.refillPool();
      }
   }

   public static ObjectPool create(int var0, ObjectPool.Poolable var1) {
      synchronized(ObjectPool.class){}

      ObjectPool var2;
      try {
         var2 = new ObjectPool(var0, var1);
         var2.poolId = ids++;
      } finally {
         ;
      }

      return var2;
   }

   private void refillPool() {
      this.refillPool(this.replenishPercentage);
   }

   private void refillPool(float var1) {
      int var2 = (int)((float)this.desiredCapacity * var1);
      int var3;
      if (var2 < 1) {
         var3 = 1;
      } else {
         var3 = var2;
         if (var2 > this.desiredCapacity) {
            var3 = this.desiredCapacity;
         }
      }

      for(var2 = 0; var2 < var3; ++var2) {
         this.objects[var2] = this.modelObject.instantiate();
      }

      this.objectsPointer = var3 - 1;
   }

   private void resizePool() {
      int var1 = this.desiredCapacity;
      this.desiredCapacity *= 2;
      Object[] var2 = new Object[this.desiredCapacity];

      for(int var3 = 0; var3 < var1; ++var3) {
         var2[var3] = this.objects[var3];
      }

      this.objects = var2;
   }

   public ObjectPool.Poolable get() {
      synchronized(this){}

      ObjectPool.Poolable var1;
      try {
         if (this.objectsPointer == -1 && this.replenishPercentage > 0.0F) {
            this.refillPool();
         }

         var1 = (ObjectPool.Poolable)this.objects[this.objectsPointer];
         var1.currentOwnerId = ObjectPool.Poolable.NO_OWNER;
         --this.objectsPointer;
      } finally {
         ;
      }

      return var1;
   }

   public int getPoolCapacity() {
      return this.objects.length;
   }

   public int getPoolCount() {
      return this.objectsPointer + 1;
   }

   public int getPoolId() {
      return this.poolId;
   }

   public float getReplenishPercentage() {
      return this.replenishPercentage;
   }

   public void recycle(ObjectPool.Poolable var1) {
      synchronized(this){}

      try {
         if (var1.currentOwnerId != ObjectPool.Poolable.NO_OWNER) {
            if (var1.currentOwnerId == this.poolId) {
               IllegalArgumentException var6 = new IllegalArgumentException("The object passed is already stored in this pool!");
               throw var6;
            }

            StringBuilder var3 = new StringBuilder();
            var3.append("The object to recycle already belongs to poolId ");
            var3.append(var1.currentOwnerId);
            var3.append(".  Object cannot belong to two different pool instances simultaneously!");
            IllegalArgumentException var2 = new IllegalArgumentException(var3.toString());
            throw var2;
         }

         ++this.objectsPointer;
         if (this.objectsPointer >= this.objects.length) {
            this.resizePool();
         }

         var1.currentOwnerId = this.poolId;
         this.objects[this.objectsPointer] = var1;
      } finally {
         ;
      }

   }

   public void recycle(List var1) {
      synchronized(this){}

      while(true) {
         Throwable var10000;
         label455: {
            boolean var10001;
            try {
               if (var1.size() + this.objectsPointer + 1 > this.desiredCapacity) {
                  this.resizePool();
                  continue;
               }
            } catch (Throwable var46) {
               var10000 = var46;
               var10001 = false;
               break label455;
            }

            int var2;
            try {
               var2 = var1.size();
            } catch (Throwable var45) {
               var10000 = var45;
               var10001 = false;
               break label455;
            }

            int var3 = 0;

            while(true) {
               if (var3 >= var2) {
                  try {
                     this.objectsPointer += var2;
                  } catch (Throwable var43) {
                     var10000 = var43;
                     var10001 = false;
                     break;
                  }

                  return;
               }

               ObjectPool.Poolable var4;
               label446: {
                  try {
                     var4 = (ObjectPool.Poolable)var1.get(var3);
                     if (var4.currentOwnerId == ObjectPool.Poolable.NO_OWNER) {
                        break label446;
                     }

                     if (var4.currentOwnerId == this.poolId) {
                        IllegalArgumentException var49 = new IllegalArgumentException("The object passed is already stored in this pool!");
                        throw var49;
                     }
                  } catch (Throwable var47) {
                     var10000 = var47;
                     var10001 = false;
                     break;
                  }

                  try {
                     StringBuilder var48 = new StringBuilder();
                     var48.append("The object to recycle already belongs to poolId ");
                     var48.append(var4.currentOwnerId);
                     var48.append(".  Object cannot belong to two different pool instances simultaneously!");
                     IllegalArgumentException var5 = new IllegalArgumentException(var48.toString());
                     throw var5;
                  } catch (Throwable var42) {
                     var10000 = var42;
                     var10001 = false;
                     break;
                  }
               }

               try {
                  var4.currentOwnerId = this.poolId;
                  this.objects[this.objectsPointer + 1 + var3] = var4;
               } catch (Throwable var44) {
                  var10000 = var44;
                  var10001 = false;
                  break;
               }

               ++var3;
            }
         }

         Throwable var50 = var10000;
         throw var50;
      }
   }

   public void setReplenishPercentage(float var1) {
      float var2;
      if (var1 > 1.0F) {
         var2 = 1.0F;
      } else {
         var2 = var1;
         if (var1 < 0.0F) {
            var2 = 0.0F;
         }
      }

      this.replenishPercentage = var2;
   }

   public abstract static class Poolable {
      public static int NO_OWNER;
      int currentOwnerId;

      public Poolable() {
         this.currentOwnerId = NO_OWNER;
      }

      protected abstract ObjectPool.Poolable instantiate();
   }
}
