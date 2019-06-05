package com.bumptech.glide.util.pool;

import android.support.v4.util.Pools;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public final class FactoryPools {
   private static final FactoryPools.Resetter EMPTY_RESETTER = new FactoryPools.Resetter() {
      public void reset(Object var1) {
      }
   };

   private static Pools.Pool build(Pools.Pool var0, FactoryPools.Factory var1) {
      return build(var0, var1, emptyResetter());
   }

   private static Pools.Pool build(Pools.Pool var0, FactoryPools.Factory var1, FactoryPools.Resetter var2) {
      return new FactoryPools.FactoryPool(var0, var1, var2);
   }

   private static FactoryPools.Resetter emptyResetter() {
      return EMPTY_RESETTER;
   }

   public static Pools.Pool simple(int var0, FactoryPools.Factory var1) {
      return build(new Pools.SimplePool(var0), var1);
   }

   public static Pools.Pool threadSafe(int var0, FactoryPools.Factory var1) {
      return build(new Pools.SynchronizedPool(var0), var1);
   }

   public static Pools.Pool threadSafeList() {
      return threadSafeList(20);
   }

   public static Pools.Pool threadSafeList(int var0) {
      return build(new Pools.SynchronizedPool(var0), new FactoryPools.Factory() {
         public List create() {
            return new ArrayList();
         }
      }, new FactoryPools.Resetter() {
         public void reset(List var1) {
            var1.clear();
         }
      });
   }

   public interface Factory {
      Object create();
   }

   private static final class FactoryPool implements Pools.Pool {
      private final FactoryPools.Factory factory;
      private final Pools.Pool pool;
      private final FactoryPools.Resetter resetter;

      FactoryPool(Pools.Pool var1, FactoryPools.Factory var2, FactoryPools.Resetter var3) {
         this.pool = var1;
         this.factory = var2;
         this.resetter = var3;
      }

      public Object acquire() {
         Object var1 = this.pool.acquire();
         Object var2 = var1;
         if (var1 == null) {
            var1 = this.factory.create();
            var2 = var1;
            if (Log.isLoggable("FactoryPools", 2)) {
               StringBuilder var3 = new StringBuilder();
               var3.append("Created new ");
               var3.append(var1.getClass());
               Log.v("FactoryPools", var3.toString());
               var2 = var1;
            }
         }

         if (var2 instanceof FactoryPools.Poolable) {
            ((FactoryPools.Poolable)var2).getVerifier().setRecycled(false);
         }

         return var2;
      }

      public boolean release(Object var1) {
         if (var1 instanceof FactoryPools.Poolable) {
            ((FactoryPools.Poolable)var1).getVerifier().setRecycled(true);
         }

         this.resetter.reset(var1);
         return this.pool.release(var1);
      }
   }

   public interface Poolable {
      StateVerifier getVerifier();
   }

   public interface Resetter {
      void reset(Object var1);
   }
}
